package com.omi.ui.activity.utils;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.omi.R;
import com.omi.databinding.ActivityImagesBrowseBinding;
import com.omi.ui.adapter.ImagesAdapter;
import com.omi.ui.base.BaseActivity;


/**
 * Created by SensYang on 2016/7/12 0012.
 */
public class ImageBrowseActivity extends BaseActivity {
    ActivityImagesBrowseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] imageUrls = getIntent().getStringArrayExtra("imageurls");
        if (imageUrls == null) {
            finish();
            return;
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_images_browse);
        if (imageUrls.length < 2) {
            binding.textView.setVisibility(View.GONE);
        } else {
            binding.textView.setText("1/" + imageUrls.length);
        }
        ImagesAdapter imagesAdapter = new ImagesAdapter(binding.textView, false);
        binding.setImageAdapter(imagesAdapter);
        binding.imagesViewPager.addOnPageChangeListener(imagesAdapter);
        imagesAdapter.addData(imageUrls);
        imagesAdapter.notifyDataSetChanged();
        int position = getIntent().getIntExtra("position", 0);
        binding.imagesViewPager.setCurrentItem(position);
    }
}