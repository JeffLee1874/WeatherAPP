package com.example.weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import lecho.lib.hellocharts.model.PointValue;


public class HttpConnect {

    private String APPID = "49888748";
    private String APPSecret = "JXpvYs3U";
    private String version = "v6";
    private String pathv6 = "https://tianqiapi.com/api?version=v6&appid=49888748&appsecret=JXpvYs3U";
    private String pathv1 = "https://tianqiapi.com/api?version=v1&appid=49888748&appsecret=JXpvYs3U";
    private String pathv8 = "https://tianqiapi.com/api?version=v8&appid=49888748&appsecret=JXpvYs3U";
    private String amap = "https://restapi.amap.com/v3/config/district?keywords=北京&subdistrict=1&key=a49940d20155cf43485776f82d0b49c2";
    private JSONObject jsonObject;
    private String message = "";

    public HttpConnect()
    {

    }
    public HttpConnect(String way, String time, String location) throws IOException, JSONException {
        if(way.equals("IP") && time.equals("today"))
            connectwithIP_today();
        else if(way.equals("location") && time.equals("today"))
            connectwithlocation_today(location);
        else if(way.equals("IP") && time.equals("7day"))
            connectwithIP_sevenday();
        else if(way.equals("location") && time.equals("7day"))
            connectwithlocation_sevenday(location);
    }
    public HttpConnect(String flag) throws IOException, JSONException {
       connect_v8();
    }
    public void MapConnect(String city, String flag, String code) throws IOException, JSONException {
        if(!flag.equals("weather"))
        {
            amap = amap.replace("北京",city.replace("市", ""));
        }
        else
            amap = "https://restapi.amap.com/v3/weather/weatherInfo?extensions=base&city=" + code + "&key=a49940d20155cf43485776f82d0b49c2";
        Log.i("fuck2","连接检查" + amap);
        HttpsURLConnection connection = (HttpsURLConnection) new URL(amap).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(10000);

        if(connection.getResponseCode() == 200)
        {
            InputStream json = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(json));
            String string;
            StringBuilder builder = new StringBuilder();
            while((string = bufferedReader.readLine())!=null)
            {
                builder.append(string);
            }
            bufferedReader.close();
            jsonObject = new JSONObject(builder.toString());
            if(flag.equals("weather"))
            {
                String string1 = jsonObject.getString("lives");
                JSONObject object = new JSONArray(string1).getJSONObject(0);
                message = object.getString("temperature") + "℃ | " + object.getString("weather");
            }
        }
    }


    public ArrayList<MarkerOptions> getMarkers(Context context) throws JSONException, IOException {
        ArrayList<MarkerOptions> markerOptions = new ArrayList<>();
        String str = jsonObject.getString("districts");
        JSONArray jsonArray = new JSONArray(str);
        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
        JSONArray jsonArray1 = new JSONArray(jsonObject1.getString("districts"));
        for(int i = 0; i < jsonArray1.length(); i++)
        {
            View view = View.inflate(context, R.layout.infowindow, null);
            TextView textView = view.findViewById(R.id.info);
            JSONObject temp = jsonArray1.getJSONObject(i);
            MarkerOptions mark = new MarkerOptions();
            String[] strings = temp.getString("center").split(",");
            LatLng latLng = new LatLng(Double.parseDouble(strings[1]), Double.parseDouble(strings[0]));
            mark.position(latLng);
            MapConnect("null", "weather", temp.getString("adcode"));
            textView.setText(temp.getString("name") + "\r\n" + message);
            mark.icon(BitmapDescriptorFactory.fromView(view));
            markerOptions.add(mark);
        }
        return markerOptions;
    }




    private void connect_v8() throws IOException, JSONException {
        HttpsURLConnection connection = (HttpsURLConnection) new URL(pathv8).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(10000);

        if(connection.getResponseCode() == 200)
        {
            InputStream json = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(json));
            String string;
            StringBuilder builder = new StringBuilder();
            while((string = bufferedReader.readLine())!=null)
            {
                builder.append(string);
            }
            bufferedReader.close();
            jsonObject = new JSONObject(builder.toString());
        }
    }
    private void connectwithIP_today() throws IOException, JSONException {
        HttpsURLConnection connection = (HttpsURLConnection) new URL(pathv6).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(10000);

        if(connection.getResponseCode() == 200)
        {
            InputStream json = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(json));
            String string;
            StringBuilder builder = new StringBuilder();
            while((string = bufferedReader.readLine())!=null)
            {
                builder.append(string);
            }
            bufferedReader.close();
            jsonObject = new JSONObject(builder.toString());
        }

    }
    private void connectwithlocation_today(String city) throws IOException, JSONException {
        HttpsURLConnection connection = (HttpsURLConnection) new URL(pathv6 + "&city="+city).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(10000);

        if(connection.getResponseCode() == 200)
        {
            InputStream json = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(json));
            String string;
            StringBuilder builder = new StringBuilder();
            while((string = bufferedReader.readLine())!=null)
            {
                builder.append(string);
            }
            bufferedReader.close();
            jsonObject = new JSONObject(builder.toString());
        }
    }
    private void connectwithIP_sevenday() throws IOException, JSONException {
        HttpsURLConnection connection = (HttpsURLConnection) new URL(pathv1).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(10000);
        if(connection.getResponseCode() == 200)
        {
            InputStream json = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(json));
            String string;
            StringBuilder builder = new StringBuilder();
            while((string = bufferedReader.readLine())!=null)
            {
                builder.append(string);
            }
            bufferedReader.close();
            jsonObject = new JSONObject(builder.toString());
            JSONArray  jsonArray = new JSONArray(jsonObject.getString("data"));
            JSONObject i = jsonArray.getJSONObject(0);
            Log.i("seven", i.getString("wea"));
        }
    }
    private void connectwithlocation_sevenday(String city) throws IOException, JSONException {
        HttpsURLConnection connection = (HttpsURLConnection) new URL(pathv1+ "&city="+city).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(10000);

        if(connection.getResponseCode() == 200)
        {
            InputStream json = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(json));
            String string;
            StringBuilder builder = new StringBuilder();
            while((string = bufferedReader.readLine())!=null)
            {
                builder.append(string);
            }
            bufferedReader.close();
            jsonObject = new JSONObject(builder.toString());
        }
    }


    private byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


    public Bitmap getRainIMG() throws JSONException, IOException {
        JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
        JSONObject object = jsonArray.getJSONObject(0);
        String path = object.getString("pic");
        URL url = new URL(path);
        URLConnection connection = url.openConnection();
        connection.connect();
        byte[] getData = readInputStream(connection.getInputStream());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(new ByteArrayInputStream(getData), new Rect(0,259, 400, 0), options);
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(getData), new Rect(0,259, 400, 0), options);
        Log.i("fuck2","图片地址" + path);
        Log.i("fuck2","图片" + bitmap.toString());
        return bitmap;
    }


    public String[] getsevenwea() throws JSONException {
        String[] wea = new String[13];
        JSONArray  jsonArray = new JSONArray(jsonObject.getString("data"));
        JSONObject temp = jsonArray.getJSONObject(0);
        wea[0] = temp.getString("wea");
        for(int i = 1; i < jsonArray.length(); i++)
        {
            JSONObject j = jsonArray.getJSONObject(i);
            wea[i] = j.getString("wea");
            wea[i+6] = j.getString("date").substring(5);
        }
        return wea;
    }

    public float[] twentyfour_tem() throws JSONException {
        JSONArray  jsonArray = new JSONArray(jsonObject.getString("data"));
        JSONObject temp = jsonArray.getJSONObject(0);
        JSONArray jsonArray1 = new JSONArray(temp.getString("hours"));
        float[] yValue = {-100, -100, -100, -100, -100, -100, -100, -100};
        for(int i = 0; i < jsonArray1.length(); i++)
        {
            JSONObject j = jsonArray1.getJSONObject(i);
            float temp1 = Float.valueOf(j.getString("day").substring(3,5));
            float temp2 = Float.valueOf(j.getString("tem").substring(0,j.getString("tem").length()-1));
            if(temp1 == 8)
            {
                yValue[0] = temp2;
            }
            else if(temp1 == 11)
            {
                yValue[1] = temp2;
            }
            else if(temp1 == 14)
                yValue[2] = temp2;
            else if(temp1 == 17)
                yValue[3] = temp2;
            else if(temp1 == 20)
                yValue[4] = temp2;
            else if(temp1 == 23)
                yValue[5] = temp2;
            else if(temp1 == 2)
                yValue[6] = temp2;
            else if(temp1 == 5)
                yValue[7] = temp2;

        }

        return yValue;
    }
//    public List<PointValue> twentyfour_tem2() throws JSONException {
//        JSONArray  jsonArray = new JSONArray(jsonObject.getString("data"));
//        JSONObject temp = jsonArray.getJSONObject(0);
//        JSONArray jsonArray1 = new JSONArray(temp.getString("hours"));
//        List<PointValue> pointValues = new ArrayList<>();
//        float[] yValue = {-100, -100, -100, -100, -100, -100, -100, -100};
//        int [] xvalue = {8, 11, 14, 17, 20, 23, 26, 29};
//        for(int i = 0; i < jsonArray1.length(); i++)
//        {
//            JSONObject j = jsonArray1.getJSONObject(i);
//            int temp1 = Integer.valueOf(j.getString("day").substring(3,5));
//            float temp2 = Float.valueOf(j.getString("tem").substring(0,j.getString("tem").length()-1));
//            if(temp1 == 8)
//            {
//                yValue[0] = temp2;
//            }
//            else if(temp1 == 11)
//            {
//                yValue[1] = temp2;
//            }
//            else if(temp1 == 14)
//                yValue[2] = temp2;
//            else if(temp1 == 17)
//                yValue[3] = temp2;
//            else if(temp1 == 20)
//                yValue[4] = temp2;
//            else if(temp1 == 23)
//                yValue[5] = temp2;
//            else if(temp1 == 2)
//                yValue[6] = temp2;
//            else if(temp1 == 5)
//                yValue[7] = temp2;
//        }
//        for(int i = 0; i < yValue.length; i++)
//        {
//            pointValues.add(new PointValue(xvalue[i], yValue[i]));
//        }
//        return pointValues;
//    }
    public int[] getsevenweaimg() throws JSONException {
        int[] wea = new int[7];
        JSONArray  jsonArray = new JSONArray(jsonObject.getString("data"));
        for(int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject j = jsonArray.getJSONObject(i);
            wea[i] = weatherjudge(j.getString("wea_img"));
        }
        return wea;
    }
    public String get_update_time() throws JSONException{
        return "更新时间: " + jsonObject.getString("update_time");
    }
    public int[] get_background_linear() throws JSONException {   //整个页面的背景颜色
        String string = jsonObject.getString("wea_img");
        switch (string) {
            case "wu":
            case "bingbao":
            case "xue":
                return new int[]{R.color.bgxuewu};
            case "lei":
            case "shachen":
                return new int[]{R.color.bgleishachen};
            case "yin":
                Log.i("fuck", "color: " + R.color.bgyin);
                return new int[]{R.color.bgyin};
            case "yun":
            case "qing":
                return new int[]{R.color.bgqingyun};
            case "yu":
                return new int[]{R.color.bgyu};
            default:
                return new int[]{0};
        }
    }
    public int[] get_gif() throws JSONException {          //gif图，也就是界面最顶上的图片
        String string = jsonObject.getString("wea_img");
        switch (string) {
            case "wu":
                return new int[]{R.drawable.weawu};
            case "bingbao":
            case "xue":
                return new int[]{R.drawable.weaxue};
            case "lei":
                return new int[]{R.drawable.wealei};
            case "shachen":
                return new int[]{R.drawable.weashachen};
            case "yin":
                return new int[]{R.drawable.weabgyin};
            case "yun":
                return new int[]{R.drawable.weabgyun};
            case "qing":
                return new int[]{R.drawable.weabgqing1};
            case "yu":
                return new int[]{R.drawable.weayu};
            default:
                return new int[]{0};
        }
    }
    public int[] get_location_color() throws JSONException {           //定位的字体的颜色
        String string = jsonObject.getString("wea_img");
        switch (string) {
            case "xue":
            case "yin":
            case "yu":
            case "yun":
            case "wu":
                return new int[]{R.color.black};
            default:
                return new int[]{R.color.white};
        }
    }
    public int getlocationimg() throws JSONException {
        String string = jsonObject.getString("wea_img");
        switch (string) {
            case "xue":
            case "yin":
            case "yu":
            case "yun":
            case "wu":
                return R.drawable.placeblack;
            default:
                return R.drawable.place;
        }
    }
    public int getListcolor() throws JSONException {
        String string = jsonObject.getString("wea_img");
        switch (string) {
            case "xue":
            case "yin":
            case "yu":
            case "yun":
            case "wu":
                return R.drawable.listblack;
            default:
                return R.drawable.list;
        }
    }
    public String getnowtem() throws JSONException {
        return jsonObject.getString("tem")+"℃";
    }

    public String getlowtem() throws JSONException {
        Log.i("fuck", "最低温"+jsonObject.getString("tem2"));
        return jsonObject.getString("tem2")+"℃";
    }

    public String gethightem() throws JSONException {
        Log.i("fuck", "最高温"+jsonObject.getString("tem1"));
        return jsonObject.getString("tem1")+"℃";
    }

    public String getwether() throws JSONException {
        Log.i("fuck", "天气"+jsonObject.getString("wea"));
        return jsonObject.getString("wea");
    }

    public String getLocation() throws JSONException {
        Log.i("fuck", "地址"+jsonObject.getString("city"));
        return jsonObject.getString("city") + " " + jsonObject.getString("cityEn").toUpperCase();
    }

    public String getLocationCN() throws JSONException {
        return jsonObject.getString("city");
    }
    public String getWinSpeed() throws JSONException {
        return jsonObject.getString("win_speed");
    }
    public String getWin() throws JSONException {
        return jsonObject.getString("win");
    }
    public String getWinMeter() throws JSONException {
        return jsonObject.getString("win_meter");
    }
    public String getHumidity() throws JSONException {
        return jsonObject.getString("humidity");
    }
    public String getVisibility() throws JSONException {
        return jsonObject.getString("visibility");
    }
    public String getPressure() throws JSONException {
        return jsonObject.getString("pressure");
    }
    public String getAir() throws JSONException {
        return jsonObject.getString("air")+" - " + jsonObject.getString("air_level");
    }
    public String getair_tips() throws JSONException {
        return jsonObject.getString("air_tips");
    }

    public int getweatherimg() throws JSONException {
        String str = jsonObject.getString("wea_img");
        Log.i("fuck", "天气img"+jsonObject.getString("wea_img"));
        return weatherjudge(str);
    }
    public int weatherjudge(String string)
    {
        switch (string) {
            case "xue":
                return R.drawable.xue;
            case "lei":
                return R.drawable.lei;
            case "shachen":
                return R.drawable.shachen;
            case "wu":
                return R.drawable.wu;
            case "bingbao":
                return R.drawable.bingbao;
            case "yun":
                return R.drawable.yun;
            case "yu":
                return R.drawable.yu;
            case "yin":
                Log.i("fuck", "天气imgid" + String.valueOf(R.drawable.yin));
                return R.drawable.yin;
            case "qing":
                return R.drawable.qing;
            default:
                return 0;
        }
    }

    public List<PointValue> getpoitstem1() throws JSONException {   //最高温
        JSONArray  jsonArray = new JSONArray(jsonObject.getString("data"));
        List<PointValue> pointValues = new ArrayList<PointValue>();
        for(int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject j = jsonArray.getJSONObject(i);
            PointValue pointValue = new PointValue(i, Float.valueOf(j.getString("tem1").substring(0,j.getString("tem1").length()-1)));
            pointValues.add(pointValue);
        }
        return pointValues;
    }
    public List<PointValue> getpoitstem2() throws JSONException {    //最低温
        JSONArray  jsonArray = new JSONArray(jsonObject.getString("data"));
        List<PointValue> pointValues = new ArrayList<PointValue>();
        for(int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject j = jsonArray.getJSONObject(i);
            PointValue pointValue = new PointValue(i, Float.valueOf(j.getString("tem2").substring(0,j.getString("tem2").length()-1)));
            pointValues.add(pointValue);
        }
        return pointValues;
    }
}
