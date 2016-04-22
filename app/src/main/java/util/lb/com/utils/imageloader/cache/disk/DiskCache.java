package util.lb.com.utils.imageloader.cache.disk;

import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import util.lb.com.utils.imageloader.utils.IoUtils;

public interface DiskCache {
    File getDirectory();

    File get(String imageUri);

    boolean save(String imageUri, InputStream imageStream, IoUtils.CopyListener listener) throws IOException;

    boolean save(String imageUri, Bitmap bitmap) throws IOException;

    boolean remove(String imageUri);

    void close();

    void clear();
}
