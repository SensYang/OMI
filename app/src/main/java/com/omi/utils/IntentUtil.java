package com.omi.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by SensYang on 2016/7/26 0026.
 */
public class IntentUtil {
    private static IntentUtil instance;

    private IntentUtil() {
    }

    public static IntentUtil getInstance() {
        if (instance == null) {
            instance = new IntentUtil();
        }
        return instance;
    }

    /**
     * 从相册选择图片并裁剪
     */
    public Intent getPickWithCropIntent(Uri imgUri) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        return getIntent(intent, imgUri);
    }

    /**
     * 从相册选择图片
     */
    public Intent getPickImageIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        return intent;
    }

    /**
     * 从相机拍照
     */
    public Intent getTakePhotoIntent(Uri imgUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        intent.putExtra("return-data", false);
        return intent;
    }

    private Intent getIntent(Intent intent, Uri outUri) {
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

//        // outputX,outputY 是剪裁图片的宽高
//        intent.putExtra("outputX", 640);
//        intent.putExtra("outputY", 640);
        intent.putExtra("scale", true); // 去黑边
        intent.putExtra("scaleUpIfNeeded", true); // 去黑边
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        return intent;
    }

    public Intent getCropPhotoIntent(Uri inputUri, Uri outUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inputUri, "image/*");
        return getIntent(intent, outUri);
    }

    public Intent getCropPhotoIntent(Uri inputUri, Uri outUri, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inputUri, "image/*");
        intent = getIntent(intent, outUri);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("aspectX", outputX);
        intent.putExtra("aspectY", outputY);
        return intent;
    }

    public Intent getRecordVideoIntent() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        //限制时长
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);
        return intent;
    }
}