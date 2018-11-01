package com.omi.ui.widget;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.TextView;

import com.omi.BR;

import java.lang.reflect.Field;

/**
 * Created by SensYang on 2017/03/10 17:32
 */

public class OverLineTextView extends TextView implements Observable {
    private transient PropertyChangeRegistry mCallbacks;

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        synchronized (this) {
            if (mCallbacks == null) {
                mCallbacks = new PropertyChangeRegistry();
            }
        }
        mCallbacks.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        synchronized (this) {
            if (mCallbacks == null) {
                return;
            }
        }
        mCallbacks.remove(callback);
    }

    /**
     * Notifies listeners that all properties of this instance have changed.
     */
    public void notifyChange() {
        synchronized (this) {
            if (mCallbacks == null) {
                return;
            }
        }
        mCallbacks.notifyCallbacks(this, 0, null);
    }

    /**
     * Notifies listeners that a specific property has changed. The getter for the property
     * that changes should be marked with {@link Bindable} to generate a field in
     * <code>BR</code> to be used as <code>fieldId</code>.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    public void notifyPropertyChanged(int fieldId) {
        synchronized (this) {
            if (mCallbacks == null) {
                return;
            }
        }
        mCallbacks.notifyCallbacks(this, fieldId, null);
    }

    ///////////////////////////////////////
    protected boolean isOverSize = false;
    private boolean resetText = false;
    private OnOverLineChangedListener overLineChangedListener;

    public OverLineTextView(Context context) {
        this(context, null);
    }

    public OverLineTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverLineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        resetText = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (resetText) {
            setOverSize(checkOverLine());
            resetText = false;
        }
    }

    public boolean checkOverLine() {
        boolean isOverSize = false;
        try {
            Field field = getClass().getSuperclass().getDeclaredField("mLayout");
            field.setAccessible(true);
            Layout mLayout = (Layout) field.get(this);
            if (mLayout == null) return false;
            int lineCount = mLayout.getLineCount();
            if (lineCount > 0)
                isOverSize = mLayout.getEllipsisCount(lineCount - 1) > 0 ? true : false;
            else isOverSize = false;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return isOverSize;
    }

    @Bindable
    public boolean isOverSize() {
        return isOverSize;
    }

    public void setOverSize(boolean overSize) {
        if (overSize == isOverSize) return;
        isOverSize = overSize;
        notifyPropertyChanged(BR.overSize);
        if (overLineChangedListener != null) {
            overLineChangedListener.onOverLineChanged(isOverSize);
        }
    }

    public void setOnOverLineChangedListener(OnOverLineChangedListener changedListener) {
        this.overLineChangedListener = changedListener;
    }

    public interface OnOverLineChangedListener {
        void onOverLineChanged(boolean isOverLine);
    }

}
