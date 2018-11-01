package com.omi.ui.activity.user;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.omi.R;
import com.omi.bean.User;
import com.omi.bean.conversation.Group;
import com.omi.database.LiteOrmDBUtil;
import com.omi.databinding.ActivityGroupInfoBinding;
import com.omi.net.ApiByHttp;
import com.omi.net.ResponseCallback;
import com.omi.ui.adapter.contact.GroupMemberAdapter;
import com.omi.ui.base.BaseActivity;
import com.omi.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import static com.omi.config.OmiAction.RESULT_EXIT_GROUP;

/**
 * Created by SensYang on 2017/04/26 20:51
 */
public class GroupInfoActivity extends BaseActivity {
    ActivityGroupInfoBinding binding;
    private String groupId;
    private GroupMemberAdapter memberAdapter;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupId = getIntent().getStringExtra("groupid");
        if (groupId == null) {
            ToastUtil.showToast(R.string.wrong_userid);
            finish();
            return;
        }
        EMGroup tempgroup = EMClient.getInstance().groupManager().getGroup(groupId);
        if (tempgroup == null) {
            ToastUtil.showToast(R.string.none_group);
            finish();
            return;
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_info);
        binding.setGroupInfoActivity(this);
        memberAdapter = new GroupMemberAdapter();
        group = new Group();
        group.setGroup(tempgroup);
        new Thread(memberRunnable).start();
        binding.setAdapter(memberAdapter);
        binding.setGroup(group);
    }

    private Runnable memberRunnable = new Runnable() {
        @Override
        public void run() {
            List<String> members = new ArrayList<>();
            if (group.getOwner() != null) members.add(group.getOwner());
            EMCursorResult<String> result = null;
            int pageSize = 20;
            do {
                try {
                    result = EMClient.getInstance().groupManager().fetchGroupMembers(groupId, result != null ? result.getCursor() : "", pageSize);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                members.addAll(result.getData());
            } while (result.getData().size() == pageSize);
            for (String member : members) {
                User.Data user = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(User.Data.class, "member_name", member);
                if (user != null && user.getMember_avatar() != null) {
                    memberAdapter.addUser(user);
                } else {
                    ApiByHttp.getInstance().getUserInfo(member, userCallBack);
                }
            }
            getHandler().sendEmptyMessage(0);
        }
    };

    private ResponseCallback userCallBack = new ResponseCallback<User>() {
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
                memberAdapter.addUser(user);
                getHandler().sendEmptyMessage(0);
            }
        }
    };

    @Override
    protected void handlerPacketMsg(Message msg) {
        memberAdapter.notifyDataSetChanged();
    }

    @Override
    public void topLeftClick(View view) {
        finish();
    }

    public void onTopChatClick(View view) {}

    public void onDisturbClick(View view) {
        group.setMsgBlocked(group.isMsgBlocked());
        new Thread(blockRunnable).start();
    }

    private Runnable blockRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (group.isMsgBlocked()) {
                    EMClient.getInstance().groupManager().blockGroupMessage(groupId);//需异步处理
                } else {
                    EMClient.getInstance().groupManager().unblockGroupMessage(groupId);//需异步处理
                }
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
    };

    public void onExitClick(View view) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (group.isSelfGroup()) {
                        EMClient.getInstance().groupManager().destroyGroup(groupId);
                    } else {
                        EMClient.getInstance().groupManager().leaveGroup(groupId);
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        setResult(RESULT_EXIT_GROUP);
        finish();
    }
}
