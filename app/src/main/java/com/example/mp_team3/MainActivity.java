package com.example.mp_team3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth; // 파이어베이스
    BottomNavigationView bottomNavigationView;  // 바텀네비게이션 뷰
    Button logout;  // 로그아웃 버튼 (임시)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavi);
        logout = (Button)findViewById(R.id.logout); // 로그아웃 버튼 (임시)

        // 파이어베이스 사용자 정보 가져오기
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // 로그인 여부 확인하기
        if (user == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // 로그아웃 버튼 (임시)
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 처음 화면
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new Fragment_home()).commit();

        // 바텀 네비게이션 뷰 안의 아이템 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    // 아이템을 클릭 시 id 값을 가져와 프레임 레이아웃에 fragment.xml 띄우기
                    case R.id.item_home:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, new Fragment_home()).commit();
                        break;
                    case R.id.item_chat:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, new Fragment_chat()).commit();
                        break;
                    case R.id.item_wish:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, new Fragment_wish()).commit();
                        break;
                    case R.id.item_my:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, new Fragment_my()).commit();
                        break;
                }
                return true;
            }
        });
    }

}