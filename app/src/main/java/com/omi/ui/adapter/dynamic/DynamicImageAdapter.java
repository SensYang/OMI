package com.omi.ui.adapter.dynamic;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.omi.R;
import com.omi.databinding.ItemPublishDynamicImgBinding;
import com.omi.ui.adapter.base.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by SensYang on 2017/04/20 17:50
 */

public class DynamicImageAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {
    private List<Uri> uriList = new ArrayList<>();
    private HashMap<Integer, Integer> positionMap = new HashMap<>();

    public void addUri(Uri uri) {
        uriList.add(uri);
    }

    public void remove(int position) {
        if (position < uriList.size()) uriList.remove(position);
    }

    public List<Uri> getUriList() {
        return uriList;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemPublishDynamicImgBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_publish_dynamic_img, parent, false);
        binding.setDynamicImageAdapter(this);
        return new ImageHolder(binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        holder.bindData(position);
        positionMap.put(position, position);
    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    class ImageHolder extends BaseRecyclerViewHolder<Integer> {
        ItemPublishDynamicImgBinding binding;

        public ImageHolder(ItemPublishDynamicImgBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(Integer position) {
            binding.setUri(uriList.get(position));
            binding.setPosition(position);
        }
    }

    public void onImageClick(int position) {
        int delAt = positionMap.get(position);
        for (int i = 1; i < getItemCount(); i++) {
            Integer oldP;
            if ((oldP = positionMap.get(position + i)) != null) {
                positionMap.put(position + i, oldP - 1);
            }
        }
        remove(delAt);
        notifyItemRemoved(delAt);
    }
}
