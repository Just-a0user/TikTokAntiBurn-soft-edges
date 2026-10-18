package made.by.human.tiktokantiburn;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RootCheck {
    private static final String TAG = "RootCheck";
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private static final Handler MAIN = new Handler(Looper.getMainLooper());
    private static volatile Boolean cached = null;



    public interface Callback {
        void onResult(boolean rooted);
    }



    public static boolean isDeviceRooted() {
        return checkSuInPaths() || checkSuExec();
    }

    public static Boolean getCached() {
        return cached;
    }

    public static void isDeviceRootedAsync(Callback callback) {
        Boolean c = cached;
        if (c != null) {
            callback.onResult(c);
            return;
        }
        EXECUTOR.execute(() -> {
            boolean result = checkSuInPaths() || checkSuExec();
            cached = result;
            MAIN.post(() -> callback.onResult(result));
        });
    }



    private static boolean checkSuInPaths() {
        String[] paths = {
                "/system/bin/",
                "/system/xbin/",
                "/sbin/",
                "/system/sd/xbin/",
                "/system/bin/failsafe/",
                "/data/local/xbin/",
                "/data/local/bin/",
                "/data/local/"
        };

        for (String path : paths) {
            File file = new File(path + "su");
            if (file.exists()) {
                Log.d(TAG, "Found su at: " + file.getAbsolutePath());
                return true;
            }
        }
        return false;
    }


    private static boolean checkSuExec() {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitValue = process.waitFor();
            Log.d(TAG, "SU exit value: " + exitValue);
            return exitValue == 0;
        } catch (Exception e) {
            Log.d(TAG, "SU execution failed: " + e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) os.close();
                if (process != null) process.destroy();
            } catch (Exception ignored) {}
        }
    }
}
