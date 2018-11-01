package com.omi.bean.conversation;

import android.databinding.Bindable;
import android.support.annotation.NonNull;

import com.litesuits.orm.db.annotation.Column;
import com.omi.BR;
import com.omi.bean.base.BaseBean;
import com.omi.utils.TimeUtils;

import java.util.List;

/**
 * Created by SensYang on 2017/03/15 13:48
 */
public class Conversation extends BaseBean implements Comparable<Conversation> {
    private ConversationType conversationType;
    private List<String> headList;
    @Column("_from")
    private String from;
    private String fromName;
    private String messageDetial;
    private long messageCreateTime;
    private int unReadCount;
    private String conversationId;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public ConversationType getConversationType() {
        return conversationType;
    }

    public void setConversationType(ConversationType conversationType) {
        this.conversationType = conversationType;
    }

    @Bindable
    public List<String> getHeadList() {
        return headList;
    }

    public void setHeadList(List<String> headList) {
        this.headList = headList;
        notifyPropertyChanged(BR.headList);
    }

    @Bindable
    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
        notifyPropertyChanged(BR.fromName);
    }

    public String getMessageDetial() {
        return messageDetial;
    }

    public void setMessageDetial(String messageDetial) {
        this.messageDetial = messageDetial;
    }

    public long getMessageCreateTime() {
        return messageCreateTime;
    }

    public String getMessageCreateTimeString() {
        return TimeUtils.showDate(messageCreateTime);
    }

    public void setMessageCreateTime(long messageCreateTime) {
        this.messageCreateTime = messageCreateTime;
    }

    @Bindable
    public int getUnReadCount() {
        return unReadCount;
    }

    public String getUnReadCountString() {
        if (unReadCount < 100) return unReadCount + "";
        else return "99+";
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
        notifyPropertyChanged(BR.unReadCount);
    }

    @Override
    public int compareTo(@NonNull Conversation o) {
        if (messageCreateTime == o.messageCreateTime) return 0;
        else if (messageCreateTime > o.messageCreateTime) return 1;
        else return -1;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
