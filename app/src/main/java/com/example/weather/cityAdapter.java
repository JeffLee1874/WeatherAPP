package com.example.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class cityAdapter extends ArrayAdapter {

    private  final int resourceID;

    public cityAdapter(Context context, int textViewResourceId, List<City> objects) {
        super(context, textViewResourceId, objects);
        this.resourceID = textViewResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        City city = (City)getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceID, null);  //将city_item的layou转换为view
        TextView cityname = (TextView) view.findViewById(R.id.city_name);
        TextView provincename = (TextView) view.findViewById(R.id.provincename);
        TextView cityid = (TextView) view.findViewById(R.id.cityid);
        cityname.setText(city.getCityName());
        provincename.setText(city.getProvincename());
        return view;
    }
}
