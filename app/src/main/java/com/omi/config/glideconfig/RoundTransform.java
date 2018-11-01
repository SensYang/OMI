package com.omi.config.glideconfig;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import static android.R.attr.radius;

/**
 * Created by SensYang on 2016/4/12 0012.
 */
public class RoundTransform extends BitmapTransformation {
    private Paint bitmapPaint;
    private Paint colorPaint;
    private Matrix matrix;
    private RectF rectF;
    private Canvas canvas;
    private float outsideWidth;
    private float leftTopRadius = 0;
    private float rightTopRadius = 0;
    private float rightBottomRadius = 0;
    private float leftBottomRadius = 0;
    private Path roundPath;
    private boolean isCircle;

    public RoundTransform(Context context) {
        super(context);
        this.bitmapPaint = new Paint();
        this.bitmapPaint.setAntiAlias(true);
        this.matrix = new Matrix();
        this.rectF = new RectF();
        this.canvas = new Canvas();
        this.roundPath = new Path();
    }

    public RoundTransform setOutsideWidth(float width) {
        if (width < 0) return this;
        this.outsideWidth = width;
        return this;
    }

    public RoundTransform setOutsideColor(int color) {
        if (this.colorPaint == null) this.colorPaint = new Paint();
        this.colorPaint.setAntiAlias(true);
        this.colorPaint.setColor(color);
        return this;
    }

    public RoundTransform setLeftTopRadius(float leftTopRadius) {
        if (leftTopRadius < 0) return this;
        this.leftTopRadius = leftTopRadius;
        return this;
    }

    public RoundTransform setRightTopRadius(float rightTopRadius) {
        if (rightTopRadius < 0) return this;
        this.rightTopRadius = rightTopRadius;
        return this;
    }

    public RoundTransform setRightBottomRadius(float rightBottomRadius) {
        if (rightBottomRadius < 0) return this;
        this.rightBottomRadius = rightBottomRadius;
        return this;
    }

    public RoundTransform setLeftBottomRadius(float leftBottomRadius) {
        if (leftBottomRadius < 0) return this;
        this.leftBottomRadius = leftBottomRadius;
        return this;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {
        if (source == null) return null;
        float width = source.getWidth();
        float height = source.getHeight();
        initMatrix(width, height, outWidth, outHeight);
        initPaint(source);
        Bitmap result = initCanvas(pool, outWidth, outHeight);
        initPath(0, 0, outWidth, outHeight);
        doDraw(outWidth, outHeight);
        return result;
    }

    private void initMatrix(float width, float height, float outWidth, float outHeight) {
        float scale;
        float xDiff = 0;
        float yDiff = 0;
        if (height / width < outHeight / outWidth) {
            scale = (outHeight - outsideWidth) / height;
            xDiff = (height - width) / 2f * scale + outsideWidth / 2f;
        } else {
            scale = (outWidth - outsideWidth) / width;
            yDiff = (width - height) / 2f * scale + outsideWidth / 2f;
        }
        matrix.reset();
        // 缩放图片动作
        matrix.postScale(scale, scale);
        matrix.postTranslate(xDiff, yDiff);
    }

    private void initPaint(Bitmap source) {
        bitmapPaint.setShader(new BitmapShader(source, BitmapShader.TileMode.MIRROR, BitmapShader.TileMode.MIRROR));
        bitmapPaint.getShader().setLocalMatrix(matrix);
    }

    private void initPath(float xDiff, float yDiff, float width, float height) {
        this.rectF.set(xDiff, yDiff, width, height);
        this.leftTopRadius = Math.min(this.leftTopRadius, Math.min(width, height) * 0.5F);
        this.rightTopRadius = Math.min(this.rightTopRadius, Math.min(width, height) * 0.5F);
        this.rightBottomRadius = Math.min(this.rightBottomRadius, Math.min(width, height) * 0.5F);
        this.leftBottomRadius = Math.min(this.leftBottomRadius, Math.min(width, height) * 0.5F);
        this.isCircle = leftTopRadius == 0 && rightTopRadius == 0 && rightBottomRadius == 0 && leftBottomRadius == 0;
        if (isCircle) return;
        this.roundPath.reset();
        this.roundPath.addRoundRect(rectF, new float[]{this.leftTopRadius, this.leftTopRadius, this.rightTopRadius, this.rightTopRadius, this.rightBottomRadius, this.rightBottomRadius, this.leftBottomRadius, this.leftBottomRadius}, Path.Direction.CW);
    }

    private Bitmap initCanvas(BitmapPool pool, int outWidth, int outHeight) {
        Bitmap result = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        }
        canvas.setBitmap(result);
        return result;
    }

    private void doDraw(float outWidth, float outHeight) {
        if (isCircle) {
            if (outsideWidth > 0) {
                canvas.drawCircle(rectF.centerX(), rectF.centerY(), Math.min(rectF.centerX(), rectF.centerY()), colorPaint);
            }
            canvas.drawCircle(rectF.centerX(), rectF.centerY(), Math.min(rectF.centerX(), rectF.centerY()) - outsideWidth / 2f, bitmapPaint);
        } else {
            if (outsideWidth > 0) {
                canvas.drawPath(roundPath, colorPaint);
                initPath(outsideWidth, outsideWidth, outWidth - outsideWidth * 2, outHeight - outsideWidth * 2);
            }
            canvas.drawPath(roundPath, bitmapPaint);
        }
    }

    @Override
    public String getId() {
        return getClass().getName() + Math.round(radius);
    }

}