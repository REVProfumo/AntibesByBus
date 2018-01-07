package com.mycompany.antibes;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.SubMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.Normalizer;

import java.text.SimpleDateFormat;
import java.util.Date;

import Utils.Utils;


public class MainActivity extends AppCompatActivity {
    FeedReaderDbHelper mDbHelper;
    Cursor cursorGlobal=null;
    Menu menu_global;
    int flag_update=0;
    int[] hashes_existing = new int[200];
    int nr_hashes = 0;
    String fileName = "MyFile";

    public int hash_value(String text) {
        int hash = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            hash += (int) c;
        }
        return hash;
    }

    public void init_hashes_existing(Menu menu){
        hashes_existing = new int[200];
        for (int i = 0; i < menu.size(); i++) {
                String text = (String) menu.getItem(i).getTitle();
            hashes_existing[i] = hash_value(text);
            nr_hashes += 1;
        }
    }


    private void cleanTable(TableLayout table) {

        table.removeAllViews();
        flag_update = 0;
    }

    private void createFirstLineTable(TableLayout table)
    {
        Resources res = getResources();
        TableRow tbrow0 = new TableRow(this);


        TextView tv0 = new TextView(this);
        TextView tv01 = new TextView(this);
        TextView tv1 = new TextView(this);
        TextView tv2 = new TextView(this);

        String toastMsg;
        String text0="";
        String text01="";
        String text1="";
        String text2="";

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                text0 = String.format(res.getString(R.string.line_large));
                text01 = String.format(res.getString(R.string.origin_large));
                text1 = String.format(res.getString(R.string.direction_large));
                text2 = String.format(res.getString(R.string.time_large));
                toastMsg = "Large screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                text0 = String.format(res.getString(R.string.line));
                tv0.setTextSize(10);
                text01 = String.format(res.getString(R.string.origin));
                tv01.setTextSize(10);
                text1 = String.format(res.getString(R.string.direction));
                tv1.setTextSize(10);
                text2 = String.format(res.getString(R.string.time));
                tv2.setTextSize(10);
                toastMsg = "Normal screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                toastMsg = "Small screen";
                break;
            default:
                toastMsg = "Screen size is neither large, normal or small";
        }



        tv0.setText(text0);
        tv0.setPadding(0, 0, 10, 0);

        tv0.setBackgroundDrawable(getResources().getDrawable(R.drawable.cell_shape));
        tv0.setTextColor(Color.WHITE);
        tv0.setGravity(Gravity.LEFT);
        //tv0.setLayoutParams(tvPar0);
        tbrow0.addView(tv0);


        tv01.setText(text01);

        tv01.setBackgroundDrawable(getResources().getDrawable(R.drawable.cell_shape));
        tv01.setTextColor(Color.WHITE);
        tv01.setGravity(Gravity.LEFT);
        // tv1.setLayoutParams(tvPar1);
        tbrow0.addView(tv01);



        tv1.setText(text1);

        tv1.setBackgroundDrawable(getResources().getDrawable(R.drawable.cell_shape));
        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.LEFT);
       // tv1.setLayoutParams(tvPar1);
        tbrow0.addView(tv1);

        tv2.setText(text2);

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
                //createFirstLineTable(table);
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

        //TODO this will be useful , it should be reintroduced
        //TODO use method createFirstLineTable(stk);

        createOutputFile();

        //following is to update the textview each 1 minute
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyy HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());
                        System.out.println(currentDateandTime);
                        //System.out.println(DateFormat.getDateTimeInstance().format(new Date()));
                        if (flag_update ==1) {
                            String currentDateTimeString =  currentDateandTime;//DateFormat.getDateTimeInstance().format(new Date());
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
            public void onClick(View v) {
                EditText ed = (EditText) findViewById(R.id.text);

                String text = ed.getText().toString();
                Cursor aCursor = Utils.cursor(mDbHelper, text);
                if (aCursor != null && aCursor.getCount() > 0) {
                    int new_hash_value = hash_value(text);
                    int flag_exist = 0;
                    for (int i = 0; i < nr_hashes; i++) {
                        if (hashes_existing[i] == new_hash_value)
                            flag_exist = 1;
                    }

                    if (new_hash_value == 0)
                        flag_exist = 1;

                    if (flag_exist == 0) {
                        hashes_existing[nr_hashes] = hash_value(text);
                        nr_hashes += 1;
                        menu_global.add(0, new_hash_value, 0, text);
                        addToOutputFile(text);

                    }
                }
            }

        });


        Button buttonclean = (Button)findViewById(R.id.textremove);
// Register the onClick listener with the implementation above
        buttonclean.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                cleanFavorites();
                invalidateOptionsMenu();
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
        init_hashes_existing(menuItem);

        //the menu option text is defined in resources
        getInputFile(menu);


        return true;
    }

    public void getInputFile(Menu menu){
        try {
            BufferedReader br = new BufferedReader(new FileReader(getFilesDir() + "/" + fileName));
            String thisLine;
            while ((thisLine = br.readLine()) != null) {
                System.out.println(thisLine);
                String stop = thisLine;
                int hash = hash_value(stop);
                hashes_existing[nr_hashes] = hash;
                nr_hashes += 1;
                menu_global.add(0, hash, 0, stop);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cleanFavorites()
    {
     try{
         (new File(getFilesDir() + "/"+ fileName )).delete();
         (new File(getFilesDir() + "/" + fileName)).mkdir();
         int size = menu_global.size();
         for(int i=0;i<size;i++)
            menu_global.removeItem(i);
         init_hashes_existing(menu_global);

     }
     catch (Exception e){
        e.printStackTrace();

     }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                return true;

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

    public void createOutputFile() {
        //trying to write and read on file
        FileOutputStream outputStream = null;
        File f=new File(getFilesDir() + "/" + fileName);
        try {
            if (f.isFile()==false) {
                outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                outputStream.close();
                System.out.println("file doesn't exist");

            }
            else{
                System.out.println("file exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addToOutputFile(String newStop){
        try {
            FileOutputStream outputStream=new FileOutputStream(getFilesDir() + "/" + fileName,true);
            outputStream.write(newStop.getBytes());
            outputStream.write("\n".getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void generateTextView(Cursor cursor){

        flag_update = 1;

        TableLayout stk = (TableLayout) findViewById(R.id.table_main);
        cleanTable(stk);
        stk.setBackgroundDrawable(getResources().getDrawable(R.drawable.cell_shape2));

        //TODO this will be useful , it should be reintroduced
        //TODO use method createFirstLineTable(stk);
        try {


            if (cursor != null && cursor.getCount() > 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                    Utils.ObjectRow objectRow = Utils.getRowElements(cursor);

                    if (objectRow != null) {

                        //from here I create the view in the table
                        TableRow tbrow = new TableRow(this);
                        tbrow.setBackgroundDrawable(getResources().getDrawable(R.drawable.cell_shape2));

                        TextView t1v = new TextView(this);
                        t1v.setText(objectRow.getDirection());
                        t1v.setTextColor(Color.WHITE);
                        t1v.setGravity(Gravity.LEFT);

                        TextView t01v = new TextView(this);
                        t01v.setText(objectRow.getRow());
                        t01v.setTextColor(Color.WHITE);
                        t01v.setGravity(Gravity.LEFT);


                        TextView t2v = new TextView(this);
                        t2v.setText(objectRow.getName());
                        t2v.setTextColor(Color.WHITE);
                        t2v.setGravity(Gravity.LEFT);


                        TextView t3v = new TextView(this);
                        t3v.setText(objectRow.getTime());
                        t3v.setTextColor(Color.WHITE);
                        t3v.setGravity(Gravity.LEFT);


                        int screenSize = getResources().getConfiguration().screenLayout &
                                Configuration.SCREENLAYOUT_SIZE_MASK;

                        switch (screenSize) {
                            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                                break;
                            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                                t01v.setTextSize(15);
                                t1v.setTextSize(20);
                                t2v.setTextSize(15);
                                t3v.setTextSize(15);
                                break;
                            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                                break;
                            default:
                        }
                        LinearLayout linearLayout = new LinearLayout(this);
                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayout.setHorizontalGravity(Gravity.CENTER);
                        linearLayout.setVerticalGravity(Gravity.CENTER);
                        linearLayout.setGravity(Gravity.CENTER);
                        t1v.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                        linearLayout.addView(t1v, new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.40f));
                        LinearLayout sublinearLayout = new LinearLayout(this);
                        sublinearLayout.setOrientation(LinearLayout.VERTICAL);
                        sublinearLayout.addView(t01v);
                        sublinearLayout.addView(t2v);
                        sublinearLayout.addView(t3v);
                        linearLayout.addView(sublinearLayout, new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.20f));
                        tbrow.addView(linearLayout);
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



    }

    public void lookUp(View v) {
        ViewGroup layout = (ViewGroup) findViewById(R.id.content_main);
        View toRemove = layout.findViewById(1);
        layout.removeView(toRemove);

        EditText mEdit = (EditText) findViewById(R.id.text);
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

        Cursor cursor = Utils.cursor(mDbHelper, string);
        cursorGlobal = cursor;
        generateTextView(cursorGlobal);

    }
}
