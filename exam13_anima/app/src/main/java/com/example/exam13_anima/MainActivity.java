package com.example.exam13_anima;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    FrameLayout Background;

    ImageView Cloud1, Cloud2, Cloud3;
    ImageView Pig1, Pig2;

    Animation AnimCloud1, AnimCloud2, AnimCloud3, AnimPigwalking, AnimOpenning, AnimPigLeftUp, AnimPigRigthUp, AnimCenterUp;

    MediaPlayer Bgm_title, Bgm_walk, Bgm_popup;

    ImageButton BtnMole, BtnCard;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        AnimPigwalking.cancel();
        AnimOpenning.cancel();
        AnimPigLeftUp.cancel();
        AnimPigRigthUp.cancel();

        BtnMole.setVisibility(View.VISIBLE);
        BtnCard.setVisibility(View.VISIBLE);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bgm_title = MediaPlayer.create(this, R.raw.bg);
        Bgm_title.start();
        Bgm_walk = MediaPlayer.create(this, R.raw.walk);
        Bgm_popup = MediaPlayer.create(this, R.raw.sideup);

        BtnMole = (ImageButton) findViewById(R.id.btn_mole);
        BtnMole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MoleActivity.class);
                startActivity(intent);
            }
        });

        BtnCard = (ImageButton) findViewById(R.id.btn_card);
        BtnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CardMatchActivity.class);
                startActivity(intent);
            }
        });

        Background = (FrameLayout) findViewById(R.id.back_ground);

        Cloud1 = (ImageView) findViewById(R.id.img_cloud1);
        Cloud2 = (ImageView) findViewById(R.id.img_cloud2);
        Cloud3 = (ImageView) findViewById(R.id.img_cloud3);

        Pig1 = (ImageView) findViewById(R.id.img_pig1);
        Pig2 = (ImageView) findViewById(R.id.img_pig2);

        AnimOpenning = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.openning_anim);

        AnimCloud1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cloud1anim);
        AnimCloud2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cloud2anim);
        AnimCloud3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cloud3anim);

        AnimPigwalking = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pig1_walking_anim);
        AnimPigLeftUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pig_left_up_anim);
        AnimPigRigthUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pig_rigth_up_anim);
        AnimCenterUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pig_center_up_anim);

        Background.startAnimation(AnimOpenning);

        Cloud1.startAnimation(AnimCloud1);
        Cloud2.startAnimation(AnimCloud2);
        Cloud3.startAnimation(AnimCloud3);


        AnimOpenning.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Pig1.setVisibility(View.VISIBLE);
                Pig1.startAnimation(AnimPigwalking);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        AnimPigwalking.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Bgm_walk.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Pig1.setVisibility(View.GONE);
                Pig2.setVisibility(View.VISIBLE);
                Pig2.setRotation(45);
                Pig2.startAnimation(AnimPigLeftUp);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        AnimPigLeftUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Bgm_popup.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Pig2.setRotation(-45);
                Pig2.startAnimation(AnimPigRigthUp);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        AnimPigRigthUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Bgm_popup.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Pig2.setRotation(0);
                Pig2.startAnimation(AnimCenterUp);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        AnimCenterUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Bgm_popup.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                BtnMole.setVisibility(View.VISIBLE);
                BtnCard.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });



    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(Bgm_title.isPlaying()) {
            Bgm_title.stop();
        }
    }

}

