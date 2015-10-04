package com.mycompany.formulas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by elio-profumo on 21/09/15.
 */
public class New_set_3 extends AppCompatActivity {
    private LinearLayout mLayout;
    private EditText mEditText, mEditText2, mEditText3;
    private Button mButton;
    private OnClickListener onClick;
    private OnClickListener onClick2;
    private KeyListener listener, listener2, listener3;
    private KeyListener nonnulllistener, nonnulllistener2, nonnulllistener3;
    public TextView mean;

    public static int integer = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_set_3);

        TextView mean = (TextView)this.findViewById(R.id.average);
        mean.setText("mean");

        mLayout = (TableLayout) findViewById(R.id.hints);
        mEditText = (EditText) findViewById(R.id.add_hint);
        mEditText2 = (EditText) findViewById(R.id.add_hint2);
        mEditText3 = (EditText) findViewById(R.id.add_hint3);

        mButton = (Button) findViewById(R.id.add);
        mButton.setOnClickListener(onClick());

        TextView textView = new TextView(this);
        TextView textView2 = new TextView(this);
        textView.setText(mEditText.getText().toString());
        textView2.setText(mEditText2.getText().toString());
        textView2.setText(mEditText3.getText().toString());


    }

    private OnClickListener onClick() {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                createNewTextView(mEditText.getText().toString(),
                        mEditText2.getText().toString(), mEditText3.getText().toString());

            }
        };
    }

    private void createNewTextView(String text, String text2, String text3) {
        TableLayout ll = (TableLayout) findViewById(R.id.hints);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);

        TableRow row = new TableRow(this);
        row.setLayoutParams(lp);

        final EditText textView= new EditText(this);
        final EditText textView2= new EditText(this);
        final EditText textView3= new EditText(this);

        textView.setId(integer);
        textView2.setId(integer);
        textView3.setId(integer);

        textView.setText(text);
        textView2.setText(text2);
        textView3.setText(text3);

        nonnulllistener = textView.getKeyListener(); // Save the default KeyListener!!!
        nonnulllistener2 = textView2.getKeyListener(); // Save the default KeyListener!!!
        nonnulllistener3 = textView3.getKeyListener(); // Save the default KeyListener!!!

        textView.setKeyListener(null);
        textView2.setKeyListener(null);
        textView3.setKeyListener(null);

        row.addView(textView);
        row.addView(textView2);
        row.addView(textView3);

        Button btn = new Button(this);
        btn.setText("X");
        //btn.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        row.addView(btn);

        Button btn2 = new Button(this);
        btn2.setText("V");
        row.addView(btn2);

        ll.addView(row);
        integer += 1;

        onClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
                View row = (View) v.getParent();
                ViewGroup container = ((ViewGroup) row.getParent());
                container.removeView(row);
                container.invalidate();
            }
        };


        btn.setOnClickListener(onClick);


        onClick2 = new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener = textView.getKeyListener(); // Save the default KeyListener!!!
                listener2 = textView.getKeyListener(); // Save the default KeyListener!!!
                listener3 = textView.getKeyListener(); // Save the default KeyListener!!!

                if (listener==null){
                    textView.setKeyListener(nonnulllistener);
                    textView2.setKeyListener(nonnulllistener2);
                    textView3.setKeyListener(nonnulllistener3);

                }
                    else{
                    textView.setKeyListener(null);
                    textView2.setKeyListener(null);
                    textView3.setKeyListener(null);

                }


            }
        };

        btn2.setOnClickListener( onClick2 );

    }

    public class Example
    {
        float ar[] = new float[3];

    public float[] setAr(float sum, float sqrt, float num){
        ar[0] = sum;
        ar[1] = sqrt;
        ar[2] = num;
        return ar;

    }
    }




    public float[] averages(View v){
        TableLayout table = (TableLayout)findViewById(R.id.hints);
        float sum2 =0;
        float sqr=0;
        float num=0;
        for(int i = 0, j = table.getChildCount(); i < j; i++) {
            View view = table.getChildAt(i);
            if (view instanceof TableRow) {
                // then, you can remove the the row you want...
                // for instance...
                TableRow row = (TableRow) view;
                TextView firstTextView = (TextView) row.getChildAt(0);
                float firstText = Float.parseFloat(firstTextView.getText().toString());
                sum2 += firstText;
                num += 1;
                sqr += Math.pow(firstText, 2.0);
            }
        }

        Example arrayy = new Example();
        return arrayy.setAr(sum2, sqr, num);
    }


    public void average(View v)
    {
        float sum =0;
        float num = 0;
        float[] array = averages(v);
        num = array[2];
        sum =  array[0]/num;

        TextView tv = (TextView)findViewById(R.id.average);
        tv.setText("mean\n"+sum);
    }


    public void sigma(View v)
    {
        float sum2 =0;
        float sum =0;
        float sqr = 0;
        float num = 0;
        float[] array = averages(v);
        num = array[2];
        sum =  array[0]/num;
        sqr = array[1]/num;
        sqr -= Math.pow(sum, 2.0);

        TextView tv = (TextView)findViewById(R.id.sigma);
        tv.setText("sigma\n"+sqr);
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