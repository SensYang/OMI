package com.omi.ui.activity.account;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Message;
import android.text.InputType;
import android.view.View;

import com.omi.R;
import com.omi.bean.account.AccountElement;
import com.omi.bean.base.BaseJson;
import com.omi.databinding.ActivityFindPasswordBinding;
import com.omi.net.ApiByHttp;
import com.omi.net.ResponseCallback;
import com.omi.ui.base.BaseActivity;
import com.omi.utils.ContactsUtil;
import com.omi.utils.ToastUtil;
import com.yanzhenjie.nohttp.rest.Response;

/**
 * Created by SensYang on 2017/04/03 19:37
 */

public class FindPasswordActivity extends BaseActivity implements AccountElement.OnActionClickListener {
    private ActivityFindPasswordBinding binding;
    private AccountElement phone;
    private AccountElement verification;
    private AccountElement password;
    private AccountElement rePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phone = new AccountElement();
        phone.setHint("手机号");
        phone.setInputType(InputType.TYPE_CLASS_PHONE);
        verification = new AccountElement();
        verification.setHint("验证码");
        verification.setAction("发送");
        verification.setInputType(InputType.TYPE_CLASS_NUMBER);
        verification.setOnActionClickListener(this);
        verification.setClickAble(true);
        password = new AccountElement();
        password.setHint("密码");
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        rePassword = new AccountElement();
        rePassword.setHint("确认密码");
        rePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_password);
        binding.setFindPasswordActivity(this);
    }

    public AccountElement getPhone() {
        return phone;
    }

    public AccountElement getVerification() {
        return verification;
    }

    public AccountElement getPassword() {
        return password;
    }

    public AccountElement getRePassword() {
        return rePassword;
    }

    public void onFinishClick(View view) {
        finish();
    }

    public void onConfirmClick(View view) {
        if (!ContactsUtil.isMobileNO(phone.getElement())) {
            ToastUtil.showToast(R.string.wrong_phone);
            return;
        }
        if (verification.getElement() == null || verification.getElement().length() < 4) {
            ToastUtil.showToast(R.string.wrong_verification);
            return;
        }
        if (password.getElement() == null || password.getElement().length() < 6) {
            ToastUtil.showToast(R.string.wrong_password);
            return;
        }
        if (!password.getElement().equalsIgnoreCase(rePassword.getElement())) {
            ToastUtil.showToast(R.string.wrong_repassword);
            return;
        }
        ApiByHttp.getInstance().findPassword(phone.getElement().trim(), password.getElement().trim(), verification.getElement().trim(), findPasswordCallback);
    }

    private ResponseCallback findPasswordCallback = new ResponseCallback<BaseJson>() {
        @Override
        public void onSucceed(int what, Response<BaseJson> response) {
            if (response.get().getResult().equalsIgnoreCase("ok")) {
                ToastUtil.showToast("找回密码成功，请登录");
                finish();
            } else {
                ToastUtil.showToast(response.get().getMsg());
            }
        }
    };

    @Override
    public void onActionClick(AccountElement element) {
        if (element == verification) {
            if (!ContactsUtil.isMobileNO(phone.getElement())) {
                ToastUtil.showToast(R.string.wrong_phone);
                return;
            }
            String phone = getPhone().getElement().trim();
            ApiByHttp.getInstance().getVerification(phone, verificationCallback);
        }
    }

    private ResponseCallback verificationCallback = new ResponseCallback<BaseJson>() {
        @Override
        public void onSucceed(int what, Response<BaseJson> response) {
            if (response.get().getResult().equalsIgnoreCase("ok")) {
                verification.setClickAble(false);
                getHandler().sendEmptyMessage(60);
            } else {
                ToastUtil.showToast(response.get().getMsg());
            }
        }
    };

    @Override
    protected void handlerPacketMsg(Message msg) {
        if (msg.what == 0) {
            verification.setClickAble(true);
            verification.setAction("发送");
            return;
        }
        verification.setAction(msg.what + "");
        getHandler().sendEmptyMessageDelayed(--msg.what, 1000);
    }
}
