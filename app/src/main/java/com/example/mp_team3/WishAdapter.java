package com.example.mp_team3;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class WishAdapter extends RecyclerView.Adapter<WishAdapter.ViewHolder> {
    private ArrayList<WishModel>  wList;
    private Context wContext;

    public WishAdapter(ArrayList<WishModel> wList, Context context) {
        this.wList = wList;
        this.wContext = context;
    }


    @NonNull
    @Override
    public WishAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.wish, parent, false);
        return new WishAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 제품 정보 가져오기
        holder.tvWishItemName.setText(wList.get(position).getTitle());
        holder.tvWishItemCategory.setText(wList.get(position).getCategory());
        holder.tvWishItemPrice.setText(wList.get(position).getPrice());

        Glide.with(wContext)
                .load(Uri.parse(wList.get(position).getProdPic()))
                .into(holder.imgWish);

        //클릭시 제품으로 이동 - 제품 액티비티와 연결 필요
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //제품 정보 전달
            }
        });

    }

    @Override
    public int getItemCount() {
        return wList == null ? 0 : wList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgWish;
        ImageButton btnUnwish;
        TextView tvWishItemName, tvWishItemPrice, tvWishItemCategory;

        ViewHolder(View itemView) {
            super(itemView);

            imgWish = itemView.findViewById(R.id.imgWish);
            btnUnwish = itemView.findViewById(R.id.btnUnwish);
            tvWishItemName = itemView.findViewById(R.id.tvWishItemName);
            tvWishItemCategory = itemView.findViewById(R.id.tvWishItemCategory);
            tvWishItemPrice = itemView.findViewById(R.id.tvWishItemPrice);

            btnUnwish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnUnwish.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    //상품목록 wish 버튼 해지
                }
            });
        }
    }
}
