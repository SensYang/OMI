package com.omi.ui.adapter.base;

import android.support.annotation.NonNull;

import java.util.Collection;

/**
 * Created by SensYang on 2017/03/22 13:06
 */

public interface AdapterProxy<T> {
    void addData(@NonNull T data);

    void addData(@NonNull T... data);

    void addData(@NonNull Collection<T> list);

    void clear();
}
