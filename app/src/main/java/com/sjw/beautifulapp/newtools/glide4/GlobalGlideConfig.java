package com.sjw.beautifulapp.newtools.glide4;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

/**
 * Description:  * Glide全局配置，使用GlideModule注解执行自动代码生成，生成GlideApp，后续的Glide
 * 调用都需要替换为GlideApp.with(context).load(url).into(imageView) 的方式
 * Data：2018/9/3-16:24
 * QQ:1175476869@qq.com
 * Author: 沈佳伟
 */
@GlideModule
public class GlobalGlideConfig extends AppGlideModule {

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);

        /**
         * DiskCacheStrategy.NONE： 表示不缓存任何内容。
         * DiskCacheStrategy.DATA： 表示只缓存原始图片。
         * DiskCacheStrategy.RESOURCE： 表示只缓存转换过后的图片。
         * DiskCacheStrategy.ALL ： 表示既缓存原始图片，也缓存转换过后的图片。
         * DiskCacheStrategy.AUTOMATIC： 表示让Glide根据图片资源智能地选择使用哪一种缓存策略（默认选项）。
         */
        builder.setDefaultRequestOptions(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));

        /**
         * 优先外部存储作为磁盘缓存目录，防止内部存储文件过大
         * 外部存储目录默认地址为：/sdcard/Android/data/com.sina.weibolite/cache/image_manager_disk_cache
         */
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context));
    }
}