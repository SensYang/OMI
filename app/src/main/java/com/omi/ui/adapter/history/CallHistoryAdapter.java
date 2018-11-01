package com.omi.ui.adapter.history;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.omi.R;
import com.omi.bean.call.CallHistory;
import com.omi.databinding.ItemCallHistoryBinding;
import com.omi.ui.adapter.base.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by SensYang on 2017/04/26 18:04
 */

public class CallHistoryAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder<CallHistory>> {
    private List<CallHistory> historyList = new ArrayList<>();

    public void addCallHistory(CallHistory history) {
        historyList.add(history);
    }

    public void addCallHistory(Collection<CallHistory> histories) {
        if (histories == null) return;
        historyList.addAll(histories);
    }

    public void clear() {
        historyList.clear();
    }

    @Override
    public BaseRecyclerViewHolder<CallHistory> onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCallHistoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_call_history, parent, false);
        return new HistoryHolder(binding);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder<CallHistory> holder, int position) {
        holder.bindData(historyList.get(position));
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    class HistoryHolder extends BaseRecyclerViewHolder<CallHistory> {
        private ItemCallHistoryBinding binding;

        public HistoryHolder(ItemCallHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bindData(CallHistory data) {
            binding.setHistory(data);
        }
    }
}
