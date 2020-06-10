package com.example.weather;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class City_Card extends AppCompatActivity {

    public List<Citycarditem> citycarditemList = new ArrayList<>();
    private ImageButton imageButton;
    private ImageButton back;
    private citycard_data card_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citycard);
        card_data = new citycard_data(this, "citicardata");    //获取数据文件
        setStatusBarFullTransparentLight();

        Log.i("card" ,"第一个城市:" + MainActivity.firstcity.getCityname());
        if(MainActivity.firstcity != null)
        {
            Citycarditem temp = new Citycarditem(MainActivity.firstcity.getCityname(), MainActivity.firstcity.getTemwea());
            citycarditemList.add(temp);
        }
        initializeListview();

        imageButton = (ImageButton)findViewById(R.id.add);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(City_Card.this, Search_City.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });

        back = (ImageButton)findViewById(R.id.back2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.leftinto, R.anim.rightout);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            Bundle bundle = data.getExtras();
            assert bundle != null;
            String str = bundle.getString("c");
            int flag = 0;     //判断是否存在相同值
            for(Citycarditem i : citycarditemList)
            {
                if(i.getCityname().equals(str))
                {
                    Toast.makeText(this, "城市已经存在", Toast.LENGTH_SHORT).show();
                    flag = 1;
                    break;
                }
            }
            if(flag == 0)
            {
                citycarditemList.add(new Citycarditem(str, "℃ /" ));
                RefreshListview(citycarditemList);
                if(card_data.isEmpty())
                {
                    Set<String> set = new HashSet<>();
                    Log.i("card","数据不存在");
                    set.add(str);
                    card_data.putValue("city",null);             //必须先用null赋值，否则会出现无法覆盖数据的情况
                    card_data.putValue("city",set);
                }
                else
                {
                    Log.i("card","数据存在!!!!!!!");
                    Set<String> set = card_data.getvalue("city");
                    set.add(str);
                    Log.i("card","增加的数据 " + set.toString());
                    card_data.putValue("city",null);
                    card_data.putValue("city", set);
                }
            }
        }
    }

    private void RefreshListview(List<Citycarditem> list)
    {
        Citycardadapter adapter = new Citycardadapter(City_Card.this, R.layout.citycard_item, list, "fuck");
        ListView listView = (ListView)findViewById(R.id.citycardlist);
        listView.setDividerHeight(0);                    //去除分割线
        listView.setVerticalScrollBarEnabled(false);     //去掉滚动条
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Citycarditem citycarditem = citycarditemList.get(position);
                Intent intent = getIntent();
                Bundle bundle = intent.getExtras();
                assert bundle != null;
                bundle.putString("c", citycarditem.getCityname());
                intent.putExtras(bundle);
                setResult(AppCompatActivity.RESULT_OK, intent);
                finish();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                vib.vibrate(150);
                Citycarditem citycarditem = citycarditemList.get(position);
                citycarditemList.remove(position);
                RefreshListview(citycarditemList);
                List<Citycarditem> temp = new ArrayList<>();
                temp.addAll(citycarditemList);
                temp.remove(0);
                Set<String> i = card_data.getvalue("city");
                i.remove(citycarditem.getCityname());
                card_data.putValue("city",null);
                card_data.putValue("city", i);
                return false;
            }
        });
    }

    protected void setStatusBarFullTransparentLight() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    private void initializeListview()
    {
        if(!card_data.isEmpty())
        {
            for(String i : card_data.getvalue("city"))
            {
                if(!card_data.isEmptyWea(i))
                {
                    citycarditemList.add(new Citycarditem(i, card_data.getWeavalue(i)));
                }
                else
                {
                    citycarditemList.add(new Citycarditem(i, "--℃ /--" ));
                }
            }
        }
        RefreshListview(citycarditemList);
    }
}
