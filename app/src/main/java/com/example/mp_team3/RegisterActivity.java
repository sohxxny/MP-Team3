package com.example.mp_team3;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    static final int PICK_FROM_ALBUM = 1;
    static final String TAG = "RegisterActivity";
    Uri imageUri = null;
    FirebaseStorage mStorage;
    FirebaseAuth mAuth;
    Button btnRegister;
    EditText edInputEmail;
    EditText edInputPassword;
    EditText edInputNickname;
    CircleImageView imgInputProf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        edInputEmail = (EditText)findViewById(R.id.edInputEmail);
        edInputPassword = (EditText)findViewById(R.id.edInputPassword);
        edInputNickname = (EditText) findViewById(R.id.edInputNickname);
        imgInputProf = (CircleImageView) findViewById(R.id.imgInputProf);

        // 파이어베이스
        mAuth = FirebaseAuth.getInstance();

        // Register button 클릭 리스너
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
        imgInputProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAlbum();
            }
        });


    }

    private void gotoAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        imageUri = data.getData();
                        Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + imageUri);
                        imgInputProf.setImageURI(imageUri); // 이미지 띄움
                    }
                }
            });

    // 회원 가입
    private void Register() {
        // 문자열로 변환
        String email = edInputEmail.getText().toString();
        String password = edInputPassword.getText().toString();
        String nickname = edInputNickname.getText().toString();
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

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            final String uid = user.getUid();

                            if (imageUri != null) {
                                mStorage = FirebaseStorage.getInstance();
                                StorageReference storageReference = mStorage.getReference()
                                        .child("usersprofileImages/" + uid + ".jpg");
                                storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        Log.i("이미지","테스트");
                                        final Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl();
                                        while (!imageUrl.isComplete()) ;

                                        UserModel userModel = new UserModel(nickname, imageUrl.getResult().toString(), uid);

                                        db.collection("users").document(user.getUid()).set(userModel);
                                    }
                                });
                            } else {
                                UserModel userModel = new UserModel(nickname, null, uid);
                                db.collection("users").document(user.getUid()).set(userModel);
                            }

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