package top.zeroyiq.master_help_me.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telecom.Connection;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by ZeroyiQ on 2017/5/31.
 * 管理安卓本身的组件
 */

public class AndroidUtils {
    public static void hideSoftInput(Activity activity) {
        if (activity == null) {
            return;
        }
        if (activity.getCurrentFocus() != null && activity.getCurrentFocus().getWindowToken() != null) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 判断是否有 Wifi 连接
     *
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManage = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activieNetInfo = connectivityManage.getActiveNetworkInfo();
        return activieNetInfo != null && activieNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 获得版本号
     *
     * @param context context
     * @return versionCode
     */
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    /**
     * 返回包信息
     *
     * @param context
     * @return packageInfo
     */
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo packageInfo = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }
}
