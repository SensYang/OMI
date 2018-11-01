package com.omi.ui.adapter.near;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omi.R;
import com.omi.bean.User;
import com.omi.databinding.ItemNearPeopleBinding;
import com.omi.ui.activity.user.UserInfoActivity;
import com.omi.ui.adapter.base.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by SensYang on 2017/04/26 15:12
 */

public class NearPeopleAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder<User.Data>> {
    private List<User.Data> userList = new ArrayList<>();

    public void addUser(Collection<User.Data> list) {
        if (list == null) return;
        userList.addAll(list);
    }

    public void clear() {
        userList.clear();
    }

    @Override
    public BaseRecyclerViewHolder<User.Data> onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemNearPeopleBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_near_people, parent, false);
        binding.setNearPeopleAdapter(this);
        return new UserHolder(binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder<User.Data> holder, int position) {
        holder.bindData(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserHolder extends BaseRecyclerViewHolder<User.Data> {
        private ItemNearPeopleBinding binding;

        public UserHolder(ItemNearPeopleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(User.Data data) {
            binding.setUser(data);
        }
    }

    public void onUserClick(View view, User.Data data) {
        Intent intent = new Intent(view.getContext(), UserInfoActivity.class);
        intent.putExtra("userid", data.getMember_name());
        view.getContext().startActivity(intent);
    }
}
