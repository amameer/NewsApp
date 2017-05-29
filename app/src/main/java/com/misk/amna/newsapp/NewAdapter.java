package com.misk.amna.newsapp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Amna on 5/29/2017 AD.
 */

public class NewAdapter extends ArrayAdapter<New> {


    public NewAdapter(Context context, ArrayList<New> News) {
        super(context, 0, News);
    }


    @Override
    public View getView(int position, @Nullable View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.new_list_item, parent, false);
        }

        New CurrentNew = getItem(position);

        TextView NewTitle = (TextView) listItemView.findViewById(R.id.title);
        NewTitle.setText(CurrentNew.getTitle());

        TextView NewAutor = (TextView) listItemView.findViewById(R.id.author);
        NewAutor.setText(CurrentNew.getAuthor());

        TextView Description = (TextView) listItemView.findViewById(R.id.description);
        Description.setText(CurrentNew.getDescription());
        return listItemView;
    }


}
