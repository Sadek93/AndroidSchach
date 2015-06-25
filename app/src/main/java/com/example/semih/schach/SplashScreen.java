package com.example.semih.schach;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;

public class SplashScreen extends Activity {

    private final String CNAME = "SplashScreen ";

    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            final String MNAME = "handleMessage";
            final String TAG = CNAME + MNAME;
            if(ApplicationManager.DBG_APPLICATION) Log.i(TAG, "entered..");
            goMenu();

        }
    };

   private void goMenu(){
       final String MNAME = "goMenu";
       final String TAG = CNAME + MNAME;
       if(ApplicationManager.DBG_APPLICATION) Log.i(TAG, "entered..");
       Intent i = new Intent(this, MenuScreen.class);
       startActivity(i);
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String MNAME = "onCreate";
        final String TAG = CNAME + MNAME;
        if(ApplicationManager.DBG_APPLICATION) Log.i(TAG, "entered..");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(com.example.semih.schach.R.layout.activity_splash_screen);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                long futureTime = System.currentTimeMillis() + 0;

                while(System.currentTimeMillis() < futureTime) {
                    synchronized (this) {
                        try {
                            wait(futureTime - System.currentTimeMillis());
                        } catch (Exception e) {
                        }
                    }
                }
                handler.sendEmptyMessage(0);
            }
        };
        Thread t = new Thread(r);
        t.start();

    }
}
