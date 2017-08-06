package com.example.myapplicationui.Function;

/**
 * Created by 박지찬 on 2017-08-03.
 */

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.EditText;

public class SimpleData implements Parcelable {
    // 숫자 데이터
    //int number;

    // 문자열 데이터
    String message;
    String message2;
    double x;
    double y;
    /**
     * 데이터 2개를 이용하여 초기화하는 생성자
     *  @param num
     * @param msg
     * @param msg2
     */
    public SimpleData(EditText msg, EditText msg2) {
        message = msg.getText().toString();
        message2 = msg2.getText().toString();
    }
    public SimpleData(double msg, double msg2, EditText msg3) {
        x = msg;
        y = msg2;
        message2 = msg3.getText().toString();
    }
    /**
     * 다른 Parcel 객체를 이용해 초기화하는 생성자
     *
     * @param src
     */
    public SimpleData(Parcel src) {
        message = src.readString();
        message2 = src.readString();
        x = src.readDouble();
        y = src.readDouble();
    }

    /**
     * 내부의 CREATOR 객체 생성
     */
    @SuppressWarnings("unchecked")
    public static final Creator CREATOR = new Creator() {

        public SimpleData createFromParcel(Parcel in) {
            return new SimpleData(in);
        }

        public SimpleData[] newArray(int size) {
            return new SimpleData[size];
        }

    };


    public int describeContents() {
        return 0;
    }

    /**
     * 데이터를 Parcel 객체로 쓰기
     */
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeString(message2);
        dest.writeDouble(x);
        dest.writeDouble(y);
    }

    /* public int getNumber() {
         return number;
     }

     public void setNumber(int number) {
         this.number = number;
     }
 */
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage2() {
        return message2;
    }
    public void setMessage2(String message2) {
        this.message2 = message2;
    }

    public double getstX() { return x; }
    public void setstX(double message) {
        this.x = message;
    }

    public double getstY() { return y; }
    public void setstY(double message) {
        this.y = message;
    }
}

