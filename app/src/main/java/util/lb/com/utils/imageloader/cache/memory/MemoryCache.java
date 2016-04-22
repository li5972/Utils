package util.lb.com.utils.imageloader.cache.memory;

import android.graphics.Bitmap;

import java.util.Collection;

/**
 * Created by Libin on 2016/4/22.
 */
public interface MemoryCache {
    boolean put(String key, Bitmap value);

    Bitmap get(String key);

    Bitmap remove(String key);

    Collection<String> keys();

    void clear();
}
