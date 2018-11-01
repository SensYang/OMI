package com.omi.ui.activity.user;

import android.content.Intent;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.omi.BR;
import com.omi.R;
import com.omi.bean.User;
import com.omi.bean.conversation.ConversationType;
import com.omi.database.LiteOrmDBUtil;
import com.omi.databinding.ActivityUserInfoBinding;
import com.omi.net.ApiByHttp;
import com.omi.net.ResponseCallback;
import com.omi.ui.activity.chat.ChatActivity;
import com.omi.ui.activity.utils.ImageBrowseActivity;
import com.omi.ui.base.BaseActivity;
import com.omi.utils.ToastUtil;

import java.util.List;

/**
 * Created by SensYang on 2017/04/07 16:25
 */
public class UserInfoActivity extends BaseActivity {
    private String userid;
    private ActivityUserInfoBinding binding;
    private boolean isFriend = false;
    private User.Data user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userid = getIntent().getStringExtra("userid");
        if (userid == null) {
            ToastUtil.showToast(R.string.wrong_userid);
            finish();
            return;
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_info);
        new Thread(getFriendRunnable).start();
        user = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(User.Data.class, "member_name", userid);
        ApiByHttp.getInstance().getUserInfo(userid, new ResponseCallback<User>() {
            @Override
            public void onSucceed(int what, User res) throws Exception {
                if (res == null) {
                    ToastUtil.showToast("用户不存在");
                    finish();
                    return;
                }
                if (res.getUserinfo() != null && res.getUserinfo().size() > 0) {
                    if (user != null) {
                        user.setUser(res.getUserinfo().get(0));
                        LiteOrmDBUtil.getUserDBUtil().update(user);
                    } else {
                        user = res.getUserinfo().get(0);
                        LiteOrmDBUtil.getUserDBUtil().save(user);
                    }
                    binding.setUser(user);
                } else {
                    ToastUtil.showToast("用户不存在");
                    finish();
                }
            }

            @Override
            public void onFailed(int what, User response) throws Exception {
                ToastUtil.showToast("用户不存在");
            }
        });
        binding.setUserInfoActivity(this);
    }

    public void onHeadClick(View view) {
        if (user != null && user.getMember_avatar() != null) {
            Intent intent = new Intent(this, ImageBrowseActivity.class);
            intent.putExtra("imageurls", new String[]{user.getMember_avatar()});
            startActivity(intent);
        }
    }

    private Runnable getFriendRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                if (usernames == null) return;
                for (String username : usernames) {
                    if (username.equalsIgnoreCase(userid)) {
                        isFriend = true;
                        notifyPropertyChanged(BR.friend);
                        break;
                    }
                }
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
    };

    @Bindable
    public boolean isFriend() {
        return isFriend;
    }

    @Override
    public void topLeftClick(View view) {
        finish();
    }

    public void addFriendClick(View view) {
        Intent intent = new Intent(this, RequestFriendActivity.class);
        intent.putExtra("userid", userid);
        startActivity(intent);
    }

    public void sendMessageClick(View view) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chatWith", userid);
        intent.putExtra("conversationType", ConversationType.SINGLE_CHAT);
        startActivity(intent);
    }
}
