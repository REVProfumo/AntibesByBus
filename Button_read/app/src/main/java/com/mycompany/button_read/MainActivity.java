package com.mycompany.button_read;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import android.widget.TextView;
import java.io.*;
import android.content.Context;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button button = (Button) findViewById(R.id.button_id);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//Find the directory for the SD Card using the API
//*Don't* hardcode "/sdcard"
                TextView tv = (TextView) findViewById(R.id.text_red);

                File dir = Environment.getExternalStorageDirectory();
                //File yourFile = new File(dir, "path/to/the/file/inside/the/sdcard.ext");

                //Get the text file
                File file = new File(dir, "/Download/text.txt");
                // i have kept text.txt in the sd-card

                if (file.exists())   // check if file exist
                {
                    //Read text from file
                    StringBuilder text = new StringBuilder();

                    try {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        String line;

                        while ((line = br.readLine()) != null) {
                            text.append(line);
                            text.append('\n');
                        }
                    } catch (IOException e) {
                        //You'll need to add proper error handling here
                    }
                    //Set the text
                    tv.setText(text);
                } else {
                    tv.setText("Sorry file doesn't exist!!");
                }

                try {
                String filename = "newtext";
                String fpath = "/sdcard/"+filename+".txt";

                File file2 = new File(fpath);

                // If file does not exists, then create it
                if (!file2.exists()) {
                file2.createNewFile();
                }
                String string = "nuovo file!";
                FileOutputStream outputStream;

                    FileWriter fw = new FileWriter(file2.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(string);
                    bw.write("\n");
                    bw.write("\n");
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;

                    while ((line = br.readLine()) != null) {
                        bw.write(line);
                        bw.write("\n");
                    }
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
