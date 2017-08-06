package com.example.myapplicationui.Function;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 박지찬 on 2017-07-24.
 */

public class FileClass {

    public static String saveBitmapToJpeg(Context context, Bitmap bitmap, String name){

        File storage = context.getCacheDir(); // 임시파일 저장 경로

        String fileName = name + ".jpg";  // 파일이름

        File tempFile = new File(storage,fileName);

        try{
            tempFile.createNewFile();  // 파일생성

            FileOutputStream out = new FileOutputStream(tempFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90 , out);  // 넘겨 받은 bitmap을 jpeg(손실압축)으로 저장

            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tempFile.getAbsolutePath();   // 임시파일 저장경로를 리턴
    }

}
