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

    public static final int PICK_FROM_ALBUM = 1;
    private static final String TAG = "RegisterActivity";
    private Uri imageUri;
    private String pathUri;
    private File tempFile;
    private FirebaseStorage mStorage;
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
        mStorage = FirebaseStorage.getInstance();

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

            }
        });


    }

    private void gotoAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_OK) { // 코드가 틀릴경우
            Toast.makeText(RegisterActivity.this, "취소 되었습니다", Toast.LENGTH_SHORT);
            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }
            return;
        }

        switch (requestCode) {
            case PICK_FROM_ALBUM: { // 코드 일치
                // Uri
                imageUri = data.getData();
                pathUri = getPath(data.getData());
                Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + imageUri);
                imgInputProf.setImageURI(imageUri); // 이미지 띄움
                break;
            }
        }
    }

    // uri 절대경로 가져오기
    public String getPath(Uri uri) {

        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();
        return cursor.getString(index);
    }

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

                            final String uid = task.getResult().getUser().getUid();
                            final Uri file = Uri.fromFile(new File(pathUri)); // path

                            StorageReference storageReference = mStorage.getReference()
                                    .child("usersprofileImages").child("uid/"+file.getLastPathSegment());
                            storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    final Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl();
                                    while (!imageUrl.isComplete()) ;

                                    UserModel userModel = new UserModel(nickname, imageUrl.getResult().toString(), uid);

                                    db.collection("users").document(user.getUid()).set(userModel);


                                }

                            });

//                            if(user != null) {
//                                db.collection("users").document(user.getUid()).set(userModel)
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                Toast.makeText(RegisterActivity.this, "회원정보 등록을 성공했습니다", Toast.LENGTH_SHORT);
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Toast.makeText(RegisterActivity.this, "회원정보 등록에 실패했습니다", Toast.LENGTH_SHORT);
//                                                Log.w(TAG, "Error writing document", e);
//                                            }
//                                        });
//                            } else{
//
//                            }
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