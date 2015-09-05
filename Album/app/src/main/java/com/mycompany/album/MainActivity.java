package com.mycompany.album;

import android.app.Activity;
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

public class MainActivity extends Activity {
    public static int nrpic=0;

    Button button;
    ImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnButton();

    }

    public void addListenerOnButton() {
        image = (ImageView) findViewById(R.id.imageView1);

        button = (Button) findViewById(R.id.button_id);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                nrpic += 1;
                nrpic= nrpic % 4;
                String string=Integer.toString(nrpic);
                System.out.println(string);

                String mDrawableName = "aa"+string;
                int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
                image.setImageResource(resID);

            }
        });

    }

}

