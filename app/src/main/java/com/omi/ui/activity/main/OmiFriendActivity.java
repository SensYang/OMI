package com.omi.ui.activity.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.omi.R;
import com.omi.bean.User;
import com.omi.bean.account.ContactInvited;
import com.omi.database.LiteOrmDBUtil;
import com.omi.databinding.ActivityOmiFriendBinding;
import com.omi.net.ApiByHttp;
import com.omi.net.ResponseCallback;
import com.omi.ui.activity.search.SearchActivity;
import com.omi.ui.adapter.OmiFriendAdapter;
import com.omi.ui.base.BaseActivity;
import com.omi.utils.Log;
import com.omi.utils.ToastUtil;

import java.util.List;

/**
 * Created by SensYang on 2017/04/20 15:11
 */

public class OmiFriendActivity extends BaseActivity {
    private ActivityOmiFriendBinding binding;
    private OmiFriendAdapter omiFriendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_omi_friend);
        omiFriendAdapter = new OmiFriendAdapter();
        binding.setAdapter(omiFriendAdapter);
        binding.setLayoutManager(new LinearLayoutManager(this));
        EMClient.getInstance().contactManager().setContactListener(contactListener);
        new Thread(getFriendRunnable).start();
    }

    /**
     * 进入搜索
     */
    public void goSearch(View view) {
        startActivity(new Intent(this, SearchActivity.class));
    }

    @Override
    public void topLeftClick(View view) {
        finish();
    }

    private Runnable getFriendRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                if (usernames == null) return;
                for (String username : usernames) {
                    User.Data user = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(User.Data.class, "member_name", username);
                    if (user == null) {
                        ApiByHttp.getInstance().getUserInfo(username, userCallback);
                    } else {
                        omiFriendAdapter.addUser(user);
                    }
                }
                omiFriendAdapter.sort();
                getHandler().sendEmptyMessage(0);
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
    };

    private EMContactListener contactListener = new EMContactListener() {
        @Override
        public void onContactAdded(String username) {
            Log.e("onContactAdded--->", "username: " + username);
            ApiByHttp.getInstance().addFriend(username, null);
            //增加了联系人时回调此方法
            User.Data user = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(User.Data.class, "member_name", username);
            if (user != null) {
                omiFriendAdapter.addUser(user);
                omiFriendAdapter.sort();
                getHandler().sendEmptyMessage(0);
            } else {
                ApiByHttp.getInstance().getUserInfo(username, userCallback);
            }
            ContactInvited contactInvited = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(ContactInvited.class, "username", username);
            if (contactInvited != null) {
                contactInvited.setHasAdd(true);
                LiteOrmDBUtil.getUserDBUtil().update(contactInvited);
            }
        }

        @Override
        public void onContactDeleted(String username) {
            omiFriendAdapter.remove(username);
        }

        @Override
        public void onContactInvited(String username, String reason) {
        }

        @Override
        public void onFriendRequestAccepted(String username) {
            //好友请求被同意
            ApiByHttp.getInstance().addFriend(username, null);
        }

        @Override
        public void onFriendRequestDeclined(String username) {
            //好友请求被拒绝
            ToastUtil.showLongToast(username + "拒绝了你的好友邀请");
        }
    };

    private ResponseCallback userCallback = new ResponseCallback<User>() {
        @Override
        public void onSucceed(int what, User response) throws Exception {
            if (response.getUserinfo() == null || response.getUserinfo().size() == 0) {
                return;
            }
            User.Data user = response.getUserinfo().get(0);
            if (user == null) return;
            omiFriendAdapter.addUser(user);
            LiteOrmDBUtil.getUserDBUtil().save(user);
            omiFriendAdapter.sort();
            getHandler().sendEmptyMessage(0);
        }
    };

    @Override
    protected void handlerPacketMsg(Message msg) {
        omiFriendAdapter.notifyDataSetChanged();
    }

}
