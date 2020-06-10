package com.example.weather;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Search_City extends AppCompatActivity {

    private ImageButton back;
    private EditText editText;
    private List<City> cityList = new ArrayList<City>();
    private GetFromJSON getFromJSON = new GetFromJSON();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcity);
        setStatusBarFullTransparent();
        editText = (EditText)findViewById(R.id.citystring);
        editText.postDelayed(new Runnable() {          //直接弹出输入法
            @Override
            public void run() {
                editText.requestFocus();
                InputMethodManager manager = ((InputMethodManager)Search_City.this.getSystemService(Context.INPUT_METHOD_SERVICE));
                if (manager != null)
                    manager.showSoftInput(editText, 0);
            }
        }, 500);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = editText.getText().toString();
                try {
                    cityList = getFromJSON.getcityid(Search_City.this, name);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                cityAdapter adapter = new cityAdapter(Search_City.this, R.layout.city_item, cityList);
                ListView listView = (ListView)findViewById(R.id.citylist);
                listView.setDividerHeight(0);                    //去除分割线
                listView.setVerticalScrollBarEnabled(false);     //去掉滚动条
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        City city = cityList.get(position);
                        Intent intent = getIntent();
                        Bundle bundle = intent.getExtras();
                        bundle.putString("c", city.getCityName());
                        intent.putExtras(bundle);
                        Log.i("card","shit2 " + city.getCityName());
                        setResult(AppCompatActivity.RESULT_OK, intent);
                        finish();
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        back = (ImageButton)findViewById(R.id.back2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //全透明状态栏，导入的是外部代码
    protected void setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
