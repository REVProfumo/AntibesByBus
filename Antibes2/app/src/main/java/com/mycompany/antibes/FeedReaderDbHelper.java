package com.mycompany.antibes;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elio-profumo on 04/10/15.
 */
public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";
    private final Context fContext;


    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        fContext = context;

    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

        ContentValues values = new ContentValues();
        Resources res = fContext.getResources();
        String[] myArray = res.getStringArray(R.array.my_array);
        for (String item : myArray){
            String[] split = item.split("\\s+");

            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID, split[0]);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, split[1]);
            db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        }


    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
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
                    FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + TEXT_TYPE +
//        ... // Any other options for the CREATE command
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;
}