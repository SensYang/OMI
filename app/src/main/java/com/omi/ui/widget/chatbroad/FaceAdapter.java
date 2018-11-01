package com.omi.ui.widget.chatbroad;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.omi.R;
import com.omi.bean.chat.Emoji;
import com.omi.databinding.ItemEmojiBinding;
import com.omi.ui.adapter.base.BaseRecyclerViewHolder;
import com.omi.ui.widget.ChatControlView;

import java.util.List;

/**
 * Created by SensYang on 2017/03/22 18:30
 */

public class FaceAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {
    private List<Emoji> emojiList;
    private ChatControlView controlView;

    public void setEmojiList(List<Emoji> emojiList) {
        this.emojiList = emojiList;
    }

    public void setControlView(ChatControlView controlView) {
        this.controlView = controlView;
    }

    @Override
    public BaseRecyclerViewHolder<Emoji> onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemEmojiBinding faceBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_emoji, parent, false);
        faceBinding.setFaceAdapter(this);
        faceBinding.setChatControlView(controlView);
        return new EmojiViewHolder(faceBinding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        holder.bindData(emojiList.get(position));
    }

    @Override
    public int getItemCount() {
        return emojiList.size();
    }

    class EmojiViewHolder extends BaseRecyclerViewHolder<Emoji> {
        ItemEmojiBinding binding;

        public EmojiViewHolder(ItemEmojiBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            if (binding.getChatControlView() == null) {
                binding.setChatControlView(controlView);
            }
        }

        @Override
        public void bindData(Emoji data) {
            if (binding.getChatControlView() == null) {
                binding.setChatControlView(controlView);
            }
            binding.setEmoji(data);
        }
    }
}