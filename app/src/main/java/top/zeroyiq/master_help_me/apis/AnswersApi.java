package top.zeroyiq.master_help_me.apis;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import top.zeroyiq.master_help_me.models.Answers;
import top.zeroyiq.master_help_me.models.Resource;

/**
 * 回答接口
 * Created by ZeroyiQ on 2017/10/18.
 */

public interface AnswersApi {
    /**
     * 获取问题
     */
    @GET("master_help_me_db/_table/{table_name}?limit=10&order=time desc")
    Call<Resource<Answers>> getAnswers(@Path(value = "table_name") String table, @Query(value = "filter") String filter);

    @POST("master_help_me_db/_table/{table_name}")
    Call<ResponseBody> postAnswer(@Path(value = "table_name") String table, @Body Answers[] answers);
}
