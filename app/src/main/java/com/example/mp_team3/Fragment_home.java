package com.example.mp_team3;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fragment_home extends Fragment {
    FloatingActionButton createPost;
    RecyclerView homeRecycler;
    ProductAdapter adapter;
    ArrayList<PostModel> pList;
    int postNum;
    View view;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    RecyclerView.LayoutManager layoutManager;

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


        homeRecycler = (RecyclerView) view.findViewById(R.id.homeRecycler);
        homeRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        homeRecycler.setLayoutManager(layoutManager);
        pList = new ArrayList<>();
        //iList = new ArrayList<>();

        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("posts");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pList.clear();
                //iList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PostModel postModel = dataSnapshot.getValue(PostModel.class);
                    pList.add(postModel);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new ProductAdapter(pList, getContext());
        homeRecycler.setAdapter(adapter);

        return view;
    }

}