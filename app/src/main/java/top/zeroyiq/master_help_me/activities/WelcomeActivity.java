package top.zeroyiq.master_help_me.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.drakeet.materialdialog.MaterialDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import top.zeroyiq.master_help_me.MyApplication;
import top.zeroyiq.master_help_me.R;
import top.zeroyiq.master_help_me.apis.APISource;
import top.zeroyiq.master_help_me.apis.AuthAPI;
import top.zeroyiq.master_help_me.apis.UpdateApI;
import top.zeroyiq.master_help_me.models.ErrorMessage;
import top.zeroyiq.master_help_me.models.RefreshTokenRequestBody;
import top.zeroyiq.master_help_me.models.UpdateInfo;
import top.zeroyiq.master_help_me.models.User;
import top.zeroyiq.master_help_me.utils.ACache;
import top.zeroyiq.master_help_me.utils.AndroidUtils;
import top.zeroyiq.master_help_me.utils.SharedPreferenceUtil;

/**
 * 程序第一个Activity
 */
public class WelcomeActivity extends BaseActivity {

    private static final int PERMISSION_STORAGE_CODE = 2; //储存许可代码
    private static String TAG = "WelcomeActivity";
    private UpdateInfo updateInfo;
    private View parentView;
    private Handler handler = new Handler();
    private ACache aCache = ACache.get(MyApplication.getAppContext());
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //刷新令牌
            String toRefreshToken = SharedPreferenceUtil.getString(MyApplication.getAppContext(), "TO_REFRESH_SESSION_TOKEN");
            if (toRefreshToken == null || toRefreshToken.isEmpty()) { //没有 token 则进入 RegisterActivity
                RegisterActivity.actionStart(WelcomeActivity.this);
            } else {//每次启动刷新 token
                AuthAPI authAPI = new Retrofit.Builder().baseUrl(MyApplication.INSTANCE_URL)
                        .addConverterFactory(JacksonConverterFactory
                                .create(new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                                        .setSerializationInclusion(JsonInclude.Include.NON_NULL)))
                        .build()
                        .create(AuthAPI.class);

                RefreshTokenRequestBody refreshTokenRequestBody = new RefreshTokenRequestBody();
                refreshTokenRequestBody.setApi_key(MyApplication.API_KEY);
                refreshTokenRequestBody.setSession_token(toRefreshToken);
                Call<User> call = authAPI.refreshToken(refreshTokenRequestBody);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {//如果成功
                            User user = response.body();
                            aCache.put("user", user, 30 * ACache.TIME_DAY);//保存user对象
                            //记录token,保存到缓存是为了检测token是否过期，保存到preference是为了刷新token时读取旧token
                            SharedPreferenceUtil.putString(MyApplication.getAppContext(), "TO_REFRESH_SESSION_TOKEN", user
                                    .getSessionToken());
                            aCache.put("SESSION_TOKEN", user.getSessionToken(), 24 * ACache.TIME_HOUR);//token保存24小时
                            System.out.println("刷新token成功");
                        } else {//刷新失败
                            ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                            onFailure(call, e.toException());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        System.out.println("刷新token失败：" + t.getMessage());
                    }
                });
                HomeActivity.actionStart(WelcomeActivity.this);
            }
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = LayoutInflater.from(this).inflate(R.layout.activity_welcome, null);
        setContentView(parentView);
        //进行 wifi 连接判断
        if (AndroidUtils.isWifi(MyApplication.getAppContext())) {
            //对于 SDK 和 写入外存 权限判断
            if (Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(WelcomeActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                checkUpdateOnStart();
            } else {    // 没有权限，申请写入权限
                ActivityCompat.requestPermissions(WelcomeActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_STORAGE_CODE);
            }
        } else {
            handler.postDelayed(runnable, 1000); //1000 ms 后调用
        }
    }

    /**
     * 对权限回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_STORAGE_CODE: //同类权限只要有一个被允许，其他都算允许
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {// 当权限被允许
                    checkUpdateOnStart();   //启动更新
                } else {
                    Toast.makeText(WelcomeActivity.this, "没有访问储存权限，将无法下载更新包！", Toast.LENGTH_SHORT).show();
                    /**
                     * ---调试---
                     */
                    checkUpdateOnStart();
                    /**
                     * ---调试---
                     */
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 启动时候的检查更新
     */
    private void checkUpdateOnStart() {
        /**
         * --------暂且没有更新------
         */
        handler.post(runnable);

        final UpdateApI updateAPI = APISource.getInstance().getAPIObject(UpdateApI.class);
        Call<UpdateInfo> call = updateAPI.getUpdateInfo();
        call.enqueue(new Callback<UpdateInfo>() {
            @Override
            public void onResponse(Call<UpdateInfo> call, Response<UpdateInfo> response) {
                if (response.isSuccessful()) {
                    updateInfo = response.body();
                    if (updateInfo.getVersionCode() > AndroidUtils.getVersionCode(MyApplication.getAppContext())) {
                        showUpdateDialog(updateInfo);
                    } else {
                        Log.i(TAG, updateInfo.toString());
                        handler.post(runnable);
                    }
                } else {
                    ErrorMessage e = APISource.getErrorMessage(response); //解析错误信息
                    onFailure(call, e.toException());
                }
            }

            @Override
            public void onFailure(Call<UpdateInfo> call, Throwable t) {
                Snackbar.make(parentView, "检查失败" + t.getMessage(), Snackbar.LENGTH_LONG).show();
                handler.post(runnable);

            }
        });

    }

    /**
     * 弹出跟新提示框
     *
     * @param updateInfo
     */
    private void showUpdateDialog(final UpdateInfo updateInfo) {
        final MaterialDialog dialog = new MaterialDialog(this);
        dialog.setPositiveButton("更新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WelcomeActivity.this, "正在后台下载安装包，请查看通知栏下载进度", Toast.LENGTH_LONG).show();
                //构造下载链接
                MyApplication.UPDATE_DOWNLOAD_URL = MyApplication.INSTANCE_URL + updateInfo.getPath()
                        + updateInfo.getName() + "?api_key=" + MyApplication.API_KEY;
                /**
                 * 启动更新
                 */

                dialog.dismiss();
                handler.post(runnable);

            }
        }).setNegativeButton("以后再说", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                handler.post(runnable);
            }
        });
        dialog.setTitle("下载更新");
        dialog.setMessage(updateInfo.toString());
        dialog.show();
    }
}
