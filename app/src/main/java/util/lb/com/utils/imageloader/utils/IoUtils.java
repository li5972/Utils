package util.lb.com.utils.imageloader.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IoUtils {
    public static final int DEFAULT_BUFFER_SIZE = 32 * 1024; // 32 KB
    public static final int DEFAULT_IMAGE_TOTAL_SIZE = 500 * 1024; // 500 Kb
    public static final int CONTINUE_LOADING_PERCENTAGE = 75;

    public static interface CopyListener {
        boolean onBytesCopied(int current, int total);
    }

    public static boolean copyStream(InputStream is, OutputStream os, CopyListener listener, int bufferSize)
            throws IOException {
        int current = 0;
        int total = is.available();
        if (total <= 0) {
            total = DEFAULT_IMAGE_TOTAL_SIZE;
        }

        final byte[] bytes = new byte[bufferSize];
        int count;
        if (shouldStopLoading(listener, current, total)) return false;
        while ((count = is.read(bytes, 0, bufferSize)) != -1) {
            os.write(bytes, 0, count);
            current += count;
            if (shouldStopLoading(listener, current, total)) return false;
        }
        os.flush();
        return true;
    }

    private static boolean shouldStopLoading(CopyListener listener, int current, int total) {
        if (listener != null) {
            boolean shouldContinue = listener.onBytesCopied(current, total);
            if (!shouldContinue) {
                if (100 * current / total < CONTINUE_LOADING_PERCENTAGE) {
                    return true; // if loaded more than 75% then continue loading anyway
                }
            }
        }
        return false;
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignored) {
            }
        }
    }
}
