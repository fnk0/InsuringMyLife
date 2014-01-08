package com.gabilheri.insuringmylife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by marcus on 1/3/14.
 */
public class MyArrayAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] values;

    static class ViewHolder {
        public TextView text;
        public ImageView image;
    }
    public MyArrayAdapter(Context context, String[] values) {
        super(context, R.layout.car_list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        if(rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.car_list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) rowView.findViewById(R.id.carListTitle);
            viewHolder.image = (ImageView) rowView.findViewById(R.id.carListImage);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        String s = values[position];
        holder.text.setText(s);
        return rowView;
    }
}
