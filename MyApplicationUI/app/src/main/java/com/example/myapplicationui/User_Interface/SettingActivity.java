package com.example.myapplicationui.User_Interface;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.myapplicationui.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        final RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroup1);

        RadioButton radioM = (RadioButton)findViewById(R.id.voiceBtnM);
        RadioButton radioW = (RadioButton)findViewById(R.id.voiceBtnW);

        radioM.setChecked(true);

    }
}
