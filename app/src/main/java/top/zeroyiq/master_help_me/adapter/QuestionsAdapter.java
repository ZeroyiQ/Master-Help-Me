package top.zeroyiq.master_help_me.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import top.zeroyiq.master_help_me.R;
import top.zeroyiq.master_help_me.models.Questions;

/**
 * 问题适配器
 * Created by ZeroyiQ on 2017/9/8.
 */

public class QuestionsAdapter extends ArrayAdapter<Questions> {
    private int resourceId;
    private List<Questions> questionsList;

    public QuestionsAdapter(Context context, int textViewResourced, List<Questions> objects) {
        super(context, textViewResourced, objects);
        this.resourceId = textViewResourced;
        this.questionsList = objects;

    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Questions question = questionsList.get(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.queListTitle = (TextView) view.findViewById(R.id.questionsList_title);
            viewHolder.queListIntro = (TextView) view.findViewById(R.id.questionsList_content);
            viewHolder.queListTime = (TextView) view.findViewById(R.id.questionsList_time);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        if (question != null) {
            viewHolder.queListTitle.setText(question.getTitle());
            viewHolder.queListIntro.setText(question.getContent());
            viewHolder.queListTime.setText(question.getTime());
        }
        return view;
    }

    /**
     * 更新后的列表同步到 listView
     *
     * @param list
     */
//    public void addItem(List<Questions> list) {
//        questionsList = list;
//    }

    /**
     * 优化 listView
     */
    private class ViewHolder {
        private TextView queListTitle, queListIntro, queListTime;
    }

}
