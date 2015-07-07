package com.example.semih.schach.Util;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.example.semih.schach.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class consoleFragment extends Fragment {

    ScrollView console;

    public consoleFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_console, container, false);
    }


}
