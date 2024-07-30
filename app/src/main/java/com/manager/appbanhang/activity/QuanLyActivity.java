package com.manager.appbanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.manager.appbanhang.R;

public class QuanLyActivity extends AppCompatActivity {
    MaterialCardView them_sanpham;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quan_ly);
        initView();
        initControl();
    }

    private void initControl() {
        them_sanpham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ThemSanPhamActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        them_sanpham = findViewById(R.id.layout_add);
    }
}