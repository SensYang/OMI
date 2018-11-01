package com.omi.ui.widget.indexlist;

import android.support.v7.widget.RecyclerView;

import com.omi.bean.base.ComparableBean;

import java.util.HashMap;

/**
 * Created by DELL on 2016/4/2.
 */
public abstract class IndexerAdapter<H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H> {
    private HashMap<Character, Integer> sectionToPosition = new HashMap<>();

    @Override
    @Deprecated
    public void onBindViewHolder(H holder, int position) {
        int diff = getComparableItemDiff();
        String headerString = null;
        if (position > diff - 1) {
            ComparableBean currentData = getComparableItem(position - diff);
            if (currentData != null) {
                char header = currentData.getHeader();
                ComparableBean lastData = null;
                if (position - diff > 0) lastData = getComparableItem(position - diff - 1);
                if (lastData == null) {
                    headerString = String.valueOf(header);
                    currentData.setTopOne(true);
                } else if (header != lastData.getHeader()) {
                    headerString = String.valueOf(header);
                    currentData.setTopOne(true);
                } else {
                    currentData.setTopOne(false);
                }
            }
        }
        onBindViewHolder(holder, position, headerString);
    }

    public abstract void onBindViewHolder(H holder, int position, String header);

    public void initSections() {
        sectionToPosition.clear();
        sectionToPosition.put('↑', 0);
        char lastHeader = 0;
        int count = getComparableCount();
        int diff = getComparableItemDiff();
        for (int i = 0; i < count; i++) {
            ComparableBean comparableBean = getComparableItem(i);
            if (comparableBean == null) continue;
            char header = comparableBean.getHeader();
            if (lastHeader != header) {
                lastHeader = header;
                sectionToPosition.put(header, i + diff);
            }
        }
    }

    public abstract ComparableBean getComparableItem(int position);

    public abstract int getComparableCount();

    public abstract int getComparableItemDiff();

    public int getPositionForSection(char headerChar) {
        if (sectionToPosition.size() == 0) initSections();
        Integer value = sectionToPosition.get(headerChar);
        if (value != null) return value;
        return -1;
    }
}
