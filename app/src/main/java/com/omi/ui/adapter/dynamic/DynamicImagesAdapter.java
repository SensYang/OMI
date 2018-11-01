package com.omi.ui.adapter.dynamic;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omi.R;
import com.omi.bean.dynamic.Dynamic;
import com.omi.bean.dynamic.DynamicImages;
import com.omi.databinding.ItemDynamicImgBinding;
import com.omi.ui.adapter.base.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SensYang on 2017/04/21 17:14
 */

public class DynamicImagesAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {
    private List<String> urlList = new ArrayList<>();
    private Dynamic.Data dynamic;
    private DynamicAdapter adapter;

    public DynamicImagesAdapter(DynamicAdapter adapter) {
        this.adapter = adapter;
    }

    private void addUrl(String uri) {
        urlList.add(uri);
    }

    private void clear() {
        urlList.clear();
    }

    public void setDynamic(Dynamic.Data dynamic) {
        this.dynamic = dynamic;
        clear();
        for (DynamicImages.Data data : dynamic.getImages()) {
            addUrl(data.getImage_name());
        }
        notifyDataSetChanged();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemDynamicImgBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_dynamic_img, parent, false);
        binding.setDynamicImagesAdapter(this);
        return new ImageHolder(binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        holder.bindData(urlList.get(position));
        if (holder instanceof ImageHolder) {
            ((ImageHolder) holder).setPosition(position);
        }
    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }

    class ImageHolder extends BaseRecyclerViewHolder<String> {
        ItemDynamicImgBinding binding;

        public ImageHolder(ItemDynamicImgBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(String data) {
            binding.setUrl(data);
        }

        public void setPosition(int position) {
            binding.setPostion(position);
        }
    }

    public void onImageClick(View view,int position) {
        adapter.onImageClick(view,dynamic, position);
    }

}
