package com.example.semih.schach.Main;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.semih.schach.Util.Client;
import com.example.semih.schach.Util.ConsoleItem;
import com.example.semih.schach.R;
import com.example.semih.schach.Util.ConsoleListAdapter;
import com.example.semih.schach.Util.NewGameDialog;

import java.util.ArrayList;

public class Game extends FragmentActivity {

    private final String CNAME = "Game ";
    private Grafik gfx;
    private DrawView drawView;
    private ListView consoleList;
    private static FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String MNAME = "onCreate";
        final String TAG = CNAME + MNAME;
        if (ApplicationManager.DBG_APPLICATION) Log.i(TAG, "entered..");
        setContentView(R.layout.activity_game);

        manager = getFragmentManager();
        consoleList = (ListView) findViewById(R.id.consoleList);
        ArrayList<ConsoleItem> items = new ArrayList<>();
        for(int i = 0; i <= 10; i++){
            ConsoleItem item = new ConsoleItem("Dummy title", "content");
            items.add(item);
        }
        ConsoleListAdapter adapter = new ConsoleListAdapter(this, items);
        consoleList.setAdapter(adapter);
        consoleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!ApplicationManager.consoleExpanded) expandConsole();
                else closeConsole();

            }
        });

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.idRelativeLayout);
        drawView = (DrawView)findViewById(R.id.idSchachbrett);
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
                                    if(ApplicationManager.render) gfx.render();
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
        consoleList.setLayoutParams(new RelativeLayout.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT));
        getSpielstatus();
        ApplicationManager.consoleExpanded = true;
    }

    private void closeConsole(){
        consoleList.setLayoutParams(new RelativeLayout.LayoutParams(ListView.LayoutParams.MATCH_PARENT, dpToPx(50)));
        getSpielstatus();
        ApplicationManager.consoleExpanded = false;
    }

    public static void öffneNeuesSpielDialog(){
            NewGameDialog dialog = new NewGameDialog();
            dialog.show(manager, "neues spiel");

    }

}


