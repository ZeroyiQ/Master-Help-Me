package top.zeroyiq.master_help_me.models;

/**
 * 问题类
 * Created by ZeroyiQ on 2017/9/8.
 */

public class Questions extends BaseRecord {
    private int id;
    private String title;
    private String content;
    private String time;
    private String user;

    public Questions() {
    }

    public Questions(String title, String content, String user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public Questions(int id, String title, String content, String time, String user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.user = user;
    }

    @Override
    public String toString() {
        return "问题{" +"\n"+
                "题目：" + "   "+title + '\n' +
                "内容：" + "   "+ content + '\n' +
                "用户：" + "   "+ user + "\n"+
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
