package com.omi.ui.adapter.chat;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.omi.R;
import com.omi.bean.User;
import com.omi.bean.chat.IMMessage;
import com.omi.bean.chat.IMMessageType;
import com.omi.databinding.ItemChatBigfaceOtherBinding;
import com.omi.databinding.ItemChatBigfaceSelfBinding;
import com.omi.databinding.ItemChatImageOtherBinding;
import com.omi.databinding.ItemChatImageSelfBinding;
import com.omi.databinding.ItemChatLocationOtherBinding;
import com.omi.databinding.ItemChatLocationSelfBinding;
import com.omi.databinding.ItemChatTextOtherBinding;
import com.omi.databinding.ItemChatTextSelfBinding;
import com.omi.databinding.ItemChatVideoOtherBinding;
import com.omi.databinding.ItemChatVideoSelfBinding;
import com.omi.databinding.ItemChatVoiceOtherBinding;
import com.omi.databinding.ItemChatVoiceSelfBinding;
import com.omi.ui.adapter.base.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by SensYang on 2017/03/15 18:28
 */
public class ChatAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder<IMMessage>> {
    private List<IMMessage> messageList = new ArrayList<>();
    private HashMap<String, User.Data> userMap = new HashMap<>();

    public HashMap<String, User.Data> getUserMap() {
        return userMap;
    }

    public void addNewMessage(IMMessage message) {
        messageList.add(message);
    }

    public void addNewMessage(Collection<IMMessage> message) {
        messageList.addAll(message);
    }

    public void addOldMessage(IMMessage message) {
        messageList.add(0, message);
    }

    public void addOldMessage(Collection<IMMessage> message) {
        messageList.addAll(0, message);
    }

    public int indexOf(IMMessage message) {
        return messageList.indexOf(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    //消息类型：文本，图片，视频，位置，语音，文件,透传消息
    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getMessageType().getValue();
    }

    public IMMessage getItem(int position) {
        return messageList.get(position);
    }

    //0.文字消息1.语音消息2.图片消息3.大表情消息4.位置消息5.视频消息
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        IMMessageType type = IMMessageType.valueOf(viewType);
        switch (type) {
            case TXT_OTHER: {
                ItemChatTextOtherBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_text_other, parent, false);
                return new OtherTextConversationViewHolder(binding);
            }
            case TXT_SELF: {
                ItemChatTextSelfBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_text_self, parent, false);
                return new SelfTextConversationViewHolder(binding);
            }
            case IMAGE_OTHER: {
                ItemChatImageOtherBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_image_other, parent, false);
                return new OtherImageConversationViewHolder(binding);
            }
            case IMAGE_SELF: {
                ItemChatImageSelfBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_image_self, parent, false);
                return new SelfImageConversationViewHolder(binding);
            }
            case VIDEO_OTHER: {
                ItemChatVideoOtherBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_video_other, parent, false);
                return new OtherVideoConversationViewHolder(binding);
            }
            case VIDEO_SELF: {
                ItemChatVideoSelfBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_video_self, parent, false);
                return new SelfVideoConversationViewHolder(binding);
            }
            case LOCATION_OTHER: {
                ItemChatLocationOtherBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_location_other, parent, false);
                return new OtherLocationConversationViewHolder(binding);
            }
            case LOCATION_SELF: {
                ItemChatLocationSelfBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_location_self, parent, false);
                return new SelfLocationConversationViewHolder(binding);
            }
            case VOICE_OTHER: {
                ItemChatVoiceOtherBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_voice_other, parent, false);
                return new OtherVoiceConversationViewHolder(binding);
            }
            case VOICE_SELF: {
                ItemChatVoiceSelfBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_voice_self, parent, false);
                return new SelfVoiceConversationViewHolder(binding);
            }
            case GIF_OTHER: {
                ItemChatBigfaceOtherBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_bigface_other, parent, false);
                return new OtherBigfaceConversationViewHolder(binding);
            }
            case GIF_SELF: {
                ItemChatBigfaceSelfBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_bigface_self, parent, false);
                return new SelfBigfaceConversationViewHolder(binding);
            }
        }
        ItemChatTextOtherBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_text_other, parent, false);
        return new OtherTextConversationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder<IMMessage> holder, int position) {
        IMMessage message = getItem(position);
        if (message.getFromHead() == null) {
            User.Data user;
            if ((user = userMap.get(message.getFrom())) != null) {
                message.setFromHead(user.getMember_avatar());
            }
        }
        holder.bindData(message);
    }

    class OtherTextConversationViewHolder extends BaseRecyclerViewHolder<IMMessage> {
        private ItemChatTextOtherBinding binding;

        public OtherTextConversationViewHolder(ItemChatTextOtherBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(IMMessage data) {
            binding.setMessage(data);
        }
    }

    class OtherVoiceConversationViewHolder extends BaseRecyclerViewHolder<IMMessage> {
        private ItemChatVoiceOtherBinding binding;

        public OtherVoiceConversationViewHolder(ItemChatVoiceOtherBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(IMMessage data) {
            binding.setMessage(data);
        }
    }

    class OtherImageConversationViewHolder extends BaseRecyclerViewHolder<IMMessage> {
        private ItemChatImageOtherBinding binding;

        public OtherImageConversationViewHolder(ItemChatImageOtherBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(IMMessage data) {
            binding.setMessage(data);
        }
    }

    class OtherBigfaceConversationViewHolder extends BaseRecyclerViewHolder<IMMessage> {
        private ItemChatBigfaceOtherBinding binding;

        public OtherBigfaceConversationViewHolder(ItemChatBigfaceOtherBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(IMMessage data) {
            binding.setMessage(data);
        }
    }

    class OtherLocationConversationViewHolder extends BaseRecyclerViewHolder<IMMessage> {
        private ItemChatLocationOtherBinding binding;

        public OtherLocationConversationViewHolder(ItemChatLocationOtherBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(IMMessage data) {
            binding.setMessage(data);
        }
    }

    class OtherVideoConversationViewHolder extends BaseRecyclerViewHolder<IMMessage> {
        private ItemChatVideoOtherBinding binding;

        public OtherVideoConversationViewHolder(ItemChatVideoOtherBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(IMMessage data) {
            binding.setMessage(data);
        }
    }

    class SelfTextConversationViewHolder extends BaseRecyclerViewHolder<IMMessage> {
        private ItemChatTextSelfBinding binding;

        public SelfTextConversationViewHolder(ItemChatTextSelfBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(IMMessage data) {
            binding.setMessage(data);
        }
    }

    class SelfVoiceConversationViewHolder extends BaseRecyclerViewHolder<IMMessage> {
        private ItemChatVoiceSelfBinding binding;

        public SelfVoiceConversationViewHolder(ItemChatVoiceSelfBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(IMMessage data) {
            binding.setMessage(data);
        }
    }

    class SelfImageConversationViewHolder extends BaseRecyclerViewHolder<IMMessage> {
        private ItemChatImageSelfBinding binding;

        public SelfImageConversationViewHolder(ItemChatImageSelfBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(IMMessage data) {
            binding.setMessage(data);
        }
    }

    class SelfBigfaceConversationViewHolder extends BaseRecyclerViewHolder<IMMessage> {
        private ItemChatBigfaceSelfBinding binding;

        public SelfBigfaceConversationViewHolder(ItemChatBigfaceSelfBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(IMMessage data) {
            binding.setMessage(data);
        }
    }

    class SelfLocationConversationViewHolder extends BaseRecyclerViewHolder<IMMessage> {
        private ItemChatLocationSelfBinding binding;

        public SelfLocationConversationViewHolder(ItemChatLocationSelfBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(IMMessage data) {
            binding.setMessage(data);
        }
    }

    class SelfVideoConversationViewHolder extends BaseRecyclerViewHolder<IMMessage> {
        private ItemChatVideoSelfBinding binding;

        public SelfVideoConversationViewHolder(ItemChatVideoSelfBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(IMMessage data) {
            binding.setMessage(data);
        }
    }

}
