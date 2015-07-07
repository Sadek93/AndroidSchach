package com.example.semih.schach.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.semih.schach.R;

import java.util.ArrayList;

/**
 * Created by semih on 25.06.2015.
 */
public class ConsoleListAdapter extends ArrayAdapter<ConsoleItem> {
    public ConsoleListAdapter(Context context, ArrayList<ConsoleItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ConsoleItem item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.console_list_item, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.idTitle);
        TextView descr = (TextView) convertView.findViewById(R.id.idDescr);

        title.setText(item.getTitle());
        descr.setText(item.getDescr());
        // Lookup view for data population

        // Populate the data into the template view using the data object

        // Return the completed view to render on screen
        return convertView;
    }
}
