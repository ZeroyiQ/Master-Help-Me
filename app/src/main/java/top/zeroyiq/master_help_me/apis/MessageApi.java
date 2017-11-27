package top.zeroyiq.master_help_me.apis;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import top.zeroyiq.master_help_me.models.MessageBody;
import top.zeroyiq.master_help_me.models.Rank;
import top.zeroyiq.master_help_me.models.Resource;

/**
 * 获取消息
 * Created by ZeroyiQ on 2017/10/22.
 */

public interface MessageApi {

    /**
     * 获取消息
     */
    @GET("master_help_me_db/_table/message")
    Call<Resource<MessageBody>> getMessage( @Query(value = "filter") String filter);


}
