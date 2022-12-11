package com.example.mp_team3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;

public class Search extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText etSearch;
    Button btnSearch;
    ImageButton btnSearchCancel;
    Button btnTagDelete;
    RecyclerView recyclerSearch;
    String searchItem = "";
    ArrayList<String> searchList;
    SearchItemAdapter adapter;
    private final String TAG = "Search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 파이어베이스 사용자 정보 가져오기
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        etSearch = (EditText) findViewById(R.id.etSearch);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnTagDelete = (Button) findViewById(R.id.btnTagDelete);
        recyclerSearch = findViewById(R.id.recyclerSearch) ;
        btnSearchCancel = (ImageButton) findViewById(R.id.btnSearchCancel);

        // 로그인 여부 확인하기
        if (user == null) {
            Intent intent = new Intent(Search.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // 최근 검색한 키워드 보여주기
        DocumentReference docRef = db.collection("users").document(user.getUid());
        searchList = new ArrayList<>();
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    searchList.clear();
                    searchList = (ArrayList<String>) (ArrayList<?>) document.getData().get("searchItem");
                    Collections.reverse(searchList);;   // 마지막 검색한 키워드가 앞으로 나오도록 뒤집기
                    // 리사이클러뷰에 최근 검색어 나열
                    adapter = new SearchItemAdapter(searchList) ;
                    recyclerSearch.setAdapter(adapter) ;
                    recyclerSearch.setLayoutManager(new LinearLayoutManager(Search.this, LinearLayoutManager.HORIZONTAL, true));
                }
            }
        });

        // 뒤로가기 버튼
        btnSearchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 전체 삭제 버튼 누르면 실행
        btnTagDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder msgBuilder = new AlertDialog.Builder(Search.this)
                        .setMessage("최근 검색어를 삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override // 다이얼로그 확인 버튼 누르면 실행
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DocumentReference docRef = db.collection("users").document(user.getUid());
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot document = task.getResult();
                                        ArrayList<String> deleteTag = new ArrayList<>();
                                        deleteTag.clear();
                                        deleteTag = (ArrayList<String>) (ArrayList<?>) document.getData().get("searchItem");
                                        for (int i = 0; i < deleteTag.size(); i++) {
                                            docRef.update("searchItem", FieldValue.arrayRemove(deleteTag.get(i)));
                                        }
                                    }
                                });
                                // ############### 새로고침 하는 코드 필요 ###############
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                AlertDialog msgDlg = msgBuilder.create();
                msgDlg.show();
            }
        });

        // 검색 버튼 누르면 실행
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 검색창에 입력한 단어 저장
                searchItem = etSearch.getText().toString();
                // 아무것도 입력되지 않았으면 토스트 메시지 실행
                if (searchItem.equals("")) {
                    Toast.makeText(Search.this, "검색 키워드를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    // 파이어베이스에서 제목에 검색 단어가 포함된 post 불러오기

                    // List 리사이클러뷰에 정렬하기

                    // 검색한 단어를 users의 searchItem 배열에 저장
                    DocumentReference docRef = db.collection("users").document(user.getUid());
                    docRef.update("searchItem", FieldValue.arrayUnion(searchItem));
                }
            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return false;
    }
}