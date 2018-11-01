package com.omi.ui.activity.dynamic;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.omi.BR;
import com.omi.R;
import com.omi.bean.dynamic.Dynamic;
import com.omi.bean.dynamic.DynamicComments;
import com.omi.config.OmiAction;
import com.omi.databinding.ActivityDynamicBinding;
import com.omi.net.ApiByHttp;
import com.omi.net.ResponseCallback;
import com.omi.ui.adapter.dynamic.DynamicAdapter;
import com.omi.ui.base.BaseActivity;
import com.omi.ui.widget.ResizeListenerLayout;
import com.omi.utils.IMEUtil;
import com.omi.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by SensYang on 2017/03/09 10:42
 */

public class DynamicActivity extends BaseActivity {
    ActivityDynamicBinding binding;
    DynamicAdapter dynamicAdapter;
    private View inputView;
    private EditText contentET;
    private boolean isSelf;
    private float alpha;

    public void setAlpha(float alpha) {
        if (alpha == this.alpha) return;
        this.alpha = alpha;
        binding.topNavigationBar.setBackgroundAlpha(alpha);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dynamic);
        inputView = binding.getRoot().findViewById(R.id.inputView);
        binding.setInputView(inputView);
        contentET = (EditText) inputView.findViewById(R.id.contentET);
        isSelf = getIntent().getBooleanExtra("isSelf", false);
        if (isSelf) {
            binding.topNavigationBar.findViewById(R.id.rightLL).setVisibility(View.GONE);
        }
        binding.topNavigationBar.setBackgroundAlpha(alpha);
        dynamicAdapter = new DynamicAdapter(isSelf);
        dynamicAdapter.setEditView(contentET);
        dynamicAdapter.setInputView(inputView);
        binding.setAdapter(dynamicAdapter);
        binding.setKeyBoardListener(new ResizeListenerLayout.OnSoftKeyBoardListener() {
            @Override
            public void onSoftKeyBoardStateChange(boolean isShowing, int softKeyBoardHeight) {
                if (isShowing) {
                    IMEUtil.getInstance().showSoftKeyboard(contentET);
                    inputView.setVisibility(View.VISIBLE);
                } else {
                    contentET.clearFocus();
                    inputView.setVisibility(View.GONE);
                }
            }
        });
        binding.setTouchOutListener(new ResizeListenerLayout.OnTouchOutListener() {
            @Override
            public void onTouchOut() {
                contentET.clearFocus();
                IMEUtil.getInstance().hideSoftKeyboard(contentET);
                inputView.setVisibility(View.GONE);
            }
        });
        dynamicAdapter.setUser(ApiByHttp.getInstance().getUser());

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int position = layoutManager.findFirstVisibleItemPosition();
                if (position > 0) {
                    setAlpha(1);
                } else {
                    View firstView = layoutManager.findViewByPosition(position);
                    int top = firstView.getTop();
                    int height = binding.topNavigationBar.getHeight();
                    int firstHeight = firstView.getHeight();
                    setAlpha(Math.min(1f, (float) -top / (float) (firstHeight - height)));
                }
            }
        });
        getData();
        findViewById(R.id.onSendClick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendClick(v);
            }
        });
    }

    private void getData() {
        if (isSelf) {
            ApiByHttp.getInstance().getSlefDynamic(new ResponseCallback<Dynamic>() {
                @Override
                public void onSucceed(int what, Dynamic response) throws Exception {
                    dynamicAdapter.clear();
                    dynamicAdapter.addDynamic(response.getDatalist());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dynamicAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
        } else {
            ApiByHttp.getInstance().getDynamic(ApiByHttp.getInstance().getMember_id(), new ResponseCallback<Dynamic>() {
                @Override
                public void onSucceed(int what, Dynamic response) throws Exception {
                    dynamicAdapter.clear();
                    dynamicAdapter.addDynamic(response.getDatalist());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dynamicAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
        }
    }

    @Override
    public void topLeftClick(View view) {
        finish();
    }

    @Override
    public void topRightClick(View view) {
        startActivityForResult(new Intent(this, PublishDynamicActivity.class), OmiAction.ACTION_PUBLISH_DYNAMIC);
    }

    public void onSendClick(View view) {
        String content = contentET.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.showToast("请输入内容");
            return;
        }
        contentET.setText("");
        Dynamic.Data dynamic = (Dynamic.Data) contentET.getTag();
        if (dynamic != null) {
            ApiByHttp.getInstance().commentDynamic(dynamic.getDiscover_id(), content, null);
            if (dynamic.getPinglun() == null) {
                dynamic.setPinglun(new ArrayList<DynamicComments.Data>());
            }
            DynamicComments.Data data = new DynamicComments.Data();
            data.setMember_id(Integer.parseInt("0" + ApiByHttp.getInstance().getMember_id()));
            data.setMember_name(ApiByHttp.getInstance().getUser().getMember_name());
            data.setMember_nickname(ApiByHttp.getInstance().getUser().getMember_nickname());
            data.setComment_info(content);
            dynamic.getPinglun().add(data);
            dynamic.notifyPropertyChanged(BR.pinglun);
        }
        contentET.clearFocus();
        IMEUtil.getInstance().hideSoftKeyboard(contentET);
        inputView.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OmiAction.ACTION_PUBLISH_DYNAMIC && resultCode == RESULT_OK) {
            getData();
        }
    }
}