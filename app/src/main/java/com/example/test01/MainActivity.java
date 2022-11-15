package com.example.test01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;  // 바텀네비게이션 뷰

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavi);

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