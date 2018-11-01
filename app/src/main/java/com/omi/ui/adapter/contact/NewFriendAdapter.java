package com.omi.ui.adapter.contact;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.omi.R;
import com.omi.bean.account.ContactInvited;
import com.omi.database.LiteOrmDBUtil;
import com.omi.databinding.ItemInvitedBinding;
import com.omi.ui.adapter.base.BaseRecyclerViewHolder;
import com.omi.utils.CommonUtils;
import com.omi.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by SensYang on 2017/04/10 13:28
 */

public class NewFriendAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder<ContactInvited>> {
    private List<ContactInvited> invitedList = new ArrayList<>();

    public void addContactInvited(Collection<ContactInvited> inviteds) {
        invitedList.addAll(inviteds);
    }

    @Override
    public int getItemCount() {
        return invitedList.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return -1;
            case 1:
                return -2;
            default:
                return 0;
        }
    }

    @Override
    public BaseRecyclerViewHolder<ContactInvited> onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case -1:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.include_search, parent, false));
            case -2:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.temp_new_friend, parent, false));
            default:
                ItemInvitedBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_invited, parent, false);
                binding.setAdapter(this);
                return new NewFriendHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder<ContactInvited> holder, int position) {
        if (position > 1) holder.bindData(invitedList.get(position - 2));
    }

    class ViewHolder extends BaseRecyclerViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object data) {

        }
    }

    class NewFriendHolder extends BaseRecyclerViewHolder<ContactInvited> {
        private ItemInvitedBinding binding;

        public NewFriendHolder(ItemInvitedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(ContactInvited data) {
            binding.setInvited(data);
        }
    }

    public void onAgreeClick(ContactInvited invited) {
        if (CommonUtils.isFastDoubleClick()) return;
        EMClient.getInstance().contactManager().asyncAcceptInvitation(invited.getUsername(), new AcceptCallBack(invited));
    }

    public void onDeclineClick(ContactInvited invited) {
        if (CommonUtils.isFastDoubleClick()) return;
        EMClient.getInstance().contactManager().asyncDeclineInvitation(invited.getUsername(), new DeclineCallBack(invited));
    }

    class AcceptCallBack implements EMCallBack {
        ContactInvited invited;

        public AcceptCallBack(ContactInvited invited) {
            this.invited = invited;
        }

        @Override
        public void onSuccess() {
            invited.setHasAdd(true);
            invited.setHasDecline(false);
            LiteOrmDBUtil.getUserDBUtil().update(invited);
        }

        @Override
        public void onError(int code, String error) {
            ToastUtil.showToast("网络错误，请稍后重试");
        }

        @Override
        public void onProgress(int progress, String status) {

        }
    }

    class DeclineCallBack implements EMCallBack {
        ContactInvited invited;

        public DeclineCallBack(ContactInvited invited) {
            this.invited = invited;
        }

        @Override
        public void onSuccess() {
            invited.setHasAdd(false);
            invited.setHasDecline(true);
            LiteOrmDBUtil.getUserDBUtil().update(invited);
        }

        @Override
        public void onError(int code, String error) {
            ToastUtil.showToast("网络错误，请稍后重试");
        }

        @Override
        public void onProgress(int progress, String status) {

        }
    }

}
