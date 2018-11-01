package com.omi.ui.activity.dynamic;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.kincai.libjpeg.ImageUtils;
import com.omi.R;
import com.omi.bean.User;
import com.omi.bean.dynamic.Dynamic;
import com.omi.config.OmiAction;
import com.omi.databinding.ActivityPublishDynamicBinding;
import com.omi.net.ApiByHttp;
import com.omi.net.JsonCallback;
import com.omi.ui.activity.utils.amap.LocationSelectActivity;
import com.omi.ui.adapter.dynamic.DynamicImageAdapter;
import com.omi.ui.base.BaseActivity;
import com.omi.ui.widget.WatingDialog;
import com.omi.ui.widget.listener.OnAlterClickListener;
import com.omi.ui.widget.window.WaringDialog;
import com.omi.utils.ByteUtil;
import com.omi.utils.FileUtil;
import com.omi.utils.FileUtils;
import com.omi.utils.IntentUtil;
import com.omi.utils.Log;
import com.omi.utils.PermissionUtil;
import com.omi.utils.ToastUtil;
import com.omi.utils.amap.LocationUtil;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by SensYang on 2017/04/17 18:28
 */

public class PublishDynamicActivity extends BaseActivity {
    private ActivityPublishDynamicBinding binding;
    private String content;
    private DynamicImageAdapter adapter;
    private WaringDialog waringDialog;
    private Uri tempUri;
    private Dynamic.Data dynamic;
    private WatingDialog watingDialog;
    private int doneImgCount = 0;
    private int needImgCount = 0;
    private SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_publish_dynamic);
        binding.setPublish(this);
        binding.setLocation(LocationUtil.getInstance().getLocationInfo());
        adapter = new DynamicImageAdapter();
        binding.setAdapter(adapter);
        initDynamic();
        waringDialog = new WaringDialog(this);
        waringDialog.setTitleText(R.string.select_head);
        waringDialog.setLeftText(R.string.take_photo);
        waringDialog.setRightText(R.string.select_album);
        waringDialog.setCancelable(true);
        waringDialog.setOnAlterClickListener(new OnAlterClickListener() {
            @Override
            public void onCancelClick() {
                startActivityForResult(IntentUtil.getInstance().getPickImageIntent(), OmiAction.ACTION_PICK);
            }

            @Override
            public void onConfirmClick() {
                if (!PermissionUtil.checkPermission(PublishDynamicActivity.this, PermissionUtil.PermissionCode.CAMERA_PERMISSION))
                    return;
                tempUri = FileUtils.makeImageUri(System.currentTimeMillis() + "");
                startActivityForResult(IntentUtil.getInstance().getTakePhotoIntent(tempUri), OmiAction.ACTION_TAKE_PHOTO);
            }
        });
        watingDialog = new WatingDialog(this);
        watingDialog.setContent("正在发布 请稍等...");
        watingDialog.setAnim(R.drawable.wating_anim);
    }

    public void onSelectClick(View view) {
        if (adapter.getItemCount() > 8) {
            ToastUtil.showToast("最多只能上传九张图片");
            return;
        }
        waringDialog.show();
    }

    private void initDynamic() {
        User.Data uer = ApiByHttp.getInstance().getUser();
        //        dynamic = (Dynamic.Data) getIntent().getSerializableExtra("dynamic");
        Dynamic.Data getDynamic = (Dynamic.Data) getIntent().getSerializableExtra("dynamic");
        dynamic = new Dynamic.Data();
        if (getDynamic == null) {//自己的动态
            binding.setIsForward(false);

            dynamic.setDiscover_didForward(0);
            dynamic.setBe_forwarded_member_id("0");
            dynamic.setBe_forwarded_textInfo("0");
            dynamic.setBe_forwarded_discover_id("0");
        } else {//转发的动态
            binding.setName(getDynamic.getFbz_member_nickname());
            binding.setContent(getDynamic.getRelease_textInfo());
            binding.setIsForward(true);

            dynamic.setDiscover_didForward(1);
            dynamic.setBe_forwarded_member_id(getDynamic.getFbz_member_id());
            dynamic.setBe_forwarded_member_avatar(getDynamic.getFbz_member_avatar());
            dynamic.setBe_forwarded_member_name(getDynamic.getFbz_member_name());
            dynamic.setBe_forwarded_member_nickname(getDynamic.getFbz_member_nickname());
            dynamic.setBe_forwarded_textInfo(getDynamic.getRelease_textInfo());
            dynamic.setBe_forwarded_discover_id(getDynamic.getDiscover_id());
        }
        dynamic.setMember_id(uer.getMember_id());
        dynamic.setFbz_member_id(uer.getMember_id());
        dynamic.setFbz_member_avatar(uer.getMember_avatar());
        dynamic.setFbz_member_name(uer.getMember_name());
        dynamic.setFbz_member_nickname(uer.getMember_nickname());
        dynamic.setRelease_areaInfo(LocationUtil.getInstance().getLocationInfo());
        dynamic.setDiscover_haveImage(0);
        dynamic.setDiscover_text_type("0");
    }

    @Override
    public void topLeftClick(View view) {
        finish();
    }

    @Override
    public void topRightClick(View view) {//TODO
        if (content.length() == 0) {
            ToastUtil.showToast("文本不能为空");
            return;
        }
        watingDialog.show();
        dynamic.setRelease_textInfo(content);
        if (adapter.getItemCount() > 0) dynamic.setDiscover_haveImage(1);
        ApiByHttp.getInstance().publishDynamic(dynamic, new JsonCallback() {
            @Override
            public void onSucceed(int what, JSONObject response) throws Exception {
                if (response.getInt("result") == 200) {
                    Log.e("动态插入成功");
                    if (dynamic.getDiscover_haveImage() == 1) {
                        String discover_id = response.getJSONObject("data").getString("discover_id");
                        needImgCount = adapter.getItemCount();
                        doneImgCount = 0;
                        Log.e("开始插入图片");
                        boolean goAway = true;
                        int i = 0;
                        for (Uri uri : adapter.getUriList()) {
                            String outPath = FileUtils.makeImagePath(format.format(new Date()) + i++);
                            ImageUtils.compressBitmap(FileUtil.getPathWithUri(uri), outPath);
                            File file = new File(outPath);
                            String imagebase64str = ByteUtil.fileToBase64(file);
                            if (imagebase64str != null) {
                                goAway = false;
                                String image_name = file.getName();
                                ApiByHttp.getInstance().publishDynamicImage(discover_id, imagebase64str, image_name, imgCallback);
                            }
                        }
                        if (goAway) onSuccess();
                    } else {
                        onSuccess();
                    }
                } else {
                    watingDialog.dismiss();
                    ToastUtil.showToast(response.getString("msg"));
                }
            }
        });
    }

    private JsonCallback imgCallback = new JsonCallback() {
        @Override
        public void onSucceed(int what, JSONObject response) throws Exception {
            doneImgCount++;
            if (doneImgCount == needImgCount) {
                onSuccess();
            }
        }

        @Override
        public void onFailed(int what, JSONObject response) throws Exception {
            doneImgCount++;
            watingDialog.dismiss();
            ToastUtil.showToast("图片插入失败");
        }
    };

    @Override
    protected void onDestroy() {
        if(watingDialog!=null){
            watingDialog.dismiss();
        }
        super.onDestroy();
    }

    private void onSuccess() {
        watingDialog.dismiss();
        if (dynamic.getDiscover_didForward() == 0) {
            ToastUtil.showToast("发布成功");
        } else {
            ToastUtil.showToast("转发成功");
        }
        setResult(RESULT_OK);
        finish();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void onLocationClick(View view) {
        startActivityForResult(new Intent(this, LocationSelectActivity.class), OmiAction.ACTION_LOCATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case OmiAction.ACTION_TAKE_PHOTO:
                    adapter.addUri(tempUri);
                    adapter.notifyItemInserted(adapter.getItemCount() - 1);
                    break;
                case OmiAction.ACTION_PICK:
                    adapter.addUri(data.getData());
                    adapter.notifyItemInserted(adapter.getItemCount() - 1);
                    break;
                case OmiAction.ACTION_LOCATION:
                    //发送位置消息
                    String province = data.getStringExtra("province");
                    String city = data.getStringExtra("city");
                    String area = data.getStringExtra("area");
                    String road = data.getStringExtra("road");
                    String areaString = null;
                    if (province != null) {
                        areaString = province;
                        if (city != null) {
                            areaString += city;
                            if (area != null) {
                                areaString += area;
                                if (road != null) {
                                    areaString += road;
                                }
                            }
                        }
                    }
                    binding.setLocation(areaString);
                    dynamic.setRelease_areaInfo(areaString);
                    break;
            }
        }
    }
}
