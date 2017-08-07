package com.example.myapplicationui.User_Interface;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationui.CS.CSApi;
import com.example.myapplicationui.CS.CSGetResult;
import com.example.myapplicationui.CS.CSPostConfig;
import com.example.myapplicationui.CS.CSPostResult;
import com.example.myapplicationui.Function.CameraActivity;
import com.example.myapplicationui.Function.ParsingClass;
import com.example.myapplicationui.Function.TTSClass;
import com.example.myapplicationui.R;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.File;
import java.io.IOException;

public class NavigationActivity extends AppCompatActivity implements SensorEventListener {

    private static final String API_KEY = "vmCH8sw2l_2cXvsCx-wUKQ";

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();

    String temp = null;

    private SensorManager sm;
    private Sensor s;
    TextView MentView;
    TextView ClockView;
    TextView LocationView;
    TextView AtoBView;

    double mLatitude = 0; //위도
    double mLongitude = 0; //경도
    double dLatitude = 0;   //더미 위도
    double dLongtitude = 0; //더미 경도
    double distanceAToB = 0;
    int index = 0;
    boolean dataUpdate = false;
    Location pointA = new Location("A");
    Location pointB = new Location("B");
    LocationManager lm;
    //ArrayList<pathListItem> dumDB = new ArrayList<pathListItem>();

    //public static final int REQUEST_CODE_MENU = 303;
    public static final String KEY_SIMPLE_DATA = "data";

    ParsingClass parsing = new ParsingClass();

    double startX = 0.0;
    double startY = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        s = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION); // 방향센서
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE); //LocationManager 객체를 얻어온다.
        ClockView = (TextView)findViewById(R.id.clockView);

        Intent intent = new Intent(getIntent());
        String path = intent.getStringExtra("path");
        String target = intent.getStringExtra("value");
       //Log.e("value", target);
        TextView tView = (TextView)findViewById(R.id.targetView);
        tView.setText(target);
        LocationView = (TextView)findViewById(R.id.textL);
        MentView = (TextView)findViewById(R.id.mentView);
        AtoBView = (TextView)findViewById(R.id.textAtoB);

<<<<<<< HEAD
        parsing.setData("하나로마트대덕농협", 37.011272, 127.264478);
        parsing.onLoad();
=======
        parsing.setData("하나로마트대덕농협", 37.011272, 127.264478);        //단어 사이에 공백이 있으면 제대로 값이 표시되지 않는 버그 있음.
        //parsing.setData(target, pointA.getLatitude(), pointB.getLongitude());

          parsing.onLoad();
>>>>>>> 206cac0fb8ff074d35f1d545ed78064fd54d79f6

        TTSClass.Init(this, "경로안내를 시작합니다.");
        /*for(int i = 0; i<= parsing.pathListItems.size();i++) {
            dumDB.add(new pathListItem(i, parsing.pathListItems.get(i).getMent(), parsing.pathListItems.get(i).getX(), parsing.pathListItems.get(i).getY()));
        }

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

        if(path!=null){
            File mFile = new File(path);
            new ProcessCloudSight().execute(mFile);
        }
    }

    class ProcessLocation extends AsyncTask<Void, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog= new ProgressDialog(NavigationActivity.this); //ProgressDialog 객체 생성
            //dialog.setTitle("Progress");                   //ProgressDialog 제목
            dialog.setMessage("분석중입니다...");             //ProgressDialog 메세지
            dialog.setCancelable(false);                      //종료금지
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); //스피너형태의 ProgressDialog 스타일 설정
            //dialog.setCanceledOnTouchOutside(false); //ProgressDialog가 진행되는 동안 dialog의 바깥쪽을 눌러 종료하는 것을 금지
            dialog.show(); //ProgressDialog 보여주기
        }
        @Override
        protected String doInBackground(Void... voids) {
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
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
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
            // Disabled시
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
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
        // 센서값이 변경되었을 때 호출되는 콜백 메서드
        String clockBasedDirection1 = "", clockBasedDirection2 = "";
        double trueBearing = bearingP1toP2(mLatitude,mLongitude,dLatitude,dLongtitude);
        double degree = event.values[0]- trueBearing;

        /*if(index == 0 && dataUpdate) {
            startX = mLatitude;
            startY = mLongitude;
        }*/

        //거리계산을 위한 위경도 설정
        try{
            if(mLatitude != 0 && mLongitude !=0){
                pointA.setLatitude(mLatitude);
                pointA.setLongitude(mLongitude);
                dataUpdate = true;
            }
            pointB.setLatitude(dLatitude);
            pointB.setLongitude(dLongtitude);
            distanceAToB = pointA.distanceTo(pointB);
        }catch (Exception e){

        }

        if(index==0){
            TTSClass.Init(this, parsing.pathListItems.get(1).getMent());
            index++;
        }

        try{    //데이터 가져와서 사용하기.
            if(distanceAToB > 5.0){
                dLatitude = parsing.pathListItems.get(index).getX();
                dLongtitude = parsing.pathListItems.get(index).getY();
                MentView.setText("X : " + String.valueOf(parsing.pathListItems.get(index).getX()) + ", Y : " + String.valueOf(parsing.pathListItems.get(index).getY()));
            }else if(dataUpdate){
                if(distanceAToB <= 5.0 && index>=1){
                    index++;
                    TTSClass.Init(this, parsing.pathListItems.get(index).getMent());
                }
            }
            dLatitude = parsing.pathListItems.get(index).getX();
            dLongtitude = parsing.pathListItems.get(index).getY();
            pointB.setLatitude(dLatitude);
            pointB.setLongitude(dLongtitude);
            if(index == parsing.pathListItems.size()){
                index = 0;
                dataUpdate = false;
            }
        }catch (Exception e){

        }

        if(degree < 0){
            degree = Math.abs(degree);
        }else if(degree > 0){
            degree = 360 - degree;
        }
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            // 방향센서값이 변경된거라면
            int tmp = (int)(degree / 30);
            int tmp2 = (int)(event.values[0] / 30);
            if(tmp == 0)
                tmp = 12;
            if(tmp2 == 0)
                tmp2 = 12;
            clockBasedDirection2 = tmp + "시 방향";
            clockBasedDirection1 = tmp2 + "시 방향";
        }
        LocationView.setText("X : " + mLatitude + ", Y : " + mLongitude);
        ClockView.setText(clockBasedDirection2);
        AtoBView.setText(String.valueOf(distanceAToB));
        if(!pathDetect(parsing.pathListItems.get(index-1).getX(), parsing.pathListItems.get(index-1).getY(), dLatitude, dLongtitude, mLatitude, mLongitude, 15.0)){
            Toast.makeText(getApplicationContext(), "경로를 이탈했습니다.", Toast.LENGTH_SHORT).show();
        }

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
    public void onClickResearch(View view){
        parsing.setData("하나로마트대덕농협", 37.011272, 127.264478);
        parsing.onLoad();
    }
    public void onClickTAP(View view){
        Intent intent = new Intent(NavigationActivity.this, CameraActivity.class);
        startActivity(intent);
        //startActivityForResult(intent,303);
    }

    class ProcessCloudSight extends AsyncTask<File, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog= new ProgressDialog(NavigationActivity.this); //ProgressDialog 객체 생성
            //dialog.setTitle("Progress");                   //ProgressDialog 제목
            dialog.setMessage("분석중입니다...");             //ProgressDialog 메세지
            dialog.setCancelable(false);                      //종료금지
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); //스피너형태의 ProgressDialog 스타일 설정
            //dialog.setCanceledOnTouchOutside(false); //ProgressDialog가 진행되는 동안 dialog의 바깥쪽을 눌러 종료하는 것을 금지
            dialog.show(); //ProgressDialog 보여주기
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
                    while (true) {  //몇번이상 실패할경우 재캡쳐 코드 추가하기
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
            TTSClass.Init(getApplication(), name);
            temp = null;
        }
    }
}
