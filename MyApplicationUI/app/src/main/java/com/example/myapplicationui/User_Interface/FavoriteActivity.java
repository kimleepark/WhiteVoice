package com.example.myapplicationui.User_Interface;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplicationui.Function.STT_Activity;
import com.example.myapplicationui.Conection.ListViewItem;
import com.example.myapplicationui.R;
import com.example.myapplicationui.Function.SDClass;
import com.example.myapplicationui.Function.TTSClass;
import com.example.myapplicationui.Conection.whiteVoice;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity{

    public static Context mContext;
    private ListView listview;
    private FavoriteAdapter adapter;
    ArrayList<ListViewItem> items = new ArrayList<ListViewItem>() ;
    ListViewItem item;
    String tempText = "";

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

        item = new ListViewItem() ;
        item.setText("식당") ;
        list.add(item) ;

        return true ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        //TTSClass.Init(this);

        mContext = this;

        // items 로드.
        loadItemsFromDB(items) ;

        // Adapter 생성
        adapter = new FavoriteAdapter(this, R.layout.favorite_listview, items) ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.favoriteList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                ListViewItem item = (ListViewItem)parent.getItemAtPosition(position); //아이템 받아오기
                ((whiteVoice)getApplicationContext()).target = item.getText();
                Intent intent = new Intent(getApplication(), NavigationActivity.class);
                startActivity(intent);
            }
        });

        String[] tempArray = new String[10];
        //읽어줄 즐겨찾기 리스트 하나의 스트링으로 만들기
        for(int i = 0; i < items.size(); i++) {
            tempArray[i] = items.get(i).getText();
        }
        if(((whiteVoice)getApplicationContext()).WV==100) {
            ((whiteVoice)getApplicationContext()).sttCode = 2; //음성인식 구분
            TTSClass.Init(this, tempArray);
            Intent intentA = new Intent(this, STT_Activity.class);
            startActivityForResult(intentA, 110);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    public void onClickAdd(View view){
        Intent intent = new Intent(this, DestinationActivity.class);
        intent.putExtra("request", 1);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK && requestCode == 1){
            String addData= data.getStringExtra("value");
            item = new ListViewItem();
            item.setText(addData);
            items.add(item);
        }
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
        }
    }

    public void RemoveData(int nPosition){
        this.adapter.remove(this.items.get(nPosition));
    }

    /*@Override
    public void onListBtnClick(int position) {
        Toast.makeText(this, Integer.toString(position+1) + " Item is selected..", Toast.LENGTH_SHORT).show() ;
    }*/

}
