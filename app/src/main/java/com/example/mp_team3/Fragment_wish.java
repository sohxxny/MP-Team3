package com.example.mp_team3;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class Fragment_wish extends Fragment {
    View view;
    RecyclerView wishRecycler;
    ArrayList<String[]> wList;
    ArrayList<Uri> iList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wish, container, false);

        wishRecycler = (RecyclerView) view.findViewById(R.id.wishRecycler);
        wishRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));

        WishAdapter adapter = new WishAdapter(wList, iList, getActivity().getApplicationContext());
        wishRecycler.setAdapter(adapter);

        return view;
    }
}