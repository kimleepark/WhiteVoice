package com.example.myapplicationui.Conection;

import java.io.Serializable;

/**
 * Created by 박지찬 on 2017-08-02.
 */

public class pathListItem implements Serializable {
    private int idx;        //인덱스 번호
    private String ment;    //해당 문자열
    private double x, y;    //위경도 좌표

    public pathListItem(){
        this.idx = 0;
        this.ment = "Null";
        this.x = 0.0;
        this.y = 0.0;
    }


    public pathListItem(int i, String m, double x, double y){
        this.idx = i;
        this.ment = m;
        this.x = x;
        this.y = y;
    }

    public void setPathListItem(int i, String m, double x, double y){
        this.idx = i;
        this.ment = m;
        this.x = x;
        this.y = y;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getMent() {
        return ment;
    }

    public void setMent(String ment) {
        this.ment = ment;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
