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
import android.text.TextUtils;


import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;

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


        if (string.indexOf('-')>=0){
            string = string.replace("-", "&");
        }
        else{
            string = string.replace(" ", "-");
        }
        string = string.toLowerCase();

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
                null
        );
        String resultSchedule = "";

        int iRow = cursor.getColumnIndex(FeedReaderContract.FeedEntry.STOP);
        int iName = cursor.getColumnIndex(FeedReaderContract.FeedEntry.LINE);
        int iSchedule = cursor.getColumnIndex(FeedReaderContract.FeedEntry.SCHEDULE);
        int iDirection = cursor.getColumnIndex(FeedReaderContract.FeedEntry.DIRECTION);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String auxString = cursor.getString(iRow);

            auxString = auxString.replace("-", " ");
            //System.out.println(cursor.getString(iSchedule));

            String toBeCapped = auxString;

            String[] tokens = toBeCapped.split("\\s");
            toBeCapped = "";

            for (int i = 0; i < tokens.length; i++) {
                char capLetter = Character.toUpperCase(tokens[i].charAt(0));
                toBeCapped += " " + capLetter + tokens[i].substring(1);
            }
            toBeCapped = toBeCapped.trim();
            auxString = toBeCapped;

            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            String[] currentTime = currentDateTimeString.split(" ");
            String time = currentTime[3];
            String[] timeSplitted = time.split(":");
            int seconds = Integer.parseInt(timeSplitted[0]) * 3600 + Integer.parseInt(timeSplitted[1]) * 60
                    + Integer.parseInt(timeSplitted[2]);

            //System.out.println(time);
            //System.out.println(seconds);


            String[] parts = cursor.getString(iSchedule).split(" ");
            int[] times = new int[parts.length];

            int j = 0;
            int flag = -1;
            int[] nextTimes = new int[2];
            for (int i = 0; i < parts.length; i++) {
                try {
                    String[] partsSplit =parts[i].split(":");
                    times[i] = Integer.parseInt(partsSplit[0])*3600+Integer.parseInt(partsSplit[1])*60;
                            //+Integer.parseInt(partsSplit[2]);


                    if ((times[i] > seconds) & (j < 2)) {
                        nextTimes[j] = times[i];
                        j += 1;
                        flag += 1;
                    }
                    if (j == 2) break;
                } catch (NumberFormatException nfe) {
                };
            }


            if (flag == 1) {
                String nextTimesString="";
                String nextTimesChrono ="";
                for (int i = 0; i < 2; i++) {
                    int hours = nextTimes[i]/3600;
                    int mins = (nextTimes[i]-hours*3600)/60;
                    //int secs = nextTimes[i]-hours*3600 - mins*60;
                    nextTimesChrono += Integer.toString(hours)+":"+Integer.toString(mins)+" ";
                            //+":"+Integer.toString(secs)+" ";

                    nextTimesString += Integer.toString(nextTimes[i]) + " ";
                }

                resultSchedule = resultSchedule + auxString + " " + cursor.getString(iName) +
                        " " + cursor.getString(iDirection) +
                        " " + nextTimesChrono +
                        "\n";
            }
        }

        TextView textview = new TextView(getApplicationContext());
        textview.setText(resultSchedule);
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
