package com.example.mp_team3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private ArrayList<PostModel> pList;
    private Context pContext;
    static final String TAG = "ProductAdapter";
    FirebaseDatabase db;
    DatabaseReference dbRef;
    FirebaseUser user;

    public ProductAdapter(ArrayList<PostModel> pList, Context context) {
        this.pList = pList;
        this.pContext = context;
    }

    @NonNull
    //뷰홀더 생성
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

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
        holder.tvCategory.setText(pList.get(position).getCategory());

        Glide.with(holder.itemView)
                .load(Uri.parse(pList.get(position).getProdPic()))
                .into(holder.imgProduct);


        //기존 wish 판별
        DatabaseReference wishRef = db.getReference("users/" + user.getUid() + "/wishes/WISH_" + pList.get(position).getPostNum());
        wishRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                WishModel value = snapshot.getValue(WishModel.class);
                if (value != null) {
                    holder.isWished = true;
                    holder.btnWish.setImageResource(R.drawable.ic_baseline_favorite_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //클릭시 제품으로 이동
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //제품 정보 전달
                String title = pList.get(holder.getAbsoluteAdapterPosition()).getTitle();
                String price = pList.get(holder.getAbsoluteAdapterPosition()).getPrice();
                String category = pList.get(holder.getAbsoluteAdapterPosition()).getCategory();
                int postNum = pList.get(holder.getAbsoluteAdapterPosition()).getPostNum();
                String prodPic = pList.get(holder.getAbsoluteAdapterPosition()).getProdPic();
                String detail = pList.get(holder.getAbsoluteAdapterPosition()).getDetail();
                String sellerId = pList.get(holder.getAbsoluteAdapterPosition()).getUid();
                String endTime = pList.get(holder.getAbsoluteAdapterPosition()).getEndTime();
                String postingTime = pList.get(holder.getAbsoluteAdapterPosition()).getPostingTime();
                Intent intent = new Intent(pContext, ProductActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("price", price);
                intent.putExtra("category", category);
                intent.putExtra("detail", detail);
                intent.putExtra("sellerId",sellerId);
                intent.putExtra("postNum", postNum);
                intent.putExtra("prodPic", prodPic);
                intent.putExtra("endTime", endTime);
                intent.putExtra("postingTime", postingTime);
                pContext.startActivity(intent);
            }
        });

        holder.btnWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.isWished) {
                    // wish 추가
                    holder.isWished = true;
                    holder.btnWish.setImageResource(R.drawable.ic_baseline_favorite_24);
                    WishModel wishModel = new WishModel(pList.get(holder.getAbsoluteAdapterPosition()).getTitle(),
                            pList.get(holder.getAbsoluteAdapterPosition()).getProdPic(),
                            pList.get(holder.getAbsoluteAdapterPosition()).getPrice(),
                            pList.get(holder.getAbsoluteAdapterPosition()).getCategory(),
                            true,
                            pList.get(holder.getAbsoluteAdapterPosition()).getPostNum());
                    dbRef.child("users").child(user.getUid()).child("wishes").child("WISH_" +
                            String.valueOf(pList.get(holder.getAbsoluteAdapterPosition()).getPostNum())).setValue(wishModel);
                } else {
                    //wish 삭제
                    holder.isWished = false;
                    holder.btnWish.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    dbRef.child("users").child(user.getUid()).child("wishes").child("WISH_" +
                            String.valueOf(pList.get(holder.getAbsoluteAdapterPosition()).getPostNum())).removeValue();
                }
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
        TextView tvProduct, tvPrice, tvCategory;
        boolean isWished = false;

        ViewHolder(View itemView) {
            super(itemView);

            imgProduct = (ImageView) itemView.findViewById(R.id.imgProduct);
            btnWish = (ImageButton) itemView.findViewById(R.id.btnWish);
            tvProduct = (TextView) itemView.findViewById(R.id.tvProduct);
            tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);

        }
    }

}
