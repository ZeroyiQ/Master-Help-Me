package top.zeroyiq.master_help_me.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.zeroyiq.master_help_me.MyApplication;
import top.zeroyiq.master_help_me.R;
import top.zeroyiq.master_help_me.adapter.TabAdapter;
import top.zeroyiq.master_help_me.models.User;
import top.zeroyiq.master_help_me.utils.ACache;

import static top.zeroyiq.master_help_me.MyApplication.questionsCategory;

/**
 * 主页
 * Created by ZeroyiQ on 2017/6/2.
 */

public class HomeActivity extends BaseActivity {

    @BindView(R.id.toolBar_home)
    Toolbar toolBarHome;
    @BindView(R.id.float_btn_home)
    FloatingActionButton floatBtnHome;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.draw_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.pager)
    ViewPager pager;

    private ACache aCache = ACache.get(MyApplication.getAppContext());      // 获取缓存
    User user;
    TextView username;
    TextView email;

    /**
     * 设置启动导航栏按钮
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     *  返回主页的时候关闭导航栏
     */
    @Override
    protected void onResume() {
        super.onResume();
//        navView.setCheckedItem(R.id.nav_bbs);
        drawerLayout.closeDrawers();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = LayoutInflater.from(this).inflate(R.layout.activity_home, null);
        setContentView(parentView);
        ButterKnife.bind(this);
        setSupportActionBar(toolBarHome);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_48px);
        }
//        drawerLayout = (DrawerLayout) findViewById(R.id.draw_layout);
//        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        // 默认选择论坛
        navView.setCheckedItem(R.id.nav_bbs);
        // 设置每一栏的跳转
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_bbs:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_rank:
                        RankActivity.actionStart(HomeActivity.this);
                        break;
                    case R.id.nav_message:
                        MessageActivity.actionStart(HomeActivity.this);
                        break;
                    case R.id.nav_center:
                        CenterActivity.actionStart(HomeActivity.this);
                        break;
                    default:
                        drawerLayout.closeDrawers();
                        break;
                }
                return true;

            }
        });
        // 设置浮动按钮监听
        floatBtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostQuestionActivity.actionStart(HomeActivity.this);
            }
        });
        // 初始化主页
        initView();
        // 初始化导航栏头部
        initHeaderView();

    }

    /**
     * 初始化导航栏头部
     */
    private void initHeaderView() {
        View headerView = navView.getHeaderView(0);
        username = (TextView) headerView.findViewById(R.id.home_username);
        email = (TextView) headerView.findViewById(R.id.home_email);
        user = (User) aCache.getAsObject("user");
        email.setText(user.getEmail());
        if (!user.getName().isEmpty()){
            username.setText(user.getName());
        }
        Log.d("user", "initHeaderView: "+user.toString());
    }

    /**
     * 初始化主页，将 tab 和 pager 绑定，并且适配 pager
     */
    private void initView() {
        // tab 绑定 pager
        tab.setupWithViewPager(pager);

        //  设置对应的 map 键值对
        Map<String, String> map = new HashMap<>();
        map.put(questionsCategory[0], "gdsx");
        map.put(questionsCategory[1], "dxwl");
        map.put(questionsCategory[2], "dxyy");
        map.put(questionsCategory[3], "xxds");
        map.put(questionsCategory[4], "gll");

        // pager 适配器
        pager.setAdapter(new TabAdapter(getSupportFragmentManager(), Arrays.asList(questionsCategory),map));

    }

//    private void initView() {
//        Map<String, String> newsPathMap = new HashMap<>();
//        newsPathMap.put(questionsCategory[0], "gdsx");
//        newsPathMap.put(questionsCategory[1], "dxwl");
//        newsPathMap.put(questionsCategory[2], "dxyy");
//        newsPathMap.put(questionsCategory[3], "xxds");
//        newsPathMap.put(questionsCategory[4], "gll");
//
//        tab.setScrollBar(new DrawableBar(this, R.drawable.bg_tab, ScrollBar.Gravity.BOTTOM) {
//            @Override
//            public int getHeight(int tabHeight) {
//                return tabHeight / 12;
//            }
//
//            @Override
//            public int getWidth(int tabWidth) {
//                return tabWidth;
//            }
//        });
//
//
//        IndicatorViewPager indicatorViewPager = new IndicatorViewPager(tab, pager);
//        indicatorViewPager.setAdapter(new TabAdapter(getSupportFragmentManager(), HomeActivity.this, newsPathMap));
//    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        initView();
    }

    /**
     * 启动主页
     * @param context 上下文
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);

    }
}
