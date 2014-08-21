package com.leigo.android.controller.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.leigo.android.mimi.R;
import com.leigo.android.util.Utils;
import com.viewpagerindicator.IconPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2014/8/21.
 */
public class GuidancePagerAdapter extends PagerAdapter implements IconPagerAdapter {

    public static final int LAST_PAGE_INDEX = 3;
    private Context context;
    private int currentIndex;
    private int iconResId;
    private ImageView lastPage;
    private List<Integer> pageDrawableResIds;

    public GuidancePagerAdapter(Context context, List<Integer> pageDrawableResIds, int iconResId) {
        this.context = context;
        this.pageDrawableResIds = pageDrawableResIds;
        this.iconResId = iconResId;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        if (position == LAST_PAGE_INDEX) {
            lastPage = null;
        }
    }

    @Override
    public int getCount() {
        return pageDrawableResIds.size();
    }

    @Override
    public int getIconResId(int index) {
        return iconResId;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = null;
        if (position == LAST_PAGE_INDEX) {
            imageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.guidance_last_page, null);
            lastPage = imageView;
            if (currentIndex == LAST_PAGE_INDEX) {
                showAnimation();
            }
        } else {
            imageView = new ImageView(context);
        }
        imageView.setImageResource(pageDrawableResIds.get(position));
        container.addView(imageView);
        return imageView;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    public void setCurrentIndex(int index) {
        currentIndex = index;
    }

    public void showAnimation() {
        if ((lastPage == null) || (lastPage.getVisibility() == View.VISIBLE)) {
            return;
        }
        Utils.startAnimation(lastPage, AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.account_guidance_3_fade_in), View.VISIBLE);
    }

}
