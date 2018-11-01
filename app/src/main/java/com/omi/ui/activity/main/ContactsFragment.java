package com.omi.ui.activity.main;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.omi.R;
import com.omi.bean.ApplicationState;
import com.omi.bean.User;
import com.omi.bean.account.ContactInvited;
import com.omi.database.LiteOrmDBUtil;
import com.omi.databinding.FragmentContactsBinding;
import com.omi.net.ApiByHttp;
import com.omi.net.ResponseCallback;
import com.omi.net.easemob.GroupStateCallBack;
import com.omi.ui.adapter.ContactsAdapter;
import com.omi.ui.base.BaseFragment;
import com.omi.ui.widget.listener.MainFragmentProxy;
import com.omi.utils.ContactsUtil;

/**
 * Created by SensYang on 2017/03/14 14:31
 */
@SuppressLint("ValidFragment")
public class ContactsFragment extends BaseFragment implements MainFragmentProxy {
    private FragmentContactsBinding contactsBinding;
    private ApplicationState applicationState;
    private ContactsAdapter contactsAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public ContactsFragment(ApplicationState applicationState) {
        this.applicationState = applicationState;
    }

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts, container, false);
        contactsAdapter = new ContactsAdapter();
        contactsBinding.setAdapter(contactsAdapter);
        layoutManager = new LinearLayoutManager(getContext());
        contactsBinding.setLayoutManager(layoutManager);
        contactsBinding.setApplicationState(applicationState);
        return contactsBinding.getRoot();
    }

    @Override
    public void onFirstUserVisible() {
        new Thread(getContactRunnable).start();
    }

    @Override
    public void onUserVisible() {
        EMClient.getInstance().contactManager().setContactListener(contactListener);
        EMClient.getInstance().groupManager().addGroupChangeListener(groupChangeListener);
    }

    private EMContactListener contactListener = new EMContactListener() {
        @Override
        public void onContactAdded(String username) {}

        @Override
        public void onContactDeleted(String username) {}

        @Override
        public void onContactInvited(String username, String reason) {
            ContactInvited contactInvited = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(ContactInvited.class, "username", username);
            if (contactInvited == null) {
                contactInvited = new ContactInvited();
                contactInvited.setUsername(username);
                contactInvited.setReason(reason);
                User.Data user = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(User.Data.class, "member_name", username);
                if (user == null) {
                    ApiByHttp.getInstance().getUserInfo(username, userCallback);
                } else {
                    contactInvited.setNickName(user.getMember_nickname());
                    contactInvited.setUserHead(user.getMember_avatar());
                }
                LiteOrmDBUtil.getUserDBUtil().save(contactInvited);
            } else {
                contactInvited.setHasAdd(false);
                contactInvited.setHasDecline(false);
                contactInvited.setReason(reason);
                LiteOrmDBUtil.getUserDBUtil().update(contactInvited);
            }
            //收到好友邀请
            contactsAdapter.setHasNewFriend(true);
        }

        @Override
        public void onFriendRequestAccepted(String username) {}

        @Override
        public void onFriendRequestDeclined(String username) {}
    };

    private GroupStateCallBack groupChangeListener = new GroupStateCallBack() {

        /*群组被解散。*/
        @Override
        public void onGroupDestroyed(String groupId, String groupName) {
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(groupId, EMConversation.EMConversationType.GroupChat);
            if (conversation != null) conversation.clearAllMessages();
        }

        /*自动同意加入群组*/
        @Override
        public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
            //收到好友邀请
            contactsAdapter.setHasNewGroup(true);
        }
    };

    private ResponseCallback userCallback = new ResponseCallback<User>() {
        @Override
        public void onSucceed(int what, User response) throws Exception {
            User.Data user = response.getUserinfo().get(0);
            ContactInvited contactInvited = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(ContactInvited.class, "username", user.getMember_name());
            if (contactInvited != null) {
                contactInvited.setUserHead(user.getMember_avatar());
                contactInvited.setNickName(user.getMember_nickname());
                LiteOrmDBUtil.getUserDBUtil().update(contactInvited);
            }
            LiteOrmDBUtil.getUserDBUtil().save(user);
        }
    };

    private Runnable getContactRunnable = new Runnable() {
        @Override
        public void run() {
            contactsAdapter.addContact(ContactsUtil.getContact());
            contactsAdapter.sort();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    contactsAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    @Override
    public void scrollToTop() {
        if (layoutManager != null) {
            layoutManager.scrollToPosition(0);
        }
    }
}