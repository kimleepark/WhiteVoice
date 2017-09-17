package com.example.myapplicationui.User_Interface;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationui.CS.CSApi;
import com.example.myapplicationui.CS.CSGetResult;
import com.example.myapplicationui.CS.CSPostConfig;
import com.example.myapplicationui.CS.CSPostResult;
import com.example.myapplicationui.Conection.whiteVoice;
import com.example.myapplicationui.Function.CameraActivity;
import com.example.myapplicationui.Function.TTSClass;
import com.example.myapplicationui.R;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.myapplicationui.User_Interface.NavigationActivity.HTTP_TRANSPORT;
import static com.example.myapplicationui.User_Interface.NavigationActivity.JSON_FACTORY;


public class MenuActivity extends Activity {

    private static final String API_KEY = "vmCH8sw2l_2cXvsCx-wUKQ";
    private final static int PERMISSIONS_REQUEST_CODE = 100;
    private final int RESULT_SPEECH = 101;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.setTitle("");

        GpsPermissionCheckForMashMallo();
        ActivityCompat.requestPermissions( this,
                new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSIONS_REQUEST_CODE);
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

    @Override
    protected void onResume() {
        super.onResume();

        String path = ((whiteVoice)getApplicationContext()).tapPath;

        if(path!=null){
            File mFile = new File(path);
            new ProcessCloudSight().execute(mFile);
        }
    }

    public void onClickFavorite(View view) {
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
    }


    public void onClickDestinationV(View view) {
        //TTSClass.Init(this, "목적지를 말하세요");
        //doSTT();
        /*Intent intent = new Intent(this, STT_Activity.class);
        startActivity(intent);*/
        /*
        Intent intent = new Intent(this,DestinationActivity.class);
        intent.putExtra("request", 3);
        startActivity(intent);
        */


        LayoutInflater inflater=getLayoutInflater();

        //res폴더>>layout폴더>>dialog_addmember.xml 레이아웃 리소스 파일로 View 객체 생성
        //Dialog의 listener에서 사용하기 위해 final로 참조변수 선언
        final View dialogView= inflater.inflate(R.layout.activity_search_dialog, null);

        //멤버의 세부내역 입력 Dialog 생성 및 보이기
        AlertDialog.Builder buider= new AlertDialog.Builder(this); //AlertDialog.Builder 객체 생성
        //buider.setTitle("Member Information"); //Dialog 제목
        buider.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)

        //설정한 값으로 AlertDialog 객체 생성
        final AlertDialog dialog=buider.create();
        //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
        dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
        //Dialog 보이기
        dialog.show();

        LinearLayout btnV = (LinearLayout)dialogView.findViewById(R.id.btnSearch_voice);
        btnV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TTSClass.Init(getApplication(), "목적지를 말하세요");
                doSTT();
                dialog.dismiss();
            }
        });

        LinearLayout btnT = (LinearLayout)dialogView.findViewById(R.id.btnSearch_touch);
        btnT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), DestinationActivity.class);
                intent.putExtra("ABC", "2");
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }


    public void onClickSetting(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }
    /*

    public void onClickDestinationT(View view){
        Intent intent = new Intent(this, DestinationActivity.class);
        startActivity(intent);
    }
    */

    public void onClickMenuTap(View v){
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    private void doSTT(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("안내");
        builder.setMessage("목적지를 말하세요");
        final AlertDialog dialog = builder.show();
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(35);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable()  {
            public void run() {
                dialog.dismiss();

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
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
        }, 1450);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RESULT_SPEECH) {
            ArrayList<String> sstResult = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String result_stt = sstResult.get(0);

            String replace_sst = "";
            replace_sst = result_stt.replace(" ", "");

            TTSClass.Init(this, replace_sst);
            ((whiteVoice)getApplicationContext()).target = replace_sst;
            Intent intent = new Intent(this, NavigationActivity.class);
            startActivity(intent);
        }
    }

    class ProcessCloudSight extends AsyncTask<File, Void, String> {

        ProgressDialog dialog;
        boolean flagCS = true;
        String temp = null;
        String ment;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog= new ProgressDialog(MenuActivity.this, R.style.DialogCustom); //ProgressDialog 객체 생성
            //dialog.setTitle("Progress");                   //ProgressDialog 제목
            dialog.setMessage("분석중입니다...");             //ProgressDialog 메세지
            //dialog.setCancelable(false);                      //종료금지
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); //스피너형태의 ProgressDialog 스타일 설정
            dialog.setCanceledOnTouchOutside(false); //ProgressDialog가 진행되는 동안 dialog의 바깥쪽을 눌러 종료하는 것을 금지
            dialog.show(); //ProgressDialog 보여주기

            // Dialog Cancle시 Event 받기
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialog,
                                     int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dialog.dismiss();
                        flagCS = false;
                        return true;
                    }
                    return false;
                }
            });
        }

        @Override
        protected String doInBackground(File... params) {
            try {
                CSApi api = new CSApi(
                        HTTP_TRANSPORT,
                        JSON_FACTORY,
                        API_KEY
                );

                CSPostConfig imageToPost = CSPostConfig.newBuilder().withLanguage("ko-KR").withLocale("ko-KR")
                        .withImage(params[0])
                        //.withRemoteImageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRFhznR3_uOyGeN0luvThTUZRRWy1JPkqestP1TjePlJNXfq5g4")
                        .build();

                CSPostResult portResult = api.postImage(imageToPost);
                Log.e("post", "Post result: " + portResult);
                try {
                    //Thread.sleep(30000);
                    CSGetResult scoredResult = api.getImage(portResult);
                    Log.e("scored", "Scored result: " + scoredResult);

                    int i = 0;
                    while (flagCS) {  //몇번이상 실패할경우 재캡쳐 코드 추가하기
                        i++;
                        if(i>=10) {
                            temp = "다시 캡쳐하세요";
                            break;
                        }
                        else {
                            if (scoredResult.getStatus().equals("completed")) {
                                temp = scoredResult.getName();
                                Log.e("completed", "status : " + scoredResult.getStatus());
                                Log.e("completed", "name : " + scoredResult.getName());
                                break;
                            } else {
                                scoredResult = api.getImage(portResult);
                                Thread.sleep(3500);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.getCause();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return temp;
        }

        @Override
        protected void onPostExecute(String name) {
            super.onPostExecute(name);

            dialog.dismiss();
            if(name!=null)
            {
                TTSClass.Init(getApplication(), name);
                ment = name;
            }

            LayoutInflater inflater=getLayoutInflater();

            //Dialog의 listener에서 사용하기 위해 final로 참조변수 선언
            final View dialogView= inflater.inflate(R.layout.activity_tap, null);

            //멤버의 세부내역 입력 Dialog 생성 및 보이기
            AlertDialog.Builder buider= new AlertDialog.Builder(MenuActivity.this); //AlertDialog.Builder 객체 생성
            //buider.setTitle("Member Information"); //Dialog 제목
            buider.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)

            //설정한 값으로 AlertDialog 객체 생성
            final AlertDialog mDialog = buider.create();
            //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
            mDialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
            //Dialog 보이기
            mDialog.show();

            File imgFile = new File(((whiteVoice)getApplicationContext()).tapPath);
            ((whiteVoice)getApplicationContext()).tapPath = null;

            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                ImageView imageView = (ImageView)dialogView.findViewById(R.id.imageViewTap);

                imageView.setImageBitmap(myBitmap);
            }

            TextView viewMent = (TextView)dialogView.findViewById(R.id.textMentTap);
            viewMent.setText(ment);

            ImageView btnA = (ImageView) dialogView.findViewById(R.id.btnAgainTap);
            btnA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TTSClass.Init(getApplication(), ment);
                }
            });

            Button btnC = (Button)dialogView.findViewById(R.id.btnCloseTap);
            btnC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                    temp = null;
                }
            });

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TTSClass.speechStop();
    }

    @Override
    public void onBackPressed() {

        LayoutInflater inflater=getLayoutInflater();

        //res폴더>>layout폴더>>dialog_addmember.xml 레이아웃 리소스 파일로 View 객체 생성
        //Dialog의 listener에서 사용하기 위해 final로 참조변수 선언
        final View dialogView= inflater.inflate(R.layout.activity_end, null);

        //멤버의 세부내역 입력 Dialog 생성 및 보이기
        AlertDialog.Builder buider= new AlertDialog.Builder(this); //AlertDialog.Builder 객체 생성
        //buider.setTitle("Member Information"); //Dialog 제목
        buider.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)

        //설정한 값으로 AlertDialog 객체 생성
        final AlertDialog dialog=buider.create();
        //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
        dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
        //Dialog 보이기
        dialog.show();

        Button btnE = (Button)dialogView.findViewById(R.id.btnEnd);
        btnE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuActivity.this.finish();
                dialog.dismiss();
            }
        });

        Button btnC = (Button)dialogView.findViewById(R.id.btnCancel);
        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void GpsPermissionCheckForMashMallo() {
        //마시멜로우 버전 이하면 if문에 걸리지 않습니다.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("GPS 사용 허가 요청");
            alertDialog.setMessage("경로안내를 위해서는 사용자의 GPS 허가가 필요합니다.\n('허가'를 누르면 GPS 허가 요청창이 뜹니다.)");
            // OK 를 누르게 되면 설정창으로 이동합니다.
            alertDialog.setPositiveButton("허가",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {*/
                            ActivityCompat.requestPermissions(MenuActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                        }/*
                    });
            // Cancle 하면 종료 합니다.
            alertDialog.setNegativeButton("거절",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        }*/
    }

}
//  public void OnClick(View view) { switch (view.getId()) { case R.id.button: try { new DownloadFilesTask().execute(new URL("파일 다운로드 경로1")); } catch (MalformedURLException e) { e.printStackTrace(); } break; } }
// 이벤트간소화ㄱㄱ
