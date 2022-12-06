package com.example.mp_team3;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileEdit extends AppCompatActivity {

    private final String TAG = "ProfileEdit";
    CircleImageView imgProfChange;
    EditText etProfNick;
    private FirebaseStorage storage;
    private FirebaseUser user;
    String uid;
    Uri imageUri = null;
    String profExist = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        findViewById(R.id.imgProfChange).setOnClickListener(onClickListener);
        imgProfChange = (CircleImageView) findViewById(R.id.imgProfChange);

        findViewById(R.id.btnProfClear).setOnClickListener(onClickListener);
        etProfNick = (EditText) findViewById(R.id.etProfNick);

        storage = FirebaseStorage.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String oldNick = document.getData().get("nickname").toString();
                        etProfNick.setText(oldNick);
                        profExist = document.getData().get("profileImageUrl").toString();
                    }
                }
            }
        });

//        StorageReference storageRef = storage.getReference();
//        StorageReference riverRef = storageRef.child("usersprofileImages/" + uid + ".jpg");
//        riverRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imgProfChange:
                    loadAlbum();
                    break;
                case R.id.btnProfClear:
                    sendData();
                    break;
                case R.id.btnProfCancle:
                    Fragment_my fragmentMy = new Fragment_my();
                    getSupportFragmentManager().beginTransaction().add(R.id.myLinear,fragmentMy).commit();
                    break;
            }
        }
    };

    private void loadAlbum() {
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

                        try {
                            InputStream in = getContentResolver().openInputStream(data.getData());
                            Bitmap img = BitmapFactory.decodeStream(in);
                            in.close();
                            imgProfChange.setImageBitmap(img);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (profExist == null) {
                            profExist = "AddProfImg";
                        };
                    }
                }
            });

    public void sendData() {
        String nickname = etProfNick.getText().toString();

        user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.update("nickname", nickname).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "DocumentSnapshot successfully updated!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error updating document", e);
            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference riverRef = storageRef.child("usersprofileImages/" + uid + ".jpg");

        if (profExist != null && profExist != "AddProfImg") {

            riverRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }

        if (imageUri != null) {
            UploadTask uploadTask = riverRef.putFile(imageUri);

            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Task<Uri>imageUrl = task.getResult().getStorage().getDownloadUrl();
                    while (!imageUrl.isComplete());

                }
            });

        }

        Fragment_my fragmentMy = new Fragment_my();
        getSupportFragmentManager().beginTransaction().add(R.id.myLinear,fragmentMy).commit();
    }

}

