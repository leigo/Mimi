package com.leigo.android.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.leigo.android.mimi.R;
import com.leigo.android.model.helper.HttpHelper;

/**
 * Created by Administrator on 2014/10/13.
 */
public class FeedbackActivity extends Activity {

    private EditText contactView;
    private EditText contentView;

    private MenuItem sendMenuItem;

    private void updateSendMenuItemState(boolean enabled) {
        sendMenuItem.setEnabled(enabled);
        if (enabled) {
            sendMenuItem.setIcon(R.drawable.ic_send_btn_enabled);
        } else {
            sendMenuItem.setIcon(R.drawable.ic_send_btn_disabled);
        }

    }

    public void clickOnPrivacy(View v) {
       WebViewActivity.startFrom(this, HttpHelper.createUrl("privacy"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        contactView = (EditText) findViewById(R.id.edit_box_contact);
        contentView = (EditText) findViewById(R.id.edit_box);
        contentView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (sendMenuItem == null) {
                    invalidateOptionsMenu();
                } else {
                    updateSendMenuItemState(!TextUtils.isEmpty(contentView.getText().toString()));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feedback_activity_actions, menu);
        sendMenuItem = menu.findItem(R.id.action_send_feedback);
        updateSendMenuItemState(!TextUtils.isEmpty(contentView.getText().toString()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
