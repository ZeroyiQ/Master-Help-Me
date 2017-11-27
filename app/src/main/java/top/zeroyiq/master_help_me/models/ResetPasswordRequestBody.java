package top.zeroyiq.master_help_me.models;

/**
 * Created by ZeroyiQ on 2017/9/6.
 */

public class ResetPasswordRequestBody extends BaseRecord {
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
