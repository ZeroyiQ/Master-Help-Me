package top.zeroyiq.master_help_me;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * 代表整个 App 的 Application 类，程序首先加载，在 AndroidManifest 中注册
 */
public class MyApplication extends Application {
    public static String SMS_APP_KEY;               // 短信验证 KEY
    public static String SMS_APP_SECRET;            // 短信验证 秘钥KEY
    public static String UPDATE_DOWNLOAD_URL;       // 软件更新下载URL
    public static String INSTANCE_URL;              // 接口URL
    public static String API_KEY;                   // DreamFactory接口的key，可在DreamFactory控制台的apps标签下找到

    private static Context context;                 // 最高生命周期的Context
    public static String[] questionsCategory = {
            "高等数学", "大学物理", "大学英语", "线性代数", "概率论"
    };

    /**
     * 提供最高的生命周期Context
     *
     * @return
     */
    public static Context getAppContext() {
        return MyApplication.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        // 获取配置
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = applicationInfo.metaData;
            //从AndroidManifest配置文件中获取这些值
            INSTANCE_URL = bundle.getString("instanceUrl");
            API_KEY = bundle.getString("apiKey");
            SMS_APP_KEY = bundle.getString("smsKey");
            SMS_APP_SECRET = bundle.getString("smsKeySecret");
            UPDATE_DOWNLOAD_URL = bundle.getString("updateUrl");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(MyApplication.class.getSimpleName(), "读取配置文件出错", e);
        }



    }

    public static String map(String fragmentName) {
        String temp;
        switch (fragmentName) {
            case "高等数学":
                temp = "gdsx";
                break;
            case "大学物理":
                temp = "dxwl";
                break;
            case "大学英语":
                temp = "dxyy";
                break;
            case "线性代数":
                temp = "xxds";
                break;
            case "概率论":
                temp = "gll";
                break;
            default:
                temp = "";
        }
        return temp;
    }
}
