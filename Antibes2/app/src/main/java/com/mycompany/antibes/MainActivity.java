package com.mycompany.antibes;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.SubMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.text.TextUtils;
import android.widget.Toast;
import android.view.Menu;
import java.text.Normalizer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    FeedReaderDbHelper mDbHelper;
    EditText mEdit;
    Cursor cursorGlobal=null;
    Menu menu_global;
    int flag_update=0;
    int[] hashes_existing = new int[200];
    int nr_hashes = 0;

    public int hash_value(String text) {
        int hash = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            hash += (int) c;
        }
        return hash;
    }

    private void cleanTable(TableLayout table) {

        table.removeAllViews();
        flag_update = 0;
    }

    private void createFirstLineTable(TableLayout table)
    {
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        //TableRow.LayoutParams tvPar0 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f);
        tv0.setText("Line    ");
        tv0.setPadding(0, 0, 10, 0);

        tv0.setBackgroundDrawable(getResources().getDrawable(R.drawable.cell_shape));
        tv0.setTextColor(Color.WHITE);
        tv0.setGravity(Gravity.LEFT);
        //tv0.setLayoutParams(tvPar0);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        //TableRow.LayoutParams tvPar1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 5f);
        tv1.setText("Direction                                                                             ");
        tv1.setBackgroundDrawable(getResources().getDrawable(R.drawable.cell_shape));
        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.LEFT);
       // tv1.setLayoutParams(tvPar1);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        //TableRow.LayoutParams tvPar2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3f);
        tv2.setText("time                                          ");
        tv2.setBackgroundDrawable(getResources().getDrawable(R.drawable.cell_shape));
        tv2.setTextColor(Color.WHITE);
        tv2.setGravity(Gravity.LEFT);
        //tv2.setLayoutParams(tvPar2);
        tbrow0.addView(tv2);
        table.addView(tbrow0);
        table.setStretchAllColumns(true);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        TextView noStop = new TextView (this);
        noStop.setText("no stop found");
        RelativeLayout myLayout=(RelativeLayout) this.findViewById(R.id.content_main);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        noStop.setLayoutParams(lp);
        noStop.setId(1000);
        noStop.setVisibility(View.GONE);
        myLayout.addView(noStop);



        Button btnClean = (Button) findViewById(R.id.text3);

        // create click listener
        View.OnClickListener oclBtnClean = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableLayout table = (TableLayout) findViewById(R.id.table_main);
                cleanTable(table);
                createFirstLineTable(table);
                EditText line = (EditText) findViewById(R.id.text);
                line.setText("");
                TextView noStop = (TextView) findViewById(1000);
                noStop.setVisibility(View.GONE);
            }
        };

        // assign click listener to the OK button (btnOK)
        btnClean.setOnClickListener(oclBtnClean);

        mDbHelper = new FeedReaderDbHelper(getApplicationContext());
        TableLayout stk = (TableLayout) findViewById(R.id.table_main);
        createFirstLineTable(stk);

        //following is to update the textview each 1 minute
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);

                        if (flag_update ==1) {
                            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                            String[] currentTime = currentDateTimeString.split(" ");
                            String time = currentTime[3];
                            String[] timeSplitted = time.split(":");
                            int seconds = Integer.parseInt(timeSplitted[2]);
                            if (seconds == 0) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ViewGroup layout = (ViewGroup) findViewById(R.id.content_main);
                                        //View toRemove = layout.findViewById(1);
                                        //View toRemove = findViewById(R.id.table_main);
                                        TableLayout table = (TableLayout) findViewById(R.id.table_main);
                                        //if ((table!=null))
                                        //layout.removeView(toRemove);
                                        generateTextView(cursorGlobal);

                                    }
                                });
                            }
                        }
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();

        Button buttonX = (Button)findViewById(R.id.text2);
// Register the onClick listener with the implementation above
        buttonX.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                EditText ed = (EditText) findViewById(R.id.text);

                String text = ed.getText().toString();

                int new_hash_value = hash_value(text);
                int flag_exist = 0;
                for(int i=0;i<nr_hashes;i++) {
                    if (hashes_existing[i]==new_hash_value)
                            flag_exist = 1;
                }
                if (flag_exist==0)
                {
                    hashes_existing[nr_hashes] = hash_value(text);
                    nr_hashes += 1;
                    menu_global.add(0, new_hash_value, 0, text);
                }
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SubMenu menuItem = menu.findItem(R.id.action_favorite2).getSubMenu();
        this.menu_global = menuItem;
        //the menu option text is defined in resources

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                return true;

            case R.id.action_favorite: {
                RelativeLayout myLayout=(RelativeLayout) this.findViewById(R.id.content_main);

                TextView tv=new TextView(this);
                tv.setText("test");
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                tv.setLayoutParams(lp);
                myLayout.addView(tv);

                return true;

            }

            case R.id.action_favorite2:
            {
                return true;
            }


            default: {
                EditText ed = (EditText) findViewById(R.id.text);

                String text = (String) item.getTitle();
                ed.setText(text);
                return super.onOptionsItemSelected(item);
            }
        }
    }


    public Cursor cursor(String string){
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
                FeedReaderContract.FeedEntry.STOP + " = \'" + string + "\'",
                null,
                null,
                null,
                null
        );
        return cursor;
    }

    public void generateTextView(Cursor cursor){

        flag_update = 1;

        TableLayout stk = (TableLayout) findViewById(R.id.table_main);
        cleanTable(stk);
        createFirstLineTable(stk);

        try {
            int iRow = cursor.getColumnIndex(FeedReaderContract.FeedEntry.STOP);
            int iName = cursor.getColumnIndex(FeedReaderContract.FeedEntry.LINE);
            int iSchedule = cursor.getColumnIndex(FeedReaderContract.FeedEntry.SCHEDULE);
            int iDirection = cursor.getColumnIndex(FeedReaderContract.FeedEntry.DIRECTION);

            if (cursor != null && cursor.getCount() > 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    //String resultSchedule = "";
                    String auxString = cursor.getString(iRow);
                    auxString = auxString.replace("-", " ");

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

                    //System.out.println(currentDateTimeString+" this is all");

                    int actualMins;
                    int actualHours;

                    String[] timeSplitted = time.split(":");
                    int seconds = Integer.parseInt(timeSplitted[0]) * 3600 + Integer.parseInt(timeSplitted[1]) * 60
                            + Integer.parseInt(timeSplitted[2]);

                    String pm ="am";
                    try{
                        pm = currentTime[4];
                    }
                    catch (Exception e){

                    }
//                    System.out.println(pm);

                    if (pm.equals("pm")) {
//                        System.out.println(currentDateTimeString + " we're is pm");
                        seconds+=3600*12;
                    }
                    actualMins = Integer.parseInt(timeSplitted[1]);
                    actualHours = Integer.parseInt(timeSplitted[0]);

                    String[] parts = cursor.getString(iSchedule).split(" ");
                    int[] times = new int[parts.length];

                    int j = 0;
                    int flag = -1;
                    int[] nextTimes;
                    nextTimes = new int[] {-1, -1};

                    for (int i = 0; i < parts.length; i++) {
                        try {
                            String[] partsSplit = parts[i].split(":");
                            times[i] = Integer.parseInt(partsSplit[0]) * 3600 + Integer.parseInt(partsSplit[1]) * 60;

                            if ((times[i] > seconds) & (j < 2)) {
                                nextTimes[j] = times[i];
                                j += 1;
                                flag += 1;
                            }

                            if (j == 2) break;
                        } catch (NumberFormatException nfe) {
                        }
                        ;
                    }
                    String nextTimesChrono = "";
                    String newiName = "";
                    int minsNext = 0;
                    int hoursNext = 0;
                    if (flag > -1) {
                        String nextTimesString = "";
                        System.out.println(nextTimes);

                        for (int i = 0; i < 2; i++) {

                            if (nextTimes[i]==-1)
                                break;

                            int hours = nextTimes[i] / 3600;
                            int mins = (nextTimes[i] - hours * 3600) / 60;

                            if (i == 0) {
                                minsNext = mins - actualMins;
                                hoursNext = hours - actualHours;

                                if (pm.equals("pm"))
                                    hoursNext -= 12;

                                if (minsNext<0) {
                                    minsNext += 60;
                                    hoursNext -= 1;
                                }
                            }

                            String formattedHours = Integer.toString(hours);
                            if (formattedHours.length() == 1)
                                formattedHours = "0" + formattedHours;

                            String formattedMins = Integer.toString(mins);
                            if (formattedMins.length() == 1)
                                formattedMins = "0" + formattedMins;


                            nextTimesChrono += formattedHours + ":" + formattedMins + " ";

                            nextTimesString += Integer.toString(nextTimes[i]) + " ";
                        }
                        newiName += cursor.getString(iName).replace('+', ' ');

                /*resultSchedule = resultSchedule + auxString + " " + newiName +
                        " " +  cursor.getString(iDirection)+
                        " " + nextTimesChrono + "(next in "+ Integer.toString(minsNext) + " mins)" +
                        "\n";*/
                    }
                    if (flag > -1) {
                        TableRow tbrow = new TableRow(this);

                        TextView t1v = new TextView(this);
                        t1v.setText(cursor.getString(iDirection));
                        t1v.setTextColor(Color.WHITE);
                        t1v.setGravity(Gravity.LEFT);
                        //t1v.setBackgroundDrawable(getResources().getDrawable(R.drawable.cell_shape));

                        tbrow.addView(t1v);
                        TextView t2v = new TextView(this);
                        t2v.setText(newiName);
                        t2v.setTextColor(Color.WHITE);
                        t2v.setGravity(Gravity.LEFT);
                        //t2v.setBackgroundDrawable(getResources().getDrawable(R.drawable.cell_shape));

                        tbrow.addView(t2v);
                        TextView t3v = new TextView(this);
                        t3v.setText(nextTimesChrono + "(in " + Integer.toString(hoursNext) + " hr " + Integer.toString(minsNext) + " mn)");
                        t3v.setTextColor(Color.WHITE);
                        t3v.setGravity(Gravity.LEFT);
                        //t3v.setBackgroundDrawable(getResources().getDrawable(R.drawable.cell_shape));

                        tbrow.addView(t3v);
                        stk.addView(tbrow);
                    }

                }
            }
            else if(cursor != null && cursor.getCount() == 0) {
                    TextView noStop = (TextView) findViewById(1000);
                    noStop.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
             System.out.println("Still no selected stop");
        }
        /*
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
        */


    }

    public void lookUp(View v) {
        ViewGroup layout = (ViewGroup) findViewById(R.id.content_main);
        View toRemove = layout.findViewById(1);
        layout.removeView(toRemove);

        mEdit = (EditText) findViewById(R.id.text);
        String string = mEdit.getText().toString();

        System.out.println(string);
        string = Normalizer.normalize(string, Normalizer.Form.NFD);
        string = string.replaceAll("[^\\p{ASCII}]", "");
        System.out.println(string);

        if (string.indexOf('-')>=0){
            string = string.replace("-", "&");
        }
        else{
            string = string.replace(" ", "-");
        }
        string = string.toLowerCase();
        System.out.println(string);
        string = string.replaceAll("'", "''");
        System.out.println(string);

        Cursor cursor = cursor(string);
        cursorGlobal = cursor;
        generateTextView(cursorGlobal);

    }
}
