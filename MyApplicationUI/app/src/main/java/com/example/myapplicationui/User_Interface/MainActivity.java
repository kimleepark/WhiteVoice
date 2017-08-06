package com.example.myapplicationui.User_Interface;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myapplicationui.R;
import com.example.myapplicationui.Function.TTSClass;
import com.example.myapplicationui.Conection.whiteVoice;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TTSClass.Init(this, "음성메뉴는 위쪽, 터치메뉴는 아래쪽을 터치하세요");
    }

    public void onClickVoice(View view) {
        ((whiteVoice)getApplicationContext()).WV = 100;
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public void onClickTouch(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TTSClass.onDestroy();
    }
}
