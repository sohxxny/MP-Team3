package com.example.mp_team3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    FirebaseAuth mAuth; // 파이어베이스
    BottomNavigationView bottomNavigationView;  // 바텀네비게이션 뷰
    Button logout;  // 로그아웃 버튼 (임시)
    FragmentManager fragmentManager;
    Fragment fragment_home, fragment_chat, fragment_wish, fragment_my;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavi);
        logout = (Button)findViewById(R.id.logout); // 로그아웃 버튼 (임시)

        // 파이어베이스 사용자 정보 가져오기
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        fragmentManager = getSupportFragmentManager();

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

        fragment_home = new Fragment_home();
        // 처음 화면
        fragmentManager.beginTransaction().replace(R.id.main_frame, fragment_home).commit();

        // 바텀 네비게이션 뷰 안의 아이템 설정
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    // 아이템을 클릭 시 id 값을 가져와 프레임 레이아웃에 fragment.xml 띄우기
                    case R.id.item_home:
                        if (fragment_home == null) {
                            Log.e("my", "firstHome");
                            fragment_home = new Fragment_home();
                            fragmentManager.beginTransaction().add(R.id.main_frame, fragment_home).commit();
                        }

                        if (fragment_home != null) fragmentManager.beginTransaction().show(fragment_home).commit();
                        if (fragment_chat != null) fragmentManager.beginTransaction().hide(fragment_chat).commit();
                        if (fragment_wish != null) fragmentManager.beginTransaction().hide(fragment_wish).commit();
                        if (fragment_my != null) fragmentManager.beginTransaction().hide(fragment_my).commit();

                        break;
                    case R.id.item_chat:
                        if (fragment_chat == null) {
                            fragment_chat = new Fragment_chat();
                            fragmentManager.beginTransaction().add(R.id.main_frame, fragment_chat).commit();
                        }

                        if (fragment_home != null) fragmentManager.beginTransaction().hide(fragment_home).commit();
                        if (fragment_chat != null) fragmentManager.beginTransaction().show(fragment_chat).commit();
                        if (fragment_wish != null) fragmentManager.beginTransaction().hide(fragment_wish).commit();
                        if (fragment_my != null) fragmentManager.beginTransaction().hide(fragment_my).commit();

                        break;
                    case R.id.item_wish:
                        if (fragment_wish == null) {
                            fragment_wish= new Fragment_wish();
                            fragmentManager.beginTransaction().add(R.id.main_frame, fragment_wish).commit();
                        }

                        if (fragment_home != null) fragmentManager.beginTransaction().hide(fragment_home).commit();
                        if (fragment_chat != null) fragmentManager.beginTransaction().hide(fragment_chat).commit();
                        if (fragment_wish != null) fragmentManager.beginTransaction().show(fragment_wish).commit();
                        if (fragment_my != null) fragmentManager.beginTransaction().hide(fragment_my).commit();

                        break;
                    case R.id.item_my:
                        if (fragment_my == null) {
                            fragment_my = new Fragment_my();
                            fragmentManager.beginTransaction().add(R.id.main_frame, fragment_my).commit();
                        }

                        if (fragment_home != null) fragmentManager.beginTransaction().hide(fragment_home).commit();
                        if (fragment_chat != null) fragmentManager.beginTransaction().hide(fragment_chat).commit();
                        if (fragment_wish != null) fragmentManager.beginTransaction().hide(fragment_wish).commit();
                        if (fragment_my != null) fragmentManager.beginTransaction().show(fragment_my).commit();

                        break;
                }
                return true;
            }
        });
    }

}