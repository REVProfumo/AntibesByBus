package com.mycompany.formulas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.view.Gravity;
import android.widget.RelativeLayout;

/**
 * Created by elio-profumo on 21/09/15.
 */
public class New_set extends AppCompatActivity {
    private LinearLayout mLayout;
    private EditText mEditText;
    private Button mButton;

    public static int integer = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_set);
        mLayout = (LinearLayout) findViewById(R.id.hints);
        mEditText = (EditText) findViewById(R.id.add_hint);

        mButton = (Button) findViewById(R.id.add);
        mButton.setOnClickListener(onClick());
        TextView textView = new TextView(this);
        textView.setText(mEditText.getText().toString());
    }

    private OnClickListener onClick() {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                createNewTextView(mEditText.getText().toString());
            }
        };
    }

    private void createNewTextView(String text) {
        LinearLayout layout = new LinearLayout(this);

        final TextView textView = new TextView(this);
        textView.setId(integer);

        textView.setText(text);

        mLayout.addView(textView);
        integer +=1;

    }
}