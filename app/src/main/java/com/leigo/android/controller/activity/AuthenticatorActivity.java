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

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.inject.Inject;
import com.leigo.android.mimi.R;
import com.leigo.android.model.domain.Country;
import com.leigo.android.model.helper.HttpHelper;
import com.leigo.android.util.ContextToast;
import com.leigo.android.util.Utils;

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

    private Country country;

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

    private boolean isValidNumber() {
        try {
            PhoneNumberUtil.getInstance().parse(country.getCode() + phoneNumberView.getText().toString(), null);
            return true;
        } catch (NumberParseException e) {
            return false;
        }
    }

    public static void startFrom(Context context, int authenticatorType) {
        Intent intent = new Intent(context, AuthenticatorActivity.class);
        intent.putExtra(EXTRA_AUTHENTICATOR_TYPE, authenticatorType);
        context.startActivity(intent);
    }

    private void updateRegionInfo(Country country) {
        regionNameView.setText(country.getName());
        regionCodeView.setText(country.getCode());
    }

    public void clickOnButton(View v) {
        String phoneNumber = phoneNumberView.getText().toString();
        String password = passwordView.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {
            contextToast.show(R.string.toast_input_phone_number, Toast.LENGTH_SHORT);
            return;
        }
        if (!isValidNumber()) {
            contextToast.show(R.string.toast_invalid_phone_numbers, Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            contextToast.show(R.string.toast_input_password, Toast.LENGTH_SHORT);
            return;
        }
        if (password.length() < 6) {
            contextToast.show(R.string.toast_password_too_short, Toast.LENGTH_SHORT);
            return;
        }
        if (authenticatorType == 2) {

        }
    }

    public void clickOnForgotPassword(View v) {
        ForgotPasswordActivity.startFrom(this, phoneNumberView.getText().toString());
    }

    public void clickOnProtocol(View v) {
        WebViewActivity.startFrom(this, HttpHelper.createUrl("protocol"));
    }

    public void clickOnRegion(View v) {
        RegionSelectionActivity.startFrom(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 5:
                phoneNumberView.setText(data.getStringExtra("phoneNumber"));
                break;
            case 6:
                country = (Country) data.getParcelableExtra("country");
                updateRegionInfo(country);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        authenticatorType = bundle.getInt(EXTRA_AUTHENTICATOR_TYPE);
        country = (Country) bundle.getParcelable("country");
        if (country == null) {
            country = Country.DEFAULT;
        }
        updateRegionInfo(country);
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
        phoneNumberView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Utils.updateViewBackgroundResource(regionLayout, R.drawable.textfield_focused_holo_light);
                } else {
                    Utils.updateViewBackgroundResource(regionLayout, R.drawable.textfield_default_holo_light);
                }
            }
        });
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
