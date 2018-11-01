package com.omi.ui.activity.main;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;

import com.android.databinding.library.baseAdapters.BR;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.omi.R;
import com.omi.bean.User;
import com.omi.bean.chat.IMMessage;
import com.omi.bean.conversation.Conversation;
import com.omi.bean.conversation.ConversationType;
import com.omi.config.glideconfig.RoundTransform;
import com.omi.database.LiteOrmDBUtil;
import com.omi.databinding.FragmentConversationBinding;
import com.omi.net.ApiByHttp;
import com.omi.net.ResponseCallback;
import com.omi.net.easemob.GroupStateCallBack;
import com.omi.net.easemob.ReceiveMessageCallback;
import com.omi.net.easemob.ReceiveMessageCallbackVO;
import com.omi.ui.activity.chat.ChatActivity;
import com.omi.ui.activity.search.SearchActivity;
import com.omi.ui.adapter.ConversationAdapter;
import com.omi.ui.base.BaseFragment;
import com.omi.ui.widget.chatbroad.EmojiUtil;
import com.omi.ui.widget.listener.MainFragmentProxy;
import com.omi.ui.widget.window.MainMenuPop;
import com.omi.utils.DisplayUtil;
import com.omi.utils.Log;
import com.omi.utils.ToastUtil;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.omi.R.id.imageView;
import static com.omi.utils.VersionUtil.getPackageName;


/**
 * Created by SensYang on 2017/03/08 12:46
 */

public class ConversationFragment extends BaseFragment implements MainFragmentProxy {
    private FragmentConversationBinding messageBinding;
    private ConversationAdapter conversationAdapter;
    private MainMenuPop menuPop;
    private RecyclerView.LayoutManager layoutManager;
    private View.OnClickListener leftClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getContext(), SearchActivity.class));
        }
    };

    private View.OnClickListener rightClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuPop.showAsDropDown(messageBinding.topNavigationBar);
        }
    };

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        messageBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_conversation, container, false);
        menuPop = new MainMenuPop(getActivity());
        layoutManager = new LinearLayoutManager(getContext());
        conversationAdapter = new ConversationAdapter();
        messageBinding.recyclerView.setLayoutManager(layoutManager);
        messageBinding.recyclerView.setAdapter(conversationAdapter);
        messageBinding.topNavigationBar.setLeftClick(leftClick);
        messageBinding.topNavigationBar.setRightClick(rightClick);
        return messageBinding.getRoot();
    }

    @Override
    public void onFirstUserVisible() {
        ReceiveMessageCallbackVO.addReceiveMessageCallback(receiveMessageCallback);
        EMClient.getInstance().groupManager().addGroupChangeListener(groupChangeListener);
        onUserVisible();
    }

    private GroupStateCallBack groupChangeListener = new GroupStateCallBack() {
        /*群组被解散。*/
        @Override
        public void onGroupDestroyed(String groupId, String groupName) {
            for (Conversation conversation : conversationAdapter.getConversationList()) {
                if (conversation.getFrom().equalsIgnoreCase(groupId)) {
                    conversationAdapter.getConversationList().remove(conversation);
                    getHandler().sendEmptyMessage(0);
                    break;
                }
            }
        }
    };

    @Override
    public void onUserVisible() {
        conversationAdapter.onResume();
        if (ApiByHttp.getInstance().getPhone() == null) {
            conversationAdapter.clear();
            conversationAdapter.notifyDataSetChanged();
        } else {
            new Thread(conversationRunnable).start();
        }
    }

    private Runnable conversationRunnable = new Runnable() {
        @Override
        public void run() {
            conversationAdapter.clear();
            Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
            Conversation conversation = null;
            User.Data user;
            for (EMConversation emConversation : conversations.values()) {
                boolean has = false;
                for (Conversation conversation1 : conversationAdapter.getConversationList()) {
                    if (conversation1.getFrom().equalsIgnoreCase(emConversation.conversationId())) {
                        has = true;
                        break;
                    }
                }
                if (has) {
                    continue;
                }
                EMMessage lastMessage = emConversation.getLastMessage();
                if (lastMessage == null) continue;
                boolean needInsert = false;
                boolean needAdd = true;
                for (Conversation c : conversationAdapter.getConversationList()) {
                    if (c.getFrom().equalsIgnoreCase(emConversation.conversationId())) {
                        conversation = c;
                        needAdd = false;
                        break;
                    }
                }

                if (needAdd) {
                    conversation = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(Conversation.class, "_from", emConversation.conversationId());
                    if (conversation == null) {
                        needInsert = true;
                        conversation = new Conversation();
                    }

                    switch (emConversation.getType()) {
                        case Chat:
                            conversation.setConversationType(ConversationType.SINGLE_CHAT);
                            conversation.setHeadList(new ArrayList<String>(1));
                            conversation.setFrom(emConversation.conversationId());
                            user = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(User.Data.class, "member_name", conversation.getFrom());
                            if (user == null) {
                                ApiByHttp.getInstance().getUserInfo(conversation.getFrom(), new UserInfoCallback(conversation));
                            } else {
                                conversation.getHeadList().add(user.getMember_avatar());
                                conversation.setFromName(user.getMember_nickname());
                            }
                            break;
                        case GroupChat:
                            EMGroup group = EMClient.getInstance().groupManager().getGroup(emConversation.conversationId());
                            if (group == null) continue;
                            conversation.setConversationType(ConversationType.GROUP_CHAT);
                            conversation.setHeadList(new ArrayList<String>(9));
                            conversation.setFrom(group.getGroupId());
                            conversation.setFromName(group.getGroupName());
                            try {
                                EMCursorResult<String> result = EMClient.getInstance().groupManager().fetchGroupMembers(group.getGroupId(), "", 8);
                                if (result != null && result.getData() != null) {
                                    result.getData().add(group.getOwner());
                                    for (String member : result.getData()) {
                                        user = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(User.Data.class, "member_name", member);
                                        if (user == null) {
                                            ApiByHttp.getInstance().getUserInfo(member, new UserInfoCallback(conversation));
                                        } else {
                                            conversation.getHeadList().add(user.getMember_avatar());
                                        }
                                    }
                                }
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            continue;
                    }
                }
                conversation.setUnReadCount(emConversation.getUnreadMsgCount());
                IMMessage message = new IMMessage(lastMessage);
                conversation.setMessageCreateTime(message.getCreateTime());
                conversation.setMessageDetial(getMessageDetial(message));
                if (needInsert) {
                    LiteOrmDBUtil.getUserDBUtil().save(conversation);
                } else {
                    LiteOrmDBUtil.getUserDBUtil().update(conversation);
                }
                if (needAdd) conversationAdapter.addConversation(conversation);
            }
            conversationAdapter.sort();
            getHandler().sendEmptyMessage(0);
        }
    };

    private String getMessageDetial(IMMessage message) {
        switch (message.getMessageType()) {
            case TXT_OTHER:
            case TXT_SELF:
                return message.getContent();
            case IMAGE_OTHER:
            case IMAGE_SELF:
                return"[图片]";
            case VIDEO_OTHER:
            case VIDEO_SELF:
                return "[视频]";
            case LOCATION_OTHER:
            case LOCATION_SELF:
                return"[位置]";
            case VOICE_OTHER:
            case VOICE_SELF:
                return "[语音]";
            case FILE_OTHER:
            case FILE_SELF:
                return "[文件]";
            case GIF_OTHER:
            case GIF_SELF:
                return "[表情]";
        }
        return "[未知]";
    }

    class UserInfoCallback extends ResponseCallback<User> {
        Conversation conversation;

        public UserInfoCallback(Conversation conversation) {
            this.conversation = conversation;
        }

        @Override
        public void onSucceed(int what, Response<User> response) {
            User res = response.get();
            if (res == null || res.getUserinfo() == null || res.getUserinfo().size() == 0) {
                ToastUtil.showToast(res.getMsg());
                return;
            }
            User.Data user = res.getUserinfo().get(0);
            long count = LiteOrmDBUtil.getUserDBUtil().queryCount(User.Data.class, "member_id", user.getMember_id());
            if (count < 1) {
                LiteOrmDBUtil.getUserDBUtil().save(user);
            }
            Log.e("group_member--->" + user.getMember_avatar());
            conversation.getHeadList().add(user.getMember_avatar());
            conversation.notifyPropertyChanged(BR.headList);
            if (conversation.getConversationType() == ConversationType.SINGLE_CHAT) {
                conversation.setFromName(user.getMember_nickname());
            }
        }
    }

    @Override
    protected void handlerPacketMsg(Message msg) {
        switch (msg.what) {
            case 0:
                conversationAdapter.notifyDataSetChanged();
                break;
        }
    }

    class NotificationCallBack extends ResponseCallback<User> implements Runnable {
        IMMessage message;
        User.Data user;
        String name;
        String content;
        int count;

        public NotificationCallBack(IMMessage message) {
            this.message = message;
            this.content = getMessageDetial(message);
        }

        public NotificationCallBack setUser(User.Data user) {
            this.user = user;
            this.name = user.getMember_nickname();
            return this;
        }

        public NotificationCallBack setCount(int count) {
            this.count = count;
            return this;
        }

        @Override
        public void onSucceed(int what, User res) throws Exception {
            if (res == null || res.getUserinfo() == null || res.getUserinfo().size() == 0) {
                ToastUtil.showToast(res.getMsg());
                return;
            }
            setUser(res.getUserinfo().get(0));
            getActivity().runOnUiThread(this);
        }

        @Override
        public void run() {
            float radiu = DisplayUtil.dip2px(3);
            int width = (int) DisplayUtil.dip2px(30);
            Log.e("开始下载");
            Glide.with(getContext())
                    .load(user.getMember_avatar())
                    .asBitmap()
                    .transform(new RoundTransform(getContext())
                            .setLeftTopRadius(radiu)
                            .setRightTopRadius(radiu)
                            .setRightBottomRadius(radiu)
                            .setLeftBottomRadius(radiu)
                    )
                    .placeholder(R.drawable.default_head)
                    .error(R.drawable.default_head)
                    .into(new SimpleTarget<Bitmap>(width, width) {

                        CharSequence info = EmojiUtil.stringToEmoji(DisplayUtil.sp2px(14), (count>1?"["+count+"]":"")+name+":"+content) ;

                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Log.e("下载完成");
                            notification(resource, ConversationType.SINGLE_CHAT, message.getFrom(), name, info);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            Log.e("下载失败");
                            if(errorDrawable instanceof BitmapDrawable)
                                notification(((BitmapDrawable) errorDrawable).getBitmap(), ConversationType.SINGLE_CHAT, message.getFrom(), name, info);
                            else
                                notification(BitmapFactory.decodeResource(getResources(), R.drawable.default_head), ConversationType.SINGLE_CHAT, message.getFrom(), name, info);
                        }
                    });
            Glide.with(getContext()).resumeRequests();
        }
    }

    private ReceiveMessageCallback receiveMessageCallback = new ReceiveMessageCallback() {
        @Override
        public void onMessageReceived(List<IMMessage> messages) {
            for (IMMessage message : messages) {
                boolean needInsert = false;
                Conversation conversation = null;
                for (Conversation c : conversationAdapter.getConversationList()) {
                    if (c.getFrom().equalsIgnoreCase(message.getFrom())) {
                        conversation = c;
                        break;
                    }
                }
                if (conversation == null) {
                    if (message.getConversationType() == ConversationType.SINGLE_CHAT) {
                        conversation = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(Conversation.class, "_from", message.getFrom());
                    } else {
                        conversation = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(Conversation.class, "_from", message.getTo());
                    }
                    if (conversation == null) {
                        needInsert = true;
                        conversation = new Conversation();
                        conversation.setConversationType(message.getConversationType());
                        if (message.getConversationType() == ConversationType.SINGLE_CHAT) {
                            conversation.setFromName(message.getNickname());
                            conversation.setFrom(message.getFrom());
                        } else {
                            conversation.setFrom(message.getTo());
                            EMGroup group = EMClient.getInstance().groupManager().getGroup(message.getTo());
                            if (group != null) {
                                conversation.setFromName(group.getGroupName());
                            }
                        }
                    }
                    conversationAdapter.addConversation(conversation);
                }
                conversation.setMessageDetial(getMessageDetial(message));
                conversation.setMessageCreateTime(message.getCreateTime());
                if (conversation!=conversationAdapter.getChatingConversation())
                    conversation.setUnReadCount(conversation.getUnReadCount() + 1);
                if (needInsert) {
                    LiteOrmDBUtil.getUserDBUtil().save(conversation);
                } else {
                    LiteOrmDBUtil.getUserDBUtil().update(conversation);
                }
                if (isApplicationBroughtToBackground(getContext())) {
                    if(conversationAdapter.getChatingConversation()!=null&&conversationAdapter.getChatingConversation().getUnReadCount()<0){
                        conversationAdapter.getChatingConversation().setUnReadCount(0);
                        if (conversation==conversationAdapter.getChatingConversation())
                            conversation.setUnReadCount(1);
                    }
                    if (message.getConversationType() == ConversationType.SINGLE_CHAT) {
                        User.Data user = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(User.Data.class, "member_name", conversation.getFrom());
                        NotificationCallBack notificationCallBack = new NotificationCallBack(message);
                        notificationCallBack.setCount(conversation.getUnReadCount());
                        if (user == null) {
                            ApiByHttp.getInstance().getUserInfo(conversation.getFrom(), notificationCallBack);
                        } else {
                            notificationCallBack.setUser(user);
                            getActivity().runOnUiThread(notificationCallBack);
                        }
                    }
                }
            }
            conversationAdapter.sort();
            getHandler().sendEmptyMessage(0);

        }

        @Override
        public void onCmdMessageReceived(List<IMMessage> messages) {

        }

        @Override
        public void onMessageRead(List<String> messageIds) {

        }

        @Override
        public void onMessageDelivered(List<String> messageIds) {

        }

        @Override
        public void onMessageChanged(String messageId, Object change) {

        }
    };

    @Override
    public void scrollToTop() {
        if (layoutManager != null) {
            layoutManager.scrollToPosition(0);
        }
    }

    public void notification(Bitmap bitmap, ConversationType conversationType, String chatWith, String name, CharSequence info) {
        Log.e("开始提示");
        Notification notification = new Notification();
        //设置statusbar显示的icon
        notification.icon = R.mipmap.ic_launcher;
        //设置statusbar显示的文字信息
        // myNoti.tickerText= new_msg ;
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        //设置notification发生时同时发出默认声音
        notification.defaults = Notification.DEFAULT_SOUND;
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_single);
        contentView.setImageViewBitmap(imageView, bitmap);
        contentView.setTextViewText(R.id.nameTV, name);
        contentView.setTextViewText(R.id.infoTV, info);
        notification.contentView = contentView;
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("conversationType", conversationType);
        intent.putExtra("chatWith", chatWith);
        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentIntent = contentIntent;
        // 获取NotificationManager管理者对象
        NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        int id;
        if(chatWith.length()>9){
            id= Integer.parseInt(chatWith.substring(chatWith.length()-9));
        }else {
            id= Integer.parseInt(chatWith);
        }
        manager.notify(id, notification);
        Log.e("提示...");
    }

    /**
     * 判断当前应用程序处于前台还是后台
     */
    public static boolean isApplicationBroughtToBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

}