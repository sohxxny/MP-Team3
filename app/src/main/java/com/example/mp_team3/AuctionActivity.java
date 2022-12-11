package com.example.mp_team3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AuctionActivity extends AppCompatActivity {
    Intent intent;
    String title;
    String price;
    String endTime;
    String prodPic;
    ImageView imgAucPic;
    TextView tvAucTitle, tvAucPrice, tvAucTime;
    TimerTask timerTask;
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction);

        intent = getIntent();
        title = intent.getStringExtra("title");
        price = intent.getStringExtra("price");
        endTime = intent.getStringExtra("endTime");
        prodPic = intent.getStringExtra("prodPic");

        imgAucPic = (ImageView) findViewById(R.id.imgAucPic);
        tvAucTitle = (TextView) findViewById(R.id.tvAucTitle);
        tvAucPrice = (TextView) findViewById(R.id.tvAucPrice);
        tvAucTime = (TextView) findViewById(R.id.tvAucTime);

        //제품 정보 세팅
        tvAucTitle.setText(title);
        tvAucPrice.setText(price + "이상");
        Glide.with(this)
                .load(Uri.parse(prodPic))
                .into(imgAucPic);

        startTimerTask();

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

                tvAucTime.post(new Runnable() {
                    @Override
                    public void run() {
                        tvAucTime.setText("남은 시간 " + (endCal.get(Calendar.DATE) * 24 + endCal.get(Calendar.HOUR_OF_DAY))+ " : "
                                + endCal.get(Calendar.MINUTE) + " : " + endCal.get(Calendar.SECOND));
                    }
                });
            }
        };

        timer.schedule(timerTask, 0, 1000);
    }

}