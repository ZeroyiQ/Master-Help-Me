package top.zeroyiq.master_help_me.models;

/**
 * Created by ZeroyiQ on 2017/10/19.
 */

public class NewQuestions {
    private String title;
    private String content;
    private String user;

    public NewQuestions(String title, String content, String user) {
        this.title = title;
        this.content = content;
        this.user = user;
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

    public String getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "问题{" + "\n" +
                "   题目：" + title + '\n' +
                "   内容：" + content + '\n' +
                "   用户：" + user + "\n" +
                '}';
    }

    public void setUser(String user) {
        this.user = user;
    }
}
