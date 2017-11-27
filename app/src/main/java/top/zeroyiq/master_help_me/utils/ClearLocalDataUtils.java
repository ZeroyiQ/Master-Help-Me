package top.zeroyiq.master_help_me.utils;

import top.zeroyiq.master_help_me.MyApplication;

/**
 * 清空本地缓存组件
 * Created by ZeroyiQ on 2017/9/13.
 */

public class ClearLocalDataUtils {
    /**
     * 清空本地缓存和 sharePreference
     */
    public static void clearLocalData() {
        ACache.get(MyApplication.getAppContext()).clear();
        SharedPreferenceUtil.putString(MyApplication.getAppContext(),"TO_REFRESH_SESSION_TOKEN","");
    }
}
