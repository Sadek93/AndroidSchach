package com.example.semih.schach;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.ActionMenuView;
import android.util.Log;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import static com.example.semih.schach.R.id.idRelativeLayout;

public class Game extends FragmentActivity {

    private final String CNAME = "Game ";
    private Grafik gfx;
    private DrawView drawView;
    private RelativeLayout console;
    public TextView log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String MNAME = "onCreate";
        final String TAG = CNAME + MNAME;
        if (ApplicationManager.DBG_APPLICATION) Log.i(TAG, "entered..");
        setContentView(R.layout.activity_game);

        log = (TextView) findViewById(R.id.log);
        console = (RelativeLayout) findViewById(R.id.Console);
        console.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                if(!ApplicationManager.consoleExpanded) expandConsole();
                else closeConsole();

            }
        });

        ViewGroup layout = (ViewGroup) findViewById(idRelativeLayout);
        drawView = (DrawView)layout.findViewById(R.id.idSchachbrett);
        gfx = new Grafik(this, drawView);
        if(ApplicationManager.serverComputation) ApplicationManager.clientSocket = new Client("10.0.2.2");


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        initialisiereStartwerte();
        starteSpiel();
    }


    public void onClickMenu(View view){
        ApplicationManager.figurGeklickt = false;
        pausiereSpiel();
        Intent i = new Intent(this, MenuScreen.class);
        startActivity(i);
    }

    public void initialisiereStartwerte(){

        while (!"K".equals(ApplicationManager.schachBrett[ApplicationManager.positionKoenigGross / 8][ApplicationManager.positionKoenigGross % 8])) {
            ApplicationManager.positionKoenigGross++;
        }
        while (!"k".equals(ApplicationManager.schachBrett[ApplicationManager.positionKoenigKlein / 8][ApplicationManager.positionKoenigKlein % 8])) {
            ApplicationManager.positionKoenigKlein++;
        }
    }

    public void starteSpiel(){
        final String MNAME = "starteSpiel ";
        final String TAG = CNAME + MNAME;

        ApplicationManager.game_running = true;
        ApplicationManager.on_game_screen = true;

        Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        int i = 0;
                        while(ApplicationManager.on_game_screen){
                            synchronized (this){
                                // GAME LOOP
                                if(getFrameAnzahErreicht(15)){
                                    i++;
                                    if(ApplicationManager.DBG_APPLICATION) Log.i(TAG, i + ". Thread: " + Thread.currentThread().toString() + " is running...");
                                    getSpielstatus();
                                    gfx.render();
                                    if(i>= 15) i = 0;
                                }


                            }
                        }

            }
        };

        Thread t = new Thread(r);
        t.start();
    }

    public void pausiereSpiel(){
        ApplicationManager.on_game_screen = false;
    }

    public void getSpielstatus(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Console.updateConsole(log);
                Console.updateSmallConsole(log);
            }
        });

    }

    // Gibt true zurück, wenn eine Zahl x an Frames erreicht wurde. Sollte nicht im Main-Thread bentutz werden.
    public boolean getFrameAnzahErreicht(int frames){

        long futureTime = System.currentTimeMillis() + 1000 / frames;
        while(true) {
            synchronized (this) {
                if (System.currentTimeMillis() >= futureTime) {
                        return true;
                }
            }
        }

    }

    private int dpToPx(int dp)
    {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }

    private void expandConsole(){
        console.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, dpToPx(1080)));
        getSpielstatus();
        ApplicationManager.consoleExpanded = true;
    }

    private void closeConsole(){
        console.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, dpToPx(25)));
        getSpielstatus();
        ApplicationManager.consoleExpanded = false;
    }

}


