package top.zeroyiq.master_help_me.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.zeroyiq.master_help_me.MyApplication;
import top.zeroyiq.master_help_me.R;
import top.zeroyiq.master_help_me.activities.DetailsActivity;
import top.zeroyiq.master_help_me.adapter.QuestionsAdapter;
import top.zeroyiq.master_help_me.apis.APISource;
import top.zeroyiq.master_help_me.apis.QuestionsAPi;
import top.zeroyiq.master_help_me.models.ErrorMessage;
import top.zeroyiq.master_help_me.models.Questions;
import top.zeroyiq.master_help_me.models.Resource;
import top.zeroyiq.master_help_me.utils.ACache;

/**
 * 问题碎片
 * Created by ZeroyiQ on 2017/9/8.
 */

public class QuestionFragment extends Fragment {

    @BindView(R.id.progressBar_question)
    ProgressBar progressBarQuestion;
    @BindView(R.id.list_question)
    ListView listQuestion;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    private ACache aCache = ACache.get(MyApplication.getAppContext());
    private View rootView;  // fragment 的界面
    private String fragmentName;    // 每个 fragmentTab 的名字
    //    private int currentPage;        // 当前页数
    private List<Questions> queList; // 问题列表

    private String category;        // 对应科目
    private QuestionsAdapter questionsAdapter;

    public static QuestionFragment newsInstance(String fragmentName, String categoryPath) {
        QuestionFragment questionsListFragment = new QuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("fragmentName", fragmentName);
        bundle.putString("categoryPath", categoryPath);
        questionsListFragment.setArguments(bundle);
        return questionsListFragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        Resource<Questions> tempList = ((Resource<Questions>) aCache.getAsObject(fragmentName));
        if (tempList == null) {
            /**
             * ---------调试段落-----------
             */
            getQuestions();
//            initData();

            /**
             * ---------调试段落-----------
             */
        } else {
            loadData(tempList.getResource());
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initRefresh();
        Resource<Questions> tempList = ((Resource<Questions>) aCache.getAsObject(fragmentName));
        if (tempList == null) {
            /**
             * ---------调试段落-----------
             */
            getQuestions();
//            initData();

            /**
             * ---------调试段落-----------
             */
        } else {
            loadData(tempList.getResource());
        }
    }

//    private void initData() {
//        if (category.equals("gdsx")) {
//            for (int i = 0; i < 4; i++) {
//                queList.add(new Questions("-∫(0~2π)e^sint dcost=?\n" +
//                        "为什么？", "原式化为（先当做不定积分化简）" +
//                        "∫xe^xdx=∫xde^x=xe^x-∫e^xdx=xe^x-e^x=e^x(x-1)", "2017-6-8", "/111"));
//                queList.add(new Questions("第五题（X的上角标为2n）,为什么会有间断点," +
//                        "我觉得一般的间断点都是分母不能为0,这题为什么会有间断点1", "是这个样子的,你要看定义,f（x-） =f（x+）\n" +
//                        "在1 的1+ 1- 两边,f（x）不相等,所以就是间断点\n" +
//                        "简短点分两类,第一类可去和第二类无穷,这种属于第二类", "2017-5-14", "/111"));
//            }
//        } else if (category.equals("dxwl")) {
//            queList.clear();
//            for (int i = 0; i < 5; i++) {
//                queList.add(new Questions("在XOY平面内，有一运动的质点，其运动方程为r=2cos5t i +2sin5t j，" +
//                        "则t时刻其切向加速的大小为? 这个切向加速度不是只有坐曲线运动中，与法向加速度共同存在的吗？", "=dr/dt=-10sin5t i+10cos5t j\n" +
//                        "切向加速度=dV/dt=-50sin5t i-50cos5t j\n" +
//                        "ax=-50sin5t\n" +
//                        "ay=-50cos5t\n" +
//                        "切向加速度的大小为\n" +
//                        "a=根号[(ax)^2+(ay)^2]=50", "2017-7-15", "/111"));
//                queList.add(new Questions("非保守内力，就是小球与滑块间的弹力做功和为什么为零？\n" +
//                        " 质量为M半径为R的1/4圆周的光滑弧形滑块，静止在光滑桌面上，今有质量为m的物体", "弹力对滑块做正功，对小球做负功，两者之和为零。因为两者受到的弹力为一对相互作用力，但是位移方向一致，故两者和为零。\n" +
//                        "应用动量守恒和能量守恒。", "2014-02-15", "/111"));
//            }
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_questions_list, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    private void initRefresh() {
        swipeRefresh.setColorSchemeColors(Color.RED, Color.BLUE);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                              @Override
                                              public void onRefresh() {
                                                  new Handler().postDelayed(new Runnable() {
                                                      @Override
                                                      public void run() {
                                                          queList.clear();
//                                                          currentPage = 0;
                                                          /**
                                                           * -----------------调试段-----------------
                                                           */
                                                          getQuestions();
                                                          swipeRefresh.setRefreshing(false);
                                                          /**
                                                           * -----------------调试段-----------------
                                                           */
                                                          Snackbar.make(rootView, "刷新完成", Snackbar.LENGTH_LONG).show();
                                                      }
                                                  }, 1000);
                                              }
                                          }
        );
    }

    /**
     * 得到问题
     */
    private void getQuestions() {
        QuestionsAPi queApi = APISource.getInstance().getAPIObject(QuestionsAPi.class);
        Call<Resource<Questions>> call = queApi.getQuestions(category);
        call.enqueue(new Callback<Resource<Questions>>() {
                         @Override
                         public void onResponse(Call<Resource<Questions>> call, Response<Resource<Questions>> response) {
                             swipeRefresh.setRefreshing(false); // 下拉菜单收起

                             if (response.isSuccessful()) {
                                 Resource<Questions> tempList = response.body();
                                 if (tempList != null) {
                                     aCache.put(fragmentName, tempList); // 缓存数据
                                     loadData(tempList.getResource());
                                 }
                             } else {
                                 ErrorMessage e = APISource.getErrorMessage(response);
                                 onFailure(call, e.toException());
                             }

                         }

                         @Override
                         public void onFailure(Call<Resource<Questions>> call, Throwable t) {
                             swipeRefresh.setRefreshing(false);
                             Snackbar.make(rootView, "获取失败：" + t.getMessage(), Snackbar.LENGTH_LONG).show();
                             Log.d("Debug", "onFailure: " + t.getMessage());
                         }
                     }

        );

    }

    /**
     * 加载数据
     *
     * @param tempList
     */
    private void loadData(List<Questions> tempList) {
        queList.addAll(tempList);
        progressBarQuestion.setVisibility(View.GONE);
//        questionsAdapter.addItem(queList);
//        if (currentPage == 0) {
        listQuestion.setAdapter(questionsAdapter);
//        } else {
//            questionsAdapter.notifyDataSetChanged();// 强制刷新
//        }
//        currentPage++;
    }

    /**
     * 初始化数据
     */
    private void init() {
        Bundle bundle = getArguments();
        fragmentName = bundle.getString("fragmentName");
//        currentPage = 0;
        queList = new ArrayList<>();
        category = bundle.getString("categoryPath");
        questionsAdapter = new QuestionsAdapter(QuestionFragment.this.getActivity(), R.layout.item_question, queList);
        listQuestion.setAdapter(questionsAdapter);
        listQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                queLink = queList.get(position).getUser();
                DetailsActivity.actionStart(getContext(), fragmentName, category,queList.get(position));
            }
        });
    }


//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }
}
