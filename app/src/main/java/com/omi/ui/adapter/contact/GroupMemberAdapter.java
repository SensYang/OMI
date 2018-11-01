package com.omi.ui.adapter.contact;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omi.R;
import com.omi.bean.User;
import com.omi.databinding.ItemGroupMemberBinding;
import com.omi.net.ApiByHttp;
import com.omi.ui.activity.user.SelfInfoActivity;
import com.omi.ui.activity.user.UserInfoActivity;
import com.omi.ui.adapter.base.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SensYang on 2017/04/26 21:22
 */

public class GroupMemberAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder<User.Data>> {
    private List<User.Data> userList = new ArrayList<>();

    public void addUser(User.Data user) {
        userList.add(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public BaseRecyclerViewHolder<User.Data> onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemGroupMemberBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_group_member, parent, false);
        binding.setGroupMemberAdapter(this);
        return new UserHolder(binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder<User.Data> holder, int position) {
        holder.bindData(userList.get(position));
    }

    class UserHolder extends BaseRecyclerViewHolder<User.Data> {
        ItemGroupMemberBinding binding;

        public UserHolder(ItemGroupMemberBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(User.Data data) {
            binding.setUser(data);
        }
    }

    public void onUserClick(View view, User.Data user) {
        if (user == null) return;
        String userid = user.getMember_name();
        if (userid == null) return;
        if (userid.equalsIgnoreCase(ApiByHttp.getInstance().getPhone())) {
            Intent intent = new Intent(view.getContext(), SelfInfoActivity.class);
            view.getContext().startActivity(intent);
        } else {
            Intent intent = new Intent(view.getContext(), UserInfoActivity.class);
            intent.putExtra("userid", userid);
            view.getContext().startActivity(intent);
        }
    }
}
