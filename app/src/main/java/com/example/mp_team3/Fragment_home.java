package com.example.mp_team3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Fragment_home extends Fragment {
    View view;
    AppCompatButton btnGotoSearch;
    FloatingActionButton createPost;
    RecyclerView homeRecycler;
    ArrayList<String[]> pList;
    ArrayList<Uri> iList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home,container,false);
        createPost = (FloatingActionButton) view.findViewById(R.id.fabCreatePost);
        btnGotoSearch = (AppCompatButton) view.findViewById(R.id.btnGotoSearch);

        //
//        homeRecycler = (RecyclerView) view.findViewById(R.id.homeRecycler);
//        homeRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
//
//        ProductAdapter adapter = new ProductAdapter(pList, iList, getActivity().getApplicationContext());
//        homeRecycler.setAdapter(adapter);

        // FloatingActionButton 누르면 PostActivity로 이동
        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PostActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        // 검색창 누르면 Search Activity로 이동
        btnGotoSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Search.class);
                startActivity(intent);
            }
        });

        return view;
    }




}