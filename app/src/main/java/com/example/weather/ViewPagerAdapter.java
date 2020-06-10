package com.example.weather;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private  List<View> views;
    private List<String> titleLists;

    public ViewPagerAdapter(List<View> views, List<String> titleLists)
    {
        super();
        this.views = views;
        this.titleLists = titleLists;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.i("适配器", "删除之前的容器" + container.toString());
        Log.i("适配器", "删除之前的views" + views.toString());
        container.removeView(views.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleLists.get(position);
    }
}
