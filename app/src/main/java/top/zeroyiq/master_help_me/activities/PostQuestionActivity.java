package top.zeroyiq.master_help_me.activities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.zeroyiq.master_help_me.MyApplication;
import top.zeroyiq.master_help_me.R;
import top.zeroyiq.master_help_me.apis.APISource;
import top.zeroyiq.master_help_me.apis.QuestionsAPi;
import top.zeroyiq.master_help_me.models.ErrorMessage;
import top.zeroyiq.master_help_me.models.Questions;
import top.zeroyiq.master_help_me.models.User;
import top.zeroyiq.master_help_me.utils.ACache;
import top.zeroyiq.master_help_me.utils.AndroidUtils;
import top.zeroyiq.master_help_me.utils.RegexConfirmUtils;

public class PostQuestionActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.post_progressBar)
    ProgressBar progressBar;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.edit_post_title)
    EditText editPostTitle;
    @BindView(R.id.edit_post_content)
    EditText editPostContent;
    @BindView(R.id.button_post)
    Button buttonPost;


    private ArrayAdapter<String> adapter;
    private User user;
    private ACache aCache = ACache.get(MyApplication.getAppContext());
    private QuestionsAPi questionsAPi;
    private String fragmentName;
    private String title;
    private String content;
    private String date;
    private Questions newQuestion;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = getLayoutInflater().inflate(R.layout.activity_post_question, null);
        setContentView(parentView);
        ButterKnife.bind(this, parentView);
        setSupportActionBar(toolbar);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MyApplication.questionsCategory);
        spinner.setAdapter(adapter);
        init();

    }

    private void init() {
        user = (User) aCache.getAsObject("user");
        questionsAPi = APISource.getInstance().getAPIObject(QuestionsAPi.class);
        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AndroidUtils.hideSoftInput(PostQuestionActivity.this);
                fragmentName = (String) spinner.getSelectedItem();
                title = editPostTitle.getText().toString();
                content = editPostContent.getText().toString();

                if (!RegexConfirmUtils.isLengthRight(title, 4, 30)) {
                    Snackbar.make(parentView, "题目长度在5-30之间!", Snackbar.LENGTH_LONG).show();
                } else if (!RegexConfirmUtils.isLengthRight(content, 4, 300)) {
                    Snackbar.make(parentView, "描述长度在5-300之间！", Snackbar.LENGTH_LONG).show();
                } else {

                    newQuestion = new Questions(title, content, user.getName());
                    AlertDialog.Builder dialog = new AlertDialog.Builder(PostQuestionActivity.this);
                    dialog.setTitle("确认提交")
                            .setMessage(fragmentName+newQuestion.toString())
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            progressBar.setVisibility(View.VISIBLE);
                                            postQuestion(newQuestion);
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


    private void postQuestion(Questions newQuestion) {
        Questions[] questions = new Questions[]{newQuestion};
        Log.d("debug_post", "postQuestion: "+MyApplication.map(fragmentName));
        String  tempTable=MyApplication.map(fragmentName);
        Call<ResponseBody> call = questionsAPi.postQuestions(tempTable, questions);
//        Call<ResponseBody> call = questionsAPi.postNewQuestions(questions);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Snackbar.make(parentView, "问题提交成功！", Snackbar.LENGTH_LONG).show();
                    HomeActivity.actionStart(PostQuestionActivity.this);
                    finish();
                } else {
                    ErrorMessage e = APISource.getErrorMessage(response);
                    onFailure(call, e.toException());


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(parentView, "问题提交失败！" + t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public static void actionStart(Context context) {
        Intent i = new Intent(context, PostQuestionActivity.class);
        context.startActivity(i);
    }
}
