package com.example.myapplicationui.User_Interface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;

import com.example.myapplicationui.Conection.whiteVoice;
import com.example.myapplicationui.Function.TTSClass;
import com.example.myapplicationui.R;

public class NavigationPopupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_navigation_popup);
        TextView dataDisplay = (TextView)findViewById(R.id.popUp);

        ((whiteVoice) getApplicationContext()).dataExplain = false;
        Intent getData = getIntent();
        double Second = getData.getIntExtra("fullDistance",1) / 0.8;
        int hour = (int)Second / 3600, min = (int)Second % 3600 / 60;

        dataDisplay.setText("[입력]\n" + getData.getStringExtra("getVoiceString") + "\n\n[검색된 목적지]\n" + getData.getStringExtra("destinationmap") + "\n\n[총 거리]\n" + getData.getIntExtra("fullDistance",1) + "m\n\n[예상소요시간]\n" + hour + "시간 " + min +"분 예정");
        TTSClass.Init(this, "현재 입력된, 목적지는," + getData.getStringExtra("getVoiceString") + " ,이며, 가장 근접하게, 검색된, 목적지는, " + getData.getStringExtra("destinationmap")+ ", 입니다.                                                   총 " + getData.getIntExtra("fullDistance",1) + ",미터,                                            거리이며, 약,  " + hour + ",시간," + min + ",분, 소요될 예정입니다.                                        ");
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, 15000);
    }
}
