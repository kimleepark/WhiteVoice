package com.example.myapplicationui.User_Interface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplicationui.Conection.whiteVoice;
import com.example.myapplicationui.Function.OnTouchMultipleTapListener;
import com.example.myapplicationui.Function.STT_Activity;
import com.example.myapplicationui.Function.TTSClass;
import com.example.myapplicationui.R;


public class MenuActivity extends Activity {

    private Button btnSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
/*
        btnSet = (Button) findViewById(R.id.setting);
        btnSet.setOnTouchListener(new OnTouchMultipleTapListener() {
            @Override
            public void onMultipleTapEvent(MotionEvent e, int numberOfTaps) {
                if (numberOfTaps == 3) {
                    Toast.makeText(getApplicationContext(), "triple", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplication(), STT_Activity.class);
                    startActivity(intent);
                }
                else if(numberOfTaps == 1){
                    Toast.makeText(getApplicationContext(), "single", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplication(), SettingActivity.class);
                    startActivity(intent);
                }
            }
        });
        if(((whiteVoice)getApplicationContext()).WV==100) {
            TTSClass.Init(getApplication(), "즐겨찾기, 목적지, 설정 중 원하는 메뉴를 말하세요.");
            Intent intentA = new Intent(getApplication(), STT_Activity.class);
            startActivity(intentA);
        }
*/
    }

    public void onClickFavorite(View view) {
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
    }

    public void onClickDestinationV(View view) {
        Intent intent = new Intent(this,DestinationActivity.class);
        intent.putExtra("request", 3);
        startActivity(intent);
    }

    public void onClickSetting(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    public void onClickDestinationT(View view){
        Intent intent = new Intent(this, DestinationActivity.class);
        intent.putExtra("request", 2);
        startActivity(intent);
    }

}
//  public void OnClick(View view) { switch (view.getId()) { case R.id.button: try { new DownloadFilesTask().execute(new URL("파일 다운로드 경로1")); } catch (MalformedURLException e) { e.printStackTrace(); } break; } }
// 이벤트간소화ㄱㄱ
