package com.example.myapplicationui.User_Interface;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.example.myapplicationui.Conection.whiteVoice;
import com.example.myapplicationui.Function.STT_Activity;
import com.example.myapplicationui.Function.TTSClass;
import com.example.myapplicationui.R;
import com.tsengvn.typekit.TypekitContextWrapper;


public class MenuActivity extends Activity {

    private final static int PERMISSIONS_REQUEST_CODE = 100;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        GpsPermissionCheckForMashMallo();
        ActivityCompat.requestPermissions( this,
                new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSIONS_REQUEST_CODE);
        TTSClass.Init(this, "음성메뉴는 위쪽, 터치메뉴는 아래쪽을 터치하세요");
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
        */
        /*
        if(((whiteVoice)getApplicationContext()).WV==100) {
            TTSClass.Init(getApplication(), "즐겨찾기, 목적지, 설정 중 원하는 메뉴를 말하세요.");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent intentA = new Intent(getApplication(), STT_Activity.class);
            startActivity(intentA);
        }*/
    }

    public void onClickFavorite(View view) {
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
    }

    public void onClickDestinationV(View view) {
        TTSClass.Init(this, "목적지를 말하세요");
        ((whiteVoice)getApplicationContext()).sttCode=1;
        Intent intent = new Intent(this, STT_Activity.class);
        startActivity(intent);
        finish();
        /*
        Intent intent = new Intent(this,DestinationActivity.class);
        intent.putExtra("request", 3);
        startActivity(intent);
        */
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

    public void GpsPermissionCheckForMashMallo() {
        //마시멜로우 버전 이하면 if문에 걸리지 않습니다.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("GPS 사용 허가 요청");
            alertDialog.setMessage("경로안내를 위해서는 사용자의 GPS 허가가 필요합니다.\n('허가'를 누르면 GPS 허가 요청창이 뜹니다.)");
            // OK 를 누르게 되면 설정창으로 이동합니다.
            alertDialog.setPositiveButton("허가",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MenuActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                        }
                    });
            // Cancle 하면 종료 합니다.
            alertDialog.setNegativeButton("거절",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        }
    }

}
//  public void OnClick(View view) { switch (view.getId()) { case R.id.button: try { new DownloadFilesTask().execute(new URL("파일 다운로드 경로1")); } catch (MalformedURLException e) { e.printStackTrace(); } break; } }
// 이벤트간소화ㄱㄱ
