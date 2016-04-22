package util.lb.com.utils.imageloader.cache.disk.impl;

import android.graphics.Bitmap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import util.lb.com.utils.imageloader.cache.disk.DiskCache;
import util.lb.com.utils.imageloader.cache.disk.naming.FileNameGenerator;
import util.lb.com.utils.imageloader.utils.IoUtils;

public class LruDiskCache implements DiskCache {
    public static final int DEFAULT_BUFFER_SIZE = 32 * 1024; // 32 Kb
    public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;
    public static final int DEFAULT_COMPRESS_QUALITY = 100;
    private static final String ERROR_ARG_NULL = " argument must be not null";
    private static final String ERROR_ARG_NEGATIVE = " argument must be positive number";
    protected DiskLruCache cache;
    private File reserveCacheDir;

    protected final FileNameGenerator fileNameGenerator;

    protected int bufferSize = DEFAULT_BUFFER_SIZE;

    protected Bitmap.CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;
    protected int compressQuality = DEFAULT_COMPRESS_QUALITY;

    public LruDiskCache(File cacheDir, FileNameGenerator fileNameGenerator, long cacheMaxSize) throws IOException {
        this(cacheDir, null, fileNameGenerator, cacheMaxSize, 0);
    }

    public LruDiskCache(File cacheDir, File reserveCacheDir, FileNameGenerator fileNameGenerator, long cacheMaxSize,
                        int cacheMaxFileCount) throws IOException {
        if (cacheDir == null) {
            throw new IllegalArgumentException("cacheDir" + ERROR_ARG_NULL);
        }
        if (cacheMaxSize < 0) {
            throw new IllegalArgumentException("cacheMaxSize" + ERROR_ARG_NEGATIVE);
        }
        if (cacheMaxFileCount < 0) {
            throw new IllegalArgumentException("cacheMaxFileCount" + ERROR_ARG_NEGATIVE);
        }
        if (fileNameGenerator == null) {
            throw new IllegalArgumentException("fileNameGenerator" + ERROR_ARG_NULL);
        }

        if (cacheMaxSize == 0) {
            cacheMaxSize = Long.MAX_VALUE;
        }
        if (cacheMaxFileCount == 0) {
            cacheMaxFileCount = Integer.MAX_VALUE;
        }

        this.reserveCacheDir = reserveCacheDir;
        this.fileNameGenerator = fileNameGenerator;
        initCache(cacheDir, reserveCacheDir, cacheMaxSize, cacheMaxFileCount);
    }

    @Override
    public File getDirectory() {
        return cache.getDirectory();
    }

    private void initCache(File cacheDir, File reserveCacheDir, long cacheMaxSize, int cacheMaxFileCount)
            throws IOException {
        try {
            cache = DiskLruCache.open(cacheDir, 1, 1, cacheMaxSize, cacheMaxFileCount);
        } catch (IOException e) {
            if (reserveCacheDir != null) {
                initCache(reserveCacheDir, null, cacheMaxSize, cacheMaxFileCount);
            }
            if (cache == null) {
                throw e; //new RuntimeException("Can't initialize disk cache", e);
            }
        }
    }

    @Override
    public File get(String imageUri) {
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = cache.get(getKey(imageUri));
            return snapshot == null ? null : snapshot.getFile(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }
    }

    @Override
    public boolean save(String imageUri, InputStream imageStream, IoUtils.CopyListener listener) throws IOException {
        DiskLruCache.Editor editor = cache.edit(getKey(imageUri));
        if (editor == null) {
            return false;
        }

        OutputStream os = new BufferedOutputStream(editor.newOutputStream(0), bufferSize);
        boolean copied = false;
        try {
            copied = IoUtils.copyStream(imageStream, os, listener, bufferSize);
        } finally {
            IoUtils.closeSilently(os);
            if (copied) {
                editor.commit();
            } else {
                editor.abort();
            }
        }
        return copied;
    }

    @Override
    public boolean save(String imageUri, Bitmap bitmap) throws IOException {

        DiskLruCache.Editor editor = cache.edit(getKey(imageUri));
        if (editor == null) {
            return false;
        }

        OutputStream os = new BufferedOutputStream(editor.newOutputStream(0), bufferSize);
        boolean savedSuccessfully = false;
        try {
            savedSuccessfully = bitmap.compress(compressFormat, compressQuality, os);
        } finally {
            IoUtils.closeSilently(os);
        }
        if (savedSuccessfully) {
            editor.commit();
        } else {
            editor.abort();
        }
        return savedSuccessfully;
    }

    @Override
    public boolean remove(String imageUri) {
        try {
            return cache.remove(getKey(imageUri));
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void close() {
        try {
            cache.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cache = null;
    }

    @Override
    public void clear() {
        try {
            cache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            initCache(cache.getDirectory(), reserveCacheDir, cache.getMaxSize(), cache.getMaxFileCount());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getKey(String imageUri) {
        return fileNameGenerator.generate(imageUri);
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public void setCompressFormat(Bitmap.CompressFormat compressFormat) {
        this.compressFormat = compressFormat;
    }

    public void setCompressQuality(int compressQuality) {
        this.compressQuality = compressQuality;
    }

}