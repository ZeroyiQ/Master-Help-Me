package top.zeroyiq.master_help_me.apis;

import retrofit2.Call;
import retrofit2.http.GET;
import top.zeroyiq.master_help_me.models.UpdateInfo;

/**
 *  更新接口
 * Created by ZeroyiQ on 2017/6/4.
 */

public interface UpdateApI {
    /**
     * 获得更新信息
     */
    @GET("master_help_me/update/1")
    Call<UpdateInfo> getUpdateInfo();

}
