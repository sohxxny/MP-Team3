package com.example.mp_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class PostActivity extends AppCompatActivity {

    Button btnPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_post);

        btnPicture = (Button) findViewById(R.id.btnPicture);

    }
}