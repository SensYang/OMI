package com.omi.ui.widget;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ListAdapter;

/**
 * Created by SensYang on 2016/6/23 0023.
 */
public class PullableUtil {
    /**
     * 判断是否可以下拉，如果不需要下拉功能可以直接return false
     *
     * @return true如果可以下拉否则返回false
     */
    public static boolean canPullDown(View view) {
        if (view instanceof AbsListView) {
            if (((AbsListView) view).getChildCount() == 0) {
                // 没有item的时候也可以下拉刷新
                return true;
            } else if (((AbsListView) view).getFirstVisiblePosition() == 0
                    && ((AbsListView) view).getChildAt(0).getTop() >= 0) {
                // 滑到顶部了
                return true;
            }
        } else {
            if (view.getScrollY() == 0)
                return true;
        }
        return false;
    }

    /**
     * 判断是否可以上拉，如果不需要上拉功能可以直接return false
     *
     * @return true如果可以上拉否则返回false
     */
    public static boolean canPullUp(View view) {
        if (view instanceof ViewGroup) {
            int childCount = ((ViewGroup) view).getChildCount();
            if (childCount == 0) return true;
            if (view instanceof AbsListView) {
                ListAdapter adapter = ((AbsListView) view).getAdapter();
                if (adapter != null) {
                    int count = adapter.getCount();
                    if (((AbsListView) view).getLastVisiblePosition() < count - 1)
                        return false;
                }
            } else if (view instanceof WebView) {
                if (view.getScrollY() >= ((WebView) view).getContentHeight() * ((WebView) view).getScale() - view.getMeasuredHeight())
                    return true;
            }
            if (childCount > 0) {
                View lastView = ((ViewGroup) view).getChildAt(childCount - 1);
                if (lastView != null && lastView.getBottom() <= view.getMeasuredHeight() + view.getScrollY()) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }
}