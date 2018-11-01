package com.omi.ui.adapter.contact;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omi.R;
import com.omi.bean.User;
import com.omi.bean.base.ComparableBean;
import com.omi.databinding.ItemCreatGroupBinding;
import com.omi.databinding.TopSelectPeopleBinding;
import com.omi.ui.activity.contact.MyGroupActivity;
import com.omi.ui.adapter.base.BaseRecyclerViewHolder;
import com.omi.ui.widget.indexlist.IndexerAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by SensYang on 2017/04/26 19:36
 */

public class CreatGroupAdapter extends IndexerAdapter<BaseRecyclerViewHolder<User.Data>> {
    private List<User.Data> userList = new ArrayList<>();

    public List<User.Data> getUserList() {
        return userList;
    }

    public void addUser(Collection<User.Data> users) {
        userList.addAll(users);
    }

    public void addUser(User.Data user) {
        userList.add(user);
    }

    public void sort() {
        Collections.sort(userList);
        initSections();
    }

    @Override
    public BaseRecyclerViewHolder<User.Data> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            TopSelectPeopleBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.top_select_people, parent, false);
            return new SelectHolder(binding);
        } else {
            ItemCreatGroupBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_creat_group, parent, false);
            binding.setAdapter(this);
            return new UserHolder(binding);
        }
    }


    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder<User.Data> holder, int position, String header) {
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

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 0;
        else return 1;
    }

    @Override
    public int getItemCount() {
        return userList.size() + 1;
    }

    class SelectHolder extends BaseRecyclerViewHolder<User.Data> {
        private TopSelectPeopleBinding binding;

        public SelectHolder(TopSelectPeopleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(User.Data data) {
        }
    }

    class UserHolder extends BaseRecyclerViewHolder<User.Data> {
        private ItemCreatGroupBinding binding;

        public UserHolder(ItemCreatGroupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(User.Data data) {
            binding.setUser(data);
        }
    }

    public void onSelectClick(View view) {
        Intent intent = new Intent(view.getContext(), MyGroupActivity.class);
        view.getContext().startActivity(intent);
        if (view.getContext() instanceof Activity) {
            ((Activity) view.getContext()).finish();
        }
    }

    public void onUserClick(User.Data user) {
        user.setSelect(!user.isSelect());
    }
}
