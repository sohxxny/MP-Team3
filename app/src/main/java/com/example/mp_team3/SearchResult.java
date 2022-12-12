package com.example.mp_team3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SearchResult extends AppCompatActivity {

    TextView tvSearchResult;
    RecyclerView searchRecycler;
    RecyclerView.LayoutManager layoutManager;
    ImageButton btnSearchResultCancel;
    ArrayList<PostModel> pList;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        tvSearchResult = (TextView) findViewById(R.id.tvSearchResult);
        searchRecycler = (RecyclerView) findViewById(R.id.searchRecycler);
        btnSearchResultCancel = (ImageButton) findViewById(R.id.btnSearchResultCancel);

        // 뒤로가기 버튼
        btnSearchResultCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchResult.this, Search.class);
                startActivity(intent);
                finish();
            }
        });

        // 검색어를 이전 액티비티로부터 받아와 보여주기
        Intent intent = getIntent();
        String searchItem = intent.getStringExtra("searchItem");
        tvSearchResult.setText(searchItem);

        // 파이어베이스에서 문서 불러오기
        searchRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        searchRecycler.setLayoutManager(layoutManager);
        pList = new ArrayList<>();

        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("posts");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pList.clear();
                //iList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PostModel postModel = dataSnapshot.getValue(PostModel.class);
                    String postTitle = postModel.getTitle();
                    if (postTitle.indexOf(searchItem) > -1) {
                        pList.add(postModel);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new ProductAdapter(pList, getApplicationContext());
        searchRecycler.setAdapter(adapter);
    }
}