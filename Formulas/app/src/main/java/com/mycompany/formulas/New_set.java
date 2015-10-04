package com.mycompany.formulas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuItem;
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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import org.w3c.dom.Text;
import com.jjoe64.graphview.GraphView.*;

/**
 * Created by elio-profumo on 21/09/15.
 */
public class New_set extends AppCompatActivity {
    private LinearLayout mLayout;
    private EditText mEditText;
    private Button mButton, mButton_back;
    private OnClickListener onClick;
    private OnClickListener onClick2;
    private KeyListener listener;
    private KeyListener nonnulllistener;
    public TextView mean;

    public static int integer = 0;

    private int lastX = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_set);

        TextView mean = (TextView)this.findViewById(R.id.average);
        mean.setText("mean");

        mLayout = (TableLayout) findViewById(R.id.hints);
        mEditText = (EditText) findViewById(R.id.add_hint);

        mButton = (Button) findViewById(R.id.add);
        mButton.setOnClickListener(onClick());
        TextView textView = new TextView(this);
        textView.setText(mEditText.getText().toString());

        mButton.setOnClickListener(onClick());

    }

//onCreate


    public void onBackPressed() {
        Intent myIntent = new Intent(New_set.this, Second.class);
        New_set.this.startActivity(myIntent);
    }

    private OnClickListener onClick() {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                createNewTextView(mEditText.getText().toString());
            }
        };
    };

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


    public void sigma(View v) {
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
        tv.setText("sigma\n" + sqr);
    }
    public void plot(){
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> mySeries = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(1, 5),
                new DataPoint(2, 10)
        });

        graph.addSeries(mySeries);
        LinearLayout layout = (LinearLayout) findViewById(R.id.new_set);
        layout.addView(graph);
    }
    public void cancel(){

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