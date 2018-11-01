package com.omi.ui.activity.account;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.omi.R;
import com.omi.bean.User;
import com.omi.bean.account.AccountElement;
import com.omi.bean.account.Member;
import com.omi.database.GlobalSharedPreferences;
import com.omi.database.LiteOrmDBUtil;
import com.omi.database.PreferencesSetting;
import com.omi.databinding.FragmentLoginBinding;
import com.omi.net.ApiByHttp;
import com.omi.net.ResponseCallback;
import com.omi.ui.activity.user.SelfInfoActivity;
import com.omi.ui.base.BaseFragment;
import com.omi.ui.widget.WatingDialog;
import com.omi.utils.ContactsUtil;
import com.omi.utils.Log;
import com.omi.utils.MD5;
import com.omi.utils.ToastUtil;
import com.yanzhenjie.nohttp.rest.Response;

/**
 * Created by SensYang on 2017/04/02 21:00
 */

public class LoginFragment extends BaseFragment implements AccountElement.OnActionClickListener {
    private FragmentLoginBinding binding;
    private AccountElement phone;
    private AccountElement password;
    private WatingDialog watingDialog;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        phone = new AccountElement();
        phone.setHint("手机号");
        phone.setInputType(InputType.TYPE_CLASS_PHONE);
        password = new AccountElement();
        password.setHint("密码");
        password.setAction("忘记密码?");
        password.setOnActionClickListener(this);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        binding.setLoginFragment(this);
        watingDialog = new WatingDialog(getActivity());
        watingDialog.setContent("正在登陆 请稍等...");
        watingDialog.setAnim(R.drawable.wating_anim);
        return binding.getRoot();
    }

    @Override
    public void onFirstUserVisible() {

    }

    public AccountElement getPhone() {
        return phone;
    }

    public AccountElement getPassword() {
        return password;
    }

    public void onLoginClick(View v) {
        if (!ContactsUtil.isMobileNO(phone.getElement())) {
            ToastUtil.showToast(R.string.wrong_phone);
            return;
        }
        if (password.getElement() == null || password.getElement().length() < 6) {
            ToastUtil.showToast(R.string.wrong_password);
            return;
        }
        watingDialog.showDelayed(10*1000);
        ApiByHttp.getInstance().login(phone.getElement().trim(), password.getElement().trim(), new ResponseCallback<Member>() {
            @Override
            public void onSucceed(int what, Response<Member> response) {
                Member member = response.get();
                if (member != null && member.getData() != null) {
                    Log.e("开始登陆环信");
                    ApiByHttp.getInstance().setPhone(phone.getElement().trim());
                    ApiByHttp.getInstance().setMember_id(member.getData().getMember_id());
                    User.Data user = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(User.Data.class, "member_name", ApiByHttp.getInstance().getPhone());
                    if (user == null)
                        ApiByHttp.getInstance().getUserInfo(phone.getElement().trim(), new ResponseCallback<User>() {
                            @Override
                            public void onSucceed(int what, User response) throws Exception {
                                LiteOrmDBUtil.getUserDBUtil().save(response.getUserinfo().get(0));
                                ApiByHttp.getInstance().setUser(response.getUserinfo().get(0));
                            }
                        });
                    else ApiByHttp.getInstance().setUser(user);
                    EMClient.getInstance().login(phone.getElement().trim(), MD5.encode(password.getElement().trim()), callBack);
                } else if (member.getMsg() != null) {
                    watingDialog.dismiss();
                    ToastUtil.showToast(member.getMsg());
                } else {
                    watingDialog.dismiss();
                    ToastUtil.showToast(R.string.login_failed);
                }
            }

            @Override
            public void onFailed(int what, Member response) throws Exception {
                watingDialog.dismiss();
                ToastUtil.showToast("网络错误，请稍后重试");
            }
        });
    }

    private boolean hasCreat = false;

    private EMCallBack callBack = new EMCallBack() {
        @Override
        public void onSuccess() {
            watingDialog.dismiss();
            GlobalSharedPreferences.getInstance().putString(PreferencesSetting.LOGINED_MEMBER_ID.getName(), ApiByHttp.getInstance().getMember_id());
            GlobalSharedPreferences.getInstance().putString(PreferencesSetting.LOGINED_PHONE.getName(), phone.getElement().trim());
            GlobalSharedPreferences.getInstance().putString(PreferencesSetting.LOGINED_PASS.getName(), MD5.encode(password.getElement().trim()));
            EMClient.getInstance().groupManager().loadAllGroups();
            EMClient.getInstance().chatManager().loadAllConversations();
            if (getActivity() != null) getActivity().finish();
            if (ApiByHttp.getInstance().getUser() == null || ApiByHttp.getInstance().getUser().getMember_avatar() == null || ApiByHttp.getInstance().getUser().getMember_avatar().length() == 0) {
                Intent intent = new Intent(getActivity(), SelfInfoActivity.class);
                intent.putExtra("canNotBack", true);
                startActivity(intent);
                ToastUtil.showToast("请设置您的资料");
            }
        }

        @Override
        public void onError(int code, String error) {
            watingDialog.dismiss();
            ToastUtil.showToast("登陆失败，请重新登录");
            ApiByHttp.getInstance().setPhone(null);
            ApiByHttp.getInstance().setMember_id(null);
        }

        @Override
        public void onProgress(int progress, String status) {
        }
    };

    @Override
    public void onActionClick(AccountElement element) {
        if (element == password) {
            startActivity(new Intent(getActivity(), FindPasswordActivity.class));
        }
    }
}
