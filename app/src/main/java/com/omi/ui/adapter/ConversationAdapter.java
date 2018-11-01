package com.omi.ui.adapter;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omi.R;
import com.omi.bean.conversation.Conversation;
import com.omi.bean.conversation.ConversationType;
import com.omi.databinding.ItemConversationGroupBinding;
import com.omi.databinding.ItemConversationSingleBinding;
import com.omi.ui.activity.chat.ChatActivity;
import com.omi.ui.adapter.base.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by SensYang on 2017/03/15 11:30
 */

public class ConversationAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {
    private List<Conversation> conversationList = new ArrayList<>();
    private Conversation chatingConversation;

    public void addConversation(Conversation conversation) {
        if (conversation == null) return;
        for (Conversation conversation1 : conversationList) {
            if (conversation1.getFrom().equalsIgnoreCase(conversation.getFrom())) {
                return;
            }
        }
        conversationList.add(0, conversation);
    }

    public void clear() {
        conversationList.clear();
    }

    public void sort() {
        Collections.sort(conversationList);
    }

    public List<Conversation> getConversationList() {
        return conversationList;
    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return conversationList.get(position).getConversationType().ordinal();
    }

    public void onResume() {
        if (chatingConversation != null) {
            chatingConversation.setUnReadCount(0);
        }
        chatingConversation = null;
    }

    public Conversation getChatingConversation() {
        return chatingConversation;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConversationType type = ConversationType.values()[viewType];
        switch (type) {
            case SINGLE_CHAT://单人聊天
                ItemConversationSingleBinding singleBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_conversation_single, parent, false);
                singleBinding.setConversationAdapter(this);
                return new SingleViewHolder(singleBinding);
            case GROUP_CHAT://群聊
                ItemConversationGroupBinding groupBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_conversation_group, parent, false);
                groupBinding.setConversationAdapter(this);
                return new GroupViewHolder(groupBinding);
        }
        ItemConversationSingleBinding singleBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_conversation_single, parent, false);
        singleBinding.setConversationAdapter(this);
        return new SingleViewHolder(singleBinding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        holder.bindData(conversationList.get(position));
    }

    public void onItemClick(View view) {
        chatingConversation = (Conversation) view.getTag();
        Intent intent = new Intent(view.getContext(), ChatActivity.class);
        intent.putExtra("chatWith", chatingConversation.getFrom());
        intent.putExtra("conversationType", chatingConversation.getConversationType());
        view.getContext().startActivity(intent);
    }

    class SingleViewHolder extends BaseRecyclerViewHolder<Conversation> {
        ItemConversationSingleBinding binding;

        public SingleViewHolder(ItemConversationSingleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(Conversation data) {
            binding.setConversation(data);
        }
    }

    class GroupViewHolder extends BaseRecyclerViewHolder<Conversation> {
        ItemConversationGroupBinding binding;

        public GroupViewHolder(ItemConversationGroupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(Conversation data) {
            binding.setConversation(data);
        }
    }
}
