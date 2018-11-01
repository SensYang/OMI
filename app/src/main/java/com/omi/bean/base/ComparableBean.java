package com.omi.bean.base;


import android.databinding.Bindable;

import com.omi.BR;
import com.omi.ui.widget.indexlist.LetterUtil;

/**
 * Created by DELL on 2016/4/2.
 */
public abstract class ComparableBean extends BaseBean implements Comparable<ComparableBean> {
    private char header = (char) -1;
    private String compareString;
    private boolean isTopOne = false;

    protected void notifyComparableChanged() {
        initComparable();
    }

    private void initComparable() {
        compareString = getCompareString();
        if (compareString != null && compareString.length() > 0) {
            this.initHeader(compareString.toUpperCase().charAt(0));
            this.compareString = StringToCompareString(compareString);
        }
    }

    @Bindable
    public boolean isTopOne() {
        return isTopOne;
    }

    public void setTopOne(boolean topOne) {
        isTopOne = topOne;
        notifyPropertyChanged(BR.topOne);
    }

    protected void setHeader(char header) {
        this.header = header;
    }

    protected void setCompareString(String compareString) {
        this.compareString = compareString;
    }

    protected abstract String getCompareString();

    protected void initHeader(char upperChar) {
        if (Character.isDigit(upperChar)) {
            header = '#';
        } else {
            if (header >= 'A' && header <= 'Z') {
                return;
            }
            header = LetterUtil.getFirstPinYinChar(upperChar);
            if (header < 'A' || header > 'Z') {
                header = '#';
            }
        }
    }

    public char getHeader() {
        if (compareString == null) {
            initComparable();
        }
        return header;
    }

    public String getHeaderString() {
        return getHeader() + "";
    }

    private String StringToCompareString(String toString) {
        if (toString == null) return "";
        String temp = "";
        for (char tempchar : toString.toCharArray()) {
            if (LetterUtil.isChinese(tempchar)) {
                String[] pinyins = LetterUtil.getFirstPinyin(tempchar);
                for (String pinyin : pinyins) {
                    temp += pinyin;
                }
            } else {
                temp += tempchar;
            }
        }
        temp = temp.toUpperCase();
        String newchararr = "";
        for (char tempchar : temp.toCharArray()) {
            if (!LetterUtil.isLetter(tempchar)) {
                tempchar = (char) (tempchar + 123);
            }
            newchararr += tempchar;
        }
        return newchararr;
    }

    @Override
    public int compareTo(ComparableBean another) {
        if (compareString == null) initComparable();
        if (another.compareString == null) another.initComparable();
        if (compareString == null) return -1;
        if (another.compareString == null) return 1;
        if (header == '#' && another.header != '#') return 1;
        if (header != '#' && another.header == '#') return -1;
        return compareString.compareTo(another.compareString);
    }
}
