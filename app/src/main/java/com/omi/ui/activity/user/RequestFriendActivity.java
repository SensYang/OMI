package com.omi.ui.activity.user;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.omi.BR;
import com.omi.R;
import com.omi.bean.account.Contact;
import com.omi.databinding.ActivityRequestFriendBinding;
import com.omi.ui.base.BaseActivity;
import com.omi.utils.ContactsUtil;
import com.omi.utils.ToastUtil;

/**
 * Created by SensYang on 2017/04/08 15:53
 */

public class RequestFriendActivity extends BaseActivity {
    private ActivityRequestFriendBinding binding;
    private String userid;
    private String name;
    private String reason;
    private String remark;
    private boolean givePower = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userid = getIntent().getStringExtra("userid");
        if (userid == null) {
            finish();
            return;
        }
        if (ContactsUtil.getContact() != null) for (Contact contact : ContactsUtil.getContact()) {
            if (contact.getPhone().equalsIgnoreCase(userid)) {
                name = contact.getContact_name();
            }
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_friend);
        binding.setRequestFriendActivity(this);
    }

    public String getName() {
        return name;
    }

    public View.OnFocusChangeListener getFocusChangeListener() {
        return focusChangeListener;
    }

    private View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (v.getTag() == null || !(v.getTag() instanceof View)) return;
            if (hasFocus) {
                ((View) v.getTag()).setVisibility(View.VISIBLE);
            } else {
                ((View) v.getTag()).setVisibility(View.GONE);
            }
        }
    };

    public void topLeftClick(View view) {
        finish();
    }

    public void topRightClick(View view) {
        //TODO  备注 权限
        try {
            EMClient.getInstance().contactManager().addContact(userid, reason);
        } catch (HyphenateException e) {
            ToastUtil.showToast("发送请求失败，请稍后重试");
            e.printStackTrace();
            return;
        }
        ToastUtil.showToast("已发送");
        finish();
    }

    public void clear(View view) {
        if (view.getTag() != null && view.getTag() instanceof TextView)
            ((TextView) view.getTag()).setText("");
    }

    public void fillName(View view) {
        setRemark(name);
        notifyPropertyChanged(BR.remark);
    }

    public void powerChange(View view) {
        setGivePower(!isGivePower());
    }

    @Bindable
    public boolean isGivePower() {
        return givePower;
    }

    public void setGivePower(boolean givePower) {
        if (this.givePower == givePower) return;
        this.givePower = givePower;
        notifyPropertyChanged(BR.givePower);
    }

    @Bindable
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        if (remark.equalsIgnoreCase(this.remark)) return;
        this.remark = remark;
    }

    @Bindable
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}