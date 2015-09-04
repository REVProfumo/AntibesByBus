package com.mycompany.read_file;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class Activityfullscreen extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc);
        ImageView img=(ImageView)findViewById(R.id.widget45);
        img.setBackgroundResource(R.drawable.digitallovesaktid);
    }
}
}
