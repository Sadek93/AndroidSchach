package com.example.semih.schach.Util;

import android.widget.TextView;

/**
 * Created by Semih on 25.06.2015.
 */
public class ConsoleItem {

    private String title;
    private String descr;

    public ConsoleItem(String title, String descr){
        this.title = title;
        this.descr = descr;
    }

    public  String getTitle() {
        return title;
    }

    public  void setTitle(String title) {
        this.title = title;
    }

    public  String getDescr() {
        return descr;
    }

    public  void setDescr(String descr) {
        this.descr = descr;
    }

}
