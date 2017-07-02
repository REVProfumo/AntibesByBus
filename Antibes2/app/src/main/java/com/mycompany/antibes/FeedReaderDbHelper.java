package com.mycompany.antibes;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;
import java.lang.Object;

public class FeedReaderDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";
    private final Context fContext;

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        fContext = context;
    }

    private void insertValue(SQLiteDatabase db, String[] myArray, String tableName){
        ContentValues values = new ContentValues();
        for (String item : myArray){
            String[] split = item.split("\\s+");

            values.put(FeedReaderContract.FeedEntry.STOP, split[0]);
            values.put(FeedReaderContract.FeedEntry.LINE, split[1]);
            values.put(FeedReaderContract.FeedEntry.DIRECTION, split[2]);

            String schedule="";

            String[] stringArray = item.split("\\s+");
            int length = stringArray.length;

            for (int i = 3; i < length; i++) {
                if(schedule.trim().length()>0){
                    schedule += split[i]+" ";
                }else{
                    schedule = split[i]+" ";
                }
            }
            values.put(FeedReaderContract.FeedEntry.SCHEDULE, schedule);
            db.insert(tableName, null, values);
        }
    }


    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME0 + SQL_CREATE_ENTRIES);
        db.execSQL("CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + SQL_CREATE_ENTRIES);
        db.execSQL("CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME2 + SQL_CREATE_ENTRIES);
        db.execSQL("CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME3 + SQL_CREATE_ENTRIES);
        db.execSQL("CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME4 + SQL_CREATE_ENTRIES);
        db.execSQL("CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME5 + SQL_CREATE_ENTRIES);

        Resources res = fContext.getResources();

        insertValue(db, res.getStringArray(R.array.my_array), FeedReaderContract.FeedEntry.TABLE_NAME);
        insertValue(db, res.getStringArray(R.array.my_array_sat), FeedReaderContract.FeedEntry.TABLE_NAME2);
        insertValue(db, res.getStringArray(R.array.my_array_sun), FeedReaderContract.FeedEntry.TABLE_NAME3);
        insertValue(db, res.getStringArray(R.array.my_array_vacances), FeedReaderContract.FeedEntry.TABLE_NAME0);
        insertValue(db, res.getStringArray(R.array.my_array_sat_vacances), FeedReaderContract.FeedEntry.TABLE_NAME4);
        insertValue(db, res.getStringArray(R.array.my_array_sun_vacances), FeedReaderContract.FeedEntry.TABLE_NAME5);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " ( " +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY, " +
                    FeedReaderContract.FeedEntry.STOP + TEXT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.LINE + TEXT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.DIRECTION + TEXT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.SCHEDULE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;
}