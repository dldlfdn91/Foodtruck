package com.ftproject.food_truck;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {

    Context context;
    int layout;
    ArrayList<GridViewItem> list;

    LayoutInflater inf;

    public GridViewAdapter(Context context, int layout, ArrayList<GridViewItem> list) {
        this.context =context;
        this.layout =layout;
        this.list =list;

        inf =(LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { //View를 만들어서 리턴해준다.

        if(convertView ==null)
        {
            convertView =inf.inflate(layout,null);
        }

        ImageView imgIcon =(ImageView)convertView.findViewById(R.id.imageView01);

        TextView txtName =(TextView)convertView.findViewById(R.id.textView01);
        TextView txtPrice =(TextView)convertView.findViewById(R.id.textView02);

        GridViewItem gvi= list.get(position);

        Picasso .with(context)
                .load(gvi.getImgURL())
                .into(imgIcon);

        txtName.setText(gvi.getName());
        txtPrice.setText(gvi.getPrice());

        return convertView;
    }
}
