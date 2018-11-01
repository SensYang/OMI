package com.omi.config.glideconfig;

import android.content.Context;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;

import java.io.InputStream;

/**
 * Created by SensYang on 2016/7/4 0004.
 */
public class HttpsUrlGlideUrlLoader implements ModelLoader<GlideUrl, InputStream> {
    private static HttpsUrlGlideUrlLoader instance;
    private final ModelCache<GlideDataModel, GlideUrl> modelCache;

    private HttpsUrlGlideUrlLoader(ModelCache<GlideDataModel, GlideUrl> modelCache) {
        this.modelCache = modelCache;
    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(GlideUrl model, int width, int height) {
        return new HttpsUrlFetcher(model);
    }

    /**
     * The default factory for {@link com.bumptech.glide.load.model.stream.HttpUrlGlideUrlLoader}s.
     */
    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {
        private final ModelCache<GlideDataModel, GlideUrl> modelCache = new ModelCache<GlideDataModel, GlideUrl>(500);

        @Override
        public ModelLoader<GlideUrl, InputStream> build(Context context, GenericLoaderFactory factories) {
            if (instance == null) {
                instance = new HttpsUrlGlideUrlLoader(modelCache);
            }
            return instance;
        }

        @Override
        public void teardown() {
            // Do nothing.
        }
    }
}