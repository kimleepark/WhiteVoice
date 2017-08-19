package com.example.myapplicationui.User_Interface;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import com.example.myapplicationui.Function.SharedPrefManager;
import com.example.myapplicationui.R;
import com.tsengvn.typekit.TypekitContextWrapper;

public class SettingActivity extends AppCompatActivity {

    private SharedPrefManager mSharedPrefs;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mSharedPrefs = SharedPrefManager.getmInstance(this);
        int tmpS = changeProgress(mSharedPrefs.getVoiceSpeed());

        SeekBar seekBarV = (SeekBar)findViewById(R.id.speedBar);
        seekBarV.setProgress(tmpS);

        seekBarV.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                mSharedPrefs.setVoiceSpeed(changeFloat(i));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        /*
        final RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroup1);

        RadioButton radioM = (RadioButton)findViewById(R.id.voiceBtnM);
        RadioButton radioW = (RadioButton)findViewById(R.id.voiceBtnW);

        radioM.setChecked(true);
        */
    }

    public int changeProgress(float f){
        int progress = 2;
        if(f==0.6f){
            progress = 0;
        } else if(f==0.8f){
            progress = 1;
        } else if(f==1.0f){
            progress = 2;
        }
        else if(f==1.2f){
            progress = 3;
        }
        else if(f==1.4f){
            progress = 4;
        }
        else if(f==1.6f){
            progress = 5;
        }
        return progress;
    }

    public float changeFloat(int i){
        float progress = 1.0f;
        if(i==0){
            progress = 0.6f;
        } else if(i==1){
            progress = 0.8f;
        } else if(i==2){
            progress = 1.0f;
        }
        else if(i==3){
            progress = 1.2f;
        }
        else if(i==4){
            progress = 1.4f;
        }
        else if(i==5){
            progress = 1.6f;
        }
        return progress;
    }

}
