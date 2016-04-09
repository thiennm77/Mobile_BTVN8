package com.example.qlhs;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter {

    private ArrayList<MainActivity.HocSinh> items;
    private Context context;

    public CustomAdapter(Context context, ArrayList<MainActivity.HocSinh> objects) {
        super(context, R.layout.list_row , objects);
        items = objects;
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row, null);

            holder = new ViewHolder();
            holder.maHS= (TextView) convertView.findViewById(R.id.maHS);
            holder.tenHS= (TextView) convertView.findViewById(R.id.tenHS);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        MainActivity.HocSinh hs = items.get(position);
        holder.maHS.setText(hs.getMaHS());
        holder.tenHS.setText(hs.getTenHS());

        return convertView;
    }

    class ViewHolder
    {
        public TextView maHS;
        public TextView tenHS;
    }
}
