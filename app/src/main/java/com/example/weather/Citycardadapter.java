package com.example.weather;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class Citycardadapter extends ArrayAdapter {

    private  final int resourceID;

    public Citycardadapter(Context context, int textViewResourceId, List<Citycarditem> objects, String flag) {
        super(context, textViewResourceId, objects);
        this.resourceID = textViewResourceId;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Citycarditem citycarditem = (Citycarditem) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceID, null);  //将city_item的layou转换为view
        TextView cityname = (TextView) view.findViewById(R.id.cardlocation);
        TextView cardtem = (TextView) view.findViewById(R.id.cardwea);
        assert citycarditem != null;
        cityname.setText(citycarditem.getCityname());
        cardtem.setText(citycarditem.getTemwea());
        return view;
    }
}
