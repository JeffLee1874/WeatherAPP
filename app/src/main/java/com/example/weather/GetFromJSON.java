package com.example.weather;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GetFromJSON {   //将城市名-id的JSON文件放入assets中，这个类负责从本地JSON中获取数据

    public List<City> getcityid(Context context, String nameZN) throws IOException, JSONException {
        AssetManager assetManager =  context.getAssets();
        InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open("city.json"));
        BufferedReader br = new BufferedReader(inputStreamReader);
        String string;
        StringBuilder builder = new StringBuilder();
        List<City> result= new ArrayList<City>();
        while((string = br.readLine())!=null)
        {
            builder.append(string);
        }
        br.close();
        inputStreamReader.close();

        JSONArray array = new JSONArray(builder.toString());
        for(int i = 0; i < array.length(); i++)
        {
            JSONObject jsonObject = array.getJSONObject(i) ;
            String provinceZh = jsonObject.getString("provinceZh");
            String id = jsonObject.getString("id");
            String cityzn = jsonObject.getString("cityZh");
            String provinceEn = jsonObject.getString("provinceEn");
            if(cityzn.contains(nameZN) && !nameZN.equals(""))   //对直辖市和自治区的名字进行处理
            {
                switch (provinceZh) {
                    case "天津":
                    case "北京":
                    case "重庆":
                    case "上海":
                        provinceZh += "市";
                        break;
                    case "香港":
                    case "澳门":
                        provinceZh += "特别行政区";
                        break;
                    case "内蒙古":
                    case "西藏":
                        provinceZh += "自治区";
                        break;
                    case "广西":
                        provinceZh += "壮族自治区";
                        break;
                    case "宁夏":
                        provinceZh += "回族自治区";
                        break;
                    case "新疆":
                        provinceZh += "维吾尔自治区";
                        break;
                    default:
                        provinceZh += "省";
                        break;
                }
                result.add(new City(cityzn, provinceZh+" "+provinceEn.toUpperCase(),id));
                Log.i("fuck", "initData "+provinceZh+cityzn+id);
            }
        }

        return result;
    }
}
