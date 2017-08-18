package com.example.myapplicationui.User_Interface;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplicationui.Conection.whiteVoice;
import com.example.myapplicationui.R;
import com.tsengvn.typekit.TypekitContextWrapper;

public class DestinationActivity extends AppCompatActivity {

    EditText editText;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        Button BtnGuide = (Button)findViewById(R.id.btnStartG);
        BtnGuide.setEnabled(false) ; // 초기 버튼 상태 비활성 상태로 지정.

        editText = (EditText) findViewById(R.id.editDA) ;
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                Button buttonAdd = (Button) findViewById(R.id.btnAddF) ;
                if (edit.toString().length() > 0) {
                    // 버튼 상태 활성화.
                    buttonAdd.setEnabled(true) ;
                } else {
                    // 버튼 상태 비활성화.
                    buttonAdd.setEnabled(false) ;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

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
        editText.setText("");
        startActivity(intent);
    }
}
