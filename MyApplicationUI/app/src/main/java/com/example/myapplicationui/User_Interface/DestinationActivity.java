package com.example.myapplicationui.User_Interface;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplicationui.Conection.whiteVoice;
import com.example.myapplicationui.Function.TTSClass;
import com.example.myapplicationui.R;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;

public class DestinationActivity extends AppCompatActivity {

    EditText editText;
    private final int RESULT_SPEECH = 101;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);
        editText = (EditText) findViewById(R.id.editDA);
        //Button BtnOK = (Button)findViewById(R.id.btnOK);

        /*
        int request = getIntent().getIntExtra("request", -1);
        switch(request) {
            case 1:
                BtnOK.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //setContentView(R.layout.activity_destination);
                        String strText = editText.getText().toString().replace(" ", "");
                        Intent intent = new Intent();
                        intent.putExtra("value", strText);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });
                break;

            case 2:
                BtnOK.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((whiteVoice)getApplicationContext()).target = editText.getText().toString().replace(" ","");
                        Intent intent = new Intent(getApplication(), NavigationActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                break;

            case 3:
                TTSClass.Init(this, "목적지를 말하세요");
                Intent intent = new Intent(this, STT_Activity.class);
                startActivity(intent);
                finish();
                break;
        }
        */
    }
    public void onClickGuide(View view){
        ((whiteVoice)getApplicationContext()).target = editText.getText().toString().replace(" ","");
        Intent intent = new Intent(getApplication(), NavigationActivity.class);
        startActivity(intent);
    }

    public void onClickAddF(View view){
        Intent intent = new Intent(this, FavoriteActivity.class);
        intent.putExtra("value", editText.getText().toString().replace(" ", ""));
        intent.putExtra("addCode","400");
        startActivity(intent);
    }
}
