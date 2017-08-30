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
import com.example.myapplicationui.Function.DebugClass;
import com.example.myapplicationui.R;
import com.tsengvn.typekit.TypekitContextWrapper;

public class DestinationActivity extends AppCompatActivity {

    EditText editText;
    //public static Activity DesA;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //DesA = DestinationActivity.this;
        DebugClass.logv(new Exception(), "Ssomething to print onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        this.setTitle("");

        editText = (EditText) findViewById(R.id.editDA) ;
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                Button btnGuide = (Button)findViewById(R.id.btnStartG);
                if (edit.toString().length() > 0) {
                    // 버튼 상태 활성화.
                    btnGuide.setEnabled(true);
                } else {
                    // 버튼 상태 비활성화.
                    btnGuide.setEnabled(false);
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


<<<<<<< HEAD
    public void onClickOK(View view){
        Intent intent = getIntent();
        if(intent.getStringExtra("ABC").equals("1")) {  //즐겨찾기
            intent = new Intent();
            intent.putExtra("value", editText.getText().toString().replace(" ",""));
            setResult(RESULT_OK, intent);
            finish();
        }
        else if(intent.getStringExtra("ABC").equals("2")) { //메뉴
            ((whiteVoice)getApplicationContext()).target = editText.getText().toString().replace(" ","");
            intent = new Intent(this, NavigationActivity.class);
            startActivity(intent);
            finish();
        }
        editText.setText("");
=======
    public void onClickGuide(View view){
        ((whiteVoice)getApplicationContext()).target = editText.getText().toString().replace(" ","");
        Intent intent = new Intent(getApplication(), NavigationActivity.class);
        startActivity(intent);
>>>>>>> bfee0a8473d5f20b4ae48ee4218857b11fc473fc
    }

    @Override
    public void onStop(){
        DebugClass.logv(new Exception(), "Ssomething to print Stop");

        super.onStop();
    }
    @Override
    public void onPause(){
        DebugClass.logv(new Exception(), "Ssomething to print Pause");
        super.onPause();
    }
    @Override
    public void onDestroy(){
        DebugClass.logv(new Exception(), "Ssomething to print Destroy");
        super.onDestroy();
    }
    @Override
    public void onBackPressed(){
        DebugClass.logv(new Exception(), "Ssomething to print Back");
        super.onBackPressed();
    }
}
