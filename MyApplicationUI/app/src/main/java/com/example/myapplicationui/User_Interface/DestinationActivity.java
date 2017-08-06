package com.example.myapplicationui.User_Interface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.myapplicationui.Conection.whiteVoice;
import com.example.myapplicationui.Function.STT_Activity;
import com.example.myapplicationui.Function.TTSClass;
import com.example.myapplicationui.R;

public class DestinationActivity extends AppCompatActivity {

    private static final int REQUEST_DA = 301;
    static final String[] LIST_SEARCH = {"우체국", "롯데리아", "이마트"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_SEARCH);

        ListView listview = (ListView) findViewById(R.id.searchList);
        listview.setAdapter(adapter);
        int request = getIntent().getIntExtra("request",-1);
        switch(request) {
            case 1:
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {

                        String strText = (String) parent.getItemAtPosition(position);
                        Intent intent = new Intent();
                        intent.putExtra("value", strText);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });
                break;

            case 2:
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        String strText = (String) parent.getItemAtPosition(position);
                        Intent intent = new Intent(getApplication(), NavigationActivity.class);
                        intent.putExtra("value", strText);
                        startActivity(intent);
                        finish();
                    }
                });
                break;
        }

        TTSClass.Init(this, "목적지를 말하세요");
        Intent intent = new Intent(this, STT_Activity.class);
        ((whiteVoice) getApplicationContext()).sttCode = 1;
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode==RESULT_OK && requestCode==REQUEST_DA) {
            EditText editText = (EditText) findViewById(R.id.editDA);
            editText.setText(data.getStringExtra("DA"));
        }
    }
}
