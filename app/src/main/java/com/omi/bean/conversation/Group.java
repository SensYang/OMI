package com.omi.bean.conversation;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.hyphenate.chat.EMGroup;
import com.omi.BR;
import com.omi.net.ApiByHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SensYang on 2017/04/26 18:58
 */

public class Group extends BaseObservable {
    private String groupId;
    private String groupName;
    private List<String> groupHeads;
    private int memberCount;
    private String owner;
    private String description;
    private boolean msgBlocked;
    private boolean isSelfGroup;

    public void setGroup(EMGroup group) {
        groupId = group.getGroupId();
        groupName = group.getGroupName();
        memberCount = group.getMemberCount();
        owner = group.getOwner();
        description = group.getDescription();
        groupHeads = new ArrayList<>();
        msgBlocked = group.isMsgBlocked();
        isSelfGroup = ApiByHttp.getInstance().getUser().getMember_name().equalsIgnoreCase(owner);
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Bindable
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        if (groupName != null && groupName.equalsIgnoreCase(this.groupName)) return;
        this.groupName = groupName;
        notifyPropertyChanged(BR.groupName);
    }

    @Bindable
    public List<String> getGroupHeads() {
        return groupHeads;
    }

    public void setGroupHeads(List<String> groupHeads) {
        this.groupHeads = groupHeads;
        notifyPropertyChanged(BR.groupHeads);
    }

    @Bindable
    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        if (memberCount == this.memberCount) return;
        this.memberCount = memberCount;
        notifyPropertyChanged(BR.memberCount);
    }

    @Bindable
    public String getOwner() {
        return owner;
    }

    public void setOwner(String ownerId) {
        if (owner != null && ownerId.equalsIgnoreCase(this.owner)) return;
        this.owner = ownerId;
        notifyPropertyChanged(BR.owner);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null && description.equalsIgnoreCase(this.description)) return;
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public boolean isMsgBlocked() {
        return msgBlocked;
    }

    public void setMsgBlocked(boolean msgBlocked) {
        if (msgBlocked == this.msgBlocked) return;
        this.msgBlocked = msgBlocked;
        notifyPropertyChanged(BR.msgBlocked);
    }

    public boolean isSelfGroup() {
        return isSelfGroup;
    }

    public void setSelfGroup(boolean selfGroup) {
        isSelfGroup = selfGroup;
    }
}
