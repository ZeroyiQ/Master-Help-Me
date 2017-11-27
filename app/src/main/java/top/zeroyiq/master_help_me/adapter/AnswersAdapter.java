package top.zeroyiq.master_help_me.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.zeroyiq.master_help_me.R;
import top.zeroyiq.master_help_me.models.Answers;

/**
 * 消息适配器
 * Created by ZeroyiQ on 2017/9/12.
 */

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.ViewHolder> {

    private List<Answers> bodyList;

    public AnswersAdapter(List<Answers> bodyList) {
        this.bodyList = bodyList;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Answers body = bodyList.get(position);

        holder.tvItemAnswer.setText(body.getUser());               // 回答者
        holder.tvItemContent.setText(body.getAnswer());            // 内容
        holder.tvItemDate.setText(body.getTime());                 // 时间

    }


    @Override
    public int getItemCount() {
        return bodyList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_content)
        TextView tvItemContent;
        @BindView(R.id.tv_item_answer)
        TextView tvItemAnswer;
        @BindView(R.id.tv_item_date)
        TextView tvItemDate;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}