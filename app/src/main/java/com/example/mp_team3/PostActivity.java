package com.example.mp_team3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class PostActivity extends AppCompatActivity {

    private static final String TAG = "PostActivity";
    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체

    FirebaseAuth mAuth;
    Button btnPicture;
    ImageButton btnPostCancel;
    RecyclerView recyclerView;  // 이미지를 보여줄 리사이클러뷰
    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_post);

        // 파이어베이스 사용자 정보 가져오기
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        btnPicture = (Button) findViewById(R.id.btnPicture);
        btnPostCancel = (ImageButton) findViewById(R.id.btnPostCancel);
        btnPicture = (Button) findViewById(R.id.btnPicture);
        recyclerView = findViewById(R.id.recyclerView);

        // 로그인 여부 확인하기
        if (user == null) {
            Intent intent = new Intent(PostActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // 뒤로가기 버튼
        btnPostCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postCancel();
            }
        });

        // 앨범으로 이동하는 버튼
        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2222);
            }
        });

        // 사진의 X 버튼 누르면 사진 지우기
        adapter = new MultiImageAdapter(uriList, PostActivity.this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                uriList.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, uriList.size());
            }
        });
    }

    // 뒤로가기 버튼 함수
    void postCancel() {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(PostActivity.this)
                .setMessage("게시물이 저장되지 않습니다.\n작성을 취소하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
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

    // 앨범에서 액티비티로 돌아온 후 실행되는 메소드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null){   // 어떤 이미지도 선택하지 않은 경우 (아무 동작 X)
        }
        else{   // 이미지를 하나라도 선택한 경우
            if(data.getClipData() == null){     // 이미지를 하나만 선택한 경우
                Uri imageUri = data.getData();
                uriList.add(imageUri);
                adapter = new MultiImageAdapter(uriList, getApplicationContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
            }
            else{      // 이미지를 여러장 선택한 경우
                ClipData clipData = data.getClipData();
                if(clipData.getItemCount() > 10){   // 선택한 이미지가 11장 이상인 경우
                    Toast.makeText(getApplicationContext(), "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                }
                else{   // 선택한 이미지가 1장 이상 10장 이하인 경우
                    for (int i = 0; i < clipData.getItemCount(); i++){
                        Uri imageUri = clipData.getItemAt(i).getUri();  // 선택한 이미지들의 uri를 가져온다.
                        try {
                            uriList.add(imageUri);  //uri를 list에 담는다.
                        } catch (Exception e) {
                            Log.e(TAG, "사진 선택 에러", e);
                        }
                    }
                    adapter = new MultiImageAdapter(uriList, getApplicationContext());
                    recyclerView.setAdapter(adapter);   // 리사이클러뷰에 어댑터 세팅
                    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));     // 리사이클러뷰 수평 스크롤 적용
                }
            }
        }
    }
}