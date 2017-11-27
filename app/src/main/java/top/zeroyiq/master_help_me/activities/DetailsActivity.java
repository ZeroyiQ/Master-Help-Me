package top.zeroyiq.master_help_me.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.zeroyiq.master_help_me.R;
import top.zeroyiq.master_help_me.adapter.AnswersAdapter;
import top.zeroyiq.master_help_me.adapter.MessageAdapter;
import top.zeroyiq.master_help_me.apis.APISource;
import top.zeroyiq.master_help_me.apis.AnswersApi;
import top.zeroyiq.master_help_me.apis.QuestionsAPi;
import top.zeroyiq.master_help_me.models.Answers;
import top.zeroyiq.master_help_me.models.ErrorMessage;
import top.zeroyiq.master_help_me.models.MessageBody;
import top.zeroyiq.master_help_me.models.Questions;
import top.zeroyiq.master_help_me.models.Resource;

public class DetailsActivity extends BaseActivity {

    @BindView(R.id.tv_detail_title)
    TextView tvDetailTitle;
    @BindView(R.id.tv_detail_date)
    TextView tvDetailDate;
    @BindView(R.id.tv_detail_content)
    TextView tvDetailContent;
    @BindView(R.id.tv_detail_asker)
    TextView tvDetailAsker;

    private Questions question;
    private String category_ans;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = LayoutInflater.from(this).inflate(R.layout.activity_details, null);
        setContentView(parentView);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_answer);

//        getDetail();
        /**
         * --------------------------测试代码段----------------------
         */
        loadDetail(question);                           // 加载问题内容
        loadAnswers(category_ans, question.getId());     // 加载回答
        /**
         * --------------------------测试代码段----------------------
         */

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_detail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostAnswerActivity.actionStart(DetailsActivity.this, category_ans, question.getId());
            }
        });
    }

    private void loadAnswers(String category_ans, int id) {
        AnswersApi answersApi = APISource.getInstance().getAPIObject(AnswersApi.class);
        Call<Resource<Answers>> call = answersApi.getAnswers(category_ans, "ans_id="+id);
        call.enqueue(new Callback<Resource<Answers>>() {
            @Override
            public void onResponse(Call<Resource<Answers>> call, Response<Resource<Answers>> response) {
                if (response.isSuccessful()) {
                    Resource<Answers> tempList = response.body();
                    Snackbar.make(parentView, "新回答获取成功！", Snackbar.LENGTH_LONG).show();
                    loadAnswersDetail(tempList.getResource());
                } else {
                    ErrorMessage e = APISource.getErrorMessage(response);
                    onFailure(call, e.toException());
                }
            }


            @Override
            public void onFailure(Call<Resource<Answers>> call, Throwable t) {
                Snackbar.make(parentView, "获取失败：" + t.getMessage(), Snackbar.LENGTH_LONG).show();
                Log.d("Debug", "onFailure: " + t.getMessage());
            }
        });
    }

    private void loadAnswersDetail(List<Answers> resource) {
        if (!resource.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            AnswersAdapter adapter = new AnswersAdapter(resource);

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
    }


    /**
     * 初始化
     */
    private void initView() {
        Intent intent = getIntent();

        setTitle(intent.getStringExtra("fragment"));
        category_ans = intent.getStringExtra("category") + "_ans";
        question = (Questions) intent.getSerializableExtra("question");
    }

    /**
     * 获取回答问题内容
     */
//    private void getDetail() {
//        QuestionsAPi aPi = APISource.getInstance().getAPIObject(QuestionsAPi.class);
////        Call<Answers> call = aPi.getNewsDetail(question);
//        call.enqueue(new Callback<Answers>() {
//            @Override
//            public void onResponse(Call<Answers> call, Response<Answers> response) {
//                if (response.isSuccessful()) {
//                    Answers detail = response.body();
//                    Log.d("test", "onResponse: " + detail);
//                } else {
//                    ErrorMessage e = APISource.getErrorMessage(response);
//                    onFailure(call, e.toException());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Answers> call, Throwable t) {
//                Toast.makeText(DetailsActivity.this, "错误" + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

    private void loadDetail(Questions detail) {
        tvDetailTitle.setText(detail.getTitle());
        tvDetailDate.setText(detail.getTime());
        tvDetailContent.setText(detail.getContent());
        tvDetailAsker.setText(detail.getUser());
    }


    /**
     * 启动 DetailActivity
     *
     * @param context      上下文
     * @param fragmentName
     */
    public static void actionStart(Context context, String fragmentName, String category, Questions questions) {
        Intent i = new Intent(context, DetailsActivity.class);
        i.putExtra("fragment", fragmentName);
        i.putExtra("category", category);
        i.putExtra("question", questions);
        context.startActivity(i);
    }
}
