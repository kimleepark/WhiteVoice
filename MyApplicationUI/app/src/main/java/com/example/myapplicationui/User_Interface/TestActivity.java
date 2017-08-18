package com.example.myapplicationui.User_Interface;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationui.R;

import java.util.ArrayList;

public class TestActivity extends Activity {

    ArrayList<String> arDump = new ArrayList<String>();

    TextView statusView,resultsView;

    Intent recognizerIntent;

    SpeechRecognizer mSpeechRecognizer;

    ProgressBar volumeView;

    String str;


    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);

        statusView = (TextView)findViewById(R.id.status);

        resultsView = (TextView)findViewById(R.id.results);

        volumeView = (ProgressBar)findViewById(R.id.volumeView);

        volumeView.setMax(100);

        recognizerIntent = new Intent(RecognizerIntent.ACTION_WEB_SEARCH);

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

        Button btnTest = (Button)findViewById(R.id.button1234);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpeechRecognizer.startListening(recognizerIntent);

                arDump.clear();

                resultsView.setText("");
            }
        });

    }



    @Override
    public void onResume() {

        // TODO Auto-generated method stub

        super.onResume();

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        mSpeechRecognizer.setRecognitionListener(mRecognitionListener);


    }

    @Override
    public void onPause() {

        // TODO Auto-generated method stub

        super.onPause();

        mSpeechRecognizer.destroy();

    }

    /*public void on‌ClickTest(View view) {

        mSpeechRecognizer.startListening(recognizerIntent);

        arDump.clear();

        resultsView.setText("");

    }*/

    RecognitionListener mRecognitionListener = new RecognitionListener() {

        @Override
        public void onBeginningOfSpeech() {

            // TODO Auto-generated method stub

            AppendText("onBeginningOfSpeech");
        }



        @Override
        public void onBufferReceived(byte[] buffer) {

            // TODO Auto-generated method stub

            AppendText("onBufferReceived "+buffer[0]);

        }



        @Override
        public void onEndOfSpeech() {

            // TODO Auto-generated method stub

            AppendText("onEndOfSpeech");
        }

        @Override
        public void onError(int i) {
            // TODO Auto-generated method stub

            AppendText("on‌Error "+ i);
        }

        @Override
        public void onEvent(int eventType, Bundle params) {

            // TODO Auto-generated method stub

            AppendText("onEvent");
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

            // TODO Auto-generated method stub

            AppendText("onPartialResults");
        }



        @Override
        public void onReadyForSpeech(Bundle params) {

            // TODO Auto-generated method stub

            AppendText("onReadyForSpeech");
        }

        @SuppressWarnings("unchecked")

        @Override
        public void onResults(Bundle results) {

            // TODO Auto-generated method stub

            AppendText("onResults");

            resultsView.setText("");

            ArrayList<String> rsts = (ArrayList<String>) results.get(SpeechRecognizer.RESULTS_RECOGNITION);

            for(int i=0;i<rsts.size();i++)

                resultsView.setText(rsts.get(i)+"\n");

        }



        @Override
        public void onRmsChanged(float rmsdB) {

            // TODO Auto-generated method stub

            volumeView.setProgress((int)rmsdB);

        }
    };



    void AppendText(String text) {

        if(arDump.size()>17)

            arDump.remove(0);

        arDump.add(text);



        StringBuilder result = new StringBuilder();

        for(String s : arDump)

            result.append(s+"\n");



        statusView.setText(result.toString());

    }

}