package com.omi.ui.widget;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.omi.R;
import com.omi.ui.widget.window.base.BaseDialog;

/**
 * Created by SensYang on 2017/04/19 11:32
 */

public class InputDialog extends BaseDialog {
    private EditText editText;
    private TextView textView;

    public InputDialog(Context context) {
        super(context);
        setShowingAlpha(0.5f);
        setCancelable(false);
        setContentView(R.layout.dialog_intput);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public String getString() {
        return editText.getText().toString();
    }

    public void setString(String s) {
        editText.setText(s);
    }
}
