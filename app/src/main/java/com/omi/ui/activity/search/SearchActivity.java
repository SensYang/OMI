package com.omi.ui.activity.search;

import android.content.Intent;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;

import com.omi.R;
import com.omi.bean.account.Contact;
import com.omi.databinding.ActivitySearchBinding;
import com.omi.ui.activity.user.UserInfoActivity;
import com.omi.ui.adapter.search.SearchAdapter;
import com.omi.ui.base.BaseActivity;
import com.omi.utils.ContactsUtil;
import com.omi.utils.IMEUtil;

/**
 * Created by SensYang on 2017/04/07 14:23
 */

public class SearchActivity extends BaseActivity {
    private ActivitySearchBinding binding;
    private SearchAdapter adapter;
    private String searchKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        adapter = new SearchAdapter(this);
        binding.setAdapter(adapter);
        binding.setSearchActivity(this);
        binding.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    public void onFinishClick(View view) {
        finish();
    }

    @Bindable
    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        if (searchKey.equalsIgnoreCase(this.searchKey)) return;
        this.searchKey = searchKey;
        adapter.clear();
        if (ContactsUtil.mabyMobileNO(searchKey)) {
            for (Contact contact : ContactsUtil.getContact()) {
                if (contact.getPhone().startsWith(searchKey)) {
                    adapter.addContact(contact);
                }
            }
            adapter.setShowTop(true);
        } else {
            adapter.setShowTop(false);
        }
        adapter.notifyDataSetChanged();
    }

    public View.OnKeyListener getListener() {
        return listener;
    }

    private View.OnKeyListener listener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                IMEUtil.getInstance().hideSoftKeyboard(binding.getRoot());
                onSearchClick(binding.getRoot());
            }
            return false;
        }
    };

    public void onSearchClick(View view) {
        Intent intent = new Intent(this, UserInfoActivity.class);
        intent.putExtra("userid", searchKey);
        startActivity(intent);
    }

    public void onContactClick(Contact contact) {
        Intent intent = new Intent(this, UserInfoActivity.class);
        intent.putExtra("userid", contact.getPhone());
        startActivity(intent);
    }
}