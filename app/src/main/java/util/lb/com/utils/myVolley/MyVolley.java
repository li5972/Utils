package util.lb.com.utils.myVolley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import util.lb.com.utils.Constant;
import util.lb.com.utils.R;
import util.lb.com.utils.myVolley.cache.BitmapImageCache;
import util.lb.com.utils.myVolley.cache.DiskLruBasedCache;
import util.lb.com.utils.myVolley.cache.DiskLruBasedCache.ImageCacheParams;
import util.lb.com.utils.myVolley.image.SimpleImageLoader;

/**
 * Created by Libin on 2016/4/20.
 */
public class MyVolley {
    private static RequestQueue mRequestQueue;
    private static SimpleImageLoader mImageLoader;


    private MyVolley() {
        // no instances
    }

    public static void init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);

        ImageCacheParams cacheParams =
                new ImageCacheParams(context, Constant.DIR_IMAGE_CACHE);
        cacheParams.setMemCacheSizePercent(0.2f);
        mImageLoader = new SimpleImageLoader(context, cacheParams);
        mImageLoader.setDefaultDrawable(R.mipmap.ic_launcher);
    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    public static SimpleImageLoader getImageLoader() {
        if (mImageLoader != null) {
            return mImageLoader;
        } else {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }
}
