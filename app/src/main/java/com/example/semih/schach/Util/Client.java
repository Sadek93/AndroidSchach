package com.example.semih.schach.Util;

import android.util.Log;

import com.example.semih.schach.Main.ApplicationManager;

import java.io.EOFException;
import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;

import java.net.Socket;


/**
 * Created by semih on 08.06.2015.
 */
public class Client {

    private static final String CNAME = "Client";
    public ObjectOutputStream output;
    public ObjectInputStream input;
    private Socket connection;
    private static String placeHolderMove = "";

    public Client(final String serverIP) {
        final String MNAME = " constructor";
        final String TAG = CNAME + MNAME;

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connectToServer(serverIP);
                    streamSetup();

                } catch (EOFException ex) {
                    Log.i(TAG, "Server ended connection!\n");
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    // close();
                }
            }

        });

        t.start();

    }


    private void connectToServer(String serverIP) throws IOException {
        final String MNAME = "waitForConnection";
        final String TAG = CNAME + MNAME;

        Log.i(TAG, "Attempting Connection... \n");
        connection = new Socket(InetAddress.getByName(serverIP), 2558);
        Log.i(TAG, "Now connected to "
                + connection.getInetAddress().getHostName() + "\n");
    }

    private void streamSetup() throws IOException {
        final String MNAME = "streamSetup";
        final String TAG = CNAME + MNAME;
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();

        input = new ObjectInputStream(connection.getInputStream());

        Log.i(TAG, "Stream are now setup\n");
    }

    public static void sendeDaten(ObjectOutputStream out){
        try {
            out.writeInt(ApplicationManager.positionKoenigGross);
            out.writeInt(ApplicationManager.positionKoenigKlein);
            out.writeInt(ApplicationManager.suchTiefe);
            out.writeObject(ApplicationManager.schachBrett);
            out.flush();
            out.reset();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String empfangeDaten(final ObjectInputStream in){
        final String MNAME = "streamSetup";
        final String TAG = CNAME + MNAME;

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String move = "";
                try {
                    move = (String)in.readObject();
                    Log.i(TAG, "Empfangen Bewegung: " + move);
                    setMove(move);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
        t.start();

        while(placeHolderMove == ""){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



        return placeHolderMove;
    }

    public static void setMove(String move){
       placeHolderMove = move;
    }

    private void close() {

        try {
            if (output != null) output.close();
            if (input != null) input.close();
            if (connection != null) connection.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
