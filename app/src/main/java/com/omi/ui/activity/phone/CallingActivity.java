package com.omi.ui.activity.phone;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.omi.R;
import com.omi.bean.call.CallHistory;
import com.omi.database.LiteOrmDBUtil;
import com.omi.receiver.PhoneReceiver;
import com.omi.ui.base.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SensYang on 2017/04/15 20:01
 */
public class CallingActivity extends BaseActivity {
    private TextView nameTV;
    private ImageView watingIV;
    private String name;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        nameTV = (TextView) findViewById(R.id.nameTV);
        nameTV.setText(name);
        watingIV = (ImageView) findViewById(R.id.watingIV);
        AnimationDrawable animationDrawable = (AnimationDrawable) watingIV.getDrawable();
        animationDrawable.start();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CallHistory callHistory = new CallHistory();
        callHistory.setCallName(name);
        callHistory.setCallNumber(phone);
        callHistory.setCallTime(simpleDateFormat.format(new Date()));
        LiteOrmDBUtil.getUserDBUtil().save(callHistory);
        PhoneReceiver.setPhoneStateCallback(phoneStateCallback);
        findViewById(R.id.endCall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endCall(v);
            }
        });
    }

    private PhoneReceiver.PhoneStateCallback phoneStateCallback = new PhoneReceiver.PhoneStateCallback() {
        @Override
        public void onEndCall(String phoneNum) {
            if (phoneNum == null || phoneNum.indexOf(phone) > -1) {
                finish();
            }
        }

        @Override
        public void onCalling(String phoneNum) {

        }

        @Override
        public void onRinging(String phoneNum) {

        }
    };

    @Override
    protected void onDestroy() {
        PhoneReceiver.setPhoneStateCallback(null);
        super.onDestroy();
    }

    public void endCall(View view) {
        finish();
    }


}