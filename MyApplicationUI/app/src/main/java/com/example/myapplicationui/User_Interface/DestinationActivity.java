package com.example.myapplicationui.User_Interface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplicationui.Conection.whiteVoice;
import com.example.myapplicationui.Function.STT_Activity;
import com.example.myapplicationui.Function.TTSClass;
import com.example.myapplicationui.R;

public class DestinationActivity extends AppCompatActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);
        editText = (EditText) findViewById(R.id.editDA);
        Button BtnOK = (Button)findViewById(R.id.btnOK);

        int request = getIntent().getIntExtra("request", -1);
        switch(request) {
            case 1:
                if(((whiteVoice)getApplicationContext()).WV == 100) {
                    TTSClass.Init(getApplication(), "목적지를 말하세요");
                    Intent intent = new Intent(getApplication(), STT_Activity.class);
                    ((whiteVoice) getApplicationContext()).sttCode = 2;
                    startActivity(intent);
                    finish();
                }
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
                ((whiteVoice) getApplicationContext()).sttCode = 1;
                startActivity(intent);
                finish();
                break;
        }
    }
}
