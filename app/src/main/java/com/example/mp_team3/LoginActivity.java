package com.example.mp_team3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    String nick;
    FirebaseAuth mAuth;
    EditText edId;
    EditText edPassword;
    Button btnSignIn;
    Button btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edId = (EditText) findViewById(R.id.edId);
        edPassword = (EditText) findViewById(R.id.edPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnJoin = (Button) findViewById(R.id.btnJoin);

        // 파이어베이스 초기화
        mAuth = FirebaseAuth.getInstance();

        // 로그인 버튼 리스너
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
            }
        });

        // 회원가입 버튼 리스너
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    private void SignIn() {
        String email = edId.getText().toString().trim();
        String password = edPassword.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공 시 메인 화면으로 전환
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // 로그인 실패 시
                            if (task.getException() != null) {
                                String errorText = task.getException().toString();
                                Log.e("로그인 오류", errorText);
                            }
                        }
                    }
                });
    }
}