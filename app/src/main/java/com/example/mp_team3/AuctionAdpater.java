package com.example.mp_team3;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AuctionAdpater extends RecyclerView.Adapter<AuctionAdpater.ViewHolder> {
    private ArrayList<AuctionModel> list;
    private Context mContext;
    static final String TAG = "AuctionAdpater";

    public AuctionAdpater(ArrayList<AuctionModel> list, Context context) {
        this.list = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public AuctionAdpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("경매", "들어옴");
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.auction, parent, false);
        return new AuctionAdpater.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuctionAdpater.ViewHolder holder, int position) {
        // 참여한 사람 정보
        holder.tvAucPriceIn.setText(list.get(position).getSuggestPrice());
        Glide.with(holder.itemView)
                .load(Uri.parse(list.get(position).getProfImg()))
                .into(holder.imgAucJoinProf);
    }

    @Override
    public int getItemCount() {
        Log.e("경매 개수", String.valueOf(list.size()));
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgAucJoinProf;
        TextView tvAucPriceIn;

        ViewHolder(View itemView) {
            super(itemView);

            this.imgAucJoinProf = (CircleImageView) itemView.findViewById(R.id.imgAucJoinProf);
            this.tvAucPriceIn = (TextView) itemView.findViewById(R.id.tvAucPriceIn);

        }

    }
}
