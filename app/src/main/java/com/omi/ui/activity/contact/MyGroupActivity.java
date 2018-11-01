package com.omi.ui.activity.contact;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.omi.BR;
import com.omi.R;
import com.omi.bean.User;
import com.omi.bean.conversation.Group;
import com.omi.database.LiteOrmDBUtil;
import com.omi.databinding.ActivityMyGroupBinding;
import com.omi.net.ApiByHttp;
import com.omi.net.ResponseCallback;
import com.omi.ui.adapter.contact.GroupAdapter;
import com.omi.ui.base.BaseActivity;

import java.util.List;

/**
 * Created by SensYang on 2017/04/10 12:18
 */

public class MyGroupActivity extends BaseActivity {
    ActivityMyGroupBinding binding;
    private GroupAdapter groupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_group);
        groupAdapter = new GroupAdapter();
        binding.setAdapter(groupAdapter);
        new Thread(groupRunnable).start();
    }

    private Runnable groupRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                List<EMGroup> grouplist = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                for (EMGroup emGroup : grouplist) {
                    Group group = new Group();
                    group.setGroup(emGroup);
                    try {
                        EMCursorResult<String> result = EMClient.getInstance().groupManager().fetchGroupMembers(group.getGroupId(), "", 8);
                        if (result != null && result.getData() != null) {
                            result.getData().add(group.getOwner());
                            for (String member : result.getData()) {
                                User.Data user = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(User.Data.class, "member_name", member);
                                if (user != null && user.getMember_avatar() != null) {
                                    group.getGroupHeads().add(user.getMember_avatar());
                                } else {
                                    ApiByHttp.getInstance().getUserInfo(member, new UserCallBack(group));
                                }
                            }
                        }
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    } finally {
                        groupAdapter.addGroup(group);
                    }
                }
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
            getHandler().sendEmptyMessage(0);
        }
    };

    @Override
    protected void handlerPacketMsg(Message msg) {
        groupAdapter.notifyDataSetChanged();
    }

    class UserCallBack extends ResponseCallback<User> {
        Group group;

        public UserCallBack(Group group) {
            this.group = group;
        }

        @Override
        public void onSucceed(int what, User response) throws Exception {
            if (response.getUserinfo() != null && response.getUserinfo().size() > 0) {
                User.Data getUser = response.getUserinfo().get(0);
                User.Data user = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(User.Data.class, "member_name", getUser.getMember_name());
                if (user != null) {
                    user.setUser(getUser);
                    LiteOrmDBUtil.getUserDBUtil().update(user);
                } else {
                    LiteOrmDBUtil.getUserDBUtil().save(getUser);
                }
                group.getGroupHeads().add(getUser.getMember_avatar());
                group.notifyPropertyChanged(BR.groupHeads);
            }
        }
    }

    @Override
    public void topLeftClick(View view) {
        finish();
    }
}
