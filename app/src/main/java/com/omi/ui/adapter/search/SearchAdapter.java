package com.omi.ui.adapter.search;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.omi.R;
import com.omi.bean.User;
import com.omi.bean.account.Contact;
import com.omi.database.LiteOrmDBUtil;
import com.omi.databinding.ItemSearchBinding;
import com.omi.databinding.TopSearchBinding;
import com.omi.ui.activity.search.SearchActivity;
import com.omi.ui.adapter.base.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by SensYang on 2017/04/07 14:47
 */
public class SearchAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {
    private ArrayList<Contact> contactList = new ArrayList<>();
    private SearchActivity searchActivity;
    private boolean showTop = false;

    public SearchAdapter(SearchActivity searchActivity) {
        this.searchActivity = searchActivity;
    }

    public void addContact(Contact contact) {
        contactList.add(contact);
    }

    public void addContact(Collection<Contact> contacts) {
        contactList.addAll(contacts);
    }

    public void addContact(Contact... contacts) {
        for (Contact contact : contacts) {
            contactList.add(contact);
        }
    }

    public void clear() {
        contactList.clear();
    }

    public void setShowTop(boolean showTop) {
        this.showTop = showTop;
    }

    @Override
    public int getItemCount() {
        int count = contactList.size();
        if (showTop) count += 1;
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return -1;
        else return 0;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case -1: {
                TopSearchBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.top_search, parent, false);
                return new TopHolder(binding);
            }
            case 0: {
                ItemSearchBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_search, parent, false);
                binding.setSearchActivity(searchActivity);
                return new ContactHolder(binding);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        if (position == 0) {
            holder.bindData(null);
            return;
        }
        holder.bindData(contactList.get(position - 1));
    }

    class TopHolder extends BaseRecyclerViewHolder {
        private TopSearchBinding binding;

        public TopHolder(TopSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(Object data) {
            binding.setSearchActivity(searchActivity);
        }
    }

    class ContactHolder extends BaseRecyclerViewHolder<Contact> {
        private ItemSearchBinding binding;

        public ContactHolder(ItemSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(Contact data) {
            binding.setContact(data);
            User.Data user = userMap.get(data.getPhone());
            if (user == null) {
                user = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(User.Data.class, "member_name", data.getPhone());
                if (user != null) {
                    userMap.put(data.getPhone(), user);
                }
            }
            binding.setUser(user);
        }
    }

    private HashMap<String, User.Data> userMap = new HashMap<>();
}