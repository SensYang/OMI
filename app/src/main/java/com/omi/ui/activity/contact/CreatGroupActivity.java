package com.omi.ui.activity.contact;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.exceptions.HyphenateException;
import com.omi.R;
import com.omi.bean.User;
import com.omi.bean.conversation.ConversationType;
import com.omi.database.LiteOrmDBUtil;
import com.omi.databinding.ActivityCreatGroupBinding;
import com.omi.net.ApiByHttp;
import com.omi.net.ResponseCallback;
import com.omi.ui.activity.chat.ChatActivity;
import com.omi.ui.adapter.contact.CreatGroupAdapter;
import com.omi.ui.base.BaseActivity;
import com.omi.utils.CommonUtils;
import com.omi.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SensYang on 2017/04/26 19:32
 */

public class CreatGroupActivity extends BaseActivity {
    private ActivityCreatGroupBinding binding;
    private CreatGroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_creat_group);
        adapter = new CreatGroupAdapter();
        binding.setAdapter(adapter);
        new Thread(getFriendRunnable).start();
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
                        adapter.addUser(user);
                    }
                }
                adapter.sort();
                getHandler().sendEmptyMessage(0);
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
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
            adapter.addUser(user);
            LiteOrmDBUtil.getUserDBUtil().save(user);
            adapter.sort();
            getHandler().sendEmptyMessage(0);
        }
    };

    @Override
    public void topLeftClick(View view) {
        finish();
    }


    @Override
    public void topRightClick(View view) {
        if (CommonUtils.isFastDoubleClick()) return;
        List<User.Data> userList = new ArrayList<>();
        User.Data user = null;
        for (User.Data data : adapter.getUserList()) {
            if (data.isSelect()) {
                user = data;
                userList.add(data);
            }
        }
        if (userList.size() == 1 && user != null) {
            Intent intent = new Intent(CreatGroupActivity.this, ChatActivity.class);
            intent.putExtra("conversationType", ConversationType.SINGLE_CHAT);
            intent.putExtra("chatWith", user.getMember_name());
            startActivity(intent);
        } else {//TODO 创建群组
            String[] allMembers = new String[userList.size()];
            StringBuilder sb = new StringBuilder(ApiByHttp.getInstance().getUser().getMember_nickname());
            int size = Math.min(5, userList.size());
            for (int i = 0; i < size; i++) {
                User.Data temp = userList.get(i);
                allMembers[i] = temp.getMember_name();
                sb.append("、" + temp.getMember_nickname());
            }
            EMGroupManager.EMGroupOptions option = new EMGroupManager.EMGroupOptions();
            option.maxUsers = 200;
            option.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
            option.extField = ApiByHttp.getInstance().getMember_id() + System.currentTimeMillis();
            String reason = "";
            User.Data self = ApiByHttp.getInstance().getUser();
            if (self != null) {
                if (self.getMember_nickname() != null) {
                    reason = self.getMember_nickname() + "邀请你加入群聊";
                } else {
                    reason = self.getMember_name() + "邀请你加入群聊";
                }
            }
            EMClient.getInstance().groupManager().asyncCreateGroup(sb.toString(), sb.toString(), allMembers, reason, option, new EMValueCallBack<EMGroup>() {
                @Override
                public void onSuccess(final EMGroup value) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(CreatGroupActivity.this, ChatActivity.class);
                            intent.putExtra("conversationType", ConversationType.GROUP_CHAT);
                            intent.putExtra("chatWith", value.getGroupId());
                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void onError(int error, String errorMsg) {
                    ToastUtil.showToast("创建群组失败,请稍后重试");
                }
            });
        }
    }

}