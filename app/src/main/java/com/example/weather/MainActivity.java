package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CustomMapStyleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebViewClient;

import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {
    public static Citycarditem firstcity;
    private citycard_data weadata;
    private ImageButton imageButton;
    private ImageButton imageButton2;
    private ColumnChartView columnChartView;
    private NoScrollViewPager viewPager;
    private List<View> views;
    private View view1;
    private View view2;
    private View view3;
    private MapView mapView;
    private ViewPagerAdapter viewPagerAdapter;
    private List<City> cityList = new ArrayList<City>();
    private GetFromJSON getFromJSON = new GetFromJSON();
    private LayoutInflater li;
    private LineChartView lineChart2;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView searchView;
    private HttpConnect httpConnect;
    private HttpConnect httpConnect1;
    private HttpConnect httpConnect3;
    private HttpConnect httpConnect4;
    private MyHandler myHandler;
    private MyHandler myHandler2;
    private MyHandler myHandler3;
    private MyHandler myHandler4;
    private MyHandler myHandler5;
    private MyHandler myHandler6;
    private MyHandler myHandler7;
    private MyHandler myHandler8;
    private MyHandler myHandler9;    //改变适配器
    private int isableGetRainPic = 1;     //由于接口调用次数限制，所以增加此变量来控制
    private String[] x = {"8时","11时","14时", "17时", "20时", "23时", "2时", "5时"};
    private List<String> title = new ArrayList<>();
    private String getcity = "";
    private String cityswpie;
    private Bitmap bitmap;
    private AMap aMap;
    private ListView listView;
    //声明mListener对象，定位监听器
    private AMapLocationClient aMapLocationClient;
    private AMapLocationClientOption aMapLocationClientOption;
    private UiSettings uiSettings;
    private com.tencent.smtt.sdk.WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weadata = new citycard_data(this, "citicardata");    //准备好citicardata文件

        setStatusBarFullTransparentDark();
        li = getLayoutInflater();
        viewPager = findViewById(R.id.viewpager);
        view1 = li.inflate(R.layout.wea_detail_item, null, false);
        view2 = li.inflate(R.layout.map_weather, null, false);
        view3 = li.inflate(R.layout.webview, null, false);
        listView = view2.findViewById(R.id.citylist2);
        title.add("天气详情");
        title.add("城市群天气");
        title.add("专业数据");
        mapView = view2.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。
//初始化地图控制器对象
        try {
            Mapini(true);
            connnect_and_setdata("IP",null);
            webviewini();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        imageButton = findViewById(R.id.List);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, City_Card.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.rightinto,R.anim.lleftout);
            }
        });

        imageButton2 = view2.findViewById(R.id.startlocate);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mapini(true);
            }
        });


        swipeRefreshLayout = view1.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() { //根据网络数据给textview赋值
                swipeRefreshLayout.setRefreshing(true);
                try {
                    Log.i("fuck2", "刷新的城市：" + cityswpie);
                    if(cityswpie!=null)
                        connnect_and_setdata("location", cityswpie);
                    else
                        connnect_and_setdata("IP", null);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                } finally {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        searchView = view2.findViewById(R.id.mapsearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                try {
                    cityList = getFromJSON.getcityid(MainActivity.this, newText);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                cityAdapter adapter = new cityAdapter(MainActivity.this, R.layout.city_item, cityList);
                listView.setDividerHeight(0);                    //去除分割线
                listView.setVerticalScrollBarEnabled(false);     //去掉滚动条
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        City city = cityList.get(position);
                        getcity = city.getCityName()+"市";
                        Mapini(false);
                        Log.i("card","shit2 " + city.getCityName());
                        listView.setAdapter(null);
                        searchView.setQuery("", true);
                        InputMethodManager manager = ((InputMethodManager)MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE));  //收起键盘
                        if(manager != null)
                            manager.hideSoftInputFromWindow(MainActivity.this.getWindow().getDecorView().getWindowToken(), 0);
                    }
                });
                return false;
            }
        });

        ImageButton imageButton2 = view3.findViewById(R.id.refreshurl);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(webView != null)
                {
                    webView.stopLoading();
                    Log.i("fuck2", "开始刷新");
                    webView.reload();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        aMapLocationClient.onDestroy();
        mapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }
    protected void setStatusBarFullTransparentDark() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data )
    {
        super.onActivityResult(requestCode, resultCode,data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            Bundle bundle = data.getExtras();
            assert bundle != null;
            try {
//                view1 = li.inflate(R.layout.wea_detail_item, null, false);
                getcity = bundle.getString("c") + "市";
                cityswpie = bundle.getString("c");
                Mapini(false);
                connnect_and_setdata("location",bundle.getString("c"));
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void connnect_and_setdata(final String way, final String location) throws IOException, JSONException  //第一个参数是定位方式，第二个参数是通过cityname来定位则需要的字符串
    {
        myHandler = new MyHandler();     //在子线程中发送修改textview的请求
        myHandler2 = new MyHandler();    //在子线程中发送修改imageview的请求\
        myHandler3 = new MyHandler();    //在子线程中发送修改textview的请求
        myHandler4 = new MyHandler();    //在子线程中发送修改imageview的请求
        myHandler5 = new MyHandler();
        myHandler6 = new MyHandler();
        myHandler7 = new MyHandler();
        myHandler8 = new MyHandler();
        myHandler9 = new MyHandler();
        views = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    columnChartView = (ColumnChartView) view1.findViewById(R.id.temp_chart);
                    lineChart2 = (LineChartView)view1.findViewById(R.id.forcastchart);
                    httpConnect = new HttpConnect(way, "today",location);
                    httpConnect1 = new HttpConnect(way, "7day",location);
                    Log.i("card", "正常");
                    httpConnect1.twentyfour_tem();
                    Log.i("card", "正常");
                    weadata.putWeaValue(httpConnect.getLocationCN(), httpConnect.getnowtem()+" / " + httpConnect.getwether());
                    if(way.equals("IP"))
                        firstcity = new Citycarditem(httpConnect.getLocationCN(),httpConnect.getnowtem()+" / "+httpConnect.getwether());
                    if(isableGetRainPic == 1)
                    {
                        httpConnect3 = new HttpConnect("fuck");
                        bitmap = httpConnect3.getRainIMG();
                        isableGetRainPic = 0;
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                GifImageView[] gifImageViews = {findViewById(R.id.gif)};
                LinearLayout[] linearLayouts = {findViewById(R.id.background)};
                TextView[] textViews = {findViewById(R.id.location)};
                TextView[] textView = {findViewById(R.id.update_time), findViewById(R.id.temperature_now), findViewById(R.id.lowest), findViewById(R.id.highest), findViewById(R.id.weather_now),
                        findViewById(R.id.location), view1.findViewById(R.id.win_speed_info), view1.findViewById(R.id.win_info), view1.findViewById(R.id.win_meter_info), view1.findViewById(R.id.humidity_info),
                        view1.findViewById(R.id.visibility_info), view1.findViewById(R.id.pressure_info), view1.findViewById(R.id.air_info), view1.findViewById(R.id.air_tips_info)};
                TextView[] textView2 = {view1.findViewById(R.id.date1wea), view1.findViewById(R.id.date2wea), view1.findViewById(R.id.date3wea), view1.findViewById(R.id.date4wea), view1.findViewById(R.id.date5wea),
                        view1.findViewById(R.id.date6wea), view1.findViewById(R.id.date7wea), view1.findViewById(R.id.date2), view1.findViewById(R.id.date3), view1.findViewById(R.id.date4), view1.findViewById(R.id.date5),
                        view1.findViewById(R.id.date6), view1.findViewById(R.id.date7)};

                //对应当天天气，也就是httpconnect
                ImageView[] imageViews = {findViewById(R.id.weather_img), findViewById(R.id.location_img), findViewById(R.id.List)};

                //对应httpconnect1，也就是七天数据
                ImageView[] imageViews1 = {view1.findViewById(R.id.date1weaimg), view1.findViewById(R.id.date2weaimg), view1.findViewById(R.id.date3weaimg), view1.findViewById(R.id.date4weaimg), view1.findViewById(R.id.date5weaimg),
                        view1.findViewById(R.id.date6weaimg), view1.findViewById(R.id.date7weaimg)};
                try {

//                            Log.i("fuck", "mp2_Http" + httpConnect1.getpoitstem1().toString());
                    Message message1 = myHandler.obtainMessage();
                    Message message2 = myHandler2.obtainMessage();
                    Message message3 = myHandler3.obtainMessage();
                    Message message4 = myHandler4.obtainMessage();
                    Message message5 = myHandler5.obtainMessage();
                    Message message6 = myHandler6.obtainMessage();
                    Message message7 = myHandler7.obtainMessage();
                    Message message8 = myHandler8.obtainMessage();
                    Message message9 = myHandler9.obtainMessage();
                    message2.obj = "cp";
                    message1.obj = "ct";
                    message4.obj = "cp";
                    message3.obj = "ct";
                    message5.obj = "cllbg";
                    message6.obj = "cgif";
                    message7.obj = "ctc";
                    message8.obj = "bitmap";
                    message9.obj = "adapter";
                    int[] resource = {httpConnect.getweatherimg(),httpConnect.getlocationimg(),httpConnect.getListcolor()};
                    String[] texts = {httpConnect.get_update_time(), httpConnect.getnowtem(), httpConnect.getlowtem(), httpConnect.gethightem(),httpConnect.getwether(),httpConnect.getLocation(),httpConnect.getWinSpeed(),httpConnect.getWin(),
                            httpConnect.getWinMeter(),httpConnect.getHumidity(),httpConnect.getVisibility(),httpConnect.getPressure(),httpConnect.getAir(),httpConnect.getair_tips()};
                    myHandler.acceptObjects(textView);
                    myHandler.acceptString(texts);

                    myHandler2.acceptImageResourcw(resource);
                    myHandler2.acceptObjects(imageViews);

                    myHandler3.acceptObjects(textView2);
                    myHandler3.acceptString(httpConnect1.getsevenwea());

                    myHandler4.acceptObjects(imageViews1);
                    myHandler4.acceptImageResourcw(httpConnect1.getsevenweaimg());

                    myHandler5.acceptObjects(linearLayouts);
                    myHandler5.acceptImageResourcw(httpConnect.get_background_linear());

                    myHandler6.acceptObjects(gifImageViews);
                    myHandler6.acceptImageResourcw(httpConnect.get_gif());

                    myHandler7.acceptObjects(textViews);
                    myHandler7.acceptImageResourcw(httpConnect.get_location_color());
                    myHandler7.acceptContext(MainActivity.this);

                    myHandler8.acceptImageview((ImageView) view1.findViewById(R.id.nationalrain));
                    myHandler8.acceptBitmap(bitmap);;




                    myHandler.sendMessage(message1);
                    myHandler2.sendMessage(message2);
                    myHandler3.sendMessage(message3);
                    myHandler4.sendMessage(message4);
                    myHandler5.sendMessage(message5);
                    myHandler6.sendMessage(message6);
                    myHandler7.sendMessage(message7);
                    myHandler8.sendMessage(message8);
                    initColChart(columnChartView, httpConnect1.twentyfour_tem());
//                    initLineChart(lineChart, httpConnect1.twentyfour_tem2());
                    initLineChart2(lineChart2, httpConnect1.getpoitstem1(), httpConnect1.getpoitstem2(),httpConnect.get_background_linear());
                    weadata.putWeaValue(httpConnect.getLocation(), httpConnect.getwether());
                    views.add(view1);
                    views.add(view2);
                    views.add(view3);
                    viewPagerAdapter = new ViewPagerAdapter(views, title);
                    myHandler9.acceptAdapter(viewPagerAdapter);
                    myHandler9.acceptviewpager(viewPager);
                    myHandler9.sendMessage(message9);
                    Log.i("fuck2", "刷新结束");
//                    viewPager.setAdapter(viewPagerAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    /**
     * 图表的每个点的显示
     */
    private void initColChart(ColumnChartView columnChartView, float[] y){
        // 折线对象 传参数为点的集合对象   y轴值的范围自动生成 根据坐标的y值 不必自己准备数据
        int numColumns = y.length;
        //X、Y轴值list
        List<AxisValue> axisXValues = new ArrayList<>();
        for (int i = 0; i < x.length; i++) {
            //设置X轴的柱子所对应的属性名称(底部文字)
            axisXValues.add(new AxisValue(i).setLabel(x[i]));
        }
        List<SubcolumnValue> values;
        //所有的柱子
        List<Column> columns = new ArrayList<>();
        //单个柱子
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<SubcolumnValue>();
            if(y[i] != -100)
                values.add(new SubcolumnValue(y[i], Color.GRAY));
            Column column = new Column(values);
            column.setHasLabels(true);
            column.setHasLabelsOnlyForSelected(false);
            columns.add(column);
        }

        ColumnChartData data = new ColumnChartData(columns);
        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(false);
        axisX.setName("时间 / (从今日8点到次日5点)");
        axisY.setName("温度 / ℃");
        axisY.setTextColor(Color.parseColor("#DDFFF9CF"));
        axisX.setLineColor(Color.parseColor("#DDFFF9CF"));
        axisY.setLineColor(Color.parseColor("#DDFFF9CF"));
        axisX.setTextColor(Color.parseColor("#DDFFF9CF"));
        axisX.setValues(axisXValues);

        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        data.setValueLabelBackgroundEnabled(false);

        columnChartView.setColumnChartData(data);

        columnChartView.moveTo(0, 0);
        Viewport viewportMax =new Viewport(-1, columnChartView.getMaximumViewport().height()*1.2f, 8, columnChartView.getMaximumViewport().bottom);
        columnChartView.setMaximumViewport(viewportMax);
        Viewport viewport =new Viewport(-1,  columnChartView.getMaximumViewport().height()*1.2f, 8, columnChartView.getMaximumViewport().bottom);
        columnChartView.setCurrentViewport(viewport);
    }

    private void initLineChart2(LineChartView lineChartView, List<PointValue> pointValues, List<PointValue> pointValues2, int[] color){
        // 折线对象 传参数为点的集合对象   y轴值的范围自动生成 根据坐标的y值 不必自己准备数据
        LinearLayout templayout = new LinearLayout(this);
        templayout.setBackgroundResource(color[0]);
        Drawable drawable = templayout.getBackground();
        ColorDrawable colorDrawable = (ColorDrawable)drawable;
        int color1 = colorDrawable.getColor();                    //上面5行代码是将R.color的int转换为颜色的int值
        Line line = new Line(pointValues).setColor(Color.parseColor("#FFFFFF"));  //折线的颜色（橙色）
        Line line2 = new Line(pointValues2).setColor(Color.parseColor("#FFFFFF"));
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line.setPointRadius(2); //点的半径
        line.setStrokeWidth(1); //线的粗细
        lines.add(line);

        line2.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line2.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line2.setFilled(false);//是否填充曲线的面积
        line2.setHasLabels(true);//曲线的数据坐标是否加上备注
//      line2.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line2.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line2.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line2.setPointRadius(2); //点的半径
        line2.setStrokeWidth(1); //线的粗细
        lines.add(line2);

        LineChartData data = new LineChartData();
        data.setLines(lines);
        data.setValueLabelBackgroundEnabled(false);    //标签背景色


        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextSize(0);//设置字体大小
        axisX.setMaxLabelChars(0); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
//        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        axisX.setTextColor(color1); //设置线和坐标的颜色
        axisX.setAutoGenerated(false);
        axisX.setLineColor(color1);
        data.setAxisXBottom(axisX); //x 轴在底部  （顶部底部一旦设置就意味着x轴）
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(false); //x 轴分割线  每个x轴上 面有个虚线 与x轴垂直
        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
//        axisY.setName("Temperature / ℃");//y轴标注
        axisY.setTextSize(0);//设置字体大小
        axisY.setTextColor(color1);  //设置坐标和轴的颜色
        axisY.setLineColor(color1);
        data.setAxisYLeft(axisY);  //Y轴设置在左边（左面右面一旦设定就意味着y轴）
        //data.setAxisYRight(axisY);  //y轴设置在右边
        lineChartView.setLineChartData(data);

//        lineChart.setVisibility(View.VISIBLE);
        Viewport viewport;
        if(lineChartView.getMaximumViewport().bottom < 0)//根据最大值或者最小值是否为负数建立
        {
            if(lineChartView.getMaximumViewport().top < 0)
                viewport = new Viewport(lineChartView.getMaximumViewport().left, lineChartView.getMaximumViewport().top*0.95f, lineChartView.getMaximumViewport().right, lineChartView.getMaximumViewport().bottom*1.1f);
            else
                viewport = new Viewport(lineChartView.getMaximumViewport().left, lineChartView.getMaximumViewport().top*1.02f, lineChartView.getMaximumViewport().right, lineChartView.getMaximumViewport().bottom*1.1f);
        }
        else
        {
            if(lineChartView.getMaximumViewport().top < 0)
                viewport = new Viewport(lineChartView.getMaximumViewport().left, lineChartView.getMaximumViewport().top*0.95f, lineChartView.getMaximumViewport().right, lineChartView.getMaximumViewport().bottom*0.97f);  //会出现将数据顶出的情况，故而调整上下左右的比例关系
            else
                viewport = new Viewport(lineChartView.getMaximumViewport().left, lineChartView.getMaximumViewport().top*1.02f, lineChartView.getMaximumViewport().right, lineChartView.getMaximumViewport().bottom*0.97f);  //会出现将数据顶出的情况，故而调整上下左右的比例关系
        }
        lineChartView.setMaximumViewport(viewport);
        Viewport v = new Viewport(lineChartView.getMaximumViewport());
        v.left = 0;
        v.right= 25;
//        v.bottom= -50;
//        v.top= 50;
        lineChartView.setCurrentViewport(v);
    }

    public String sHA1(Context context){
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length()-1);
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void Mapini(final boolean locate)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (aMap == null)
                    aMap = mapView.getMap();
                aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        InputMethodManager manager = ((InputMethodManager)MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE));  //收起键盘
                        if(manager != null)
                            manager.hideSoftInputFromWindow(MainActivity.this.getWindow().getDecorView().getWindowToken(), 0);
                        listView.setAdapter(null);
                        searchView.setQuery("", true);
                    }
                });
                if(locate)
                {
                    MyLocationStyle myLocationStyle;
                    myLocationStyle = new MyLocationStyle();
                    myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);   //不会到当前定位，既可以随意拖动
                    aMapLocationClient = new AMapLocationClient(MainActivity.this);
                    aMapLocationClientOption = new AMapLocationClientOption();
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
                    aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
                    aMapLocationClientOption.setOnceLocation(false);   //单次定位
//                aMapLocationClient.startLocation();
//设置定位参数
                    aMapLocationClientOption.setInterval(1000);
                    aMap.setMyLocationEnabled(true);
                    aMapLocationClient.setLocationOption(aMapLocationClientOption);
                    aMap.setMyLocationStyle(myLocationStyle);
                    Log.i("fuck2", "开始定位");
                    aMapLocationClient.startLocation();
                    aMapLocationClient.setLocationListener(new AMapLocationListener() {
                        @Override
                        public void onLocationChanged(AMapLocation aMapLocation) {
                            if(aMapLocation.getErrorCode() == 0)
                            {
                                Log.i("fuck2", "定位信息" + aMapLocation.getCity());
                                getcity = aMapLocation.getCity();
                            }
                            else
                            {
                                Log.i("fuck2","鉴权码：" + sHA1(MainActivity.this));
                                Log.i("fuck2", "定错误信息" + aMapLocation.getErrorCode()  + aMapLocation.getErrorInfo());
                            }
                        }
                    });
                }
                byte[]  buffer1 = null;
                byte[]  buffer2 = null;
                InputStream is1 = null;
                InputStream is2 = null;
                try {
                    is1 = MainActivity.this.getAssets().open("style.data");
                    int lenght1 = is1.available();
                    buffer1 = new byte[lenght1];
                    is1.read(buffer1);
                    is2 = MainActivity.this.getAssets().open("style_extra.data");
                    int lenght2 = is2.available();
                    buffer2 = new byte[lenght2];
                    is2.read(buffer2);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        if (is1!=null)
                            is1.close();
                        if (is2!=null)
                            is2.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                CustomMapStyleOptions customMapStyleOptions = new CustomMapStyleOptions();
                customMapStyleOptions.setStyleData(buffer1);
                customMapStyleOptions.setStyleExtraData(buffer2);
                aMap.setCustomMapStyle(customMapStyleOptions);
                uiSettings = aMap.getUiSettings();
                uiSettings.setAllGesturesEnabled(true);  //手势操作
                while(true)
                {
                    Log.i("fuck2","定位城市变量检查" + getcity);
                    if(!getcity.equals(""))
                    {
                        aMapLocationClient.stopLocation();
                        break;              //阻塞，防止下面的getcity是空
                    }
                }
                try {
                    httpConnect4 = new HttpConnect();
                    httpConnect4.MapConnect(getcity, "null", null);
                    aMap.addMarkers(httpConnect4.getMarkers(MainActivity.this), true);
                    getcity = "";
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void webviewini()
    {
        webView = view3.findViewById(R.id.web_view);
        final com.tencent.smtt.sdk.WebSettings webSetting = webView.getSettings();
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);             //不需要缓存
        webSetting.setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");//使用电脑标识，android标识的网页会出问题
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(com.tencent.smtt.sdk.WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSetting.setAppCacheEnabled(false);
        webSetting.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.ventusky.com/");
        Log.i("fuck2", "如果这个值不是null，说明现在用的是X5内核，wv.getX5WebViewExtension() = : " + webView.getX5WebViewExtension());

    }

}
