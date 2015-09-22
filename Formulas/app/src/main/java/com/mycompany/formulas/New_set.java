package com.mycompany.formulas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.view.ViewGroup;

import org.w3c.dom.Text;

/**
 * Created by elio-profumo on 21/09/15.
 */
public class New_set extends AppCompatActivity {
    private LinearLayout mLayout;
    private EditText mEditText;
    private Button mButton;
    private OnClickListener onClick;
    private OnClickListener onClick2;
    private KeyListener listener;
    private KeyListener nonnulllistener;

    public static int integer = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_set);
        mLayout = (TableLayout) findViewById(R.id.hints);
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
        TableLayout ll = (TableLayout) findViewById(R.id.hints);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);

        TableRow row = new TableRow(this);
        row.setLayoutParams(lp);

        final EditText textView= new EditText(this);
        textView.setId(integer);

        textView.setText(text);
        nonnulllistener = textView.getKeyListener(); // Save the default KeyListener!!!

        textView.setKeyListener(null);

        row.addView(textView);

        Button btn = new Button(this);
        btn.setText("X");
        //btn.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        row.addView(btn);

        Button btn2 = new Button(this);
        btn2.setText("V");
        row.addView(btn2);

        ll.addView(row);
        integer += 1;

        onClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View row = (View) v.getParent();
                ViewGroup container = ((ViewGroup) row.getParent());
                container.removeView(row);
                container.invalidate();
            }
        };


        btn.setOnClickListener(onClick);


        onClick2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener = textView.getKeyListener(); // Save the default KeyListener!!!

                if (listener==null){
                    textView.setKeyListener(nonnulllistener);
                }
                    else{
                    textView.setKeyListener(null);
                }


            }
        };



        btn2.setOnClickListener( onClick2 );


    }

}