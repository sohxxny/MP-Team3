package com.example.mp_team3;

import android.content.Context;
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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class WishAdapter extends RecyclerView.Adapter<WishAdapter.ViewHolder> {
    private ArrayList<String []>  wList;
    private ArrayList<Uri> iList;
    private Context wContext;

    WishAdapter(ArrayList<String[]> wList, ArrayList<Uri> iList, Context context) {
        this.wList = wList;
        this.iList = iList;
        this.wContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgWish;
        ImageButton btnUnwish;
        TextView tvWishItemName, tvWishItemPlace, tvWishItemPrice;

        ViewHolder(View itemView) {
            super(itemView);

            imgWish = itemView.findViewById(R.id.imgWish);
            btnUnwish = itemView.findViewById(R.id.btnUnwish);
            tvWishItemName = itemView.findViewById(R.id.tvWishItemName);
            tvWishItemPlace = itemView.findViewById(R.id.tvWishItemPlace);
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
        String prodTitle = wList.get(position)[0];
        holder.tvWishItemName.setText(prodTitle);
        String prodPlace = wList.get(position)[1];
        holder.tvWishItemPlace.setText(prodPlace);
        String prodPrice = wList.get(position)[2];
        holder.tvWishItemPrice.setText(prodPrice);

        Uri imgUri = iList.get(position);
        Glide.with(wContext)
                .load(imgUri)
                .into(holder.imgWish);

        //클릭시 제품으로 이동 - 제품 액티비티와 연결 필요
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(it)
            }
        });

    }

    @Override
    public int getItemCount() {
        return wList.size();
    }
}
