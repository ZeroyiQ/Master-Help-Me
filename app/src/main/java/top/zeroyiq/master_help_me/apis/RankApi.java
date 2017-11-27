package top.zeroyiq.master_help_me.apis;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import top.zeroyiq.master_help_me.models.Answers;
import top.zeroyiq.master_help_me.models.Rank;
import top.zeroyiq.master_help_me.models.Resource;
import top.zeroyiq.master_help_me.models.User;

/**
 * 获取排行
 * Created by ZeroyiQ on 2017/10/22.
 */

public interface RankApi {

    /**
     * 获取排行
     */
    @GET("master_help_me_db/_table/rank_ask")
    Call<Resource<Rank>> getRank();


}
