package com.example.semih.schach;

import android.util.Log;

/**
 * Created by semih on 06.06.2015.
 */
public class SchachLogik {

    // Gibt eine Liste aller möglichen Züge zurück
    public static String moeglicheZuege() {
        String list = "";
        for (int i = 0; i < 64; i++) {
            switch (ApplicationManager.schachBrett[i / 8][i % 8]) { // [reihe][spalte]
                case "B" :
                    list += moeglichB(i);
                    break;
                case "T" :
                    list += moeglichT(i);
                    break;
                case "S" :
                    list += moeglichS(i);
                    break;
                case "L" :
                    list += moeglichL(i);
                    break;
                case "D" :
                    list += moeglichD(i);
                    break;
                case "K" :
                    list += moeglichK(i);
                    break;
            }
        }
        return list; // x1,y1,x2,y2, eroberter Stein
    }

    // ist der König sicher?
    public static boolean koenigSicher() {
        // False = König in Gefahr, True = König sicher


        // Reihe in dem der Weiße König sich befindet
        int koenigGroßR = ApplicationManager.positionKoenigGross / 8;
        // Spalte in dem der Weiße König sich befindet
        int koenigGroßC = ApplicationManager.positionKoenigGross % 8;

        // Gegner Läufer und Dame
        int temp = 1;
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                try {
                    while (" ".equals(ApplicationManager.schachBrett[koenigGroßR + temp * i][koenigGroßC + temp * j])) {
                        temp++;
                    }
                    if ("l".equals(ApplicationManager.schachBrett[koenigGroßR + temp * i][koenigGroßC + temp * j]) ||
                            ("d".equals(ApplicationManager.schachBrett[koenigGroßR + temp * i][koenigGroßC + temp * j]))) {
                        return false;
                    }
                } catch (Exception e) {
                }
                temp = 1;
            }
        }

        // Gegner Turm und Dame
        for (int i = -1; i <= 1; i += 2) {
            try {
                while (" ".equals(ApplicationManager.schachBrett[koenigGroßR][koenigGroßC + temp * i])) {
                    temp++;
                }
                if ("t".equals(ApplicationManager.schachBrett[koenigGroßR][koenigGroßC + temp * i]) ||
                        ("d".equals(ApplicationManager.schachBrett[koenigGroßR][koenigGroßC + temp * i]))) {
                    // danger
                    return false;
                }
            } catch (Exception e) {
            }
            temp = 1;
            try {
                while (" ".equals(ApplicationManager.schachBrett[koenigGroßR + temp * i][koenigGroßC])) {
                    temp++;
                }
                if ("t".equals(ApplicationManager.schachBrett[koenigGroßR + temp * i][koenigGroßC]) ||
                        ("d".equals(ApplicationManager.schachBrett[koenigGroßR + temp * i][koenigGroßC]))) {
                    // danger
                    return false;
                }
            } catch (Exception e) {
            }
            temp = 1;
        }

        // Gegner Springer
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                try {
                    if ("s".equals(ApplicationManager.schachBrett[koenigGroßR + i][koenigGroßC + j * 2])) {
                        // danger
                        return false;
                    }
                } catch (Exception e) {
                }
                try {
                    if ("s".equals(ApplicationManager.schachBrett[koenigGroßR + i * 2][koenigGroßC + j])) {
                        // danger
                        return false;
                    }
                } catch (Exception e) {
                }
            }
        }

        // Gegner Bauer
        if (ApplicationManager.positionKoenigGross >= 16) {
            try {
                if ("b".equals(ApplicationManager.schachBrett[koenigGroßR - 1][koenigGroßC - 1])) {
                    // danger
                    return false;
                }
                if ("b".equals(ApplicationManager.schachBrett[koenigGroßR + 1][koenigGroßC + 1])) {
                    // danger
                    return false;
                }
            } catch (Exception e) {
            }
        }
        // Gegner König
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {
                    try {
                        if ("k".equals(ApplicationManager.schachBrett[koenigGroßR + i][koenigGroßC + j])) {
                            // danger
                            return false;
                        }
                    } catch (Exception e) {
                    }

                }
            }
        }

        return true;
    }

    // Bauern Logik
    public static String moeglichB(int i) {
        String liste = "", alterStein;
        int r = i / 8, c = i % 8;

        try {  // Schritt nach vorne
            if (" ".equals(ApplicationManager.schachBrett[r - 1][c]) && i >= 16) {
                alterStein = ApplicationManager.schachBrett[r - 1][c];
                ApplicationManager.schachBrett[r][c] = " ";
                ApplicationManager.schachBrett[r - 1][c] = "B";
                if (koenigSicher()) {
                    liste = liste + r + c + (r - 1) + c + alterStein;
                }
                ApplicationManager.schachBrett[r][c] = "B";
                ApplicationManager.schachBrett[r - 1][c] = alterStein;
            }
        } catch (Exception e) {
        }

        try { // Zwei Schritte nach vorne, wenn Bauer auf startposition
            if (" ".equals(ApplicationManager.schachBrett[r - 1][c]) && " ".equals(ApplicationManager.schachBrett[r - 2][c]) && i >= 48) {
                alterStein = ApplicationManager.schachBrett[r - 2][c];
                ApplicationManager.schachBrett[r][c] = " ";
                ApplicationManager.schachBrett[r - 2][c] = "B";
                if (koenigSicher()) {
                    liste = liste + r + c + (r - 2) + c + alterStein;
                }
                ApplicationManager.schachBrett[r][c] = "B";
                ApplicationManager.schachBrett[r - 2][c] = alterStein;
            }
        } catch (Exception e) {
        }

        try { // Beförderung ohne Eroberung
            if (" ".equals(ApplicationManager.schachBrett[r - 1][c]) && i < 16) {
                String[] temp = {"D", "T", "L", "S"};
                for (int k = 0; k <= temp.length; k++) {
                    alterStein = ApplicationManager.schachBrett[r - 1][c];
                    ApplicationManager.schachBrett[r][c] = " ";
                    ApplicationManager.schachBrett[r - 1][c] = temp[k];
                    if (koenigSicher()) {
                        liste = liste + c + c + alterStein + temp[k] + "B";
                    }
                    ApplicationManager.schachBrett[r][c] = "B";
                    ApplicationManager.schachBrett[r - 1][c] = alterStein;
                }
            }
        } catch (Exception e) {
        }

        for (int j = -1; j <= 1; j += 2) {
            try { // Eroberung
                if (Character.isLowerCase(ApplicationManager.schachBrett[r - 1][c + j].charAt(0)) && i >= 16) {
                    alterStein = ApplicationManager.schachBrett[r - 1][c + j];
                    ApplicationManager.schachBrett[r][c] = " ";
                    ApplicationManager.schachBrett[r - 1][c + j] = "B";
                    if (koenigSicher()) {
                        liste = liste + r + c + (r - 1) + (c + j) + alterStein;
                    }
                    ApplicationManager.schachBrett[r][c] = "B";
                    ApplicationManager.schachBrett[r - 1][c + j] = alterStein;
                }
            } catch (Exception e) {
            }

            try { // Eroberung & Beförderung
                if (Character.isLowerCase(ApplicationManager.schachBrett[r - 1][c + j].charAt(0)) && i < 16) {
                    String[] temp = {"D", "T", "L", "S"};
                    for (int k = 0; k < 4; k++) {
                        alterStein = ApplicationManager.schachBrett[r - 1][c + j];
                        ApplicationManager.schachBrett[r][c] = " ";
                        ApplicationManager.schachBrett[r - 1][c + j] = temp[k];
                        if (koenigSicher()) {
                            // Rückgabe Format ein bisschen geändert, weil man zusatz Informationen braucht.
                            // palte1S, Spalte2, eroberter Stein, neuer Stein, B
                            liste = liste + c + (c + j) + alterStein + temp[k] + "B";
                        }
                        ApplicationManager.schachBrett[r][c] = "B";
                        ApplicationManager.schachBrett[r - 1][c + j] = alterStein;
                    }
                    /*alterStein = ApplicationManager.schachBrett[r - 1][c + j];
                    ApplicationManager.schachBrett[r][c] = " ";
                    ApplicationManager.schachBrett[r - 1][c + j] = "B";
                    if (koenigSicher()) {
                        liste = liste + r + c + (r - 1) + (c + j) + alterStein;
                    }
                    ApplicationManager.schachBrett[r][c] = "B";
                    ApplicationManager.schachBrett[r - 1][c + j] = alterStein;*/
                }
            } catch (Exception e) {
            }

        }
        return liste;
    }

    // Turm Logik
    public static String moeglichT(int i) {
        String liste = "", alterStein;
        int r = i / 8, c = i % 8;

        int temp = 1;
        // Checkt horizontale Bewegungen des Turms
        for (int j = -1; j <= 1; j += 2) {
            try {
                while (" ".equals(ApplicationManager.schachBrett[r][c + temp * j])) {
                    alterStein = ApplicationManager.schachBrett[r][c + temp * j];
                    ApplicationManager.schachBrett[r][c] = " ";
                    ApplicationManager.schachBrett[r][c + temp * j] = "T";
                    if (koenigSicher()) {
                        liste = liste + r + c + r + (c + temp * j) + alterStein;
                    }
                    ApplicationManager.schachBrett[r][c] = "T";
                    ApplicationManager.schachBrett[r][c + temp * j] = alterStein;
                    temp++;
                }
                if (Character.isLowerCase(ApplicationManager.schachBrett[r][c + temp * j].charAt(0))) {
                    alterStein = ApplicationManager.schachBrett[r][c + temp * j];
                    ApplicationManager.schachBrett[r][c] = " ";
                    ApplicationManager.schachBrett[r][c + temp * j] = "T";
                    if (koenigSicher()) {
                        liste = liste + r + c + r + (c + temp * j) + alterStein;
                    }
                    ApplicationManager.schachBrett[r][c] = "T";
                    ApplicationManager.schachBrett[r][c + temp * j] = alterStein;
                }
            } catch (Exception e) {
            }
        }

        temp = 1;
        // Checkt vertikale Bewegungen des Turms
        for (int j = -1; j <= 1; j += 2) {
            try {
                while (" ".equals(ApplicationManager.schachBrett[r + temp * j][c])) {
                    alterStein = ApplicationManager.schachBrett[r + temp * j][c];
                    ApplicationManager.schachBrett[r][c] = " ";
                    ApplicationManager.schachBrett[r + temp * j][c] = "T";
                    if (koenigSicher()) {
                        liste = liste + r + c + (r + temp * j) + c + alterStein;
                    }
                    ApplicationManager.schachBrett[r][c] = "T";
                    ApplicationManager.schachBrett[r + temp * j][c] = alterStein;
                    temp++;
                }
                if (Character.isLowerCase(ApplicationManager.schachBrett[r + temp * j][c].charAt(0))) {
                    alterStein = ApplicationManager.schachBrett[r + temp * j][c];
                    ApplicationManager.schachBrett[r][c] = " ";
                    ApplicationManager.schachBrett[r + temp * j][c] = "T";
                    if (koenigSicher()) {
                        liste = liste + r + c + (r + temp * j) + c + alterStein;
                    }
                    ApplicationManager.schachBrett[r][c] = "T";
                    ApplicationManager.schachBrett[r + temp * j][c] = alterStein;
                }
            } catch (Exception e) {
            }
            temp = 1;
        }
        return liste;
    }

    // Springer Logik
    public static String moeglichS(int i) {
        String liste = "", alterStein;
        int r = i / 8, c = i % 8;
        for (int j = -1; j <= 1; j += 2) {
            for (int k = -1; k <= 1; k += 2) {
                // Checkt die 4 Bewegungen des Springer in denen er horizontal 2 Felder springt.
                try {
                    if (Character.isLowerCase(ApplicationManager.schachBrett[r + j][c + k * 2].charAt(0)) || " ".equals(ApplicationManager.schachBrett[r + j][c + k * 2])) {
                        alterStein = ApplicationManager.schachBrett[r + j][c + k * 2];
                        ApplicationManager.schachBrett[r][c] = " ";
                        ApplicationManager.schachBrett[r + j][c + k * 2] = "S";
                        if (koenigSicher()) {
                            liste = liste + r + c + (r + j) + (c + k * 2) + alterStein;
                        }
                        ApplicationManager.schachBrett[r][c] = "S";
                        ApplicationManager.schachBrett[r + j][c + k * 2] = alterStein;
                    }
                } catch (Exception e) {}

                // Checkt die 4 Bewegungen des Springer in denen er vertikal 2 Felder springt.
                try {
                    if (Character.isLowerCase(ApplicationManager.schachBrett[r + j * 2][c + k].charAt(0)) || " ".equals(ApplicationManager.schachBrett[r + j * 2][c + k])) {
                        alterStein = ApplicationManager.schachBrett[r + j * 2][c + k];
                        ApplicationManager.schachBrett[r][c] = " ";
                        ApplicationManager.schachBrett[r + j * 2][c + k] = "S";
                        if (koenigSicher()) {
                            liste = liste + r + c + (r + j * 2) + (c + k) + alterStein;
                        }
                        ApplicationManager.schachBrett[r][c] = "S";
                        ApplicationManager.schachBrett[r + j * 2][c + k] = alterStein;
                    }
                } catch (Exception e) {}
            }
        }

        return liste;
    }

    // Läufer Logik
    public static String moeglichL(int i) {
        String liste = "", alterStein;
        int r = i / 8, c = i % 8;
        int temp = 1;

        // Läufer bewegungen. Checkt nur Diagonale Bewegungen. Bruchteil aus der Königin Logik
        for (int j = -1; j <= 1; j += 2) {
            for (int k = -1; k <= 1; k += 2) {
                try {
                    while (" ".equals(ApplicationManager.schachBrett[r + temp * j][c + temp * k])) {
                        alterStein = ApplicationManager.schachBrett[r + temp * j][c + temp * k];
                        ApplicationManager.schachBrett[r][c] = " ";
                        ApplicationManager.schachBrett[r + temp * j][c + temp * k] = "L";
                        if (koenigSicher()) {
                            liste = liste + r + c + (r + temp * j) + (c + temp * k) + alterStein;
                        }
                        ApplicationManager.schachBrett[r][c] = "L";
                        ApplicationManager.schachBrett[r + temp * j][c + temp * k] = alterStein;
                        temp++;
                    }
                    if (Character.isLowerCase(ApplicationManager.schachBrett[r + temp * j][c + temp * k].charAt(0))) {
                        alterStein = ApplicationManager.schachBrett[r + temp * j][c + temp * k];
                        ApplicationManager.schachBrett[r][c] = " ";
                        ApplicationManager.schachBrett[r + temp * j][c + temp * k] = "L";
                        if (koenigSicher()) {
                            liste = liste + r + c + (r + temp * j) + (c + temp * k) + alterStein;
                        }
                        ApplicationManager.schachBrett[r][c] = "L";
                        ApplicationManager.schachBrett[r + temp * j][c + temp * k] = alterStein;
                    }
                } catch (Exception e) {
                }
                temp = 1;
            }
        }
        return liste;
    }

    // Königin Logik
    public static String moeglichD(int i) {
        String liste = "", alterStein;
        int r = i / 8, c = i % 8;
        int temp = 1;
        for (int j = -1; j <= 1; j++) {
            for (int k = -1; k <= 1; k++) {
                if (j != 0 || k != 0) {
                    try {
                        // Checkt solange die Felder leer sind und returned sie als  möglichen Zug, wenn nichts im Weg steht
                        while (" ".equals(ApplicationManager.schachBrett[r + temp * j][c + temp * k])) {

                            alterStein = ApplicationManager.schachBrett[r + temp * j][c + temp * k];
                            ApplicationManager.schachBrett[r][c] = " ";
                            ApplicationManager.schachBrett[r + temp * j][c + temp * k] = "D";
                            if (koenigSicher()) {
                                liste = liste + r + c + (r + temp * j) + (c + temp * k) + alterStein;
                            }
                            ApplicationManager.schachBrett[r][c] = "D";
                            ApplicationManager.schachBrett[r + temp * j][c + temp * k] = alterStein;
                            temp++;
                        }
                        // Checkt sobald aus der while- Schleife, ob ein gegnerischer Stein getroffen wurde und returned diesen Zug
                        if (Character.isLowerCase(ApplicationManager.schachBrett[r + temp * j][c + temp * k].charAt(0))) {
                            alterStein = ApplicationManager.schachBrett[r + temp * j][c + temp * k];
                            ApplicationManager.schachBrett[r][c] = " ";
                            ApplicationManager.schachBrett[r + temp * j][c + temp * k] = "D";
                            if (koenigSicher()) {
                                liste = liste + r + c + (r + temp * j) + (c + temp * k) + alterStein;
                            }
                            ApplicationManager.schachBrett[r][c] = "D";
                            ApplicationManager.schachBrett[r + temp * j][c + temp * k] = alterStein;
                        }
                    } catch (Exception e) {}
                    temp = 1; // temp wird auf 1 gesetzt um bei dem nächsten for- Schleifen durchlauf keine Felder zu überspringen
                }
            }
        }
        return liste;
    }

    // König Logik
    public static String moeglichK(int i) {
        String liste = "", alterStein;
        int r = i / 8, c = i % 8;
        for (int j = 0; j < 9; j++) {
            if (j != 4) {
                try {
                    // Checkt, ob das Zielfeld leer ist oder von einem Gegnerischen Spielstein besetzt ist. Wenn ja wird getestet, ob dies ein möglicher Spielzug ist.
                    if (Character.isLowerCase(ApplicationManager.schachBrett[r - 1 + j / 3][c - 1 + j % 3].charAt(0)) || " ".equals(ApplicationManager.schachBrett[r - 1 + j / 3][c - 1 + j % 3])) {
                        alterStein = ApplicationManager.schachBrett[r - 1 + j / 3][c - 1 + j % 3];
                        ApplicationManager.schachBrett[r][c] = " ";
                        ApplicationManager.schachBrett[r - 1 + j / 3][c - 1 + j % 3] = "K";
                        int kingTemp = ApplicationManager.positionKoenigGross;
                        ApplicationManager.positionKoenigGross = i + (j / 3) * 8 + j % 3 - 9;
                        if (koenigSicher()) {
                            liste = liste + r + c + (r - 1 + j / 3) + (c - 1 + j % 3) + alterStein;
                        }
                        ApplicationManager.schachBrett[r][c] = "K";
                        ApplicationManager.schachBrett[r - 1 + j / 3][c - 1 + j % 3] = alterStein;
                        ApplicationManager.positionKoenigGross = kingTemp;
                    }
                } catch (Exception e) {}
            }
        }

        return liste;
    }

    public static String alphaBetaSuche(int tiefe, int beta, int alpha, String move, int player) {
        // returned ein String in der Form von x1,y1,x2,y2,score
        Console.setSmallConsoleMessage("Gegner denkt nach...");
        String liste = moeglicheZuege();
        if (tiefe == 0 || liste.length() == 0) {
            return move + (Bewertung.bewerte(liste.length(), tiefe) * (player * 2 - 1));
        }
        liste = sortiereBewegungen(liste);
        player = 1 - player; // entweder 1 oder 0
        for (int i = 0; i < liste.length(); i += 5) {
            spieleZug(liste.substring(i, i + 5));
            dreheSpielfeld();
            String returnString = alphaBetaSuche(tiefe - 1, beta, alpha, liste.substring(i, i + 5), player);
            int value = Integer.valueOf(returnString.substring(5));
            dreheSpielfeld();
            spieleZugRueckgaengig(liste.substring(i, i + 5));
            if (player == 0) {
                if (value <= beta) {
                    beta = value;
                    if (tiefe == ApplicationManager.suchTiefe) {
                        move = returnString.substring(0, 5);
                    }
                }
            } else {
                if (value > alpha) {
                    alpha = value;
                    if (tiefe == ApplicationManager.suchTiefe) {
                        move = returnString.substring(0, 5);
                    }
                }

            }
            if (alpha >= beta) {
                if (player == 0) {
                    return move + beta;
                } else {
                    return move + alpha;
                }
            }
        }
        if (player == 0) {
            return move + beta;
        } else {
            return move + alpha;
        }
    }

    public static void dreheSpielfeld() {
        String temp = " ";
        for (int i = 0; i < 32; i++) {
            int r = i / 8, c = i % 8;
            if (Character.isUpperCase(ApplicationManager.schachBrett[r][c].charAt(0))) {
                temp = ApplicationManager.schachBrett[r][c].toLowerCase();
            } else {
                temp = ApplicationManager.schachBrett[r][c].toUpperCase();
            }
            if (Character.isUpperCase(ApplicationManager.schachBrett[7 - r][7 - c].charAt(0))) {
                ApplicationManager.schachBrett[r][c] = ApplicationManager.schachBrett[7 - r][7 - c].toLowerCase();
            } else {
                ApplicationManager.schachBrett[r][c] = ApplicationManager.schachBrett[7 - r][7 - c].toUpperCase();
            }
            ApplicationManager.schachBrett[7 - r][7 - c] = temp;
        }
        // Auswechseln der KingPosition zum weiteren Tracken
        int kingTemp = ApplicationManager.positionKoenigGross;
        ApplicationManager.positionKoenigGross = 63 - ApplicationManager.positionKoenigKlein;
        ApplicationManager.positionKoenigKlein = 63 - kingTemp;
    }

    public static void spieleZug(String move) {
        if (move.charAt(4) != 'B') {
            // Normaler Spielzug in Form von: x1,y1,x2,y2,eroberter Spielstein
            ApplicationManager.schachBrett[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] =
                    ApplicationManager.schachBrett[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];
            ApplicationManager.schachBrett[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))] = " ";
            // König bewegt sich
            if ("K".equals(ApplicationManager.schachBrett[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))])) {

                ApplicationManager.positionKoenigGross = 8 * Character.getNumericValue(move.charAt(2)) + Character.getNumericValue(move.charAt(3));
            }
        }
        // wenn Bauer befördert wird
        // Bauern beförderung: alte spalte, neue spalte, eroberter Spielstein, neuer Spielstein, P
        else {
            ApplicationManager.schachBrett[1][Character.getNumericValue(move.charAt(0))] = " ";
            ApplicationManager.schachBrett[0][Character.getNumericValue(move.charAt(1))] = String.valueOf(move.charAt(3));
        }

    }

    public static void spieleZugRueckgaengig(String move) {
        if (move.charAt(4) != 'B') {
            // Normaler Spielzug in Form von: x1,y1,x2,y2,eroberter Spielstein
            ApplicationManager.schachBrett[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))] =
                    ApplicationManager.schachBrett[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))];

            ApplicationManager.schachBrett[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] = String.valueOf(move.charAt(4));
            if ("K".equals(ApplicationManager.schachBrett[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))])) {

                ApplicationManager.positionKoenigGross = 8 * Character.getNumericValue(move.charAt(0)) + Character.getNumericValue(move.charAt(1));
            }
        }
        // wenn Bauer befördert wird
        // Bauern beförderung: alte reihe, neue reihe, eroberter Spielstein, neuer Spielstein, P
        else {
            ApplicationManager.schachBrett[1][Character.getNumericValue(move.charAt(0))] = "B";
            ApplicationManager.schachBrett[0][Character.getNumericValue(move.charAt(1))] = String.valueOf(move.charAt(2));
        }
    }

    public static String sortiereBewegungen(String liste) {
        int[] punkte = new int[liste.length() / 5];
        for (int i = 0; i < liste.length() / 5; i += 5) {
            spieleZug(liste.substring(i, i + 5));
            punkte[i / 5] = -Bewertung.bewerte(-1, 0);

            spieleZugRueckgaengig(liste.substring(i, i + 5));
        }

        String neueListeA = "";
        String neueListeB = "";

        for (int i = 0; i < Math.min(6, liste.length() / 5); i++) {
            int max = -1000000, maxLocation = 0;
            for (int j = 0; j < liste.length() / 5; j++) {
                if (punkte[j] > max) {
                    max = punkte[j];
                    maxLocation = j;
                }
            }
            punkte[maxLocation] = -1000000;
            neueListeA += liste.substring(maxLocation * 5, maxLocation * 5 + 5);
            neueListeB = neueListeB.replace(liste.substring(maxLocation * 5, maxLocation * 5 + 5), "");
        }

        return neueListeA + neueListeB;
    }

    public static String spieleGegnerZug(){
        String move = "";
        Client.sendeDaten(ApplicationManager.clientSocket.output);
        move = Client.empfangeDaten(ApplicationManager.clientSocket.input);
        Client.setMove("");
        Log.i("QUICK DEBUG", move);
        return move;
    }

}
