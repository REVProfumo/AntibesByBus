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
import android.view.ViewGroup;
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

    public void lookUp(View v) {
        ViewGroup layout = (ViewGroup) findViewById(R.id.content_main);
        View toRemove = layout.findViewById(1);
        layout.removeView(toRemove);

        mEdit = (EditText) findViewById(R.id.text);
        String string = mEdit.getText().toString();

        string = string.replace(" ", "-");

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                FeedReaderContract.FeedEntry.STOP,
                FeedReaderContract.FeedEntry.LINE,
                FeedReaderContract.FeedEntry.SCHEDULE,
                FeedReaderContract.FeedEntry.DIRECTION,

        };

        String sortOrder =
                FeedReaderContract.FeedEntry.SCHEDULE + " ASC";

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                projection,
                FeedReaderContract.FeedEntry.STOP+" = \'" + string + "\'" ,
                null,
                null,
                null,
                sortOrder
        );
        String result = "";

        int iRow = cursor.getColumnIndex(FeedReaderContract.FeedEntry.STOP);
        int iName = cursor.getColumnIndex(FeedReaderContract.FeedEntry.LINE);
        int iSchedule = cursor.getColumnIndex(FeedReaderContract.FeedEntry.SCHEDULE);
        int iDirection = cursor.getColumnIndex(FeedReaderContract.FeedEntry.DIRECTION);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            result = result + cursor.getString(iRow).replace("-", " ") + " " + cursor.getString(iName) +
                    " " + cursor.getString(iDirection) +
                    " " + cursor.getString(iSchedule) +
                    "\n";
        }
        System.out.println(result);

        TextView textview = new TextView(getApplicationContext());
        textview.setText(result);
        textview.setId(1);
        RelativeLayout myLayout;
        myLayout = (RelativeLayout) findViewById(R.id.content_main);
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        TextView tv = (TextView) findViewById(R.id.button);
        textview.setTextColor(Color.RED);
        params.addRule(RelativeLayout.BELOW, tv.getId());
        myLayout.addView(textview, params);
    }
}
