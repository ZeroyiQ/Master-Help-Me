package top.zeroyiq.master_help_me.models;

/**
 * Created by ZeroyiQ on 2017/9/6.
 */

public class LoginRequestBody extends BaseRecord {
    private String email;
    private String password;

    public LoginRequestBody(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
