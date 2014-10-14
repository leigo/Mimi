package com.leigo.android.controller.activity;

import android.app.Activity;
import android.os.Bundle;

import com.leigo.android.mimi.R;

/**
 * Created by Administrator on 2014/10/13.
 */
public class PublishSecretActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_secret);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
