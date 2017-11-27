package top.zeroyiq.master_help_me.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import top.zeroyiq.master_help_me.R;
import top.zeroyiq.master_help_me.adapter.MessageAdapter;
import top.zeroyiq.master_help_me.models.MessageBody;

public class MessageActivity extends BaseActivity {
    private List<MessageBody> bodyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initList();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_message);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        MessageAdapter adapter = new MessageAdapter(bodyList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initList() {
        for (int i = 0; i < 5; i++) {
            bodyList.add(new MessageBody(1, "-∫(0~2π)e^sint dcost=?\n" +
                    "为什么？", "admin", "原式化为（先当做不定积分化简）" +
                    "∫xe^xdx=∫xde^x=xe^x-∫e^xdx=xe^x-e^x=e^x(x-1)", "2017-6-8"));

            bodyList.add(new MessageBody(1, "第五题（X的上角标为2n）,为什么会有间断点," +
                    "我觉得一般的间断点都是分母不能为0,这题为什么会有间断点1",
                    "admin", "是这个样子的,你要看定义,f（x-） =f（x+）\n" +
                    "在1 的1+ 1- 两边,f（x）不相等,所以就是间断点\n" +
                    "简短点分两类,第一类可去和第二类无穷,这种属于第二类", "2017-5-14"));


            bodyList.add(new MessageBody(1, "高等数学", "admin", getRandomLengthContent("789"), "2017-5-12"));
            bodyList.add(new MessageBody(1, "高等数学", "admin", getRandomLengthContent("123"), "2017-5-11"));

        }

    }

    private String getRandomLengthContent(String content) {
        Random random = new Random();
        int i = random.nextInt(20) + 1;
        StringBuffer buffer = new StringBuffer();
        for (int j = 0; j < i; j++) {
            buffer.append(content);
        }
        return buffer.toString();
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MessageActivity.class);
        context.startActivity(intent);
    }

}
