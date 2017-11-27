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
import top.zeroyiq.master_help_me.models.Rank;

/**
 * Created by ZeroyiQ on 2017/9/12.
 */

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {

    private List<Rank> rankBodyList;
    private int no;

    public RankAdapter(List<Rank> rankBodyList) {
        this.no=0;
        this.rankBodyList = rankBodyList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_rank_name)
        TextView tvRankName;
        @BindView(R.id.tv_rank)
        TextView tvRank;
        @BindView(R.id.tv_message_content)
        TextView tvQuestionsNum;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rank, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        no+=1;
        Rank body = rankBodyList.get(position);
        holder.tvRankName.setText(body.getUser());
        holder.tvRank.setText(no+"");
        holder.tvQuestionsNum.setText(body.getNum_all()+"");
    }

    @Override
    public int getItemCount() {
        return rankBodyList.size();
    }


}
