package com.mycompany.antibes;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    FeedReaderDbHelper mDbHelper;
    EditText mEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDbHelper = new FeedReaderDbHelper(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void request(View v) {
        mEdit = (EditText) findViewById(R.id.text);
        String string = mEdit.getText().toString();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                FeedReaderContract.FeedEntry.STOP,
                FeedReaderContract.FeedEntry.LINE,
                FeedReaderContract.FeedEntry.SCHEDULE,
        };

        String sortOrder =
                FeedReaderContract.FeedEntry.SCHEDULE + " ASC";

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                projection,
                FeedReaderContract.FeedEntry.LINE+" = " + string ,
                null,
                null,
                null,
                sortOrder
        );
        String result = "";

        int iRow = cursor.getColumnIndex(FeedReaderContract.FeedEntry.STOP);
        int iName = cursor.getColumnIndex(FeedReaderContract.FeedEntry.LINE);
        int iSchedule = cursor.getColumnIndex(FeedReaderContract.FeedEntry.SCHEDULE);


        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            result = result + cursor.getString(iRow) + " " + cursor.getString(iName) +
                    " " + cursor.getString(iSchedule) +
                    "\n";
        }
        System.out.println(result);

        TextView textview = new TextView(getApplicationContext());
        textview.setText(result);
        RelativeLayout myLayout;
        myLayout = (RelativeLayout) findViewById(R.id.content_main);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        TextView tv = (TextView) findViewById(R.id.button);
        textview.setTextColor(Color.RED);
        params.addRule(RelativeLayout.BELOW, tv.getId());
        myLayout.addView(textview, params);
    }
}
