package com.mycompany.read_file;

import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.*;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button myButton = (Button) findViewById(R.id.my_button);
        myButton.setOnClickListener(new DialogInterface.OnClickListener() {

            public void onClick(View v) {
                Intent inf=new Intent(TestbuttontestActivity.this,Activityfullscreen.class);

                startActivity(inf);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Find the directory for the SD Card using the API

    public void ReadFile() {
        File sdcard = Environment.getExternalStorageDirectory();

    //Get the text file
        File file = new File(sdcard,"file.txt");

    //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }

        //Find the view by its id
        TextView tv = (TextView) findViewById(R.id.text_view);

//Set the text
        tv.setText(text.toString());
    }
}
