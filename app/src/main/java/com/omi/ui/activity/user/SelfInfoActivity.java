package com.omi.ui.activity.user;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.kincai.libjpeg.ImageUtils;
import com.omi.R;
import com.omi.bean.User;
import com.omi.bean.UserVO;
import com.omi.bean.base.BaseJson;
import com.omi.config.OmiAction;
import com.omi.config.glideconfig.RoundTransform;
import com.omi.database.LiteOrmDBUtil;
import com.omi.databinding.ActivitySelfInfoBinding;
import com.omi.net.ApiByHttp;
import com.omi.net.JsonUtils;
import com.omi.net.ResponseCallback;
import com.omi.ui.activity.utils.amap.LocationSelectActivity;
import com.omi.ui.base.BaseActivity;
import com.omi.ui.widget.InputDialog;
import com.omi.ui.widget.QrcardDialog;
import com.omi.ui.widget.WatingDialog;
import com.omi.ui.widget.listener.OnAlterClickListener;
import com.omi.ui.widget.window.WaringDialog;
import com.omi.utils.ByteUtil;
import com.omi.utils.DisplayUtil;
import com.omi.utils.FileUtil;
import com.omi.utils.FileUtils;
import com.omi.utils.IntentUtil;
import com.omi.utils.Log;
import com.omi.utils.PermissionUtil;
import com.omi.utils.ToastUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by SensYang on 2017/04/19 9:50
 */

public class SelfInfoActivity extends BaseActivity implements OnAlterClickListener {
    ActivitySelfInfoBinding binding;
    private boolean hasChange = false;
    private WaringDialog waringDialog;
    private WaringDialog dialog;
    private Uri headUri;
    private boolean hasSelectImg = false;
    private WatingDialog watingDialog;
    private QrcardDialog qrcardDialog;
    private InputDialog inputDialog;
    private Calendar calendar;
    private DatePickerDialog pickerDialog;
    private String[] sexArry = new String[]{"女", "男"};// 性别选择
    private AlertDialog.Builder sexBuilder;
    private UserVO userVO = new UserVO();
    private boolean canNotBack;
    private SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        canNotBack = getIntent().getBooleanExtra("canNotBack", false);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_self_info);
        binding.setSelfInfoActivity(this);
        if (savedInstanceState != null) {
            headUri = savedInstanceState.getParcelable("headUri");
            userVO = (UserVO) savedInstanceState.getSerializable("userVO");
        }
        binding.setUser(userVO);
        if (ApiByHttp.getInstance().getUser() != null) {
            userVO.setUser(ApiByHttp.getInstance().getUser());
        }
        if (headUri != null) {
            setHead();
        } else {
            initFile();
        }
        if (userVO.getAvatar() == null)
            ApiByHttp.getInstance().getUserInfo(ApiByHttp.getInstance().getPhone(), new ResponseCallback<User>() {
                @Override
                public void onSucceed(int what, User response) throws Exception {
                    userVO.setUser(response.getUserinfo().get(0));
                    ApiByHttp.getInstance().setUser(response.getUserinfo().get(0));
                    qrcardDialog.setUser(response.getUserinfo().get(0));
                    Log.e(JsonUtils.parserBean2Json(response));
                    Log.e(JsonUtils.parserBean2Json(userVO));
                }
            });
        initView();
    }

    private void initView() {
        inputDialog = new InputDialog(this);
        inputDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                String s = inputDialog.getString();
                inputDialog.setString("");
                if (s.length() == 0) return;
                hasChange = true;
                if (inputDialog.getTag().equals("nickname")) {
                    userVO.setMember_nickname(s);
                } else if (inputDialog.getTag().equals("signature")) {
                    userVO.setMember_signature(s);
                }
            }
        });
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
                if (!PermissionUtil.checkPermission(SelfInfoActivity.this, PermissionUtil.PermissionCode.CAMERA_PERMISSION))
                    return;
                startActivityForResult(IntentUtil.getInstance().getTakePhotoIntent(headUri), OmiAction.ACTION_TAKE_PHOTO);
            }
        });
        dialog = new WaringDialog(this);
        dialog.setTitleText("您还有未保存的信息，确定要退出？");
        dialog.setOnAlterClickListener(this);
        watingDialog = new WatingDialog(this);
        watingDialog.setContent("正在上传资料 请稍等...");
        watingDialog.setAnim(R.drawable.wating_anim);
        qrcardDialog = new QrcardDialog(this);
        qrcardDialog.setUser(ApiByHttp.getInstance().getUser());
        calendar = Calendar.getInstance();
        pickerDialog = new DatePickerDialog(this,
                // 绑定监听器
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dayOfMonth += 1;
                        int age = calendar.get(Calendar.YEAR) - year;
                        userVO.setMember_age(age + "");
                        userVO.setMember_birthday(year + "-" + (monthOfYear < 10 ? "0" + monthOfYear : "" + monthOfYear) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth));
                        hasChange = true;
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        sexBuilder = new AlertDialog.Builder(this);// 自定义对话框
        sexBuilder.setSingleChoiceItems(sexArry, 0, new DialogInterface.OnClickListener() {// 2默认的选中

            @Override
            public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
                hasChange = true;
                userVO.setMember_sex(sexArry[which]);
                dialog.dismiss();// 随便点击一个item消失对话框，不用点击确认取消
            }
        });
    }


    private float radius = DisplayUtil.dip2px(3);

    private void setHead() {
        Glide.with(this).load(headUri).signature(new StringSignature("" + System.currentTimeMillis())).bitmapTransform(new RoundTransform(this).setLeftTopRadius(radius).setRightTopRadius(radius).setRightBottomRadius(radius).setLeftBottomRadius(radius)).crossFade().placeholder(R.drawable.default_head).into(binding.headIV);
    }

    public void initFile() {
        headUri = FileUtils.makeImageUri("omihead".hashCode() + "");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable("headUri", headUri);
        savedInstanceState.putSerializable("user", userVO);
        super.onSaveInstanceState(savedInstanceState); //实现父类方法 放在最后 防止拍照后无法返回当前activity
    }

    public void topLeftClick(View view) {
        if (canNotBack) {
            ToastUtil.showToast("请设置您的资料");
            return;
        }
        if (!hasSelectImg && userVO.getAvatar() == null) {
            ToastUtil.showToast("请设置头像");
            return;
        } else if (hasChange) {
            dialog.show();
            return;
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        topLeftClick(binding.headIV);
    }

    public void topRightClick(View view) {
        if (!hasSelectImg && userVO.getAvatar() == null) {
            ToastUtil.showToast("请设置头像");
            return;
        }
        watingDialog.show();
        new Thread() {
            @Override
            public void run() {
                if (hasSelectImg) {
                    String outPath = FileUtils.makeImagePath(format.format(new Date()));
                    ImageUtils.compressBitmap(FileUtil.getPathWithUri(headUri), outPath);
                    String bitString = ByteUtil.fileToHexString(new File(outPath));
                    if (bitString == null) {
                        ToastUtil.showToast("请设置头像");
                        return;
                    }
                    userVO.setMember_avatar(bitString);
                }
                ApiByHttp.getInstance().setUserInfo(userVO, new ResponseCallback<BaseJson>() {
                    @Override
                    public void onSucceed(int what, BaseJson response) throws Exception {
                        if ("success".equalsIgnoreCase(response.getResult())) {
                            ApiByHttp.getInstance().getUserInfo(ApiByHttp.getInstance().getPhone(), new ResponseCallback<User>() {
                                @Override
                                public void onSucceed(int what, User response) throws Exception {
                                    watingDialog.dismiss();
                                    User.Data getUser = response.getUserinfo().get(0);
                                    User.Data user = LiteOrmDBUtil.getUserDBUtil().queryOneByWhere(User.Data.class, "member_name", getUser.getMember_name());
                                    if (user != null) {
                                        user.setUser(getUser);
                                        LiteOrmDBUtil.getUserDBUtil().update(user);
                                    } else {
                                        LiteOrmDBUtil.getUserDBUtil().save(getUser);
                                    }
                                    ApiByHttp.getInstance().setUser(getUser);
                                    finish();
                                }
                            });
                        }
                    }
                });
            }
        }.start();
    }

    public void onHeadClick(View view) {
        waringDialog.show();
    }

    public void onSexClick(View view) {
        sexBuilder.show();
    }

    public void onAgeClick(View view) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        pickerDialog.show();
    }

    public void onNicknameClick(View view) {
        inputDialog.setTag("nickname");
        inputDialog.show();
    }

    public void onAreaClick(View view) {
        startActivityForResult(new Intent(this, LocationSelectActivity.class), OmiAction.ACTION_LOCATION);
    }

    public void onSignatureClick(View view) {
        inputDialog.setTag("signature");
        inputDialog.show();
    }

    public void onQrClick(View view) {
        qrcardDialog.show();
    }

    @Override
    public void onCancelClick() {

    }

    @Override
    public void onConfirmClick() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case OmiAction.ACTION_TAKE_PHOTO:
                    startActivityForResult(IntentUtil.getInstance().getCropPhotoIntent(headUri, headUri), OmiAction.ACTION_CROP);
                    break;
                case OmiAction.ACTION_PICK:
                    startActivityForResult(IntentUtil.getInstance().getCropPhotoIntent(data.getData(), headUri), OmiAction.ACTION_CROP);
                    break;
                case OmiAction.ACTION_CROP:
                    hasChange = true;
                    hasSelectImg = true;
                    setHead();
                    break;
                case OmiAction.ACTION_LOCATION:
                    //发送位置消息
                    String province = data.getStringExtra("province");
                    String city = data.getStringExtra("city");
                    String area = data.getStringExtra("area");
                    String areaString = province + city + area;
                    userVO.setMember_areainfo(areaString);
                    hasChange = true;
                    break;
            }
        }
    }
}