package com.example.mp_team3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class AuctionActivity extends AppCompatActivity {
    Intent intent;
    String title;
    String price;
    String endTime;
    String prodPic;
    ImageView imgAucPic;
    TextView tvAucTitle, tvAucPrice, tvAucTime, tvAucResult;
    EditText etAucPrice;
    ImageButton btnAucSend, btnAucBack;
    TimerTask timerTask;
    Timer timer = new Timer();
    FirebaseDatabase db;
    DatabaseReference dbRef, recRef;
    FirebaseFirestore dbFire;
    RecyclerView aucRecycler;
    RecyclerView.LayoutManager layoutManager;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    AuctionModel auctionModel;
    FirebaseStorage storage;
    String curUserProf;
    CircleImageView aucCurUserProf;
    ArrayList<AuctionModel> list;
    int postNum;
    AuctionAdpater adapter;
    String highestUid;
    int sec = 10;
    Button btnAucClear;
    LinearLayout aucEndLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        intent = getIntent();
        title = intent.getStringExtra("title");
        price = intent.getStringExtra("price");
        endTime = intent.getStringExtra("endTime");
        prodPic = intent.getStringExtra("prodPic");
        postNum = intent.getIntExtra("postNum", 0);

        imgAucPic = (ImageView) findViewById(R.id.imgAucPic);
        tvAucTitle = (TextView) findViewById(R.id.tvAucTitle);
        tvAucPrice = (TextView) findViewById(R.id.tvAucPrice);
        tvAucTime = (TextView) findViewById(R.id.tvAucTime);
        tvAucResult = (TextView) findViewById(R.id.tvAucResult);
        etAucPrice = (EditText) findViewById(R.id.etAucPrice);
        btnAucSend = (ImageButton) findViewById(R.id.btnAucSend);
        btnAucBack = (ImageButton) findViewById(R.id.btnAucBack);
        btnAucClear = (Button) findViewById(R.id.btnAucClear);
        aucCurUserProf = (CircleImageView) findViewById(R.id.aucCurUserProf);
        aucEndLayout = (LinearLayout) findViewById(R.id.aucEndLayout);

        btnAucBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //recycler 세팅
        aucRecycler = (RecyclerView) findViewById(R.id.aucRecycler);
        aucRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        aucRecycler.setLayoutManager(layoutManager);


        //제품 정보 세팅
        tvAucTitle.setText(title);
        tvAucPrice.setText(price + "이상");
        Glide.with(this)
                .load(Uri.parse(prodPic))
                .into(imgAucPic);

        startTimerTask();// 경매 시간 세팅

        list = new ArrayList<>();

        //현재 로그인중인 유저 세팅
        storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child("usersprofileImages/" + user.getUid() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                curUserProf = uri.toString();
                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(aucCurUserProf);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference();

        btnAucSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int suggestPrice = Integer.parseInt(etAucPrice.getText().toString());
                if (suggestPrice <= Integer.parseInt(price)) {
                    Toast.makeText(AuctionActivity.this,  price + "보다 높은 금액을 제시해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    auctionModel = new AuctionModel(user.getUid(), suggestPrice, curUserProf);
                    dbRef.child("posts").child("POST" + "_" + postNum).child("aucJoinUsers").child(user.getUid()).setValue(auctionModel);
                }
                finish();//인텐트 종료
                overridePendingTransition(0, 0);//인텐트 효과 없애기
                Intent intent = getIntent(); //인텐트
                startActivity(intent); //액티비티 열기
                overridePendingTransition(0, 0);
            }
        });

        recRef = db.getReference("posts/POST_" + postNum + "/aucJoinUsers");

        recRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                list.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    AuctionModel recycleAuc = dataSnapshot.getValue(AuctionModel.class);
                                    list.add(recycleAuc);
                                    Collections.sort(list, Collections.reverseOrder());
                                    highestUid = list.get(0).getUid();

                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("AuctionActivity", String.valueOf(error.toException()));
                            }
                        });

        adapter = new AuctionAdpater(list, this);
        aucRecycler.setAdapter(adapter);


    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    private void startTimerTask() {
        SimpleDateFormat transFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        Calendar endCal = Calendar.getInstance();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                Date now = new Date();
                Calendar nowCal = Calendar.getInstance();
                nowCal.setTime(now);

                try {
                    Date end = transFormat.parse(endTime);
                    endCal.setTime(end);
                    endCal.add(Calendar.MONTH, -nowCal.get(Calendar.MONTH) + 1);
                    endCal.add(Calendar.DATE, -nowCal.get(Calendar.DATE));
                    endCal.add(Calendar.HOUR_OF_DAY, -nowCal.get(Calendar.HOUR_OF_DAY));
                    endCal.add(Calendar.MINUTE, -nowCal.get(Calendar.MINUTE));
                    endCal.add(Calendar.SECOND, -nowCal.get(Calendar.SECOND));
                    System.out.println(endCal);

                } catch (Exception e) {
                    System.out.println(e);
                    Log.e("trans Data", "transData Error occur");
                }

                int day = endCal.get(Calendar.DATE);
                int hour = endCal.get(Calendar.HOUR_OF_DAY);
                int min = endCal.get(Calendar.MINUTE);
                int sec = endCal.get(Calendar.SECOND);

//                int day = 0;
//                int hour = 0;
//                int min  = 0;

                tvAucTime.post(new Runnable() {
                    @Override
                    public void run() {
                        //sec--;
                        tvAucTime.setText("남은 시간 " + (day * 24 + hour) + " : "
                                + min + " : " + sec);

                    }
                });

                if (day == 0 && hour == 0 && min == 0 && sec == 0) {
                    timer.cancel();
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);
                    aucEnd();
                }
            }
        };

        timer.schedule(timerTask, 0, 1000);
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            tvAucTime.setText("경매 종료");
            aucEndLayout.setVisibility(View.VISIBLE);
            etAucPrice.setEnabled(false);
            btnAucSend.setEnabled(false);
        }
    };

    public void aucEnd() {

        if (highestUid.equals(user.getUid())) {
            tvAucResult.setText("경매에 낙찰되었습니다!");
        } else {

            tvAucResult.setText("경매가 종료되었습니다");
        }

        btnAucClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference postRef = db.getReference("posts/POST_" + postNum);
                postRef.removeValue();

                Intent intent = new Intent(AuctionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }



}