package com.omi.ui.adapter.contact;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omi.R;
import com.omi.bean.conversation.ConversationType;
import com.omi.bean.conversation.Group;
import com.omi.databinding.ItemMyGroupBinding;
import com.omi.ui.activity.chat.ChatActivity;
import com.omi.ui.adapter.base.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by SensYang on 2017/04/26 19:14
 */

public class GroupAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder<Group>> {
    private List<Group> groupList = new ArrayList<>();

    public void addGroup(Group group) {
        groupList.add(group);
    }

    public void addGroup(Collection<Group> groups) {
        groupList.addAll(groups);
    }

    @Override
    public BaseRecyclerViewHolder<Group> onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMyGroupBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_my_group, parent, false);
        binding.setGroupAdapter(this);
        return new GroupHolder(binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder<Group> holder, int position) {
        holder.bindData(groupList.get(position));
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    class GroupHolder extends BaseRecyclerViewHolder<Group> {
        private ItemMyGroupBinding binding;

        public GroupHolder(ItemMyGroupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(Group data) {
            binding.setGroup(data);
        }
    }

    public void onGroupClick(View view, Group group) {
        Intent intent = new Intent(view.getContext(), ChatActivity.class);
        intent.putExtra("conversationType", ConversationType.GROUP_CHAT);
        intent.putExtra("chatWith", group.getGroupId());
        view.getContext().startActivity(intent);
        if (view.getContext() instanceof Activity) {
            ((Activity) view.getContext()).finish();
        }
    }
}
