package top.zeroyiq.master_help_me.models;

/**
 * Created by ZeroyiQ on 2017/9/12.
 */

public class MessageBody extends BaseRecord{
    private int id;

    private String title;

    private String answerer;

    private String content;

    private String date;

    public MessageBody(int id, String title, String answerer, String content, String date) {
        this.id = id;
        this.title = title;
        this.answerer = answerer;
        this.content = content;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnswerer() {
        return answerer;
    }

    public void setAnswerer(String answerer) {
        this.answerer = answerer;
    }


}
