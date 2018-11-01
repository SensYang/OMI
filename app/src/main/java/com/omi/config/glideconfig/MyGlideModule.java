package com.omi.config.glideconfig;

import android.app.ActivityManager;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

/**
 * Created by SensYang on 2016/4/12 0012.
 */
public class MyGlideModule implements GlideModule, MemoryCache.ResourceRemovedListener {

    /**
     * 在这里创建设置内容,之前文章所提及的图片质量就可以在这里设置
     * 还可以设置缓存池参数什么的
     */
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        long maxMemory = memoryInfo.availMem;
        int memoryCacheSize = (int) (maxMemory / 10);//设置图片内存缓存占用八分之一

        //设置内存缓存大小
        LruResourceCache resourceCache = new LruResourceCache(memoryCacheSize);
        resourceCache.setResourceRemovedListener(this);
        builder.setMemoryCache(resourceCache);
        //自定义缓存目录，磁盘缓存给150M
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "image_catch", 150 * 1024 * 1024));
    }

    /**
     * 在这里注册ModelLoaders
     * 可以在这里清除缓存
     */
    @Override
    public void registerComponents(Context context, Glide glide) {
        // register ModelLoaders here.
        glide.register(GlideUrl.class, InputStream.class, new HttpsUrlGlideUrlLoader.Factory());
    }

    @Override
    public void onResourceRemoved(Resource<?> removed) {
        removed.recycle();
    }
}