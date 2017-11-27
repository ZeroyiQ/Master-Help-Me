package top.zeroyiq.master_help_me.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 操作 SharedPreference (数据储存)文件
 * Created by ZeroyiQ on 2017/5/31.
 */

public class SharedPreferenceUtil {
    static public final class Prefs {
        public static SharedPreferences get(Context context) {
            return context.getSharedPreferences("_dreamf_pref", 0);
        }
    }

    /**
     * 通过传入的 key 值获取「Key」中内容
     *
     * @param context
     * @param key
     * @return 没有则返回空字符串
     */
    public static String getString(Context context, String key) {
        SharedPreferences settings = Prefs.get(context);
        return settings.getString(key, "");
    }

    /**
     * 存放当前设置
     * @param context
     * @param key
     * @param value
     */
    public static synchronized void putString(Context context, String key, String value) {
        SharedPreferences settings = Prefs.get(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
