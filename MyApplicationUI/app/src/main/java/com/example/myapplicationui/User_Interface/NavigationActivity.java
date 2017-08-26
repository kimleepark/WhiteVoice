package com.example.myapplicationui.User_Interface;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationui.CS.CSApi;
import com.example.myapplicationui.CS.CSGetResult;
import com.example.myapplicationui.CS.CSPostConfig;
import com.example.myapplicationui.CS.CSPostResult;
import com.example.myapplicationui.Conection.whiteVoice;
import com.example.myapplicationui.Function.CameraActivity;
import com.example.myapplicationui.Function.DebugClass;
import com.example.myapplicationui.Function.ParsingClass;
import com.example.myapplicationui.Function.TTSClass;
import com.example.myapplicationui.R;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class NavigationActivity extends AppCompatActivity implements SensorEventListener {

    private static final String API_KEY = "vmCH8sw2l_2cXvsCx-wUKQ";

    NavigationActivity NavA = (NavigationActivity)this.NavA;
    //DestinationActivity DesA = (DestinationActivity)DestinationActivity.DesA;

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private SensorManager sm;
    private Sensor s;
    /*TextView MentView;
    TextView ClockView;
    TextView LocationView;
    TextView AtoBView;*/
    //TextView tView;
    boolean vibratorTF = true;
    //LinearLayout layout;

    double mLatitude = 0; //위도
    double mLongitude = 0; //경도
    double dLatitude = 0;   //더미 위도
    double dLongtitude = 0; //더미 경도
    double distanceAToB = 0;
    double AdistanceToB = 0;
    int index = 0, first = 0, disIndex = 0, rotateNum = 0, STACK_POINT = 0, checkFlowOver = 0;
    boolean dataUpdate = false;
    boolean startDataUpdate = false;
    boolean near10m1 = false, near10m2 = true;
    boolean divFour1 = false, divFour2 = true;
    boolean firstGuide1 = false, firstGuide2 = true;
    Location pointA = new Location("A");
    Location pointB = new Location("B");
    Location detectPointA = new Location("dectedA");
    Location detectPointB = new Location("dectedB");
    double detectedX = 0;
    double detectedY = 0;
    double detectedDistance = 0;
    LocationManager lm;

    String clockBasedDirection1;

    ImageView arrow;

    //ArrayList<pathListItem> dumDB = new ArrayList<pathListItem>();

    public static final String KEY_SIMPLE_DATA = "data";

    ParsingClass parsing = new ParsingClass();
    DebugClass Debugs = new DebugClass();

    Vibrator vibrator;
    PopupWindow mPopupWindow;

    int trash;
    @Override
    protected void attachBaseContext(Context newBase) {
        Debugs.logv(new Exception(), "Something to print");
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Debugs.logv(new Exception(), "Something to print");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation2);

        this.setTitle("");
        //layout = (LinearLayout)findViewById(R.id.layoutN);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);   //진동
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        s = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION); // 방향센서
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE); //LocationManager 객체를 얻어온다.
        //ClockView = (TextView)findViewById(R.id.clockView);

       //Log.e("value", target);

        //tView.setText(((whiteVoice)getApplicationContext()).target);
        //tView.setText(parsing.destinationmap);
        //LocationView = (TextView)findViewById(R.id.textL);
        //MentView = (TextView)findViewById(R.id.mentView);
        //AtoBView = (TextView)findViewById(R.id.textAtoB);
        arrow = (ImageView)findViewById(R.id.arrow);
        detectPointA.setLatitude(0);
        detectPointA.setLongitude(0);
        detectPointB.setLatitude(0);
        detectPointB.setLongitude(0);

        /*
        dumDB.add(new pathListItem(1, "출발지",37.011285, 127.264672));
        dumDB.add(new pathListItem(2, "경유지1",37.011384, 127.264283));
        dumDB.add(new pathListItem(3, "경유지2",37.011270, 127.263430));
        dumDB.add(new pathListItem(4, "경유지3",37.010390, 127.263540));
        dumDB.add(new pathListItem(5, "경유지4",37.010491, 127.264221));
        dumDB.add(new pathListItem(6, "경유지5",37.010522, 127.264875));
        dumDB.add(new pathListItem(7, "경유지6",37.010908, 127.264854));
        dumDB.add(new pathListItem(8, "경유지7",37.010957, 127.265527));
        dumDB.add(new pathListItem(9, "목적지",37.011188, 127.265817));
        */

        try{
            //GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자. 순수 GPS 이용
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자. Network WiFi를 이용
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);

            lm.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체를 해주어야 한다.
        }catch (SecurityException ex){

        }
        new ProcessLocation().execute();

        //TTSClass.Init(this, "경로안내를 시작합니다.");
    }


    class ProcessLocation extends AsyncTask<Void, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            Debugs.logv(new Exception(), "Something to print");
            super.onPreExecute();
            dialog= new ProgressDialog(NavigationActivity.this, R.style.DialogCustom); //ProgressDialog 객체 생성
            //dialog.setTitle("Progress");                   //ProgressDialog 제목
            dialog.setMessage("경로검색 중 입니다...");             //ProgressDialog 메세지

            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); //스피너형태의 ProgressDialog 스타일 설정
            dialog.setCanceledOnTouchOutside(false); //ProgressDialog가 진행되는 동안 dialog의 바깥쪽을 눌러 종료하는 것을 금지
            dialog.show(); //ProgressDialog 보여주기
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialog,
                                     int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dialog.dismiss();
                        finish();
                        return true;
                    }
                    return false;
                }
            });
        }
        @Override
        protected String doInBackground(Void... voids) {
            Debugs.logv(new Exception(), "Something to print");
            while (true) {
                if (mLatitude != 0.0 && mLongitude != 0.0) {
                    //Toast.makeText(getApplicationContext(), "좌표설정 완료", Toast.LENGTH_SHORT).show();
                    if(parsing.complete==0) {
                        parsing.complete = 2;
                        parsing.setData(((whiteVoice) getApplicationContext()).target, mLatitude, mLongitude);        //단어 사이에 공백이 있으면 제대로 값이 표시되지 않는 버그 있음.
                        parsing.onLoad();
                    }
                    else if (parsing.complete == 1) {
                        if (parsing.destinationmap.equals("에러")) {
                            Debugs.logv(new Exception(), "sSomething to 걱정");
                            TTSClass.Init(getApplicationContext(), "입력값이 잘못되었거나 GPS오류입니다. 다시 입력해주세요.");
                            Toast.makeText(getApplicationContext(), "입력값이 잘못되었거나 GPS오류입니다. 다시 입력해주세요.", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        break;
                    }

                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            Debugs.logv(new Exception(), "Something to print");
            super.onPostExecute(s);
            //tView.setText(parsing.destinationmap);
            dialog.dismiss();
        }
    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            Log.d("test", "onLocationChanged, location:" + location);
            mLongitude = location.getLongitude(); //경도   int double float long unsigned signed boolean fucking edited text file has been deleted
            mLatitude = location.getLatitude();   //위도

            double altitude = location.getAltitude();   //고도
            float accuracy = location.getAccuracy();    //정확도
            String provider = location.getProvider();   //위치제공자

            //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
            //Network 위치제공자에 의한 위치변화
            //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.

        }
        public void onProviderDisabled(String provider) {
            Debugs.logv(new Exception(), "Something to print");
            // Disabled시
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            Debugs.logv(new Exception(), "Something to print");
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            Debugs.logv(new Exception(), "Something to print");
            // 변경시
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };

    public double bearingP1toP2(double P1_latitude, double P1_longitude, double P2_latitude, double P2_longitude) {
        // 현재 위치 : 위도나 경도는 지구 중심을 기반으로 하는 각도이기 때문에 라디안 각도로 변환한다.
        //Log.e("현위치","x = " + P1_latitude + " y = " + P1_longitude);
       // Log.e("다음위치","x = " + P2_latitude + " y = " + P2_longitude);
        double Cur_Lat_radian = P1_latitude * (Math.PI / 180);
        double Cur_Lon_radian = P1_longitude * (Math.PI / 180);

        // 목표 위치 : 위도나 경도는 지구 중심을 기반으로 하는 각도이기 때문에 라디안 각도로 변환한다.
        double Dest_Lat_radian = P2_latitude * (Math.PI / 180);
        double Dest_Lon_radian = P2_longitude * (Math.PI / 180);

        // radian distance
        double radian_distance = 0;
        radian_distance = Math.acos(Math.sin(Cur_Lat_radian) * Math.sin(Dest_Lat_radian) + Math.cos(Cur_Lat_radian) * Math.cos(Dest_Lat_radian) * Math.cos(Cur_Lon_radian - Dest_Lon_radian));

        // 목적지 이동 방향을 구한다.(현재 좌표에서 다음 좌표로 이동하기 위해서는 방향을 설정해야 한다. 라디안값이다.
        double radian_bearing = Math.acos((Math.sin(Dest_Lat_radian) - Math.sin(Cur_Lat_radian) * Math.cos(radian_distance)) / (Math.cos(Cur_Lat_radian) * Math.sin(radian_distance)));        // acos의 인수로 주어지는 x는 360분법의 각도가 아닌 radian(호도)값이다.

        double true_bearing = 0;
        if (Math.sin(Dest_Lon_radian - Cur_Lon_radian) < 0){
            true_bearing = radian_bearing * (180 / Math.PI);
            true_bearing = 360 - true_bearing;
        }else{
            true_bearing = radian_bearing * (180 / Math.PI);
        }

        //if(true_bearing > 180)
        //  true_bearing = 360 - true_bearing;

        return true_bearing;
    }

    public static Criteria getCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);
        criteria.setSpeedRequired(true);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        return criteria;
    }
/*
    public void onButton2Clicked(View v){
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        double StartX = mLatitude;
        double StartY = mLongitude;

        EditText Destination = (EditText) findViewById(R.id.destinationpoint);

        SimpleData data = new SimpleData(StartX, StartY, Destination);
        intent.putExtra(KEY_SIMPLE_DATA, data);
        startActivityForResult(intent, REQUEST_CODE_MENU);
    }
    */

    @Override
    protected void onResume() { // 화면에 보이기 직전에 센서자원 획득
        super.onResume();

         String path = ((whiteVoice)getApplicationContext()).tapPath;

        if(path!=null){
            File mFile = new File(path);
            new ProcessCloudSight().execute(mFile);
        }

        // 센서의 값이 변경되었을 때 콜백 받기위한 리스너를 등록한다
        sm.registerListener(this,        // 콜백 받을 리스너
                s,            // 콜백 원하는 센서
                SensorManager.SENSOR_DELAY_UI); // 지연시간
        //String locProv = mLocMgr.getBestProvider(new Criteria(), true);
        String locProv = lm.getBestProvider(getCriteria(), true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.requestLocationUpdates(locProv, 0, 0, mLocationListener);
        //mLocMgr.requestLocationUpdates( LocationManager.GPS_PROVIDER, 3000, 3, mLocListener );
    }

    @Override
    protected void onPause() { // 화면을 빠져나가면 즉시 센서자원 반납해야함!!
        super.onPause();
        sm.unregisterListener(this); // 반납할 센서
        lm.removeUpdates(mLocationListener);
    }

    public void onSensorChanged(SensorEvent event) {
        if (parsing.pathListItems.size() != 0) {
            clockBasedDirection1 = "";

            if (mLatitude != 0.0 && mLongitude != 0.0) {   //현재 좌표를 받아오기 시작했는가?
                while (parsing.complete != 1) {

                }
                // 방위각 산출을 위한 value
                // 실제 방위각 산출 및 적용은 현재 좌표 안정화 상태에서 실행
                double trueBearing = 0, degree = 0;
                if (!startDataUpdate) {   //지금 현재 받아온 좌표가 최초 현재좌표인가?
                    startDataUpdate = true;
                    //detectPointA = 방금 받아온 현재 위치
                    detectPointA.setLatitude(mLatitude);    //현재좌표
                    detectPointA.setLongitude(mLongitude);
                    //detectedX, detectedY = 실시간 현재좌표 이전좌표를 기억하기 위한 tmp 데이터
                    detectedX = mLatitude;  //이전좌표에 현재좌표 업데이트 여기서 파싱 클래스 종료됨
                    detectedY = mLongitude;
                    //경로상의 최초 경유지 설정
                    //비교 item index number = 1 에 표시된 x, y 좌표가 되어야함.
                    index++;
                    dLatitude = parsing.pathListItems.get(index).getX();        //에러부분
                    dLongtitude = parsing.pathListItems.get(index).getY();
                    //멘트 필터링
                    trash = 1;
                    mentChange(index);
                    //TTSClass.Init(this, parsing.pathListItems.get(index).getMent());
                } else if (startDataUpdate && index != 0) {  //지금 현재 받아온 좌표가 최초 현재좌표가 아닌가?
                    //실시간 현재좌표의 이전 누적데이터 좌표가 쌓이기 시작했기때문에 본격적인 길안내 시작
                    //다음 경유지 좌표를 계속 업데이트
                    dLatitude = parsing.pathListItems.get(index).getX();
                    dLongtitude = parsing.pathListItems.get(index).getY();
                    //pointA, pointB = 다음 경유지까지의 남은 거리 계산을 위한 value
                    pointA.setLatitude(mLatitude);
                    pointA.setLongitude(mLongitude);
                    pointB.setLatitude(dLatitude);
                    pointB.setLongitude(dLongtitude);
                    //PointA, PointB 사이의 거리 distanceAToB는 좌표가 튀지 않을때 갱신
                    detectPointA.setLatitude(mLatitude);    //현재좌표
                    detectPointA.setLongitude(mLongitude);
                    detectPointB.setLatitude(detectedX);    //현재좌표 이전좌표
                    detectPointB.setLongitude(detectedY);
                    detectedX = mLatitude;  //이전좌표에 현재좌표 업데이트
                    detectedY = mLongitude;
                    //detectedDistance = 좌표가 갑자기 튀는지를 검사하기 위한 value
                    detectedDistance = detectPointA.distanceTo(detectPointB);

                    if (detectedDistance < 50.0) { //실시간 좌표간 거리차이가 50.0m를 넘지 않는가? == 좌표가 튀지 않는가?
                        if (checkFlowOver <= 10) {    //실시간 좌표간 거리차이가 50.0m를 넘지 않는 것이 10회이상 검출되는가?
                            checkFlowOver++;
                        } else if (checkFlowOver == 11) {
                            //현재 위치와 다음 경유지까지의 거리
                            distanceAToB = pointA.distanceTo(pointB);
                            //경로이탈 감지
                            if (!pathDetect(parsing.pathListItems.get(index - 1).getX(), parsing.pathListItems.get(index - 1).getY(), dLatitude, dLongtitude, mLatitude, mLongitude, 20.0)) {
                                //Toast.makeText(getApplicationContext(), "경로를 이탈했습니다.", Toast.LENGTH_SHORT).show();
                            }
                            //방위각 설정
                            trueBearing = bearingP1toP2(mLatitude, mLongitude, dLatitude, dLongtitude);
                            degree = event.values[0] - trueBearing;
                            if (degree < 0) {
                                degree = Math.abs(degree);
                            } else if (degree > 0) {
                                degree = 360 - degree;
                            }
                            if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
                                // 방향센서값이 변경된거라면
                                int tmp1 = (int) (degree / 30);
                                int tmp2 = (int) (event.values[0] / 30);
                                if (tmp1 == 0)
                                    tmp1 = 12;
                                if (tmp2 == 0)
                                    tmp2 = 12;
                                clockBasedDirection1 = tmp1 + "시 방향";
                                //clockBasedDirection2 = tmp2 + "시 방향";
                                //화살표 이미지 방향 설정을 위한 회전도 결정 (현재 10 사이클마다 갱신)
                                if (rotateNum == 0) {
                                    arrow.setImageBitmap(rotateImage(BitmapFactory.decodeResource(getResources(), R.drawable.arrow), (float) degree));
                                    rotateNum++;
                                } else {
                                    rotateNum++;
                                    if (rotateNum == 10) {
                                        rotateNum = 0;
                                    }
                                }
                            }


                            //데이터 표시 항목 설정
                            //LocationView.setText("현재 = X : " + mLatitude + ", Y : " + mLongitude);
                            //ClockView.setText(clockBasedDirection1);
                            //AtoBView.setText(String.valueOf(distanceAToB));
                            Location A = new Location("A");
                            Location B = new Location("B");
                            if (distanceAToB > 5.0) {   //현재 위치와 다음 경유지까지의 거리가 5m 보다 큰가?
                                //목적지 근방 반경 5미터 바깥임.
                                //시계방향이 다음 경유지를 가리키면 진동
                                if (event.values[0] <= 15 && event.values[0] >= 0 && vibratorTF) {
                                    vibrator.vibrate(1500);
                                    vibratorTF = false;
                                } else if (event.values[0] >= 345 && event.values[0] <= 359.999 && vibratorTF) {
                                    vibrator.vibrate(1500);
                                    vibratorTF = false;
                                }

                                if (distanceAToB <= 10.0) {    //경유지까지 10m 안쪽으로 들어왔는가?
                                    near10m1 = true;
                                    if (near10m1 && near10m2) {   //이 안내문을 한 번 이상 실행되었는가?
                                        TTSClass.Init(this, "다음 목적지까지 ,10미터, 근방입니다.");
                                        near10m2 = false;
                                    }
                                } else {      //경유지까지의 거리가 10m 이상인가?
                                    near10m1 = false;
                                    near10m2 = true;
                                }
                                //다음 경유지까지 몇시방향으로 얼마나 남았는지 안내맨트 업데이트
                                //MentView.setText("다음 = X : " + String.valueOf(parsing.pathListItems.get(index - 1).getX()) + ", Y : " + String.valueOf(parsing.pathListItems.get(index - 1).getY()) + "\n" + parsing.mentCopy[index - 1] + "\nindex : " + index + "\nsize : " + parsing.pathListItems.size());
                                //경유지간의 거리를 측정해서 안내음 분배

                                A.setLatitude(parsing.pathListItems.get(index - 1).getX());
                                A.setLongitude(parsing.pathListItems.get(index - 1).getY());
                                B.setLatitude(parsing.pathListItems.get(index).getX());
                                B.setLongitude(parsing.pathListItems.get(index).getY());
                                AdistanceToB = A.distanceTo(B);

                                //다음 경유지까지 몇시방향으로 얼마나 남았는지 안내
                                if (firstGuide2) {    //이번 안내가 최초인가?
                                    TTSClass.Init(this, "현재 위치에서," + clockBasedDirection1 + "으로," + (int) (distanceAToB) + "미터, 남았습니다.");
                                    //tmpClock1 = String.valueOf((int) degree / 30); //다음경유지 시계방향 저장
                                    firstGuide2 = false;
                                }

                                //거리별 분배
                                if (divFour2) {
                                    if (A.distanceTo(B) < 200.0) {   //200m 미만은 반만 나눠서
                                        disIndex = 2;
                                        STACK_POINT = 2;
                                    } else if (A.distanceTo(B) >= 200.0 && A.distanceTo(B) < 500.0) {     //300m 이상, 500m 미만은 3번 나눠서
                                        disIndex = 3;
                                        STACK_POINT = 3;
                                    } else if (A.distanceTo(B) >= 500.0 && A.distanceTo(B) < 1000.0) {     //500m 이상, 1000m 미만은 4번 나눠서
                                        disIndex = 4;
                                        STACK_POINT = 4;
                                    } else {         //1000m 이상은 5번 나눠서
                                        disIndex = 5;
                                        STACK_POINT = 5;
                                    }
                                    divFour2 = false;
                                }
                                if (((A.distanceTo(B) + 5) / disIndex) * STACK_POINT > distanceAToB) {
                                    divFour1 = true;
                                }
                                if (STACK_POINT != 0 && divFour1) {
                                    TTSClass.Init(this, parsing.pathListItems.get(index - 1).getMent() + ", " + clockBasedDirection1 + "으로," + (int) (distanceAToB) + "미터, 남았습니다.");
                                    STACK_POINT--;
                                    divFour1 = false;
                                }
                            } else if (distanceAToB <= 5.0) {  //현재 위치와 다음 경유지까지의 거리가 5m 안으로 들어왔는가?
                                //목적지 근방 반경 5미터에 들어옴.
                                //다음 경유지 좌표 새로 갱신.
                                if (degree < 0) {
                                    degree = Math.abs(degree);
                                } else if (degree > 0) {
                                    degree = 360 - degree;
                                }
                                index++;
                                firstGuide2 = true;
                                divFour2 = true;
                                if (parsing.pathListItems.size() - 1 == index) { //모든 경유지를 경우했는가?
                                    index = 0;
                                    TTSClass.Init(this, "목적지 근방입니다. 안내를 종료합니다.");
                                    Handler mHandler = new Handler();
                                    mHandler.postDelayed(new Runnable() {
                                        public void run() {
                                            finish();
                                        }
                                    }, 4000);
                                }
                            }
                        }
                    } else if (detectedDistance >= 50.0) {
                        checkFlowOver = 0;
                    }
                }
            }
            // 센서값이 변경되었을 때 호출되는 콜백 메서드d
        }
    }

    public void mentChange(int index){
        Debugs.logv(new Exception(), "Something to print");
        trash=0;
        String a = parsing.pathListItems.get(index).getMent();
        String b="";
        String c;
        trash=1;

        if(a.indexOf(")")!=-1){
            if(a.indexOf(")")==a.length()-1){
                b=a.substring(0,a.indexOf("("));
                c=a.substring(a.indexOf("("),a.length());
                b=b+"이동중. 현재 위치는 ";
                c=c+"입니다.";
                a=b+c;
            }
        }

        if(a.indexOf("오른쪽길로")!=-1){
            b=a.substring(0, a.indexOf("오른쪽길로"));
            c=a.substring(a.indexOf("오른쪽길로")+5,a.length());
            a=b+c;
        }
        else if(a.indexOf("오른쪽길") !=-1){
            b=a.substring(0, a.indexOf("오른쪽길"));
            c=a.substring(a.indexOf("오른쪽길")+4,a.length());
            a=b+c;
        }
        if(a.indexOf("왼쪽길로") !=-1){
            b=a.substring(0, a.indexOf("왼쪽길로"));
            c=a.substring(a.indexOf("왼쪽길로")+4,a.length());
            a=b+c;
        }
        else if(a.indexOf("왼쪽길") !=-1 ) {
            b=a.substring(0, a.indexOf("왼쪽길"));
            c=a.substring(a.indexOf("왼쪽길")+3,a.length());
            a=b+c;
        }
        if(a.indexOf("m 이동")!=-1) {
            b=a.substring(0, a.indexOf("m 이동"));
            c=a.substring(a.indexOf("m 이동")+4,a.length());
            if(b.lastIndexOf(" ")!=-1) {
                b=b.substring(0, b.lastIndexOf(" "));
            }
            a=b+c;
        }else if(a.indexOf("이동")!=-1) {
            b=a.substring(0, a.indexOf("이동"));
            c=a.substring(a.indexOf("이동")+2,a.length());
            if(b.lastIndexOf(" ")!=-1)
                b.substring(0,b.lastIndexOf(" "));
            a=b+c;
        }

        if(Pattern.matches("^\\s?[0-9]+$", a))
            a="";

        a=a+".";
        parsing.pathListItems.get(index).setMent(a);
    }

    public boolean pathDetect(double previousX, double previousY, double nextX, double nextY, double myX, double myY, double baseValue){
        // previousX previoisY : 이전위치 위경도
        // nextX nextY : 다음위치 위경도
        // myX myY : 현재위치 위경도
        // baseValue : 경로이탈 최대 반경 (단위 : m)
        // 해론의 공식을 이용한 경로이탈 감지 메소드.
        Location previousPoint = new Location("previous");
        Location nextPoint = new Location("next");
        Location myPoint = new Location("my");
        previousPoint.setLatitude(previousX);
        previousPoint.setLongitude(previousY);
        nextPoint.setLatitude(nextX);
        nextPoint.setLongitude(nextY);
        myPoint.setLatitude(myX);
        myPoint.setLongitude(myY);
        double pToNext = previousPoint.distanceTo(nextPoint);
        double pToMy = previousPoint.distanceTo(myPoint);
        double myToNext = myPoint.distanceTo(nextPoint);
        double HarronS = (pToNext + pToMy + myToNext) / 2;
        double area = Math.abs(Math.sqrt(HarronS*(HarronS - pToNext)*(HarronS - pToMy)*(HarronS - myToNext)));
        if((baseValue * pToNext) / 2 > area){
            return true;
        }else{
            return false;
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 센서의 정확도가 변경되었을 때 호출되는 콜백 메서드
    }

    public void onClickMenuN(View view){
        Debugs.logv(new Exception(), "Something to print");

        View popupView = getLayoutInflater().inflate(R.layout.activity_navigation, null);

        mPopupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setAnimationStyle(0); // 애니메이션 설정(-1:설정안함, 0:설정)
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        //mPopupWindow.setTouchable(false);
        //mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        ((TextView)mPopupWindow.getContentView().findViewById(R.id.textTarget)).setText(parsing.destinationmap);
        mPopupWindow.showAtLocation(popupView, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);

        Button btn1 = (Button) popupView.findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TTSClass.Init(getApplication(), "경로를 재탐색합니다.");

                parsing.setData(((whiteVoice) getApplicationContext()).target, mLatitude, mLongitude);        //단어 사이에 공백이 있으면 제대로 값이 표시되지 않는 버그 있음.

                index = 0;
                disIndex = 0;
                rotateNum = 0;
                STACK_POINT = 0;
                parsing.complete = 0;

                startDataUpdate = false;
                vibratorTF = true;
                near10m1 = false;
                near10m2 = true;
                divFour1 = false;
                divFour2 = true;
                firstGuide2 = true;

                parsing.onLoad();
            }
        });

        Button btn2 = (Button) popupView.findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parsing.pathListItems.size() - 1 >  index) { //최대인덱스에 도달했는가?
                    index++;
                    vibratorTF = true;
                    mentChange(index);
                    TTSClass.Init(getApplication(), parsing.pathListItems.get(index).getMent()+clockBasedDirection1+"으로"+ (int)distanceAToB+"미터 남았습니다."); //이부분을
                } else {
                    TTSClass.Init(getApplication(), "목적지 근방입니다, 다음 경유지가 존재하지 않습니다.");
                }
            }
        });

        Button cancel = (Button) popupView.findViewById(R.id.btn3);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
    }

    public void onClickListen(View view){
        TTSClass.Init(this, "현재 위치에서," + clockBasedDirection1 + "으로," + (int) (distanceAToB) + "미터, 남았습니다.");
    }

    /*
    public void onClickResearch(View view){

        TTSClass.Init(this, "경로를 재탐색합니다.");

        parsing.setData(((whiteVoice) getApplicationContext()).target, mLatitude, mLongitude);        //단어 사이에 공백이 있으면 제대로 값이 표시되지 않는 버그 있음.

        index = 0;
        disIndex = 0; rotateNum = 0;
        STACK_POINT = 0;
        parsing.complete=0;

        startDataUpdate = false;
        vibratorTF = true;
        near10m1 = false; near10m2 = true;
        divFour1 = false; divFour2 = true;
        firstGuide2 = true;

        parsing.onLoad();
    }
    */
    /*
    public void backDestination(View view){
        Debugs.logv(new Exception(), "Something to print");
        DesA.finish();
        Intent intent = new Intent(NavigationActivity.this, DestinationActivity.class);
        startActivity(intent);
        //startActivityForResult(intent,303);
    }*/

 //   Button btn_Popup = (Button)findViewById(R.id.btnTap);
    public void onClickTAP(View view){
        Debugs.logv(new Exception(), "Something to print");
        Intent intent = new Intent(NavigationActivity.this, CameraActivity.class);
        startActivity(intent);
        //startActivityForResult(intent,303);
    }

    class ProcessCloudSight extends AsyncTask<File, Void, String> {

        ProgressDialog dialog;
        boolean flagCS = true;
        String temp = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog= new ProgressDialog(NavigationActivity.this, R.style.DialogCustom); //ProgressDialog 객체 생성
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
            }
            temp = null;
            ((whiteVoice)getApplicationContext()).tapPath = null;
        }
    }

    public Bitmap rotateImage(Bitmap src, float degree){
        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public void onClickCheckingData(View view){
        Debugs.logv(new Exception(), "Something to print");
        Context context = this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        String dataMessage = "";
        for(int num = 0; num < parsing.pathListItems.size(); num++){
            dataMessage += "x : " + parsing.pathListItems.get(num).getX()
                    + "\ny : " + parsing.pathListItems.get(num).getY()
                    + "\nment : " + parsing.pathListItems.get(num).getMent()
                    + "\nidx : " + parsing.pathListItems.get(num).getIdx()
                    + "\n\n";
        }
        dataMessage += "\nSTACK_POINT : " + STACK_POINT
                    + "\ndisIndex : " + disIndex
                    + "\nAtoB : " + (int)AdistanceToB + "m";
        alertDialogBuilder.setTitle("Check Data");
        alertDialogBuilder
                .setMessage(dataMessage)
                .setCancelable(false)
                .setNegativeButton("close", new DialogInterface.OnClickListener() {
                    //@Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

