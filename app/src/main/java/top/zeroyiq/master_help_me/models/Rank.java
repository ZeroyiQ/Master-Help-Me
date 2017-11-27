package top.zeroyiq.master_help_me.models;

/**
 * 排行模型
 * Created by ZeroyiQ on 2017/10/22.
 */

public class Rank extends BaseRecord {
    private String user;
    private int num_all;

    public Rank() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getNum_all() {
        return num_all;
    }

    public void setNum_all(int num_all) {
        this.num_all = num_all;
    }
}
