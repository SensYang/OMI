package com.omi.bean.chat;

import android.databinding.Bindable;

import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.litesuits.orm.db.annotation.Column;
import com.omi.BR;
import com.omi.bean.User;
import com.omi.bean.base.BaseBean;
import com.omi.bean.conversation.ConversationType;
import com.omi.database.LiteOrmDBUtil;
import com.omi.net.ApiByHttp;
import com.omi.net.ResponseCallback;
import com.omi.utils.Log;
import com.omi.utils.TimeUtils;

import java.io.File;

import static com.omi.bean.chat.IMMessageType.CMD_OTHER;
import static com.omi.bean.chat.IMMessageType.FILE_OTHER;
import static com.omi.bean.chat.IMMessageType.GIF_OTHER;
import static com.omi.bean.chat.IMMessageType.IMAGE_OTHER;
import static com.omi.bean.chat.IMMessageType.LOCATION_OTHER;
import static com.omi.bean.chat.IMMessageType.TXT_OTHER;
import static com.omi.bean.chat.IMMessageType.UNKNOW;
import static com.omi.bean.chat.IMMessageType.VIDEO_OTHER;
import static com.omi.bean.chat.IMMessageType.VOICE_OTHER;

/**
 * Created by SensYang on 2017/03/15 18:29
 */
public class IMMessage extends BaseBean {
    /* 0.文字消息1.语音消息2.图片消息3.大表情消息4.位置消息5.视频消息 */
    private IMMessageType messageType;
    /*是否已读*/
    private boolean hasRead;
    /*是否发送成功*/
    private boolean hasSend;
    /*是否展示创建时间*/
    private boolean timeVisibility;
    /*用户昵称*/
    private String nickname;
    /*发消息的人头像地址*/
    private String fromHead;
    /*消息来自哪里*/
    @Column("_from")
    private String from;
    /*消息发送至哪里*/
    @Column("_to")
    private String to;
    /*消息内容*/
    private String content;
    /*视频缩略图*/
    private String thumbnailUrl;
    /*媒体类型时长*/
    private long duration;
    /*经度*/
    private double longitude;
    /*纬度*/
    private double latitude;
    /*创建时间*/
    private long createTime;
    /*是否展示名字 单聊不展示名字 群聊需要展示名字*/
    private boolean showName = true;
    /*环信的消息id*/
    private String messageId;
    /*会话类型*/
    private ConversationType conversationType;
    private int progress = 0;

    public IMMessage(EMMessage message) {
        switch (message.getType()) {
            case TXT: {
                EMTextMessageBody body = (EMTextMessageBody) message.getBody();
                content = body.getMessage();
                if (message.getBooleanAttribute("isGif", false)) {
                    messageType = GIF_OTHER;
                } else if (message.getBooleanAttribute("isVoice", false)) {
                    duration = message.getLongAttribute("mediaLength", 0);
                    messageType = VOICE_OTHER;
                } else {
                    messageType = TXT_OTHER;
                }
            }
            break;
            case IMAGE: {
                EMImageMessageBody body = (EMImageMessageBody) message.getBody();
                thumbnailUrl = body.getThumbnailUrl();
                content = body.getRemoteUrl();
                messageType = IMAGE_OTHER;
            }
            break;
            case VIDEO: {
                EMVideoMessageBody body = (EMVideoMessageBody) message.getBody();
                thumbnailUrl = body.getThumbnailUrl();
                duration = body.getDuration() * 1000;
                content = body.getLocalUrl();
                messageType = VIDEO_OTHER;
                if (content != null && new File(content).exists()) {
                    progress = 100;
                }
                Log.e("body.getDuration()--->"+body.getDuration());
            }
            break;
            case LOCATION: {
                EMLocationMessageBody body = (EMLocationMessageBody) message.getBody();
                content = body.getAddress();
                longitude = body.getLongitude();
                latitude = body.getLatitude();
                messageType = LOCATION_OTHER;
            }
            break;
            case VOICE: {
                EMVoiceMessageBody body = (EMVoiceMessageBody) message.getBody();
                duration = body.getLength() * 1000;
                content = body.getLocalUrl();
                messageType = VOICE_OTHER;
                Log.e("body.getDuration()--->"+body.getLength());
            }
            break;
            case FILE: {
                EMFileMessageBody body = (EMFileMessageBody) message.getBody();
                content = body.getLocalUrl();
                messageType = FILE_OTHER;
            }
            break;
            case CMD: {
                messageType = CMD_OTHER;
            }
            break;
            default: {
                messageType = UNKNOW;
            }
            break;
        }
        switch (message.getChatType()) {
            case Chat:
                conversationType = ConversationType.SINGLE_CHAT;
                break;
            case GroupChat:
                conversationType = ConversationType.GROUP_CHAT;
                break;
            default:
                conversationType = ConversationType.UNKNOW;
                break;
        }
        to = message.getTo();
        from = message.getFrom();
        if (from.equalsIgnoreCase(ApiByHttp.getInstance().getPhone())) {
            messageType = IMMessageType.valueOf(messageType.getValue() + 100);
        }
        if (message.getChatType() != EMMessage.ChatType.Chat) {
            showName = false;
        }
        createTime = message.getMsgTime();
        messageId = message.getMsgId();
        nickname = message.getUserName();
        User.Data user = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(User.Data.class, "member_name", message.getUserName());
        if (user != null) {
            nickname = user.getMember_nickname();
        } else {
            ApiByHttp.getInstance().getUserInfo(message.getFrom(), new ResponseCallback<User>() {
                @Override
                public void onSucceed(int what, User response) throws Exception {
                    if (response.getUserinfo() == null || response.getUserinfo().size() == 0)
                        return;
                    User.Data user = response.getUserinfo().get(0);
                    setNickname(user.getMember_nickname());
                    long count = LiteOrmDBUtil.getUserDBUtil().queryCount(User.Data.class, "member_id", user.getMember_id());
                    if (count < 1) {
                        LiteOrmDBUtil.getUserDBUtil().save(user);
                    }
                }
            });
        }
        hasRead = !message.isUnread();
    }

    public IMMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(IMMessageType messageType) {
        this.messageType = messageType;
    }

    @Bindable
    public boolean isHasRead() {
        return hasRead;
    }

    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
        notifyPropertyChanged(BR.hasRead);
    }

    @Bindable
    public boolean isHasSend() {
        return hasSend;
    }

    public void setHasSend(boolean hasSend) {
        this.hasSend = hasSend;
        notifyPropertyChanged(BR.hasSend);
    }

    @Bindable
    public boolean isTimeVisibility() {
        return timeVisibility;
    }

    public void setTimeVisibility(boolean timeVisibility) {
        this.timeVisibility = timeVisibility;
        notifyPropertyChanged(BR.timeVisibility);
    }

    @Bindable
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        if (nickname != null && nickname.equalsIgnoreCase(this.nickname)) return;
        this.nickname = nickname;
        notifyPropertyChanged(BR.nickname);
    }

    public String getFromHead() {
        return fromHead;
    }

    public void setFromHead(String fromHead) {
        this.fromHead = fromHead;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isShowName() {
        return showName;
    }

    public void setShowName(boolean showName) {
        this.showName = showName;
    }

    public String getCreateTimeString() {
        return TimeUtils.showDate(createTime);
    }

    public String getDurationString() {
        return TimeUtils.showDuration(duration);
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public ConversationType getConversationType() {
        return conversationType;
    }

    public void setConversationType(ConversationType conversationType) {
        this.conversationType = conversationType;
    }

    @Bindable
    public int getProgress() {
        return 50 - progress / 2;
    }

    public void setProgress(int progress) {
        if (progress == this.progress) return;
        this.progress = progress;
        notifyPropertyChanged(BR.progress);
    }
}