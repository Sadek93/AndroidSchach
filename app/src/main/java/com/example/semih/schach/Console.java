package com.example.semih.schach;

import android.widget.TextView;

/**
 * Created by Semih on 25.06.2015.
 */
public class Console{

    private static String smallConsole;
    private static String console;

    private static String getSmallConsoleMessage() {
        return smallConsole;
    }

    public static void setSmallConsoleMessage(String message) {
        smallConsole = message;
    }

    private static String getConsoleMessage() {
        return console;
    }

    public static void setConsoleMessage(String message) {
        console = message;
    }

    public static void updateSmallConsole(TextView log){
        log.setText(getSmallConsoleMessage());
    }

    public static void updateConsole(TextView log){
        log.setText(getConsoleMessage());
    }
}
