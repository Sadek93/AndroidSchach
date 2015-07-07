package com.example.semih.schach.Main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.semih.schach.R;

/**
 * Created by semih on 03.06.2015.
 */
public class DrawView extends View {
    private final String CNAME = "DrawView ";

    private Display deviceDisplay;
    private Paint paint = new Paint();
    private float height;
    private float felderGroesse;
    private int r, c;
    private Canvas canvas;
    private int clickX, clickY;
    private String moeglicheZuege = "", angepassteZuege = "";

    public DrawView(Context pContext, AttributeSet pAttrs) {
        super(pContext, pAttrs);

        final String MNAME = "Constructor";
        final String TAG = CNAME + MNAME;
        if (ApplicationManager.DBG_APPLICATION) Log.i(TAG, "entered..");

        WindowManager wm = (WindowManager) pContext.getSystemService(Context.WINDOW_SERVICE);
        deviceDisplay = wm.getDefaultDisplay();

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
    }

    // Ruft die onDraw Methode dieses Views auf
    public void render() {
        final String MNAME = "render";
        final String TAG = CNAME + MNAME;
        ApplicationManager.render = true;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final String MNAME = "onDraw";
        final String TAG = CNAME + MNAME;
        this.canvas = canvas;

        zeichneSpielfeld();
        zeichneSpielfiguren(ApplicationManager.schachBrett);

        ApplicationManager.render = false;
        super.onDraw(canvas);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final String MNAME = "onTouchEvent";
        final String TAG = CNAME + MNAME;

        moeglicheZuege = SchachLogik.moeglicheZuege();
        clickX = (int) event.getX();
        clickY = (int) event.getY();
        this.r = (int) (clickY / felderGroesse);
        this.c = (int) (clickX / felderGroesse);

        if (ApplicationManager.DBG_GAME) {
            Log.i(TAG, "entered..");
            Log.i(TAG, "X - " + String.valueOf(event.getX() + "; Y - " + String.valueOf(event.getY())));
            Log.i(TAG, "Geklickt auf: " + String.valueOf(r) + String.valueOf(c));
        }

        // Überprüft, ob einer der Spielsteine bereits geklickt wurde
        // und setzt ein Spielstein auf eine neue Position
        if (ApplicationManager.figurGeklickt) {

            for (int i = 0; i < angepassteZuege.length(); i += 5) {
                if (angepassteZuege.charAt(i + 4) != 'B') {
                    if (angepassteZuege.substring(i + 2, i + 4).equals(String.valueOf(r) + String.valueOf(c))) {
                        if (ApplicationManager.DBG_GAME) Log.i(TAG, "Spielzug möglich !");
                        SchachLogik.spieleZug(angepassteZuege.substring(i, i + 5));
                        SchachLogik.dreheSpielfeld();

                        if(ApplicationManager.clientSocket != null){
                            String zug = SchachLogik.spieleZugUeberServer();
                            SchachLogik.spieleZug(zug);
                            SchachLogik.dreheSpielfeld();
                        }else{
                            gegnerZugImThread();
                        }
                        break;
                    }
                } else {
                    String ausgewaehlteBefoerderung = "D";
                    if (angepassteZuege.substring(i + 1, i + 2).equals(String.valueOf(c)) &&
                            angepassteZuege.substring(i + 3, i + 4).equals(ausgewaehlteBefoerderung)) {
                        if (ApplicationManager.DBG_GAME) Log.i(TAG, "Spielzug möglich !");
                        SchachLogik.spieleZug(angepassteZuege.substring(i, i + 5));
                        SchachLogik.dreheSpielfeld();
                        if(ApplicationManager.clientSocket != null){
                            String zug = SchachLogik.spieleZugUeberServer();
                            SchachLogik.spieleZug(zug);
                            SchachLogik.dreheSpielfeld();
                        }else{
                            gegnerZugImThread();
                        }
                        break;
                    }
                }
            }
            ApplicationManager.figurGeklickt = false;
            ApplicationManager.render = true;
            angepassteZuege = "";
            return super.onTouchEvent(event);

            // laesst ein Spielstein auswaehlen
            // und passt die Züge auf eine kürzere Liste aller möglichen Züge für das ausgewählte Spielfeld an.
        } else {
            for (int i = 0; i < moeglicheZuege.length(); i += 5) {

                if (moeglicheZuege.charAt(i + 4) != 'B') {
                    if (moeglicheZuege.substring(i, i + 2).equals(String.valueOf(r) + String.valueOf(c))) {
                        angepassteZuege += moeglicheZuege.substring(i, i + 5);
                        ApplicationManager.figurGeklickt = true;


                    }
                } else {
                    if ((1 == (int) (clickY / felderGroesse)) && ((moeglicheZuege.substring(i, i + 2).equals(String.valueOf(c) + String.valueOf(c))) ||
                            (moeglicheZuege.substring(i, i + 2).equals(String.valueOf(c) + String.valueOf(c + 1))) ||
                            (moeglicheZuege.substring(i, i + 2).equals(String.valueOf(c) + String.valueOf(c - 1))))) {
                        angepassteZuege += moeglicheZuege.substring(i, i + 5);
                        ApplicationManager.figurGeklickt = true;

                    }
                }
            }

            if (ApplicationManager.DBG_GAME) Log.i(TAG, "Angepasste Z�ge: " + angepassteZuege);
            ApplicationManager.render = true;
        }

        if (ApplicationManager.DBG_GAME)
            Log.i(TAG, "Figur geklickt: " + Boolean.toString(ApplicationManager.figurGeklickt));
        return super.onTouchEvent(event);
    }

    private void zeichneSpielfeld() {
        final String MNAME = "zeichneSpielfeld";
        final String TAG = CNAME + MNAME;
        if (ApplicationManager.DBG_APPLICATION) Log.i(TAG, "entered..");

        Point size = new Point();
        deviceDisplay.getSize(size);
        height = size.y;
        felderGroesse = height / 8;
        if (ApplicationManager.DBG_GAME)
            Log.i(TAG, "Felder Gr��e: " + String.valueOf(felderGroesse) + "px");

        for (int i = 0; i < 64; i += 2) {
            paint.setColor(Color.rgb(255, 200, 100));
            canvas.drawRect((i % 8 + (i / 8) % 2) * felderGroesse,
                    (i / 8) * felderGroesse,
                    (i % 8 + (i / 8) % 2) * felderGroesse + felderGroesse,
                    (i / 8) * felderGroesse + felderGroesse,
                    paint);
            paint.setColor(Color.rgb(150, 50, 30));
            canvas.drawRect(((i + 1) % 8 - ((i + 1) / 8) % 2) * felderGroesse,
                    ((i + 1) / 8) * felderGroesse,
                    ((i + 1) % 8 - ((i + 1) / 8) % 2) * felderGroesse + felderGroesse,
                    ((i + 1) / 8) * felderGroesse + felderGroesse,
                    paint);
        }

        if (ApplicationManager.figurGeklickt) {

            String moeglicheZuege = SchachLogik.moeglicheZuege();
            if (ApplicationManager.DBG_GAME) Log.i(TAG, "M�gliche Z�ge: " + moeglicheZuege);

            for (int i = 0; i < moeglicheZuege.length(); i += 5) {
                if (moeglicheZuege.charAt(i + 4) != 'B') {
                    if ((Integer.valueOf(moeglicheZuege.substring(i, i + 1)) == (int) (clickY / felderGroesse)) &&
                            (Integer.valueOf(moeglicheZuege.substring(i + 1, i + 2)) == (int) (clickX / felderGroesse))) {
                        paint.setColor(Color.argb(130, 0, 150, 0)); // Gr�n
                        canvas.drawRect(Integer.valueOf(moeglicheZuege.substring(i + 3, i + 4)) * felderGroesse,
                                Integer.valueOf(moeglicheZuege.substring(i + 2, i + 3)) * felderGroesse,
                                Integer.valueOf(moeglicheZuege.substring(i + 3, i + 4)) * felderGroesse + felderGroesse,
                                Integer.valueOf(moeglicheZuege.substring(i + 2, i + 3)) * felderGroesse + felderGroesse,
                                paint);


                    }
                } else {
                    if (ApplicationManager.schachBrett[(int) (clickY / felderGroesse)][(int) (clickX / felderGroesse)].charAt(0) == 'B' &&
                            ((int) (clickY / felderGroesse) * 8 + (int) (clickX / felderGroesse) < 16)) {
                        if (Integer.valueOf(moeglicheZuege.substring(i, i + 1)) == (int) (clickX / felderGroesse)) {
                            paint.setColor(Color.argb(130, 242, 173, 12)); // Orange
                            if (moeglicheZuege.charAt(i + 2) == ' ') {
                                canvas.drawRect(Integer.valueOf(moeglicheZuege.substring(i, i + 1)) * felderGroesse,
                                        0 * felderGroesse,
                                        Integer.valueOf(moeglicheZuege.substring(i, i + 1)) * felderGroesse + felderGroesse,
                                        0 * felderGroesse + felderGroesse,
                                        paint);
                            } else {
                                canvas.drawRect(Integer.valueOf(moeglicheZuege.substring(i + 1, i + 2)) * felderGroesse,
                                        0 * felderGroesse,
                                        Integer.valueOf(moeglicheZuege.substring(i + 1, i + 2)) * felderGroesse + felderGroesse,
                                        0 * felderGroesse + felderGroesse,
                                        paint);
                            }
                        }
                    }
                }
            }
        }

    }


    private void zeichneSpielfiguren(String[][] schachBrett) {

        BitmapDrawable schachFigurenBD = (BitmapDrawable) getResources().getDrawable(R.drawable.chesspieces);
        Bitmap schachFiguren = schachFigurenBD.getBitmap();
        int bildGroeßenFaktor = canvas.getWidth() / 6;

        paint.setColor(Color.argb(255, 255, 255, 255));
        for (int i = 0; i < 64; i++) {
            int j = -1, k = -1;
            switch (schachBrett[i / 8][i % 8]) { // [reihe][spalte]
                case "B":
                    j = 5;
                    k = 0;
                    break;
                case "b":
                    j = 5;
                    k = 1;
                    break;
                case "T":
                    j = 2;
                    k = 0;
                    break;
                case "t":
                    j = 2;
                    k = 1;
                    break;
                case "S":
                    j = 4;
                    k = 0;
                    break;
                case "s":
                    j = 4;
                    k = 1;
                    break;
                case "L":
                    j = 3;
                    k = 0;
                    break;
                case "l":
                    j = 3;
                    k = 1;
                    break;
                case "D":
                    j = 1;
                    k = 0;
                    break;
                case "d":
                    j = 1;
                    k = 1;
                    break;
                case "K":
                    j = 0;
                    k = 0;
                    break;
                case "k":
                    j = 0;
                    k = 1;
                    break;
            }

            // Wenn kein Spielstein gefunden wurde Zeichne nichts.
            if (j != -1 && k != -1) {
                canvas.drawBitmap(schachFiguren,
                        new Rect(j * bildGroeßenFaktor, k * bildGroeßenFaktor, (j + 1) * bildGroeßenFaktor, (k + 1) * bildGroeßenFaktor),
                        new Rect((i % 8) * (int) felderGroesse, (i / 8) * (int) felderGroesse, (i % 8 + 1) * (int) felderGroesse, (i / 8 + 1) * (int) felderGroesse),
                        paint);
            }

        }
    }

    private void gegnerZugImThread(){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                ApplicationManager.render = false;
                String gegnerZug = SchachLogik.alphaBetaSuche(ApplicationManager.suchTiefe, 10000, -10000, "", 0); // TODO: VERBUGGT ? Liefert -52000 als Wert zurück
                Log.i("ZUG:", gegnerZug);
                SchachLogik.spieleZug(gegnerZug);
                SchachLogik.dreheSpielfeld();
                ApplicationManager.render = true;
            }
        };

        Thread t = new Thread(r);
        t.start();

    }
}
