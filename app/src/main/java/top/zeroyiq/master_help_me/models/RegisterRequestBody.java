package top.zeroyiq.master_help_me.models;

/**
 * Created by ZeroyiQ on 2017/9/4.
 */

public class RegisterRequestBody extends BaseRecord {
    private String email;
    private String phone;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
