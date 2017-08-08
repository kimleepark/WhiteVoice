package com.example.myapplicationui.Function;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.myapplicationui.User_Interface.DestinationActivity;
import com.example.myapplicationui.User_Interface.FavoriteActivity;
import com.example.myapplicationui.User_Interface.NavigationActivity;
import com.example.myapplicationui.User_Interface.SettingActivity;
import com.example.myapplicationui.Conection.whiteVoice;

import java.util.ArrayList;

public class STT_Activity extends Activity {

    private final int RESULT_SPEECH = 101;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView(R.layout.activity_popup);
        //resultView = (TextView)findViewById(R.id.txtText);

        doSTT();
    }

    private void doSTT(){
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "지금 말하세요");

        try {
            startActivityForResult(intent, RESULT_SPEECH);
        }
        catch (ActivityNotFoundException e){
            Toast.makeText(getApplicationContext(), "오류입니다", Toast.LENGTH_SHORT).show();
            e.getStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==RESULT_SPEECH) {
            ArrayList<String> sstResult = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String result_stt = sstResult.get(0);

            String replace_sst = "";
            replace_sst = result_stt.replace(" ", "");

            TTSClass.Init(this, replace_sst);

            if (((whiteVoice)getApplicationContext()).sttCode==1) { //목적지
                ((whiteVoice)getApplicationContext()).target = replace_sst;
                intent = new Intent(this, NavigationActivity.class);
                startActivity(intent);
                ((whiteVoice)getApplicationContext()).sttCode = 0;
                finish();
            }else if (((whiteVoice)getApplicationContext()).sttCode==0){ // 메뉴
                if (SDClass.distance(replace_sst,"목적지")<=1) {
                    intent = new Intent(this, DestinationActivity.class);
                    startActivity(intent);
                    finish();
                } else if (SDClass.distance(replace_sst,"즐겨찾기")<=2) {
                    intent = new Intent(this, FavoriteActivity.class);
                    startActivity(intent);
                    finish();
                } else if (SDClass.distance(replace_sst,"설정")<=1) {
                    intent = new Intent(this, SettingActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(STT_Activity.this, "없는 메뉴입니다.", Toast.LENGTH_SHORT).show();
                    doSTT();
                }
            }
            else if(((whiteVoice)getApplicationContext()).sttCode==2){ //즐겨찾기
                intent = new Intent(this, FavoriteActivity.class);
                intent.putExtra("value", replace_sst);
                setResult(Activity.RESULT_OK, intent);
                ((whiteVoice)getApplicationContext()).sttCode=0;
                finish();
            }
        }
    }

    public void onClickCancel(View view){
        finish();
    }
}

