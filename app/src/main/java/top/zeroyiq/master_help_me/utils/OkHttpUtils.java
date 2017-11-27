package top.zeroyiq.master_help_me.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by ZeroyiQ on 2017/9/4.
 */

public class OkHttpUtils {
    private void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
