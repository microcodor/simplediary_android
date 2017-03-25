package com.wxdroid.simplediary.widget.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wxdroid.simplediary.R;

/**
 * Created by jinchun on 2017/1/7.
 */

public class SpinnerTimerAdapter extends ArrayAdapter<String> {
    Context context;
    String[] items = new String[] {};

    public SpinnerTimerAdapter(final Context context,
                               final int textViewResourceId, final String[] objects) {
        super(context, textViewResourceId, objects);
        this.items = objects;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(
                    R.layout.item_spinner_simple, parent, false);
        }

        TextView tv = (TextView) convertView
                .findViewById(R.id.item_text);
        tv.setText(items[position]);
        //tv.setGravity(Gravity.CENTER);
        //tv.setTextColor(context.getResources().getColor(R.color.idea_bottom_text_color));
        //tv.setTextSize(30);
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(
                    R.layout.item_spinner_simple, parent, false);
        }

        // android.R.id.text1 is default text view in resource of the android.
        // android.R.layout.simple_spinner_item is default layout in resources of android.

        TextView tv = (TextView) convertView
                .findViewById(R.id.item_text);
        tv.setText(items[position]);
        //tv.setGravity(Gravity.CENTER);
        //tv.setTextColor(context.getResources().getColor(R.color.idea_bottom_text_color));
        //tv.setTextSize(30);
        return convertView;
    }
}
