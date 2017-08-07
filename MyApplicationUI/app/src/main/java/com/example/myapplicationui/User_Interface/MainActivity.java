package com.example.myapplicationui.User_Interface;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.example.myapplicationui.Conection.whiteVoice;
import com.example.myapplicationui.Function.TTSClass;
import com.example.myapplicationui.R;

public class MainActivity extends Activity {
<<<<<<< HEAD

=======
>>>>>>> 206cac0fb8ff074d35f1d545ed78064fd54d79f6
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //GPS 허가
        GpsPermissionCheckForMashMallo();
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

    public void GpsPermissionCheckForMashMallo() {
        //마시멜로우 버전 이하면 if문에 걸리지 않습니다.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("GPS 사용 허가 요청");
            alertDialog.setMessage("앰버요청 발견을 알리기위해서는 사용자의 GPS 허가가 필요합니다.\n('허가'를 누르면 GPS 허가 요청창이 뜹니다.)");
            // OK 를 누르게 되면 설정창으로 이동합니다.
            alertDialog.setPositiveButton("허가",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
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
