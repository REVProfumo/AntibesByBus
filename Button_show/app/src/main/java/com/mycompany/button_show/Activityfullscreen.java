package com.mycompany.button_show;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Activityfullscreen  extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        ImageView img = (ImageView) findViewById(R.id.widget45);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height_screen = displaymetrics.heightPixels;
        int width_screen = displaymetrics.widthPixels;
        img.getLayoutParams().height = (int) (height_screen / 3.);

        img.setBackgroundResource(R.drawable.aa);

        Button button = (Button) findViewById(R.id.button_back);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent inf = new Intent(Activityfullscreen.this, MainActivity.class);

                startActivity(inf);
            }
        });
    }

}
