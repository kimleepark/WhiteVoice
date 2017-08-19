package com.example.myapplicationui.Function;

import android.app.Activity;
import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by 박지찬 on 2017-07-16.
 */

public class TTSClass extends Activity{

    private static TextToSpeech mTTS;
    private static Context mMain;
    private static String mText;
    private static String[] mArray;
    private static SharedPrefManager mSharedPrefs;

    public static void Init(Context main, String text) {
        mMain = main;
        mSharedPrefs = SharedPrefManager.getmInstance(mMain);
        mText = text;

        mTTS = new TextToSpeech(mMain, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                //onInitListener.onInit(status); -- it raises up an error;
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.KOREA);
                    mTTS.setPitch(1);
                    mTTS.setSpeechRate(mSharedPrefs.getVoiceSpeed());
                    //mTTS.setVoice(mTTS.getVoices());
                    //Log.e("voice",mTTS.getVoices().toString());
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        //Log.e(TAG, "Language is not available.");
                    } else {
                        if (mTTS.isSpeaking()) {
                           mTTS.speak(mText, TextToSpeech.QUEUE_ADD, null);
                        } else {
                        mTTS.speak(mText, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                } else {
                    // Initialization failed.
                    //Log.e(TAG, "Could not initialize TextToSpeech.");
                }
            }
        });
    }
    public static void Init(Context main, String[] text) {
        mMain = main;
        mArray = text;

        mTTS = new TextToSpeech(mMain, new TextToSpeech.OnInitListener()
        {
            @Override
            public void onInit(int status) {
                //onInitListener.onInit(status); -- it raises up an error;
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.KOREA);
                    mTTS.setPitch(1);
                    mTTS.setSpeechRate(1);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        //Log.e(TAG, "Language is not available.");
                    } else {
                        mTTS.speak("1번 " + mArray[0], TextToSpeech.QUEUE_FLUSH, null);
                        for (int i = 1; i < mArray.length; i++) {
                            if (mArray[i] != null) {
                                mTTS.speak(i + 1 + "번 " + mArray[i], TextToSpeech.QUEUE_ADD, null);
                            }
                        }
                    }
                } else {
                    // Initialization failed.
                    //Log.e(TAG, "Could not initialize TextToSpeech.");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
    }
}
/*
    public static void Init(Context main, String[] text) {
        mMain = main;
        mArray = text;
        mTTS = new TextToSpeech(mMain, new TextToSpeech.OnInitListener()
        {
            @Override
            public void onInit(int status)
            {
                //onInitListener.onInit(status); -- it raises up an error;
                if (status == TextToSpeech.SUCCESS)
                {
                    int result = mTTS.setLanguage(Locale.KOREA);
                    mTTS.setPitch(1);
                    mTTS.setSpeechRate(1);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        //Log.e(TAG, "Language is not available.");
                    }
                    else
                    {
                        mTTS.speak("1번 " + mArray[0] , TextToSpeech.QUEUE_FLUSH,  null);
                        for(int i = 1; i<mArray.length; i++){
                            if(mArray[i]!=null) {
                                mTTS.speak(i+1 + "번 " + mArray[i], TextToSpeech.QUEUE_ADD, null);
                            }
                        }
                    }
                }
                else
                {
                    // Initialization failed.
                    //Log.e(TAG, "Could not initialize TextToSpeech.");
                }
            }
        }
        );
    }
}
*/
