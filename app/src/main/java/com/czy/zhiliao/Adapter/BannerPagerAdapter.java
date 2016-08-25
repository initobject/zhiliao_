package com.czy.zhiliao.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.czy.zhiliao.Listener.OnBannerClickListener;

import java.util.List;


/**
 * Banner Adapter
 * Created by ZY on 2016/7/29.
 */
public class BannerPagerAdapter extends PagerAdapter {

    private List<ImageView> imageViews;

    private OnBannerClickListener onBannerClickListener;

    public BannerPagerAdapter(List<ImageView> imageViews) {
        this.imageViews = imageViews;
    }

    @Override
    public int getCount() {
        return imageViews.size();
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        ImageView iv = imageViews.get(position);
        container.addView(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Banner点击事件监听
                if (onBannerClickListener != null) {
                    onBannerClickListener.onClick(position);
                }
            }
        });
        return iv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
    }
}
