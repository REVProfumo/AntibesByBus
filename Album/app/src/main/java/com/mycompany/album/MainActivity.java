package com.mycompany.album;

import android.app.Activity;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.support.v4.content.ContextCompat;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    public static int nrpic=0;
    public static int flag=0;

    Button button;
    Button button2;
    ImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnButton();
        addListenerOnButton2();
        Button p1_button = (Button)findViewById(R.id.button_id2);
        p1_button.setText("Start scroll");

    }

    public void update(){
        nrpic += 1;
        nrpic = nrpic % 4;
        String string = Integer.toString(nrpic);
        System.out.println(string);

        String mDrawableName = "aa" + string;
        int resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
        image.setImageResource(resID);

    }

    public void addListenerOnButton() {
        image = (ImageView) findViewById(R.id.imageView1);

        button = (Button) findViewById(R.id.button_id);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                update();

            }
        });

    }

    public void addListenerOnButton2() {


        image = (ImageView) findViewById(R.id.imageView1);

        button = (Button) findViewById(R.id.button_id2);
        button.setOnClickListener(new OnClickListener() {
            Timer timer = new Timer();

            @Override
            public void onClick(View arg0) {

                flag +=1;
                flag %= 2;
                System.out.println(flag);
                timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if(flag !=0) {
                                //do your thing

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        update();                       // task to be done every 1000 milliseconds
                                    }
                                });

                            }
                        }
                    }, 0, 5000);
                Button p1_button = (Button)findViewById(R.id.button_id2);
                if (flag == 0){
                    p1_button.setText("Start again");
                }
                    else {
                        p1_button.setText("Pause");
                    }
                }

        });

    }

}

