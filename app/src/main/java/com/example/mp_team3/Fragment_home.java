package com.example.mp_team3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Fragment_home extends Fragment {
    FloatingActionButton createPost;
    RecyclerView homeRecycler;
    ProductAdapter adapter;
    ArrayList<String[]> pList = new ArrayList<>();
    ArrayList<Uri> iList = new ArrayList<>();
    public static int postNum;
    View view;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home,container,false);
        createPost = (FloatingActionButton) view.findViewById(R.id.fabCreatePost);

        // FloatingActionButton 누르면 PostActivity로 이동
        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PostActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();

        DocumentReference docRefPostNum = db.collection("posts").document("postNum");
        //post 개수 가져오기
        docRefPostNum.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        postNum = Integer.parseInt(document.getData().get("count").toString());
                    }
                }
            }
        });

        for (int i = 0; i < postNum; i++) {
            //포스트 추가
            DocumentReference docRefPost = db.collection("posts").document("POST" + "_" + i);
            docRefPost.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String title = document.getData().get("title").toString();
                            String price = document.getData().get("price").toString();
                            String [] productInfo = {title, price};
                            pList.add(productInfo);
                        }
                    }
                }
            });
            //포스트 이미지 추가
            StorageReference storageRef = storage.getReference();
            StorageReference imgRef = storageRef.child("postImages/" + "POST_" + i + "_0.jpg");
            imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.d("itemImage" , "itemImage download success");
                    iList.add(uri);
                    System.out.println(iList);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
            Log.e("fragement_home", String.valueOf(i));
        }

        homeRecycler = (RecyclerView) view.findViewById(R.id.homeRecycler);
        homeRecycler.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        homeRecycler.setLayoutManager(layoutManager);
        homeRecycler.setItemAnimator(new DefaultItemAnimator());

        adapter = new ProductAdapter(pList, iList, getContext());
        homeRecycler.setAdapter(adapter);

        return view;
    }

}