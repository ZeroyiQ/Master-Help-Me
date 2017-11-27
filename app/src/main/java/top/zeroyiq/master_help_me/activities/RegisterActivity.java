package top.zeroyiq.master_help_me.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import top.zeroyiq.master_help_me.MyApplication;
import top.zeroyiq.master_help_me.R;
import top.zeroyiq.master_help_me.apis.APISource;
import top.zeroyiq.master_help_me.apis.AuthAPI;
import top.zeroyiq.master_help_me.fragments.LoginFragment;
import top.zeroyiq.master_help_me.models.ErrorMessage;
import top.zeroyiq.master_help_me.models.RegisterRequestBody;
import top.zeroyiq.master_help_me.utils.AndroidUtils;
import top.zeroyiq.master_help_me.utils.MyCountTimer;
import top.zeroyiq.master_help_me.utils.RegexConfirmUtils;

/**
 * 注册 Activity
 * Created by ZeroyiQ on 2017/6/2.
 */

public class RegisterActivity extends BaseActivity {
    public static final String TAG = "RegisterActivity";
    public static final int CONFIRM_SUCCESS = 1;        // 验证码验证成功
    public static final int CONFIRM_FAIL = 0;          // 验证码验证失败

    @BindView(R.id.edit_phone_input)
    EditText editPhoneInput;
    @BindView(R.id.edit_confirmCode_input)
    EditText editConfirmCodeInput;
    @BindView(R.id.edit_email_input)
    EditText editEmailInput;
    @BindView(R.id.edit_password_input)
    EditText editPasswordInput;
    @BindView(R.id.edit_confirmPassword_input)
    EditText editConfirmPasswordInput;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btn_send_code)
    Button btnSendCode;
    @BindView(R.id.toolBar_register)
    Toolbar toolBarRegister;
    @BindView(R.id.login_map)
    ImageView loginMap;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.btn_Login)
    Button btnLogin;
    @BindView(R.id.registerLayout)
    LinearLayout registerLayout;
    @BindView(R.id.registerScrollView)
    ScrollView registerScrollView;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;


    private FragmentManager fragmentManager;
    private AuthAPI authAPI;
    private String phone;
    private String email;
    private String password;
    private String confirmPhone;
    private Bundle bundle;
    private Fragment loginFragment;

    /**
     * mob 短信校验 SDK 提供的事件回调
     */
    private EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            Message msg = new Message();                          // 存放消息
            if (result == SMSSDK.RESULT_COMPLETE) {               // 回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {       // 提交正确的验证码
                    msg.arg1 = CONFIRM_SUCCESS;     // 设置消息为 验证成功
                    Log.i(TAG, "afterEvent: 验证码正确--" + data);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {   // 成功发送验证码
                    Snackbar.make(parentView, "验证码发送成功，请在10分钟内验证，60秒后可重新发送！", Snackbar.LENGTH_LONG).show();
                    Log.i(TAG, "afterEvent: 验证码已经发送到手机--" + data);
                } else {
                    ((Throwable) data).printStackTrace();
                    Log.i(TAG, "afterEvent: 其他事件--" + data);
                }
            } else {                // 回调失败
                Log.i(TAG, "afterEvent: 回调失败--" + ((Throwable) data).getMessage());
                msg.arg1 = CONFIRM_FAIL;    // 设置消息为 验证失败
                try {
                    JSONObject jsonObject = new JSONObject(((Throwable) data).getMessage());
                    Snackbar.make(parentView, jsonObject.getString("date1"), Snackbar.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            handlerOfUI.sendMessage(msg);   // 将短信验证后的结果消息发送给主线程

        }
    };

    /**
     * 操作主线程
     */
    private Handler handlerOfUI = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case CONFIRM_SUCCESS:
                    Log.d(TAG, "handleMessage: Success");
                    handlerOfUI.post(runnable);         // 启动注册线程
                    break;
                case CONFIRM_FAIL:
                    progressBar.setVisibility(View.GONE);   //加载失败让进度条隐藏
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 处理验证码校验的成功后注册的线程
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            register(email, password, phone);
        }
    };




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = LayoutInflater.from(this).inflate(R.layout.activity_register, null);
        setContentView(parentView);
        ButterKnife.bind(this);
        toolBarRegister.setTitle("注册");
        setSupportActionBar(toolBarRegister);

        // 初始化
        authAPI = APISource.getInstance().getAPIObject(AuthAPI.class);
        bundle = new Bundle();
        fragmentManager = getSupportFragmentManager();
        initSMSConfirm();
        loginFragment = new LoginFragment();
    }

    private void initSMSConfirm() {
        SMSSDK.initSDK(this, MyApplication.SMS_APP_KEY, MyApplication.SMS_APP_SECRET);
        SMSSDK.registerEventHandler(eventHandler);      // 注册短信回调 handler
    }


    /**
     * 发送验证码 Button
     *
     * @param v
     */
    public void onGetConfirmCodeButtonClick(View v) {
        AndroidUtils.hideSoftInput(RegisterActivity.this);
        confirmPhone = editPhoneInput.getText().toString();
        if (!RegexConfirmUtils.isMobile(confirmPhone)) {
            Snackbar.make(parentView, "请填写正确的手机号！", Snackbar.LENGTH_LONG).show();
        } else {
            new MyCountTimer(60000, 1000, btnSendCode, RegisterActivity.this).start();      // 发送成功开始倒计时
            SMSSDK.getVerificationCode("86", confirmPhone);     // 向填写手机号发送验证码
        }
    }

    /**
     * 注册 Button
     *
     * @param v
     */
    public void onButtonRegisterClick(View v) {
        AndroidUtils.hideSoftInput(RegisterActivity.this);
        phone = editPhoneInput.getText().toString();        // 电话
        password = editPasswordInput.getText().toString();  // 密码
        email = editEmailInput.getText().toString();        // 邮箱
        String confirmPassword = editConfirmPasswordInput.getText().toString();     // 确认密码
        String confirmCode = editConfirmCodeInput.getText().toString();             // 验证码

        if (!RegexConfirmUtils.isMobile(phone)) {
            Snackbar.make(parentView, "请填写正确的手机号！", Snackbar.LENGTH_LONG).show();
        } else if (!RegexConfirmUtils.isNotNull(confirmCode)) {
            Snackbar.make(parentView, "请输入收到的验证码！", Snackbar.LENGTH_LONG).show();
        } else if (!RegexConfirmUtils.isEmail(email)) {
            Snackbar.make(parentView, "请输入正确的邮箱！", Snackbar.LENGTH_LONG).show();
        } else if (!RegexConfirmUtils.isNotContainsSpecial(password)) {
            Snackbar.make(parentView, "密码中不能包含特殊的字符！", Snackbar.LENGTH_LONG).show();
        } else if (!password.equals(confirmPassword)) {
            Snackbar.make(parentView, "输入的两次密码不同", Snackbar.LENGTH_LONG).show();
        } else if (!RegexConfirmUtils.isLengthRight(password, 5, 17)) {
            Snackbar.make(parentView, "密码长度在6-16之间", Snackbar.LENGTH_LONG).show();
        } else if (!phone.equals(confirmPhone)) {
            Snackbar.make(parentView, "手机号与发送验证码手机号不同 ", Snackbar.LENGTH_LONG).show();
        }

        progressBar.setVisibility(View.VISIBLE);
        SMSSDK.submitVerificationCode("86", phone, confirmCode);        // 向后台提交手机号和输入验证码，检验输入手机号和验证码是否正确

    }

    /**
     * 直接登陆 Button
     *
     * @param v
     */
    public void onButtonGoLoginClick(View v) {
        AndroidUtils.hideSoftInput(RegisterActivity.this);
        switchToLogin(bundle);
    }
    /**
     * 发起注册请求
     *
     * @param email
     * @param password
     * @param phone
     */
    private void register(final String email, final String password, final String phone) {
        final RegisterRequestBody request = new RegisterRequestBody();
        request.setEmail(email);
        request.setPassword(password);
        request.setPhone(phone);


        //  和 DreamFactory 连接
        Call<ResponseBody> call = authAPI.register(request);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {// 如果成功
                    Snackbar.make(parentView, "注册成功", Snackbar.LENGTH_LONG).show();
                    bundle.putString("email", email);
                    bundle.putString("password", password);
                    switchToLogin(bundle);
                } else {// 失败
                    ErrorMessage e = APISource.getErrorMessage(response);// 解析错误信息
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(parentView, "注册失败：" + e.getError(), Snackbar.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(parentView, "注册失败：" + t.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });


    }



    /**
     * .
     * 转到登录页面
     *
     * @param bundle
     */
    private void switchToLogin(Bundle bundle) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        loginFragment.setArguments(bundle);
        transaction.replace(R.id.fragment_container, loginFragment);
        transaction.addToBackStack(null);   //把 Fragment 添加到退回栈中
        transaction.commit();       //提交事务
    }

    /**
     * 启动 registerActivity
     * @param context context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }
}
