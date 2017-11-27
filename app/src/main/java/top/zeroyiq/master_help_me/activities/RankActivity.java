package top.zeroyiq.master_help_me.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.zeroyiq.master_help_me.R;
import top.zeroyiq.master_help_me.adapter.RankAdapter;
import top.zeroyiq.master_help_me.apis.APISource;
import top.zeroyiq.master_help_me.apis.RankApi;
import top.zeroyiq.master_help_me.models.ErrorMessage;
import top.zeroyiq.master_help_me.models.Rank;
import top.zeroyiq.master_help_me.models.Resource;

public class RankActivity extends BaseActivity {
    private RecyclerView recyclerView;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = LayoutInflater.from(this).inflate(R.layout.activity_rank, null);
        setContentView(parentView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_rank);
        setSupportActionBar(toolbar);
        loadRank();
    }


    private void loadRank() {
        RankApi api = APISource.getInstance().getAPIObject(RankApi.class);
        Call<Resource<Rank>> call = api.getRank();
        call.enqueue(new Callback<Resource<Rank>>() {
            @Override
            public void onResponse(Call<Resource<Rank>> call, Response<Resource<Rank>> response) {
                if (response.isSuccessful()) {
                    Resource<Rank> tempList = response.body();
                    Snackbar.make(parentView, "排名获取成功！", Snackbar.LENGTH_LONG).show();
                    loadAnswersDetail(tempList.getResource());
                } else {
                    ErrorMessage e = APISource.getErrorMessage(response);
                    onFailure(call, e.toException());
                }
            }

            @Override
            public void onFailure(Call<Resource<Rank>> call, Throwable t) {
                Snackbar.make(parentView, "获取失败：" + t.getMessage(), Snackbar.LENGTH_LONG).show();
                Log.d("Debug", "onFailure: " + t.getMessage());
            }
        });
    }

    private void loadAnswersDetail(List<Rank> resource) {
        if (!resource.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            RankAdapter adapter = new RankAdapter(resource);

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, RankActivity.class);
        context.startActivity(intent);
    }

}
