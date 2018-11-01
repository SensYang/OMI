package com.omi.ui.activity.main;

import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omi.BR;
import com.omi.R;
import com.omi.bean.call.CallHistory;
import com.omi.database.LiteOrmDBUtil;
import com.omi.databinding.FragmentPhoneBinding;
import com.omi.net.ApiByHttp;
import com.omi.net.JsonCallback;
import com.omi.ui.activity.phone.CallingActivity;
import com.omi.ui.adapter.history.CallHistoryAdapter;
import com.omi.ui.base.BaseFragment;
import com.omi.ui.widget.listener.OnRepeatListener;
import com.omi.utils.ContactsUtil;
import com.omi.utils.ToastUtil;

import org.json.JSONObject;


/**
 * Created by SensYang on 2017/03/08 15:29
 */

public class PhoneFragment extends BaseFragment {
    private FragmentPhoneBinding binding;
    private String phone;
    private ToneGenerator toneGenerator;
    private Object lock = new Object();
    private AudioManager audioManager;
    private boolean showHistory = false;
    private CallHistoryAdapter historyAdapter;
    private TextView phoneET;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_phone, container, false);
        binding.setPhoneFragment(this);
        phoneET = (TextView) binding.getRoot().findViewById(R.id.phoneET);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            phoneET.setShowSoftInputOnFocus(false);
        } else {
            phoneET.setInputType(InputType.TYPE_NULL);
        }
        phoneET.setLongClickable(false);
        historyAdapter = new CallHistoryAdapter();
        binding.setAdapter(historyAdapter);
        return binding.getRoot();
    }

    @Override
    public void onFirstUserVisible() {
        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        //按键声音播放设置及初始化
        // 获取系统参数“按键操作音”是否开启
        toneGenerator = new ToneGenerator(AudioManager.STREAM_DTMF, 80); // 设置声音的大小
        getActivity().setVolumeControlStream(AudioManager.STREAM_DTMF);
        onUserVisible();
    }

    @Override
    public void onUserVisible() {
        if (ApiByHttp.getInstance().getPhone() != null) {
            historyAdapter.clear();
            historyAdapter.addCallHistory(LiteOrmDBUtil.getUserDBUtil().query(CallHistory.class));
            historyAdapter.notifyDataSetChanged();
        }
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (phone.equalsIgnoreCase(this.phone)) return;
        this.phone = phone;
    }

    @Bindable
    public boolean isShowHistory() {
        return showHistory;
    }

    public void setShowHistory(boolean showHistory) {
        if (showHistory == this.showHistory) return;
        this.showHistory = showHistory;
        notifyPropertyChanged(BR.showHistory);
    }

    /**
     * 重复点击事件
     */
    private OnRepeatListener repeatListener = new OnRepeatListener(500, 50, new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String s = phoneET.getText().toString().trim();
            if (s == null || s.length() == 0) return;
            phoneET.setText(s.substring(0, s.length() - 1));
        }
    });

    public OnRepeatListener getRepeatListener() {
        return repeatListener;
    }

    public void onPhoneClick(int num) {
        if (num < 12) {
            playTone(num);
            String s = phoneET.getText().toString().trim();
            if (num < 10) {
                s += num;
            } else if (num == 10) {
                s += "*";
            } else if (num == 11) {
                s += "#";
            }
            phoneET.setText(s);
        } else {
            if (!ContactsUtil.isMobileNO(phone)) {
                ToastUtil.showToast(R.string.wrong_phone);
                return;
            }
            if (phone.equalsIgnoreCase(ApiByHttp.getInstance().getPhone())) {
                ToastUtil.showToast(R.string.call_self);
                return;
            }
            ApiByHttp.getInstance().callPhone(phone, callPhoneCallback);
        }
    }

    private JsonCallback callPhoneCallback = new JsonCallback() {
        @Override
        public void onSucceed(int what, JSONObject response) throws Exception {
            if (response.getInt("result") == 200) {
                Intent intent = new Intent(getContext(), CallingActivity.class);
                intent.putExtra("name", phone);
                intent.putExtra("phone", phone);
                startActivity(intent);
            } else {
                ToastUtil.showToast(response.getString("msg"));
            }
        }
    };

    /**
     * 播放按键声音
     */
    private void playTone(int tone) {
        int ringerMode = audioManager.getRingerMode();
        if (ringerMode == AudioManager.RINGER_MODE_SILENT || ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
            // 静音或者震动时不发出声音
            return;
        }
        synchronized (lock) {
            if (toneGenerator == null) {
                return;
            }
            toneGenerator.startTone(tone, 120);   //发出声音
        }
    }
}