package com.example.semih.schach.Main;

import android.app.Application;

import com.example.semih.schach.Util.Client;

/**
 * Created by semih on 02.06.2015.
 */
public class ApplicationManager extends Application{

    private final String CNAME = "ApplicationManager ";
    private static ApplicationManager singleton;

    // FLAGS
    public static final boolean DBG_GAME = true; // Soll das Spiel debugged werden?
    public static final boolean DBG_APPLICATION = false;
    public static boolean game_running = false; // Ist gerade ein Spiel am laufen ?
    public static boolean serverComputation = false; // Gibt an, ob ein Sockel erstellt werden soll um sich mit einen Rechenserver zu verbinden.
    public static boolean on_game_screen = false; // Ist die derzeitig laufende Activity Game?
    public static boolean render = true; // Soll das Spielfeld gerendert werden?
    public static boolean figurGeklickt = false; // Ist eine Figur geklickt ?
    public static boolean consoleExpanded = false;

    //Applikationsweite variablen
    public static Client clientSocket = null; //Client zum Verbinden des computing Servers
    public static int positionKoenigGross, positionKoenigKlein; // Positionen der beiden Königer ( ganzzahlig )
    public static int suchTiefe = 4; // Wieviele Züge tief soll nach den besten Spielzügen gesucht werden ?

    // Representation des Schachbrett
    // Großbuchstaben = Weiß;   Kleinbuchstaben = Schwarz
    // B = Bauer;   T = Turm;   S = Springer;   L = Läufer;     D = Königin;    K = König
    public static String schachBrett[][]	= {
            { "t", "s", "l", "d", "k", "l", "s", "t" },
            { "b", "b", "b", "b", "b", "b", "b", "b" },
            { " ", " ", " ", " ", " ", " ", " ", " " },
            { " ", " ", " ", " ", " ", " ", " ", " " },
            { " ", " ", " ", " ", " ", " ", " ", " " },
            { " ", " ", " ", " ", " ", " ", " ", " " },
            { "B", "B", "B", "B", "B", "B", "B", "B" },
            { "T", "S", "L", "D", "K", "L", "S", "T" }, };

}
