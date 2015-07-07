package com.example.semih.schach.Main;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.semih.schach.R;


public class MenuScreen extends Activity{

    private final String CNAME = "MenuScreen";
    private Button neuesSpiel, einstellungen, beenden;
    private String neuesSpielText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String MNAME = "onCreate";
        final String TAG = CNAME + MNAME;
        if(ApplicationManager.DBG_APPLICATION) Log.i(TAG, "entered..");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(com.example.semih.schach.R.layout.activity_menu_screen);

        neuesSpiel = (Button) findViewById(R.id.idNeuesSpielButton);
        einstellungen = (Button) findViewById(R.id.idEinstellungenButton);
        beenden = (Button) findViewById(R.id.idBeendenButton);

        if(ApplicationManager.game_running) neuesSpielText = "Fortsetzen";
        if(!ApplicationManager.game_running) neuesSpielText = "Neues Spiel";

        neuesSpiel.setText(neuesSpielText);
    }

    public void onClickNeuesSpiel(View view){

        final String MNAME = "onClickNeuesSpiel";
        final String TAG = CNAME + MNAME;
        if(ApplicationManager.DBG_APPLICATION) Log.i(TAG, "entered..");

        ApplicationManager.render = true;
        Intent i = new Intent(this, Game.class);
        startActivity(i);
    }

    public void onClickEinstellungen(View view){

        final String MNAME = "onClickEinstellungen";
        final String TAG = CNAME + MNAME;
        if(ApplicationManager.DBG_APPLICATION) Log.i(TAG, "entered..");
    }

    public void onClickBeenden(View view){

        final String MNAME = "onClickBeenden";
        final String TAG = CNAME + MNAME;
        if(ApplicationManager.DBG_APPLICATION) Log.i(TAG, "entered..");


        if(ApplicationManager.DBG_GAME) System.exit(0);
        else{
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }

    }
}
