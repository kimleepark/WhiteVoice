package com.example.myapplicationui.Conection;

import android.app.Application;
import com.tsengvn.typekit.Typekit;

/**
 * Created by 박지찬 on 2017-07-16.
 */

public class whiteVoice extends Application{
    //프로젝트 내에서 쓸 수 있는 변수
    public String target;
    public float vSpeed;

    @Override
    public void onCreate() {
        super.onCreate();

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "BM-HANNA.ttf"));// "fonts/폰트.ttf"
    }
}
