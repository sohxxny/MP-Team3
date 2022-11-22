package com.example.mp_team3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button btnRegister;
    EditText edInputEmail;
    EditText edInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        edInputEmail = (EditText)findViewById(R.id.edInputEmail);
        edInputPassword = (EditText)findViewById(R.id.edInputPassword);

        // 파이어베이스
        mAuth = FirebaseAuth.getInstance();

        // Register button 클릭 리스너
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
    }

    // 회원 가입
    private void Register() {
        // 문자열로 변환
        String email = edInputEmail.getText().toString();
        String password = edInputPassword.getText().toString();
        // 입력하지 않은 칸이 있을 경우 회원가입 실패

        // 이미 존재하는 이메일 계정이면 회원가입 실패

        // 비밀번호, 비밀번호 확인 다를 시 회원가입 실패

        // 닉네임 중복이면 회원가입 실패

        // 이용약관 동의하지 않으면 회원가입 실패

        // 받아온 값으로 회원 정보 생성
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공 시, 입력받은 정보로 회원 정보 등록 후
                            Toast.makeText(RegisterActivity.this, "성공", Toast.LENGTH_LONG).show();
                            // Login 화면으로 전환
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // 회원가입 실패 시,
                            Toast.makeText(RegisterActivity.this, "실패", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}