package com.example.mp_team3;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
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
    private ArrayList<PostModel> pList;
    private Context pContext;
    static final String TAG = "ProductAdapter";

    public ProductAdapter(ArrayList<PostModel> pList, Context context) {
        this.pList = pList;
        this.pContext = context;
    }

    @NonNull
    //뷰홀더 생성
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "태그 onCreateViewHolder 들어옴");
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.product, parent, false);
        return new ProductAdapter.ViewHolder(view);
    }
    //뷰 재활용 메소드
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 제품 정보 가져오기
        holder.tvProduct.setText(pList.get(position).getTitle());
        holder.tvPrice.setText(pList.get(position).getPrice());

        Glide.with(holder.itemView)
                .load(Uri.parse(pList.get(position).getProdPic()))
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
        //Log.e("productadapter", );
        return pList == null ? 0 : pList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        ImageButton btnWish;
        TextView tvProduct, tvPrice;

        ViewHolder(View itemView) {
            super(itemView);

            imgProduct = (ImageView) itemView.findViewById(R.id.imgProduct);
            btnWish = (ImageButton) itemView.findViewById(R.id.btnWish);
            tvProduct = (TextView) itemView.findViewById(R.id.tvProduct);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);

            btnWish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnWish.setImageResource(R.drawable.ic_baseline_favorite_24);
                    // wish에 추가 해야됨
                }
            });
        }
    }

}
