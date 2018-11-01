package com.omi.ui.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.omi.R;
import com.omi.ui.widget.window.base.BaseDialog;

public class WatingDialog extends BaseDialog {
    private ImageView imageView;
    private TextView textView;

    public WatingDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_wating);
        textView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);
        setShowingAlpha(0.5f);
    }

    public void setContent(String str) {
        textView.setText(str);
    }

    public void setAnim(int animRes) {
        imageView.setBackgroundResource(animRes);
        if (imageView.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
            animationDrawable.start();
        }
    }
}
