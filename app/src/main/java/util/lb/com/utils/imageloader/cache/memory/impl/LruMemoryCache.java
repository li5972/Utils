package util.lb.com.utils.imageloader.cache.memory.impl;

import android.graphics.Bitmap;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import util.lb.com.utils.imageloader.cache.memory.MemoryCache;

public class LruMemoryCache implements MemoryCache {

    private final LinkedHashMap<String, Bitmap> map;
    private final int maxSize;

    /**
     * 当前缓存的大小(bytes)
     */
    private int size;

    public LruMemoryCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("LruMemoryCache maxSize <= 0");
        }
        this.maxSize = maxSize;
        this.map = new LinkedHashMap<String, Bitmap>(0, 0.75f, true);
    }

    @Override
    public boolean put(String key, Bitmap value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }
        synchronized (this) {
            size += sizeOf(value);
            Bitmap previous = map.put(key, value);//Map中key不重复时,返回的是null,当key已存在时,返回旧值
            if (previous != null) {
                size -= sizeOf(previous);
            }
        }
        trimToSize(maxSize);
        return true;
    }

    @Override
    public Bitmap get(String key) {
        if (key == null) {
            return null;
        }
        synchronized (this) {
            return map.get(key);
        }
    }

    @Override
    public Bitmap remove(String key) {
        if (key == null) return null;
        synchronized (this) {
            Bitmap previous = map.remove(key);
            if (previous != null) {
                size -= sizeOf(previous);
            }
            return previous;
        }
    }

    @Override
    public Collection<String> keys() {
        synchronized (this) {
            return new HashSet<String>(map.keySet());
        }
    }

    @Override
    public void clear() {
        trimToSize(-1);
    }

    /**
     * Bitmap大小
     */
    private int sizeOf(Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    /**
     * 如果当前缓存的bitmap总数小于设定值maxSize，不做任何处理，
     * 如果当前缓存的bitmap总数大于maxSize,删除LinkedHashMap中的第一个元素(也就是最老的元素)，
     * size中减去该bitmap对应的byte数
     */
    private void trimToSize(int maxSize) {
        while (true) {
            String key;
            Bitmap value;
            synchronized (this) {
                if (size < 0 || (map.isEmpty() && size != 0)) {
                    throw new IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
                }

                if (size <= maxSize || map.isEmpty()) {
                    break;
                }

                Map.Entry<String, Bitmap> toEvict = map.entrySet().iterator().next();
                if (toEvict == null) {
                    break;
                }
                key = toEvict.getKey();
                value = toEvict.getValue();
                map.remove(key);
                size -= sizeOf(value);
            }
        }
    }

    @Override
    public synchronized final String toString() {
        return String.format("LruCache[maxSize=%d]", maxSize);
    }
}
