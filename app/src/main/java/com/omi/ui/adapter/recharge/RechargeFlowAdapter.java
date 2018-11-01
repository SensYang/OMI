package com.omi.ui.adapter.recharge;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableInt;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.omi.R;
import com.omi.bean.recharge.Flow;
import com.omi.databinding.ItemRechargeFlowBinding;
import com.omi.ui.adapter.base.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by SensYang on 2017/04/28 18:25
 */

public class RechargeFlowAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder<Integer>> {
    private List<Flow.Data> flowList = new ArrayList<>();
    private ObservableInt selectAt = new ObservableInt(-1);

    public int getSelectAt() {
        return selectAt.get();
    }

    public void setSelectAt(int selectAt) {
        if (selectAt == this.selectAt.get()) return;
        this.selectAt.set(selectAt);
    }

    public void addFlow(Collection<Flow.Data> flows) {
        if (flows == null) return;
        flowList.addAll(flows);
    }

    public void clear() {
        flowList.clear();
    }

    public Flow.Data getSelectFlow() {
        int select = getSelectAt();
        if (select > -1 && select < flowList.size()) {
            return flowList.get(select);
        }
        return null;
    }

    @Override
    public BaseRecyclerViewHolder<Integer> onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemRechargeFlowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_recharge_flow, parent, false);
        binding.setSelectAt(selectAt);
        binding.setRechargeFlowAdapter(this);
        return new FlowHolder(binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder<Integer> holder, int position) {
        holder.bindData(position);
    }

    class FlowHolder extends BaseRecyclerViewHolder<Integer> {
        ItemRechargeFlowBinding binding;

        public FlowHolder(ItemRechargeFlowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(Integer position) {
            binding.setPosition(position);
            binding.setFlow(flowList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return flowList.size();
    }

    public void onFlowClick(int pointAt) {
        setSelectAt(pointAt);
    }
}
