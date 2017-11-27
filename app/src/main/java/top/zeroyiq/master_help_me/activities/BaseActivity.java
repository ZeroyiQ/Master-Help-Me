package top.zeroyiq.master_help_me.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * 基础 Activity
 * Created by ZeroyiQ on 2017/6/2.
 */

public class BaseActivity extends AppCompatActivity {
     View parentView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", "onCreate: " + getClass().getSimpleName());
    }
}
