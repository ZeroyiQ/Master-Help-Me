package top.zeroyiq.master_help_me.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import junit.framework.Assert;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.zeroyiq.master_help_me.MyApplication;
import top.zeroyiq.master_help_me.R;
import top.zeroyiq.master_help_me.activities.HomeActivity;
import top.zeroyiq.master_help_me.apis.APISource;
import top.zeroyiq.master_help_me.apis.AuthAPI;
import top.zeroyiq.master_help_me.models.ErrorMessage;
import top.zeroyiq.master_help_me.models.LoginRequestBody;
import top.zeroyiq.master_help_me.models.ResetPasswordRequestBody;
import top.zeroyiq.master_help_me.models.User;
import top.zeroyiq.master_help_me.utils.ACache;
import top.zeroyiq.master_help_me.utils.AndroidUtils;
import top.zeroyiq.master_help_me.utils.RegexConfirmUtils;
import top.zeroyiq.master_help_me.utils.SharedPreferenceUtil;

/**
 * 登录页面
 * Created by ZeroyiQ on 2017/9/4.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.toolBar_login)
    Toolbar toolBar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.login_map)
    ImageView loginMap;
    @BindView(R.id.edit_login_email)
    EditText editLoginEmail;
    @BindView(R.id.edit_login_password)
    EditText editLoginPassword;
    @BindView(R.id.btn_login_login)
    Button btnLogin;
    @BindView(R.id.registerLayout)
    LinearLayout registerLayout;
    Unbinder unbinder;
    private AuthAPI authAPI;
    private ACache cache = ACache.get(MyApplication.getAppContext());
    private View parentView;

    @OnClick({R.id.btn_login_login, R.id.btn_forgot_password})
    public void onClick(View view) {
        AndroidUtils.hideSoftInput(LoginFragment.this.getActivity());
        String email = editLoginEmail.getText().toString();
        switch (view.getId()) {
            case R.id.btn_login_login:
                String password = editLoginPassword.getText().toString();

                if (!RegexConfirmUtils.isEmail(email)) {
                    Snackbar.make(parentView, "请输入正确的邮箱", Snackbar.LENGTH_LONG).show();
                } else if (!RegexConfirmUtils.isNotContainsSpecial(password)) {
                    Snackbar.make(parentView, "密码不能包含特殊字符", Snackbar.LENGTH_LONG).show();
                } else if (!RegexConfirmUtils.isLengthRight(password, 5, 17)) {
                    Snackbar.make(parentView, "密码长度在6-16之间", Snackbar.LENGTH_LONG).show();
                } else {

                    progressBar.setVisibility(View.VISIBLE);

                    /**
                     * 调试 Block--------------------------------------------------
                     */
//                    if (email.equals("admin@qq.com")&&password.equals("123456")) {
//                        User user = new User();
//                        user.setEmail("admin@qq.com");
//                        cache.put("user",user,30*ACache.TIME_DAY);
//                        getActivity().finish();
//                        HomeActivity.actionStart(LoginFragment.this.getActivity());
//                        break;

//                    }
                    /**
                     * 调试 Block-------------------------------------------------
                     */

                    final LoginRequestBody request = new LoginRequestBody(email, password);

                    Call<User> call = authAPI.login(request);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            progressBar.setVisibility(View.GONE);
                            if (response.isSuccessful()) {
                                User user = response.body();
                                cache.put("user", user, 30 * ACache.TIME_DAY);      // 保存 user 对象
                                Assert.assertEquals(user.getEmail(), request.getEmail());   // Assert 正确无影响，失败则弹出消息
                                SharedPreferenceUtil.putString(MyApplication.getAppContext(),
                                        "TO_REFRESH_SESSION_TOKEN", user.getSessionToken());  // 保存到 preference 为了刷新 token
                                cache.put("SESSION_TOKEN", user.getSessionToken(), 24 * ACache.TIME_HOUR);  // 缓存中 token 保存 24 小时
                                getActivity().finish();
                                HomeActivity.actionStart(LoginFragment.this.getActivity());
                            } else {    // 报错解析错误
                                ErrorMessage e = APISource.getErrorMessage(response);
                                onFailure(call, e.toException());
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            Snackbar.make(parentView, "登陆失败" + t.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
                break;
            case R.id.btn_forgot_password:
                if (!RegexConfirmUtils.isEmail(email)) {
                    Snackbar.make(parentView, "请输入正确的邮箱", Snackbar.LENGTH_LONG).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    final ResetPasswordRequestBody requestBody = new ResetPasswordRequestBody();
                    requestBody.setEmail(email);

                    Call<ResponseBody> call = authAPI.resetPassword(requestBody);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            progressBar.setVisibility(View.GONE);
                            if (response.isSuccessful()) {
                                Snackbar.make(parentView, "我们已经向您的邮箱发送了重置邮件，请登陆邮箱查看！", Snackbar.LENGTH_LONG).show();
                            } else {
                                ErrorMessage e = APISource.getErrorMessage(response); // 解析错误
                                onFailure(call, e.toException());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            Snackbar.make(parentView, "发送重置邮件失败！" + t.getMessage(), Snackbar.LENGTH_LONG).show();

                        }
                    });

                }
                break;
            default:
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        authAPI = APISource.getInstance().getAPIObject(AuthAPI.class);
        parentView = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, parentView);

        Bundle bundle = getArguments();
        ((AppCompatActivity) LoginFragment.this.getActivity()).setSupportActionBar(toolBar);
        if (bundle != null) {
            editLoginEmail.setText(bundle.getString("email"));
            editLoginPassword.setText(bundle.getString("password"));

        }
        return parentView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
