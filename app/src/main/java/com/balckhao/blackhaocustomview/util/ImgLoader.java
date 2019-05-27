package com.balckhao.blackhaocustomview.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.balckhao.blackhaocustomview.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.L;

public class ImgLoader {

    public static void stop() {
        if (ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().stop();
        }
    }

    public static void resume() {
        if (ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().resume();
        }
    }

    public static void init(Context context) {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        int cacheSize = maxMemory / 4;// 使用最大可用内存值的1/4作为缓存的大小。

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context);
        builder.threadPriority(Thread.NORM_PRIORITY - 1);// 设置线程的优先级【10， 1】，高优先级 -> 低优先级.
        builder.threadPoolSize(3);
        builder.denyCacheImageMultipleSizesInMemory();// 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
        builder.memoryCacheSize(cacheSize);
        builder.defaultDisplayImageOptions(optionsDefault);
        builder.imageDownloader(new BaseImageDownloader(context, 5000, 20000));

        ImageLoaderConfiguration config = builder.build();
        ImageLoader.getInstance().init(config);

        L.disableLogging();
    }

    public static void clearDiskCache(Context context) {
        if (!ImageLoader.getInstance().isInited()) {
            ImgLoader.init(context.getApplicationContext());
        }

        ImageLoader.getInstance().clearDiskCache(); // 清除本地缓存
    }

    private static DisplayImageOptions optionsDefault = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .showImageOnFail(R.drawable.wsq_default_avatar)
            .showImageForEmptyUri(R.drawable.wsq_default_avatar)
            .build();

    private static DisplayImageOptions optionsIconDefault = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .showImageOnFail(R.drawable.wsq_default_avatar)
            .build();


    private static DisplayImageOptions optionsRound = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .displayer(new RoundedBitmapDisplayer(1000)).build();


    public static void display(ImageView iv, String url, int defaultImgId) {
        if (!ImageLoader.getInstance().isInited()) {
            ImgLoader.init(iv.getContext().getApplicationContext());
        }
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.cacheInMemory(true);
        builder.cacheOnDisk(true);
        builder.bitmapConfig(Bitmap.Config.RGB_565);
        builder.showImageOnFail(defaultImgId);
        builder.showImageForEmptyUri(defaultImgId);
        builder.showImageOnLoading(defaultImgId);
        ImageLoader.getInstance().displayImage(url, iv, builder.build());
    }

    public static void displayDrawable(ImageView iv, int defaultImgId) {
        if (!ImageLoader.getInstance().isInited()) {
            ImgLoader.init(iv.getContext().getApplicationContext());
        }
        ImageLoader.getInstance().displayImage("drawable://" + defaultImgId, iv, optionsDefault);
    }

    public static void display(ImageView iv, String url) {
        if (!ImageLoader.getInstance().isInited()) {
            ImgLoader.init(iv.getContext().getApplicationContext());
        }
        if (TextUtils.isEmpty(url)) {
            url = "";
        }
        ImageLoader.getInstance().displayImage(url, iv, optionsDefault);
    }

    /**
     * 显示为圆形图片
     * <p>
     * param iv
     * param url
     */
    public static void displayRound(ImageView iv, String url) {
        if (!ImageLoader.getInstance().isInited()) {
            ImgLoader.init(iv.getContext().getApplicationContext());
        }
        ImageLoader.getInstance().displayImage(url, iv, optionsRound);
    }

    public static void displayRound(ImageView iv, String url, int defaultImgId) {
        if (!ImageLoader.getInstance().isInited()) {
            ImgLoader.init(iv.getContext().getApplicationContext());
        }
        ImageLoader.getInstance().displayImage(url, iv,
                new DisplayImageOptions.Builder().cacheInMemory(true)
                        .cacheOnDisk(true)
                        .displayer(new RoundedBitmapDisplayer(1000))
                        .showImageOnLoading(defaultImgId).build());
    }

    public static void displayCorner(ImageView iv, String url, int cornerRadiusPixels, int defaultImgId) {
        if (!ImageLoader.getInstance().isInited()) {
            ImgLoader.init(iv.getContext().getApplicationContext());
        }
        ImageLoader.getInstance().displayImage(url, iv, new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(cornerRadiusPixels)).showImageOnLoading(defaultImgId).build());
    }
}
