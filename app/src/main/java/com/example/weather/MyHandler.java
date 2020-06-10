package com.example.weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import pl.droidsonroids.gif.GifImageView;

public class MyHandler extends Handler {
    private Object[] object;
    private String[] texts;
    private int[] image;
    Context context;
    Bitmap bitmap;
    ImageView imageView;
    ViewPagerAdapter viewPagerAdapter;
    private ViewPager Pager;


    @Override
    public void handleMessage(Message msg) {
        String str = (String)msg.obj;
        switch (str) {
            case "ct":              //设置ct：change textview事件
                TextView[] textViews = (TextView[]) object;
                for (int i = 0; i < texts.length; i++) {
                    textViews[i].setText(texts[i]);
                }
                break;
            case "cp":           //设置cp：change picture事件
                ImageView[] imageViews = (ImageView[]) object;
                for (int i = 0; i < image.length; i++) {
                    Log.i("fuck", "getid" + String.valueOf(image[i]));
                    imageViews[i].setImageResource(image[i]);
                }
                break;
            case "cllbg":         //改变Linearlayout的背景
                LinearLayout[] linearLayouts = (LinearLayout[]) object;
                for (int i = 0; i < image.length; i++)
                {
                    linearLayouts[i].setBackgroundResource(image[i]);
                }
                break;
            case  "cgif":        //改变gif图片
                GifImageView[] gifImageViews = (GifImageView[]) object;
                for(int i = 0; i < gifImageViews.length; i++)
                {
                    gifImageViews[i].setBackgroundResource(image[i]);
                }
                break;
            case  "ctc":        //改文字颜色
                TextView[] textViewss = (TextView[]) object;
                for(int i = 0; i < image.length; i++)
                {
                    LinearLayout templayout = new LinearLayout(context);
                    templayout.setBackgroundResource(image[i]);
                    Drawable drawable = templayout.getBackground();
                    ColorDrawable colorDrawable = (ColorDrawable)drawable;
                    int color1 = colorDrawable.getColor();
                    textViewss[i].setTextColor(color1);
                }
                break;
            case "bitmap":
                imageView.setImageBitmap(bitmap);
                break;
            case "adapter":
                Pager.setAdapter(viewPagerAdapter);
                break;
        }
    }

    public void acceptString(String[] j)
    {
        this.texts = j;
    }
    public void acceptImageResourcw(int[] j)
    {
        this.image = j;
    }
    public void acceptObjects(Object[] j)
    {
        this.object = j;
    }

    public void acceptImageview(ImageView imageView)
    {
        this.imageView = imageView;
        Log.i("fuck2", "Imageview传输后" + imageView.toString());
    }
    public void acceptBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }
    public void acceptContext(Context context)
    {
        this.context = context;
    }

    public void acceptviewpager(ViewPager viewpager)
    {
        this.Pager = viewpager;
    }

    public void acceptAdapter(ViewPagerAdapter viewPagerAdapter)
    {
        this.viewPagerAdapter = viewPagerAdapter;
    }
}
