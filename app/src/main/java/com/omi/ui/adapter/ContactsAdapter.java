package com.omi.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omi.R;
import com.omi.bean.account.Contact;
import com.omi.bean.base.ComparableBean;
import com.omi.databinding.ItemContactsBinding;
import com.omi.databinding.ItemGroupBinding;
import com.omi.databinding.ItemNewFriendBinding;
import com.omi.databinding.ItemOmiFriendBinding;
import com.omi.net.ApiByHttp;
import com.omi.net.JsonCallback;
import com.omi.ui.activity.contact.MyGroupActivity;
import com.omi.ui.activity.contact.NewFriendActivity;
import com.omi.ui.activity.main.OmiFriendActivity;
import com.omi.ui.activity.phone.CallingActivity;
import com.omi.ui.adapter.base.BaseRecyclerViewHolder;
import com.omi.ui.widget.indexlist.IndexerAdapter;
import com.omi.ui.widget.listener.OnAlterClickListener;
import com.omi.ui.widget.window.WaringDialog;
import com.omi.utils.ToastUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by SensYang on 2017/03/14 15:55
 */
public class ContactsAdapter extends IndexerAdapter<BaseRecyclerViewHolder> {
    private List<Contact> contactList = new ArrayList<>();
    private boolean canShow = false;
    private ObservableBoolean hasNewFriend = new ObservableBoolean(false);
    private ObservableBoolean hasNewGroup = new ObservableBoolean(false);

    private Context context;
    private WaringDialog waringDialog;
    private int otherCount = 4;

    public ContactsAdapter() {
    }

    public void setHasNewFriend(boolean hasNewFriend) {
        this.hasNewFriend.set(hasNewFriend);
    }

    public void setHasNewGroup(boolean hasNewFriend) {
        this.hasNewGroup.set(hasNewFriend);
    }

    public void addContact(Collection<Contact> contacts) {
        contactList.addAll(contacts);
        canShow = false;
    }

    public void sort() {
        try {
            Collections.sort(contactList);
            initSections();
            canShow = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (canShow) return contactList.size() + otherCount;
        else return otherCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < otherCount) {
            return -position - 1;
        } else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position, String header) {
        if (position > otherCount - 1) holder.bindData(contactList.get(position - otherCount));
    }

    @Override
    public int getComparableCount() {
        return contactList.size();
    }

    @Override
    public int getComparableItemDiff() {
        return otherCount;
    }

    @Override
    public ComparableBean getComparableItem(int position) {
        return contactList.get(position);
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        switch (viewType) {
            case -1:
                return new SearchHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.include_search, parent, false));
            case -2: {
                ItemGroupBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_group, parent, false);
                binding.setHasNewGroup(hasNewGroup);
                binding.setContactsAdapter(this);
                return new GroupHolder(binding.getRoot());
            }
            case -3: {
                ItemNewFriendBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_new_friend, parent, false);
                binding.setHasNewFriend(hasNewFriend);
                binding.setContactsAdapter(this);
                return new FriendHolder(binding.getRoot());
            }
            case -4: {
                ItemOmiFriendBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_omi_friend, parent, false);
                binding.setContactsAdapter(this);
                return new OmiFriendHolder(binding.getRoot());
            }
        }
        ItemContactsBinding contactsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_contacts, parent, false);
        contactsBinding.setContactsAdapter(this);
        return new ContactsHolder(contactsBinding);
    }

    /**
     * 点击item
     */
    public void onContactClick(Contact contact) {
        if (waringDialog == null) {
            waringDialog = new WaringDialog(context);
            waringDialog.setOnAlterClickListener(new OnAlterClickListener() {

                @Override
                public void onCancelClick() {

                }

                @Override
                public void onConfirmClick() {
                    Contact contact = (Contact) waringDialog.getTag();
                    if (contact != null) {
                        phone = contact.getPhone();
                        callName = contact.getContact_name();
                        ApiByHttp.getInstance().callPhone(contact.getPhone(), callPhoneCallback);
                    }
                }
            });
        }
        waringDialog.setTitleText("是否呼叫 " + contact.getContact_name() + " ?\n" + contact.getPhone());
        waringDialog.setTag(contact);
        waringDialog.show();
    }

    private String phone;
    private String callName;
    private JsonCallback callPhoneCallback = new JsonCallback() {
        @Override
        public void onSucceed(int what, JSONObject response) throws Exception {
            if (response.getInt("result") == 200) {
                Intent intent = new Intent(context, CallingActivity.class);
                intent.putExtra("name", phone);
                intent.putExtra("phone", phone);
                context.startActivity(intent);
            } else {
                ToastUtil.showToast(response.getString("msg"));
            }
        }
    };


    class SearchHolder extends BaseRecyclerViewHolder {

        public SearchHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object data) {

        }
    }

    class GroupHolder extends BaseRecyclerViewHolder {

        public GroupHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object data) {

        }
    }

    class FriendHolder extends BaseRecyclerViewHolder {

        public FriendHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object data) {

        }
    }

    class OmiFriendHolder extends BaseRecyclerViewHolder {

        public OmiFriendHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object data) {

        }
    }

    class ContactsHolder extends BaseRecyclerViewHolder<Contact> {
        ItemContactsBinding contactsBinding;

        public ContactsHolder(ItemContactsBinding contactsBinding) {
            super(contactsBinding.getRoot());
            this.contactsBinding = contactsBinding;
        }

        @Override
        public void bindData(Contact contact) {
            contactsBinding.setContact(contact);
        }
    }

    public void onGroupClick(View view) {
        setHasNewGroup(false);
        view.getContext().startActivity(new Intent(view.getContext(), MyGroupActivity.class));
    }

    public void onNewFriendClick(View view) {
        setHasNewFriend(false);
        view.getContext().startActivity(new Intent(view.getContext(), NewFriendActivity.class));
    }

    public void onOmiClick(View view) {
        view.getContext().startActivity(new Intent(view.getContext(), OmiFriendActivity.class));
    }
}