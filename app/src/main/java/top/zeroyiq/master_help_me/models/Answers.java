package top.zeroyiq.master_help_me.models;

/**
 * Created by ZeroyiQ on 2017/9/8.
 */

public class Answers extends BaseRecord {
    private int ans_id;
    private String answer;
    private String time;
    private String user;

    public Answers() {
    }

    public Answers(int ans_id, String answer, String time, String user) {
        this.ans_id = ans_id;
        this.answer = answer;
        this.time = time;
        this.user = user;
    }

    public Answers(int ans_id, String answer, String user) {
        this.ans_id = ans_id;
        this.answer = answer;
        this.user = user;

    }

    @Override

    public String toString() {
        return "回答{" +"\n"+
                "内容：  " + answer + '\n' +
                "用户名：  " + user + '\n' +
                '}';
    }

    public int getAns_id() {
        return ans_id;
    }

    public void setAns_id(int ans_id) {
        this.ans_id = ans_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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
