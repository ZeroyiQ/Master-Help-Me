package top.zeroyiq.master_help_me.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.zeroyiq.master_help_me.MyApplication;
import top.zeroyiq.master_help_me.R;
import top.zeroyiq.master_help_me.apis.APISource;
import top.zeroyiq.master_help_me.apis.AuthAPI;
import top.zeroyiq.master_help_me.models.ChangePasswordRequestBody;
import top.zeroyiq.master_help_me.models.ErrorMessage;
import top.zeroyiq.master_help_me.models.User;
import top.zeroyiq.master_help_me.utils.ACache;
import top.zeroyiq.master_help_me.utils.ClearLocalDataUtils;
import top.zeroyiq.master_help_me.utils.RegexConfirmUtils;

public class CenterActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.login_map)
    ImageView loginMap;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.editText_oldPassword)
    EditText editTextOldPassword;
    @BindView(R.id.editText_newPassword)
    EditText editTextNewPassword;
    @BindView(R.id.editText_repeatNewPassword)
    EditText editTextRepeatNewPassword;
    @BindView(R.id.progressBar_center)
    ProgressBar progressBarCenter;

    private AuthAPI authAPI;
    private ACache aCache = ACache.get(MyApplication.getAppContext());
    private View parentView;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = LayoutInflater.from(this).inflate(R.layout.activity_center, null);
        setContentView(parentView);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        authAPI = APISource.getInstance().getAPIObject(AuthAPI.class);
        user = (User) aCache.getAsObject("user");
        tvEmail.setText(user.getEmail());


    }


    /**
     * 注销或者修改密码后退出登录
     */
    private void logout() {
        ClearLocalDataUtils.clearLocalData();       //  清空所有本地存储
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);   //  清除activity栈中所有的activity,两个Flag必须一起使用
        intent.setClass(CenterActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public static void actionStart(Context context) {
        Intent i = new Intent(context, CenterActivity.class);
        context.startActivity(i);
    }


    @OnClick({R.id.btn_changePassword, R.id.button_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_changePassword:
                String oldPassword = editTextOldPassword.getText().toString();
                String newPassword = editTextNewPassword.getText().toString();
                String repeatNewPassword = editTextRepeatNewPassword.getText().toString();
                if (!RegexConfirmUtils.isNotContainsSpecial(oldPassword)) {
                    Snackbar.make(parentView, "密码不能包含特殊字符！", Snackbar.LENGTH_SHORT).show();
                } else if (!RegexConfirmUtils.isNotContainsSpecial(newPassword)) {
                    Snackbar.make(parentView, "密码不能包含特殊字符！", Snackbar.LENGTH_SHORT).show();
                } else if (!newPassword.equals(repeatNewPassword)) {
                    Snackbar.make(parentView, "两次输入的新密码不一致！", Snackbar.LENGTH_SHORT).show();
                } else if (newPassword.equals(oldPassword)) {
                    Snackbar.make(parentView, "新密码不能和旧密码一样！", Snackbar.LENGTH_SHORT).show();
                } else {
                    progressBarCenter.setVisibility(View.VISIBLE);
                    final ChangePasswordRequestBody request = new ChangePasswordRequestBody();
                    request.setEmail(user.getEmail());
                    request.setOld_password(oldPassword);
                    request.setNew_password(newPassword);
                    Call<ResponseBody> call = authAPI.changePassword(request);
                    call.enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            progressBarCenter.setVisibility(View.GONE);
                            if (response.isSuccessful()) {//如果成功
                                Toast.makeText(CenterActivity.this, "密码修改成功,请重新登录", Toast.LENGTH_SHORT)
                                        .show();
                                logout();
                            } else {
                                ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                                onFailure(call, e.toException());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            progressBarCenter.setVisibility(View.GONE);
                            Snackbar.make(parentView, "密码修改失败：" + t.getMessage(), Snackbar.LENGTH_LONG)
                                    .show();

                        }
                    });
                }
                break;
            case R.id.button_logout:
                progressBarCenter.setVisibility(View.VISIBLE);
                Call<ResponseBody> call = authAPI.logout();
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        progressBarCenter.setVisibility(View.GONE);
                        if (response.isSuccessful()) {//如果成功
                            logout();
                        } else {//失败
                            ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                            onFailure(call, e.toException());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        progressBarCenter.setVisibility(View.GONE);
                        Snackbar.make(parentView, "退出失败：" + t.getMessage(), Snackbar.LENGTH_LONG).show();

                    }
                });

                break;


        }
    }
}

