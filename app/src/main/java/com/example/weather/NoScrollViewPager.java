package com.example.weather;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.viewpager.widget.ViewPager;
import com.amap.api.maps.MapView;
import com.tencent.smtt.sdk.WebView;

public class NoScrollViewPager extends ViewPager {

    public NoScrollViewPager(Context context) {
        this(context, null);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof MapView || v instanceof WebView) {      //如果子view是地图，则让地图滑动
            return true;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }


}
