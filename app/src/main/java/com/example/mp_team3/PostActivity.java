package com.example.mp_team3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.units.qual.C;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PostActivity extends AppCompatActivity {

    private static final String TAG = "PostActivity";
    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체

    FirebaseAuth mAuth;
    Button btnPicture;
    ImageButton btnPostCancel;
    Spinner spnCategory;
    ImageButton btnTime;
    TextView tvTime;
    EditText etExplain;
    EditText etTitle;
    EditText etPrice;
    Button btnPostClear;
    String category;
    RecyclerView recyclerView;  // 이미지를 보여줄 리사이클러뷰
    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터
    //postModel 전달
    String uid;
    String title;
    String price;
    String postingTime;
    String endTime;
    String detail;
    int postNum;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_post);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // 파이어베이스 사용자 정보 가져오기
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        btnPicture = (Button) findViewById(R.id.btnPicture);
        btnPostCancel = (ImageButton) findViewById(R.id.btnPostCancel);
        btnPicture = (Button) findViewById(R.id.btnPicture);
        spnCategory = (Spinner) findViewById(R.id.spnCategory);
        btnTime = (ImageButton) findViewById(R.id.btnTime);
        tvTime = (TextView) findViewById(R.id.tvTime);
        etExplain = (EditText) findViewById(R.id.etExplain);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etPrice = (EditText) findViewById(R.id.etPrice);
        btnPostClear = (Button) findViewById(R.id.btnPostClear);
        recyclerView = findViewById(R.id.recyclerView);

        // 로그인 여부 확인하기
        if (user == null) {
            Intent intent = new Intent(PostActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        //post 개수 받아오기
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("posts").document("postNum");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            if (document.getData() != null) {
                                postNum = Integer.parseInt(document.getData().get("count").toString());
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
                startActivityForResult(intent, 1111);
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

        // 카테고리 드롭다운 (카테고리 선택 시 실행)
        ArrayAdapter spnAdapter = ArrayAdapter.createFromResource(this, R.array.spnCategory
                , android.R.layout.simple_spinner_dropdown_item);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(spnAdapter);
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                category = "카테고리";
            }
        });

        // 경매 마감일자 (날짜 선택)
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(PostActivity.this, myDatePicker, myCalendar.get(Calendar.YEAR)
                        , myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // 글쓰기 완료 버튼 클릭 시 실행
        btnPostClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PostActivity.this, category, Toast.LENGTH_SHORT).show();
                String str1 = etTitle.getText().toString();
                String str2 = etPrice.getText().toString();
                String str3 = tvTime.getText().toString();
                String str4 = etExplain.getText().toString();
                String str5 = category;

                //파이어스토어 연결
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                uid = user.getUid();
                title = etTitle.getText().toString();
                price = etPrice.getText().toString();
                postingTime = new Date().toString();
                Date end = new Date(myCalendar.getTimeInMillis());
                endTime = end.toString();
                detail = etExplain.getText().toString();

                docRef.update("count",postNum+1 ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "postNum successfully updated!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

               // 스토리지에 사진 올리기기
                FirebaseStorage storage = FirebaseStorage.getInstance();

                for (int i = 0; i < uriList.size(); i++) {
                    StorageReference storageRef = storage.getReference()
                            .child("postImages/" + "POST_" + postNum + "_" + i + ".jpg");

                    int getI = i;
                    storageRef.putFile(uriList.get(i)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            ////첫 사진 받아오기 및 database 추가
                            if (getI == 0) {
                                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imgUri = uri;
                                        PostModel postModel = new PostModel(user.getUid(), imgUri.toString(), title, price, category, postingTime, endTime, detail, postNum);
                                        mDatabase.child("posts").child("POST" + "_" + postNum).setValue(postModel);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                            }
                        }
                    });
                }

                Fragment_home fragmentHome = new Fragment_home();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.postLinear, fragmentHome);
                fragmentTransaction.commit();

                finish();
            }
        });
    }

    // 캘린더 띄우고 텍스트 출력
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy년 MM월 dd일 이후";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
            tvTime.setText(sdf.format(myCalendar.getTime()));
        }
    };

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

        if (requestCode == 1111) {  // 갤러리 접근 코드 1111
            if (data == null) {   // 어떤 이미지도 선택하지 않은 경우 (아무 동작 X)
            } else {   // 이미지를 하나라도 선택한 경우
                if (data.getClipData() == null) {     // 이미지를 하나만 선택한 경우
                    Uri imageUri = data.getData();
                    uriList.add(imageUri);
                    adapter = new MultiImageAdapter(uriList, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
                } else {      // 이미지를 여러장 선택한 경우
                    ClipData clipData = data.getClipData();
                    if (clipData.getItemCount() > 10) {   // 선택한 이미지가 11장 이상인 경우
                        Toast.makeText(getApplicationContext(), "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                    } else {   // 선택한 이미지가 1장 이상 10장 이하인 경우
                        for (int i = 0; i < clipData.getItemCount(); i++) {
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            postCancel();
            return true;
        }
        return false;
    }

}