package com.manager.appbanhang.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.manager.appbanhang.R;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        Paper.init(this);
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(1500);
                } catch (Exception e) {

                } finally {
                    if (Paper.book().read("user") == null) {
                        Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent home = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(home);
                        finish();
                    }
                }
            }
        };
        thread.start();
    }
}