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
import top.zeroyiq.master_help_me.models.MessageBody;

/**
 * 消息适配器
 * Created by ZeroyiQ on 2017/9/12.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<MessageBody> bodyList;

    public MessageAdapter(List<MessageBody> bodyList) {
        this.bodyList = bodyList;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MessageBody body = bodyList.get(position);
        holder.tvMessageTitle.setText(body.getTitle());         // 标题
        holder.tvMessageAnswerer.setText(body.getAnswerer());   // 回答者
        holder.tvContent.setText(body.getContent());            // 内容
        holder.tvMessageDate.setText(body.getDate());           // 时间

    }


    @Override
    public int getItemCount() {
        return bodyList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_message_title)
        TextView tvMessageTitle;
        @BindView(R.id.tv_message_answerer)
        TextView tvMessageAnswerer;
        @BindView(R.id.tv_message_content)
        TextView tvContent;
        @BindView(R.id.tv_message_date)
        TextView tvMessageDate;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
