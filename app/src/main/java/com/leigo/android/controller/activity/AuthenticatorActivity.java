package com.leigo.android.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.leigo.android.mimi.R;
import com.leigo.android.model.helper.HttpHelper;
import com.leigo.android.util.ContextToast;
import com.leigo.android.util.Utils;

import org.w3c.dom.Text;

import roboguice.inject.InjectView;

/**
 * Created by Administrator on 2014/8/21.
 */
public class AuthenticatorActivity extends TrackedRoboActivity {

    private static final String EXTRA_AUTHENTICATOR_TYPE = "authenticatorType";
    public static final int TYPE_LOGIN = 1;
    public static final int TYPE_REGISTRATION = 2;

    @InjectView(R.id.protocol_link)
    private TextView agreementLink;

    @InjectView(R.id.protocol_text)
    private TextView agreementText;

    private int authenticatorType;

    @InjectView(R.id.button)
    private Button button;

    @Inject
    private ContextToast contextToast;

    @InjectView(R.id.forgot_password)
    private TextView forgotPassword;

    @InjectView(R.id.password)
    private EditText passwordView;

    @InjectView(R.id.phone_number)
    private EditText phoneNumberView;

    @InjectView(R.id.region_code)
    private TextView regionCodeView;

    @InjectView(R.id.region)
    private LinearLayout regionLayout;

    @InjectView(R.id.region_name)
    private TextView regionNameView;


    public static void startFrom(Context context, int authenticatorType) {
        Intent intent = new Intent(context, AuthenticatorActivity.class);
        intent.putExtra(EXTRA_AUTHENTICATOR_TYPE, authenticatorType);
        context.startActivity(intent);
    }

    public void clickOnButton(View v) {
        String str1 = phoneNumberView.getText().toString();
        String str2 = passwordView.getText().toString().trim();
        if(TextUtils.isEmpty(str1)) {
            contextToast.show(R.string.toast_input_phone_number, Toast.LENGTH_SHORT);
            return;
        }
        if(TextUtils.isEmpty(str2)) {
            contextToast.show(R.string.toast_input_password, Toast.LENGTH_SHORT);
            return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        authenticatorType = bundle.getInt(EXTRA_AUTHENTICATOR_TYPE);
        switch (authenticatorType) {
            case TYPE_LOGIN:
                getActionBar().setTitle(R.string.login_secret);
                button.setText(R.string.login);
                Utils.setVisibility(agreementText, View.GONE);
                Utils.setVisibility(agreementLink, View.GONE);
                break;
            case TYPE_REGISTRATION:
                getActionBar().setTitle(R.string.join_secret);
                button.setText(R.string.join);
                Utils.setVisibility(forgotPassword, View.GONE);
                break;
        }
    }

    public void clickOnProtocol(View v) {
        WebViewActivity.startFrom(this, HttpHelper.createUrl("protocol"));
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
