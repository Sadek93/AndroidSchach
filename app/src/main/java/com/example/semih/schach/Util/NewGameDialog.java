package com.example.semih.schach.Util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.semih.schach.Main.ApplicationManager;
import com.example.semih.schach.Main.MenuScreen;
import com.example.semih.schach.R;

/**
 * Created by semih on 05.07.2015.
 */
public class NewGameDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.new_game)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(0);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ApplicationManager.figurGeklickt = false;
                        ApplicationManager.on_game_screen = false;
                        Intent i = new Intent(getActivity(), MenuScreen.class);
                        startActivity(i);
                    }
                });

        return builder.create();
    }
}