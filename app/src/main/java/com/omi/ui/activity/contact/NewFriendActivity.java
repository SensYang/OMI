package com.omi.ui.activity.contact;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.omi.R;
import com.omi.bean.account.ContactInvited;
import com.omi.database.LiteOrmDBUtil;
import com.omi.databinding.ActivityNewFriendBinding;
import com.omi.ui.activity.search.SearchActivity;
import com.omi.ui.activity.search.SearchTypeActivity;
import com.omi.ui.adapter.contact.NewFriendAdapter;
import com.omi.ui.base.BaseActivity;

/**
 * Created by SensYang on 2017/04/10 12:19
 */

public class NewFriendActivity extends BaseActivity {
    private ActivityNewFriendBinding binding;
    private NewFriendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_friend);
        adapter = new NewFriendAdapter();
        adapter.addContactInvited(LiteOrmDBUtil.getUserDBUtil().query(ContactInvited.class));
        binding.setAdapter(adapter);
        binding.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    /**
     * 进入搜索
     */
    public void goSearch(View view) {
        startActivity(new Intent(this, SearchActivity.class));
    }

    public void topLeftClick(View view) {
        finish();
    }

    public void topRightClick(View view) {
        startActivity(new Intent(this, SearchTypeActivity.class));
    }
}