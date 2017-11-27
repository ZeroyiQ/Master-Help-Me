package top.zeroyiq.master_help_me.apis;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import top.zeroyiq.master_help_me.models.Answers;
import top.zeroyiq.master_help_me.models.NewQuestions;
import top.zeroyiq.master_help_me.models.Questions;
import top.zeroyiq.master_help_me.models.Resource;

/**
 * 问题获取接口
 * Created by ZeroyiQ on 2017/9/8.
 */

public interface QuestionsAPi {
    /**
     * 获取问题
     */
    @GET("master_help_me_db/_table/{table_name}?limit=10&order=time desc")
    Call<Resource<Questions>> getQuestions(@Path(value = "table_name") String table);

    /**
     * 提交问题
     * @param table
     * @param answers
     * @return
     */
    @POST("master_help_me_db/_table/{table_name}")
    Call<ResponseBody> postQuestions(@Path(value = "table_name") String table, @Body Questions[] answers);
//    @POST("master_help_me/_table/gdsx")
//    Call<ResponseBody> postNewQuestions(@Body Questions[] postQuestions);
}
