package com.example.myapplicationui.User_Interface;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.example.myapplicationui.Conection.whiteVoice;
import com.example.myapplicationui.R;
import com.tsengvn.typekit.TypekitContextWrapper;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        SeekBar seekBarV = (SeekBar)findViewById(R.id.speedBar);

        seekBarV.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float tmpS = 0.8f;
                switch (i) {
                    //case 0: tmpS += 0.1f; break;
                    case 1: tmpS += 0.1f; break;
                    case 2: tmpS = 0.2f; break;
                    case 3: tmpS = 0.3f; break;
                    case 4: tmpS = 0.4f; break;
                }
                ((whiteVoice)getApplicationContext()).vSpeed = tmpS;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroup1);

        RadioButton radioM = (RadioButton)findViewById(R.id.voiceBtnM);
        RadioButton radioW = (RadioButton)findViewById(R.id.voiceBtnW);

        radioM.setChecked(true);

    }
}
