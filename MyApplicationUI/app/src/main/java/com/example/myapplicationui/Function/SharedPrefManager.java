package com.example.myapplicationui.Function;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 박지찬 on 2017-08-19.
 */

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static SharedPreferences mSharedPrefs;
    private static SharedPreferences.Editor mEdit;
    private static final String SHARED_FILE_NAME = "Setting";

    public static SharedPrefManager getmInstance(Context context){
        if(mInstance == null){
            mInstance =  new SharedPrefManager(context);
        }
        return mInstance;
    }

    private SharedPrefManager(Context context){
        mSharedPrefs =  context.getSharedPreferences(SHARED_FILE_NAME, MODE_PRIVATE);
        mEdit =  mSharedPrefs.edit();
    }

    public void setVoiceSpeed(float S){
        mEdit.putFloat("vSpeed", S);
        mEdit.commit();
    }

    public float getVoiceSpeed(){
        return mSharedPrefs.getFloat("vSpeed", 1.0f);
    }
}
