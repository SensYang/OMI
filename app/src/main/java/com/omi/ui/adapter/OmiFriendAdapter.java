package com.omi.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omi.R;
import com.omi.bean.User;
import com.omi.bean.base.ComparableBean;
import com.omi.databinding.ItemFriendsBinding;
import com.omi.ui.activity.user.UserInfoActivity;
import com.omi.ui.adapter.base.BaseRecyclerViewHolder;
import com.omi.ui.widget.indexlist.IndexerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by SensYang on 2017/04/20 15:14
 */

public class OmiFriendAdapter extends IndexerAdapter<BaseRecyclerViewHolder> {
    private List<User.Data> userList = new ArrayList<>();
    private Context context;

    public void addUser(User.Data user) {
        userList.add(user);
    }

    public void sort() {
        Collections.sort(userList);
        initSections();
    }

    public void remove(String userName) {
        for (User.Data data : userList) {
            if (data.getMember_name().equalsIgnoreCase(userName)) {
                userList.remove(data);
                return;
            }
        }
    }

    @Override
    public int getItemCount() {
        return userList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return -1;
        else return 0;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) context = parent.getContext();
        if (viewType == -1) {
            return new SearchHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.include_search, parent, false));
        } else {
            ItemFriendsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_friends, parent, false);
            binding.setAdapter(this);
            return new UserHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position, String header) {
        if (position != 0) holder.bindData(userList.get(position - 1));
    }

    @Override
    public ComparableBean getComparableItem(int position) {
        return userList.get(position);
    }

    @Override
    public int getComparableCount() {
        return userList.size();
    }

    @Override
    public int getComparableItemDiff() {
        return 1;
    }

    class SearchHolder extends BaseRecyclerViewHolder {

        public SearchHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object data) {

        }
    }

    class UserHolder extends BaseRecyclerViewHolder<User.Data> {
        ItemFriendsBinding binding;

        public UserHolder(ItemFriendsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(User.Data data) {
            binding.setUser(data);
        }
    }

    public void onUserClick(User.Data user) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra("userid", user.getMember_name());
        context.startActivity(intent);
    }
}
