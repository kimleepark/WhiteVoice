package com.example.myapplicationui.Function;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationui.R;

public class TripleTapActivity extends Activity {


    private ConstraintLayout Layout_triple;
    private TextView textView_triple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triple_tap);

        //Layout_triple = (ConstraintLayout) findViewById(R.id.tripleLayout);
        textView_triple = (TextView)findViewById(R.id.tripleView);
        textView_triple.setOnTouchListener(new OnTouchMultipleTapListener() {
            @Override
            public void onMultipleTapEvent(MotionEvent e, int numberOfTaps) {
                if (numberOfTaps == 3) {
                    Toast.makeText(getApplicationContext(), "triple", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplication(), STT_Activity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
