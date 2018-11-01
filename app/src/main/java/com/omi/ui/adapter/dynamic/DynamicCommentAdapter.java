package com.omi.ui.adapter.dynamic;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.omi.R;
import com.omi.bean.dynamic.DynamicComments;
import com.omi.databinding.ItemDynamicCommentBinding;
import com.omi.ui.adapter.base.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by SensYang on 2017/04/26 10:15
 */

public class DynamicCommentAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {
    List<DynamicComments.Data> pinglun;

    public void setPinglun(List<DynamicComments.Data> pinglun) {
        this.pinglun = pinglun;
        notifyDataSetChanged();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemDynamicCommentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_dynamic_comment, parent, false);
        return new DynamicCommentHolder(binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        holder.bindData(pinglun.get(position));
    }

    class DynamicCommentHolder extends BaseRecyclerViewHolder<DynamicComments.Data> {
        ItemDynamicCommentBinding binding;

        public DynamicCommentHolder(ItemDynamicCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(DynamicComments.Data data) {
            binding.setComment(data);
        }
    }

    @Override
    public int getItemCount() {
        return pinglun == null ? 0 : pinglun.size();
    }
}
