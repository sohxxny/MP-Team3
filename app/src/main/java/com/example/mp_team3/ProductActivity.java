package com.example.mp_team3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;
import io.grpc.Context;

public class ProductActivity extends AppCompatActivity {

    private Intent intent;
    String title;
    String price;
    String category;
    String detail;
    String sellerId;
    String prodPic;
    String endTime;
    String postingTime;
    ImageView imgItemPic;
    CircleImageView imgSellerProf;
    TextView tvSeller,tvProdTitle, tvProdPrice, tvProdCategory, tvProdDetail;
    FirebaseStorage mStorage;
    FirebaseFirestore db;
    String hasProf;
    int postNum;
    Button btnInAuction;
    private final String TAG = "ProductActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        intent = getIntent();
        title = intent.getStringExtra("title");
        price = intent.getStringExtra("price");
        category = intent.getStringExtra("category");
        detail = intent.getStringExtra("detail");
        sellerId = intent.getStringExtra("sellerId");
        postNum = intent.getIntExtra("postNum", 0);
        prodPic = intent.getStringExtra("prodPic");
        endTime = intent.getStringExtra("endTime");
        postingTime = intent.getStringExtra("postingTime");

        tvSeller = (TextView) findViewById(R.id.tvSeller);
        tvProdTitle = (TextView) findViewById(R.id.tvProdTitle);
        tvProdPrice = (TextView) findViewById(R.id.tvProdPrice);
        tvProdCategory = (TextView) findViewById(R.id.tvProdCategory);
        tvProdDetail = (TextView) findViewById(R.id.tvProdDetail);
        imgSellerProf = (CircleImageView) findViewById(R.id.imgSellerProf);
        imgItemPic = (ImageView) findViewById(R.id.imgItemPic);
        btnInAuction = (Button) findViewById(R.id.btnInAuction);

        //제품 정보 세팅
        tvProdTitle.setText(title);
        tvProdPrice.setText("최소 금액: " + price);
        tvProdCategory.setText(category);
        tvProdDetail.setText(detail);
        Glide.with(this)
                .load(Uri.parse(prodPic))
                .into(imgItemPic);

        //post 게시자 닉네임 받아오기
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(sellerId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            if (document.getData() != null) {
                                String nickname = document.getData().get("nickname").toString();
                                tvSeller.setText(nickname);
                                hasProf = document.getData().get("profileImageUrl").toString();
                                if (hasProf != null) {
                                    mStorage = FirebaseStorage.getInstance();
                                    StorageReference storageRef = mStorage.getReference();
                                    storageRef.child("usersprofileImages/" + sellerId + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            //Glide.with(this).load(uri).into(imgSellerProf);
                                            Glide.with(getApplicationContext()).load(uri).into(imgSellerProf);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                }
                            }
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }

                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        //경매 참여 버튼
        btnInAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this, AuctionActivity.class);
                intent.putExtra("endTime", endTime);
                intent.putExtra("postingTime", postingTime);
                intent.putExtra("price", price);
                intent.putExtra("title", title);
                intent.putExtra("prodPic", prodPic);
                intent.putExtra("postNum", postNum);
                startActivity(intent);
            }
        });

    }
}