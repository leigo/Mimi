package com.leigo.android.controller.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.leigo.android.controller.adapter.GuidancePagerAdapter;
import com.leigo.android.mimi.R;
import com.leigo.android.util.Utils;
import com.viewpagerindicator.IconPageIndicator;

import java.util.Arrays;

import roboguice.inject.InjectView;

/**
 * Created by Administrator on 2014/8/21.
 */
public class AccountGuidanceActivity extends TrackedRoboActivity {

    @InjectView(R.id.indicator)
    private IconPageIndicator pageIndicator;

    @InjectView(R.id.view_pager)
    private ViewPager viewPager;


    public static Intent createIntent(Context context) {
        return new Intent(context, AccountGuidanceActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    public static void startFrom(Context context) {
        context.startActivity(createIntent(context).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_guidance);
        Utils.disableOverScroll(viewPager);
        Integer[] pageDrawableResIds = new Integer[4];
        pageDrawableResIds[0] = R.drawable.account_guidance_0;
        pageDrawableResIds[1] = R.drawable.account_guidance_1;
        pageDrawableResIds[2] = R.drawable.account_guidance_2;
        pageDrawableResIds[3] = R.drawable.account_guidance_3;
        final GuidancePagerAdapter adapter = new GuidancePagerAdapter(this, Arrays.asList(pageDrawableResIds), R.drawable.ic_circle_indicator);
        viewPager.setAdapter(adapter);
        pageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                adapter.setCurrentIndex(position);
                if (position == GuidancePagerAdapter.LAST_PAGE_INDEX) {
                    adapter.showAnimation();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        pageIndicator.setViewPager(viewPager);
    }

    public void clickOnRegister(View v) {
        AuthenticatorActivity.startFrom(this, AuthenticatorActivity.TYPE_REGISTRATION);
    }

    public void clickOnLogin(View v) {
        AuthenticatorActivity.startFrom(this, AuthenticatorActivity.TYPE_LOGIN);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
