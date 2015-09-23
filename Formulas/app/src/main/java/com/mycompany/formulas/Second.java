package com.mycompany.formulas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
public class Second extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

    }

    public void Second_open(View view) {
        Intent intent = new Intent(Second.this, New_set.class);
        startActivity(intent);
    }

    public void Second_two_open(View view) {
        Intent intent = new Intent(Second.this, New_set_2.class);
        startActivity(intent);
    }

    public void Second_three_open(View view) {
        Intent intent = new Intent(Second.this, New_set_3.class);
        startActivity(intent);
    }

}
