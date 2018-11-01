package com.omi.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.omi.utils.Log;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by SensYang on 2017/04/28 14:14
 */

public class PhoneReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    private static PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            Log.e("onCallStateChanged--->" + " phone:" + incomingNumber + " state:" + state);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    if (phoneStateCallback != null) phoneStateCallback.onEndCall(incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (phoneStateCallback != null) phoneStateCallback.onCalling(incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    if (phoneStateCallback != null) phoneStateCallback.onRinging(incomingNumber);
                    break;
            }
        }
    };

    private static PhoneStateCallback phoneStateCallback;

    public static void setPhoneStateCallback(PhoneStateCallback callback) {
        phoneStateCallback = callback;
    }

    public interface PhoneStateCallback {
        void onEndCall(String phoneNum);

        void onCalling(String phoneNum);

        void onRinging(String phoneNum);
    }
}