package top.zeroyiq.master_help_me.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.zeroyiq.master_help_me.MyApplication;
import top.zeroyiq.master_help_me.R;
import top.zeroyiq.master_help_me.apis.APISource;
import top.zeroyiq.master_help_me.apis.AnswersApi;
import top.zeroyiq.master_help_me.models.Answers;
import top.zeroyiq.master_help_me.models.ErrorMessage;
import top.zeroyiq.master_help_me.models.User;
import top.zeroyiq.master_help_me.utils.ACache;
import top.zeroyiq.master_help_me.utils.AndroidUtils;
import top.zeroyiq.master_help_me.utils.RegexConfirmUtils;

public class PostAnswerActivity extends BaseActivity {
    @BindView(R.id.toolbar_post_answer)
    Toolbar toolbarPostAnswer;
    @BindView(R.id.post_progressBar)
    ProgressBar postProgressBar;
    @BindView(R.id.edit_post_content_ans)
    EditText editPostContentAns;
    @BindView(R.id.button_post_ans)
    Button buttonPostAns;
    private int ans_id;
    private String table;
    private AnswersApi answersApi;
    private Answers answers;
    private User user;
    private String content;
    private ACache aCache = ACache.get(MyApplication.getAppContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = getLayoutInflater().inflate(R.layout.activity_post_answer, null);
        setContentView(parentView);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarPostAnswer);
        ans_id = getIntent().getIntExtra("id", 0);
        table = getIntent().getStringExtra("table");
        init();

    }

    private void init() {
        user = (User) aCache.getAsObject("user");
        answersApi = APISource.getInstance().getAPIObject(AnswersApi.class);
        buttonPostAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AndroidUtils.hideSoftInput(PostAnswerActivity.this);
                content = editPostContentAns.getText().toString();
                if (!RegexConfirmUtils.isLengthRight(content, 4, 250)) {
                    Snackbar.make(parentView, "回答长度在5-250之间！", Snackbar.LENGTH_LONG).show();
                } else {
                    answers = new Answers(ans_id, content, user.getName());
                    AlertDialog.Builder dialog = new AlertDialog.Builder(PostAnswerActivity.this);
                    dialog.setTitle("确认提交")
                            .setMessage(answers.toString())
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            postProgressBar.setVisibility(View.VISIBLE);
                                            postAnswer(answers);
                                        }
                                    }
                            ).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });
    }

    private void postAnswer(Answers answers) {
        Answers[] answerses = new Answers[]{answers};
        Call<ResponseBody> call = answersApi.postAnswer(table, answerses);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    postProgressBar.setVisibility(View.GONE);
                    Snackbar.make(parentView, "回答提交成功！", Snackbar.LENGTH_LONG).show();
                    finish();
                } else {
                    ErrorMessage e = APISource.getErrorMessage(response);
                    onFailure(call, e.toException());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                postProgressBar.setVisibility(View.GONE);
                Snackbar.make(parentView, "回答提交失败！" + t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

    }

    public static void actionStart(Context context, String table, int id) {
        Intent intent = new Intent(context, PostAnswerActivity.class);
        intent.putExtra("table", table);
        intent.putExtra("id", id);
        context.startActivity(intent);

    }
}
