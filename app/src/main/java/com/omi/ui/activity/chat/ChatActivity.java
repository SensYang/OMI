package com.omi.ui.activity.chat;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.kincai.libjpeg.ImageUtils;
import com.omi.R;
import com.omi.bean.User;
import com.omi.bean.chat.IMMessage;
import com.omi.bean.conversation.ConversationType;
import com.omi.config.OmiAction;
import com.omi.database.LiteOrmDBUtil;
import com.omi.databinding.ActivityChatBinding;
import com.omi.net.ApiByHttp;
import com.omi.net.ResponseCallback;
import com.omi.net.easemob.ApiByEasemob;
import com.omi.net.easemob.GroupStateCallBack;
import com.omi.net.easemob.ReceiveMessageCallback;
import com.omi.net.easemob.ReceiveMessageCallbackVO;
import com.omi.net.easemob.SendMessageCallBackVO;
import com.omi.net.easemob.SendMessageCallback;
import com.omi.ui.activity.user.GroupInfoActivity;
import com.omi.ui.activity.user.UserInfoActivity;
import com.omi.ui.adapter.chat.ChatAdapter;
import com.omi.ui.base.BaseActivity;
import com.omi.utils.FileUtil;
import com.omi.utils.FileUtils;
import com.omi.utils.Log;
import com.omi.utils.ToastUtil;
import com.omi.utils.controller.ChatItemControl;
import com.yanzhenjie.nohttp.rest.Response;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.omi.config.OmiAction.ACTION_GROUP_INFO;
import static com.omi.config.OmiAction.ACTION_PICK_PICTURE;
import static com.omi.config.OmiAction.ACTION_RECORD_VIDEO;
import static com.omi.config.OmiAction.ACTION_SELECT_LOCATION;
import static com.omi.config.OmiAction.ACTION_TAKE_PHOTO;
import static com.omi.config.OmiAction.RESULT_EXIT_GROUP;

/**
 * Created by SensYang on 2017/03/15 17:57
 */

public class ChatActivity extends BaseActivity {
    private ActivityChatBinding chatBinding;
    private ChatAdapter chatAdapter;
    private String chatWith;
    private ConversationType conversationType;
    private EMConversation conversation;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conversationType = (ConversationType) getIntent().getSerializableExtra("conversationType");
        chatWith = getIntent().getStringExtra("chatWith");
        if (chatWith == null || conversationType == null) {
            finish();
            return;
        }
        chatBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        chatAdapter = new ChatAdapter();
        chatBinding.setConversationType(conversationType);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        chatBinding.setLayoutManager(layoutManager);
        chatAdapter.getUserMap().put(ApiByHttp.getInstance().getPhone(), ApiByHttp.getInstance().getUser());
        chatBinding.setChatWith(chatWith);
        if (conversationType == ConversationType.SINGLE_CHAT) {
            chatBinding.topNavigationBar.getRightIV().setImageResource(R.drawable.icon_single_chat);
            conversation = EMClient.getInstance().chatManager().getConversation(chatWith, EMConversation.EMConversationType.Chat, true);
            User.Data user = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(User.Data.class, "member_name", chatWith);
            if (user == null) {
                ApiByHttp.getInstance().getUserInfo(chatWith, userInfoCallback);
            } else {
                chatBinding.setChatName(user.getMember_nickname());
                chatAdapter.getUserMap().put(user.getMember_name(), user);
            }
        } else if (conversationType == ConversationType.GROUP_CHAT) {
            EMClient.getInstance().groupManager().addGroupChangeListener(groupChangeListener);
            chatBinding.topNavigationBar.getRightIV().setImageResource(R.drawable.icon_group_chat);
            conversation = EMClient.getInstance().chatManager().getConversation(chatWith, EMConversation.EMConversationType.GroupChat, true);
            EMGroup group = EMClient.getInstance().groupManager().getGroup(chatWith);
            chatBinding.setChatName(group.getGroupName());
        }
        String startMsgId = "";
        if (conversation != null) {
            conversation.markAllMessagesAsRead();
            EMMessage emMessage = conversation.getLastMessage();
            if (emMessage != null) {
                if (conversationType == ConversationType.GROUP_CHAT) {
                    User.Data user = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(User.Data.class, "member_name", emMessage.getFrom());
                    if (user == null) {
                        ApiByHttp.getInstance().getUserInfo(chatWith, userInfoCallback);
                    } else {
                        chatAdapter.getUserMap().put(user.getMember_name(), user);
                    }
                }
                chatAdapter.addNewMessage(new IMMessage(emMessage));
                startMsgId = emMessage.getMsgId();
                List<EMMessage> messages = conversation.loadMoreMsgFromDB(startMsgId, 20);
                List<IMMessage> list = new ArrayList<>(messages.size());
                for (EMMessage message : messages) {
                    list.add(new IMMessage(message));
                    if (conversationType == ConversationType.GROUP_CHAT) {
                        User.Data user = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(User.Data.class, "member_name", message.getFrom());
                        if (user == null) {
                            ApiByHttp.getInstance().getUserInfo(chatWith, userInfoCallback);
                        } else {
                            chatAdapter.getUserMap().put(user.getMember_name(), user);
                        }
                    }
                }
                chatAdapter.addOldMessage(list);
            }
        }
        chatBinding.setAdapter(chatAdapter);
        SendMessageCallBackVO.addSendMessageCallback(sendCallback);
        ReceiveMessageCallbackVO.addReceiveMessageCallback(receiveMessageCallback);
        if (chatAdapter.getItemCount() > 0)
            layoutManager.scrollToPosition(chatAdapter.getItemCount() - 1);
    }

    private GroupStateCallBack groupChangeListener = new GroupStateCallBack() {
        /*群组被解散。*/
        @Override
        public void onGroupDestroyed(String groupId, String groupName) {
            if (chatWith.equalsIgnoreCase(groupId)) {
                finish();
            }
        }
    };

    private ResponseCallback userInfoCallback = new ResponseCallback<User>() {
        @Override
        public void onSucceed(int what, Response<User> response) {
            User res = response.get();
            if (res == null) return;
            if (res.getUserinfo() != null && res.getUserinfo().size() > 0) {
                User.Data user = res.getUserinfo().get(0);
                LiteOrmDBUtil.getUserDBUtil().save(user);
                chatAdapter.getUserMap().put(user.getMember_name(), user);
                if (conversationType == ConversationType.SINGLE_CHAT)
                    chatBinding.setChatName(user.getMember_nickname());
                getHandler().sendEmptyMessage(2);
            } else {
                ToastUtil.showToast(res.getMsg());
            }
        }
    };

    /**
     * 发送消息回调
     */
    private SendMessageCallback sendCallback = new SendMessageCallback() {
        @Override
        public void onSend(IMMessage message) {
        }

        @Override
        public void onSuccess(IMMessage message) {
            if (conversation != null && conversation.getLastMessage() != null) {
                IMMessage msg = new IMMessage(conversation.getLastMessage());
                msg.setHasSend(true);
                chatAdapter.addNewMessage(msg);
                getHandler().sendEmptyMessage(0);
            }
        }

        @Override
        public void onError(IMMessage message, int code, String error) {
            if (conversation != null && conversation.getLastMessage() != null) {
                IMMessage msg = new IMMessage(conversation.getLastMessage());
                msg.setHasSend(true);
                chatAdapter.addNewMessage(msg);
                getHandler().sendEmptyMessage(0);
            }
        }

        @Override
        public void onProgress(IMMessage message, int progress, String status) {
        }
    };

    /**
     * 接收消息回调
     */
    private ReceiveMessageCallback receiveMessageCallback = new ReceiveMessageCallback() {
        @Override
        public void onMessageReceived(List<IMMessage> messages) {
            boolean needNotify = false;
            if (conversationType == ConversationType.SINGLE_CHAT) {
                for (IMMessage message : messages) {
                    if (message.getTo().equalsIgnoreCase(ApiByHttp.getInstance().getPhone())) {
                        chatAdapter.addNewMessage(message);
                        needNotify = true;
                        conversation.markMessageAsRead(message.getMessageId());
                    }
                }
            } else if (conversationType == ConversationType.GROUP_CHAT) {
                for (IMMessage message : messages) {
                    if (message.getTo().equalsIgnoreCase(chatWith)) {
                        if (chatAdapter.getUserMap().get(message.getFrom()) == null) {
                            User.Data user = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(User.Data.class, "member_name", message.getFrom());
                            if (user == null) {
                                ApiByHttp.getInstance().getUserInfo(chatWith, userInfoCallback);
                            } else {
                                chatAdapter.getUserMap().put(user.getMember_name(), user);
                            }
                        }
                        chatAdapter.addNewMessage(message);
                        needNotify = true;
                        conversation.markMessageAsRead(message.getMessageId());
                    }
                }
            }
            if (needNotify) getHandler().sendEmptyMessage(0);
        }

        @Override
        public void onCmdMessageReceived(List<IMMessage> messages) {

        }

        @Override
        public void onMessageRead(List<String> messageIds) {
            for (String messageId : messageIds) {
                for (int i = 0; i < chatAdapter.getItemCount(); i++) {
                    IMMessage imMessage = chatAdapter.getItem(i);
                    if (imMessage.getMessageId().equalsIgnoreCase(messageId)) {
                        imMessage.setHasRead(true);
                        break;
                    }
                }
            }
        }

        @Override
        public void onMessageDelivered(List<String> messageIds) {
            for (String messageId : messageIds) {
                for (int i = 0; i < chatAdapter.getItemCount(); i++) {
                    IMMessage imMessage = chatAdapter.getItem(i);
                    if (imMessage.getMessageId().equalsIgnoreCase(messageId)) {
                        imMessage.setHasSend(true);
                        break;
                    }
                }
            }
        }

        @Override
        public void onMessageChanged(String messageId, Object change) {

        }
    };

    @Override
    protected void handlerPacketMsg(Message msg) {
        switch (msg.what) {
            case 0://刷新
                int position = chatAdapter.getItemCount() - 1;
                if (position < 0) break;
                chatAdapter.notifyItemInserted(position);
                if (chatAdapter.getItemCount() > 0) layoutManager.scrollToPosition(position);
                break;
            case 1:
                chatAdapter.notifyItemChanged(msg.arg1);
                break;
            case 2:
                int first = layoutManager.findFirstVisibleItemPosition();
                int last = layoutManager.findLastVisibleItemPosition();
                chatAdapter.notifyItemRangeChanged(first, last - first + 1);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        ChatItemControl.getInstance().stop();
        if (conversation != null) conversation.markAllMessagesAsRead();
        SendMessageCallBackVO.removeSendMessageCallback(sendCallback);
        EMClient.getInstance().groupManager().removeGroupChangeListener(groupChangeListener);
        ReceiveMessageCallbackVO.removeReceiveMessageCallback(receiveMessageCallback);
        super.onDestroy();
    }

    @Override
    public void topLeftClick(View view) {
        finish();
    }

    @Override
    public void topRightClick(View view) {
        if (conversationType == ConversationType.SINGLE_CHAT) {
            Intent intent = new Intent(this, UserInfoActivity.class);
            intent.putExtra("userid", chatWith);
            startActivity(intent);
        } else if (conversationType == ConversationType.GROUP_CHAT) {
            Intent intent = new Intent(this, GroupInfoActivity.class);
            intent.putExtra("groupid", chatWith);
            startActivityForResult(intent, ACTION_GROUP_INFO);
        }
    }

    private SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
    int imgCount;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivityResult  " + requestCode);
        switch (requestCode) {
            case ACTION_GROUP_INFO:
                if (resultCode == RESULT_EXIT_GROUP) {
                    finish();
                }
                break;
            case ACTION_SELECT_LOCATION:
                if (data == null) return;
                //发送位置消息
                String province = data.getStringExtra("province");
                String city = data.getStringExtra("city");
                String area = data.getStringExtra("area");
                String road = data.getStringExtra("road");
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);
                ApiByEasemob.getInstance().sendLocationMessage(latitude, longitude, province + city + area + road, chatWith);
                break;
            case ACTION_PICK_PICTURE:
                if (data == null) return;
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    File file = new File(FileUtil.getPathWithUri(imageUri));
                    if (!file.exists()) break;
                    new SendImgThread(imageUri).start();
                }
                break;
            case ACTION_TAKE_PHOTO:
                if (OmiAction.TEMP_URI != null) {
                    File file = new File(FileUtil.getPathWithUri(OmiAction.TEMP_URI));
                    if (!file.exists()) break;
                    new SendImgThread(OmiAction.TEMP_URI).start();
                }
                OmiAction.TEMP_URI = null;
                break;
            case ACTION_RECORD_VIDEO:
                if (data == null) return;
                Uri videoUri = data.getData();//可以通过这个播放
                ApiByEasemob.getInstance().sendVideoMessage(videoUri, chatWith);
                break;
        }
    }

    class SendImgThread extends Thread {
        Uri imageUri;
        String outPath;

        public SendImgThread(Uri imageUri) {
            this.imageUri = imageUri;
        }

        @Override
        public void run() {
            if (imageUri != null) {
                outPath = FileUtils.makeImagePath(format.format(new Date()) + imgCount++);
                ImageUtils.compressBitmap(FileUtil.getPathWithUri(imageUri), outPath);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ApiByEasemob.getInstance().sendImageMessage(outPath, chatWith);
                    }
                });
            }
        }
    }
}