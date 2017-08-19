package com.example.myapplicationui.User_Interface;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationui.Conection.ListViewItem;
import com.example.myapplicationui.Conection.whiteVoice;
import com.example.myapplicationui.Function.TTSClass;
import com.example.myapplicationui.R;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity{

    private final int RESULT_SPEECH = 101;
    public static Context mContext;
    private ListView listview;
    private FavoriteAdapter adapter;
    private ArrayList<ListViewItem> items = new ArrayList<ListViewItem>() ;
    ListViewItem item;

    private final String fileName = "items.list" ;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
    /*
    public boolean loadItemsFromDB(ArrayList<ListViewItem> list) {

        if (list == null) {
            list = new ArrayList<ListViewItem>() ;
        }

        // 아이템 생성.
        item = new ListViewItem() ;
        item.setText("우리집") ;
        list.add(item) ;

        item = new ListViewItem() ;
        item.setText("학교") ;
        list.add(item) ;

        item = new ListViewItem() ;
        item.setText("카페") ;
        list.add(item) ;

        return true ;
    }
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        listview = (ListView) findViewById(R.id.favoriteList);


        //TTSClass.Init(this);

        mContext = this;

        // items 로드.
        //loadItemsFromDB(items);


        // Adapter 생성
        adapter = new FavoriteAdapter(this, R.layout.favorite_listview, items);
        listview.setAdapter(adapter) ;

        loadItemsFromFile();
        adapter.notifyDataSetChanged();

        String strNew = getIntent().getStringExtra("value");
        if(strNew!=null) {
            if (strNew.length() > 0) {
                // 리스트에 문자열 추가.
                item = new ListViewItem();
                item.setText(strNew);
                items.add(item);

                // 에디트텍스트 내용 초기화.
                //editTextNew.setText("") ;

                // 리스트뷰 갱신
                adapter.notifyDataSetChanged();

                // 리스트뷰 아이템들을 파일에 저장.
                saveItemsToFile();
            }
        }


        // 리스트뷰 참조 및 Adapter달기
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                ListViewItem itemStr = (ListViewItem)parent.getItemAtPosition(position); //아이템 받아오기
                ((whiteVoice)getApplicationContext()).target = itemStr.getText();
                Intent intent = new Intent(getApplication(), NavigationActivity.class);
                startActivity(intent);
            }
        });

        /*
        String[] tempArray = new String[10];
        //읽어줄 즐겨찾기 리스트 하나의 스트링으로 만들기
        for(int i = 0; i < items.size(); i++) {
            tempArray[i] = items.get(i).getText();
        }

        ((whiteVoice)getApplicationContext()).sttCode = 2; //음성인식 구분
        TTSClass.Init(this, tempArray);
        Intent intentA = new Intent(this, STT_Activity.class);
        startActivityForResult(intentA, 110);
        */
    }

    private void saveItemsToFile() {
        File file = new File(getFilesDir(), fileName) ;
        FileWriter fw = null ;
        BufferedWriter bufwr = null ;

        try {
            // open file.
            fw = new FileWriter(file) ;
            bufwr = new BufferedWriter(fw) ;
            String str ="";
            for (int i = 0; i<items.size(); i++ ) {
                str = items.get(i).getText();
                bufwr.write(str) ;
                bufwr.newLine() ;
            }

            // write data to the file.
            bufwr.flush() ;

        } catch (Exception e) {
            e.printStackTrace() ;
        }

        try {
            // close file.
            if (bufwr != null) {
                bufwr.close();
            }

            if (fw != null) {
                fw.close();
            }
        } catch (Exception e) {
            e.printStackTrace() ;
        }
    }

    private void loadItemsFromFile() {
        File file = new File(getFilesDir(), fileName) ;
        FileReader fr = null;
        BufferedReader bufrd = null;
        String str;

        if (file.exists()) {
            try {
                // open file.
                fr = new FileReader(file) ;
                bufrd = new BufferedReader(fr) ;

                while ((str = bufrd.readLine()) != null) {
                    item = new ListViewItem();
                    item.setText(str);
                    items.add(item);
                }

                bufrd.close() ;
                fr.close() ;
            } catch (Exception e) {
                e.printStackTrace() ;
            }
        }
    }

    public void onClickAdd(View view){
        TTSClass.Init(this, "추가할 목적지를 말하세요");
        doSTT();
    }

    private void doSTT(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("안내");
        builder.setMessage("추가할 목적지를 말하세요");
        final AlertDialog dialog = builder.show();
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(25);

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
        }, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK && requestCode == RESULT_SPEECH){
            ArrayList<String> sstResult = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String result_stt = sstResult.get(0);

            String replace_sst = "";
            replace_sst = result_stt.replace(" ", "");

            // 리스트에 문자열 추가.
            item = new ListViewItem();
            item.setText(replace_sst);
            items.add(item);

            // 에디트텍스트 내용 초기화.
            //editTextNew.setText("") ;

            // 리스트뷰 갱신
            adapter.notifyDataSetChanged();

            // 리스트뷰 아이템들을 파일에 저장.
            saveItemsToFile();

            TTSClass.Init(this, replace_sst);

        }/*
        else if(resultCode == RESULT_OK && requestCode == 110){
            String fData= data.getStringExtra("value");
            for(int i = 0; i < items.size(); i++) {
                tempText = items.get(i).getText();
                int temp = SDClass.distance(tempText,fData);
                if(temp<=1){
                    Toast.makeText(getApplicationContext(), "문자열있음 : "+ temp, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplication(), NavigationActivity.class);
                    intent.putExtra("value", tempText);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "문자열없음: "+ temp, Toast.LENGTH_SHORT).show();
                }
            }
        }*/
    }

    public void RemoveData(int nPosition){
        adapter.remove(items.get(nPosition));

        listview.clearChoices();

        adapter.notifyDataSetChanged();

        saveItemsToFile();
    }

}
