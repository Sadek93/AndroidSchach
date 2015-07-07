package com.example.semih.schach.Main;

import android.app.Activity;

import com.example.semih.schach.Main.DrawView;

/**
 * Created by semih on 07.06.2015.
 */
public class Grafik {

    private DrawView drawView;
    private Activity host;

    public Grafik(Activity host, DrawView dv){
        this.drawView = dv;
        this.host = host;
    }

    public void render(){

        host.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                drawView.render();
            }
        });

    }
}
