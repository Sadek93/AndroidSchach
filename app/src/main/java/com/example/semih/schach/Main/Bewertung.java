package com.example.semih.schach.Main;

import com.example.semih.schach.Main.ApplicationManager;
import com.example.semih.schach.Main.SchachLogik;

/**
 * Created by semih on 04.06.2015.
 */
public class Bewertung {
    static int	bauernSpielfeld[][]		= {{0, 0, 0, 0, 0, 0, 0, 0},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {10, 10, 20, 30, 30, 20, 10, 10},
            {5, 5, 10, 25, 25, 10, 5, 5},
            {0, 0, 0, 20, 20, 0, 0, 0},
            {5, -5, -10, 0, 0, -10, -5, 5},
            {5, 10, 10, -20, -20, 10, 10, 5},
            {0, 0, 0, 0, 0, 0, 0, 0}};

    static int	turmSpielfeld[][]		= {{0, 0, 0, 0, 0, 0, 0, 0},
            {5, 10, 10, 10, 10, 10, 10, 5},
            {-5, 0, 0, 0, 0, 0, 0, -5},
            {-5, 0, 0, 0, 0, 0, 0, -5},
            {-5, 0, 0, 0, 0, 0, 0, -5},
            {-5, 0, 0, 0, 0, 0, 0, -5},
            {-5, 0, 0, 0, 0, 0, 0, -5},
            {0, 0, 0, 5, 5, 0, 0, 0}};

    static int	springerSpielfeld[][]		= {{-50, -40, -30, -30, -30, -30, -40, -50},
            {-40, -20, 0, 0, 0, 0, -20, -40},
            {-30, 0, 10, 15, 15, 10, 0, -30},
            {-30, 5, 15, 20, 20, 15, 5, -30},
            {-30, 0, 15, 20, 20, 15, 0, -30},
            {-30, 5, 10, 15, 15, 10, 5, -30},
            {-40, -20, 0, 5, 5, 0, -20, -40},
            {-50, -40, -30, -30, -30, -30, -40, -50}};

    static int	laeuferSpielfeld[][]		= {{-20, -10, -10, -10, -10, -10, -10, -20},
            {-10, 0, 0, 0, 0, 0, 0, -10},
            {-10, 0, 5, 10, 10, 5, 0, -10},
            {-10, 5, 5, 10, 10, 5, 5, -10},
            {-10, 0, 10, 10, 10, 10, 0, -10},
            {-10, 10, 10, 10, 10, 10, 10, -10},
            {-10, 5, 0, 0, 0, 0, 5, -10},
            {-20, -10, -10, -10, -10, -10, -10, -20}};

    static int	dameSpielfeld[][]		= {{-20, -10, -10, -5, -5, -10, -10, -20},
            {-10, 0, 0, 0, 0, 0, 0, -10},
            {-10, 0, 5, 5, 5, 5, 0, -10},
            {-5, 0, 5, 5, 5, 5, 0, -5},
            {0, 0, 5, 5, 5, 5, 0, -5},
            {-10, 5, 5, 5, 5, 5, 0, -10},
            {-10, 0, 5, 0, 0, 0, 0, -10},
            {-20, -10, -10, -5, -5, -10, -10, -20}};

    static int	koenigSpielfeld[][]	= {{-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-20, -30, -30, -40, -40, -30, -30, -20},
            {-10, -20, -20, -20, -20, -20, -20, -10},
            {20, 20, 0, 0, 0, 0, 20, 20},
            {20, 30, 10, 0, 0, 10, 30, 20}};

    static int	koenigEndSpielfeld[][]	= {{-50, -40, -30, -20, -20, -30, -40, -50},
            {-30, -20, -10, 0, 0, -10, -20, -30},
            {-30, -10, 20, 30, 30, 20, -10, -30},
            {-30, -10, 30, 40, 40, 30, -10, -30},
            {-30, -10, 30, 40, 40, 30, -10, -30},
            {-30, -10, 20, 30, 30, 20, -10, -30},
            {-30, -30, 0, 0, 0, 0, -30, -30},
            {-50, -30, -30, -30, -30, -30, -30, -50}};

    public static int bewerte(int list, int depth) {

        int counter = 0;
        int material = bewerteSteine();

        counter += bewerteAttacke();
        counter += material;
        counter += bewerteBewegungen(list, depth, material);
        counter += bewertePositionen(material);

        SchachLogik.dreheSpielfeld();
        material = bewerteSteine();

        counter -= bewerteAttacke();
        counter -= material;
        counter -= bewerteBewegungen(list, depth, material);
        counter -= bewertePositionen(material);

        SchachLogik.dreheSpielfeld();

        return -(counter + depth * 50);
    }

    public static int bewerteAttacke() {
        int counter = 0;
        int temp = ApplicationManager.positionKoenigGross;
        for (int i = 0; i < 64; i++) {
            switch (ApplicationManager.schachBrett[i / 8][i % 8]) { // [row][column]
                case "B" : {
                    ApplicationManager.positionKoenigGross = i;
                    if (!SchachLogik.koenigSicher()) {
                        counter -= 64;
                    }
                }
                break;
                case "T" : {
                    ApplicationManager.positionKoenigGross = i;
                    if (!SchachLogik.koenigSicher()) {
                        counter -= 500;
                    }
                }
                break;
                case "S" : {
                    ApplicationManager.positionKoenigGross = i;
                    if (!SchachLogik.koenigSicher()) {
                        counter -= 300;
                    }
                }
                break;
                case "L" : {
                    ApplicationManager.positionKoenigGross = i;
                    if (!SchachLogik.koenigSicher()) {
                        counter -= 300;
                    }
                }
                break;
                case "D" : {
                    ApplicationManager.positionKoenigGross = i;
                    if (!SchachLogik.koenigSicher()) {
                        counter -= 900;
                    }
                }
                break;
            }
        }
        ApplicationManager.positionKoenigGross = temp;
        if (!SchachLogik.koenigSicher()) {
            counter -= 200;
        }
        return counter / 2;

    }

    public static int bewerteSteine() {
        int counter = 0;
        int bishopCounter = 0;
        for (int i = 0; i < 64; i++) {
            switch (ApplicationManager.schachBrett[i / 8][i % 8]) { // [row][column]
                case "B" :
                    counter += 100;
                    break;
                case "T" :
                    counter += 500;
                    break;
                case "S" :
                    counter += 300;
                    break;
                case "L" :
                    bishopCounter++;
                    break;
                case "D" :
                    counter += 900;
                    break;
            }
        }
        if (bishopCounter >= 2) {
            counter += 300 * bishopCounter;
        } else if (bishopCounter == 1) {
            counter += 250;
        }
        return counter;
    }

    public static int bewerteBewegungen(int listLength, int depth, int material) {
        int counter = 0;
        counter += listLength; // 5 Punkte f�r jeden Move
        if (listLength == 0) {
            if (!SchachLogik.koenigSicher()) { // Schachmatt
                counter -= 200000 * depth;

            } else {
                counter -= 150000 * depth;
            }
        }

        return counter;
    }

    public static int bewertePositionen(int material) {
        int counter = 0;

        for (int i = 0; i < 64; i++) {

            switch (ApplicationManager.schachBrett[i / 8][i % 8]) { // [row][column]
                case "B" :
                    counter += bauernSpielfeld[i / 8][i % 8];
                    break;
                case "T" :
                    counter += turmSpielfeld[i / 8][i % 8];
                    break;
                case "S" :
                    counter += springerSpielfeld[i / 8][i % 8];
                    break;
                case "L" :
                    counter += laeuferSpielfeld[i / 8][i % 8];
                    break;
                case "D" :
                    counter += dameSpielfeld[i / 8][i % 8];
                    break;
                case "K" :
                    if (material >= 1750) {
                        counter += koenigSpielfeld[i / 8][i % 8];
                        counter += SchachLogik.moeglichK(ApplicationManager.positionKoenigGross).length() * 10;
                    } else {
                        counter += koenigEndSpielfeld[i / 8][i % 8];
                        counter += SchachLogik.moeglichK(ApplicationManager.positionKoenigGross).length() * 30;
                    }
                    break;
            }
        }

        return counter;
    }
}
