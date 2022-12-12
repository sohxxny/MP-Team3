package com.example.mp_team3;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fragment_wish extends Fragment {
    View view;
    RecyclerView wishRecycler;
    ArrayList<WishModel> wList;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    FirebaseUser user;
    WishAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wish, container, false);

        wishRecycler = (RecyclerView) view.findViewById(R.id.wishRecycler);
        wishRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        wishRecycler.setLayoutManager(layoutManager);
        wList = new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("users/" + user.getUid() +"/wishes");

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    WishModel wishModel = dataSnapshot.getValue(WishModel.class);
                    wList.add(wishModel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new WishAdapter(wList, getContext());
        wishRecycler.setAdapter(adapter);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
}