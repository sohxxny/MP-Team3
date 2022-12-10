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

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private ArrayList<String[]> pList;
    private ArrayList<Uri> iList;
    private Context pContext;

    ProductAdapter(ArrayList<String[]> pList, ArrayList<Uri> iList, Context context) {
        this.pList = pList;
        this.iList = iList;
        this.pContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        ImageButton btnWish;
        TextView tvProduct, tvPlace, tvPrice;

        ViewHolder(View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgProduct);
            btnWish = itemView.findViewById(R.id.btnWish);
            tvProduct = itemView.findViewById(R.id.tvProduct);
            tvPlace = itemView.findViewById(R.id.tvPlace);
            tvPrice = itemView.findViewById(R.id.tvPrice);

            btnWish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnWish.setImageResource(R.drawable.ic_baseline_favorite_24);
                    // wish에 추가 해야됨
                }
            });
        }
    }

    @NonNull
    //뷰홀더 생성
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.product, parent, false);
        return new ProductAdapter.ViewHolder(view);
    }
    //뷰 재활용 메소드
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 제품 정보 가져오기
        String prodTitle = pList.get(position)[0];
        holder.tvProduct.setText(prodTitle);
        String prodPlace = pList.get(position)[1];
        holder.tvPlace.setText(prodPlace);
        String prodPrice = pList.get(position)[2];
        holder.tvPrice.setText(prodPrice);

        Uri imgUri = iList.get(position);
        Glide.with(pContext)
                .load(imgUri)
                .into(holder.imgProduct);

        //클릭시 제품으로 이동 - 제품 액티비티와 연결 필요
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(it)
            }
        });
    }
    //아이템 개수 조회
    @Override
    public int getItemCount() {
        return pList.size();
    }

}
