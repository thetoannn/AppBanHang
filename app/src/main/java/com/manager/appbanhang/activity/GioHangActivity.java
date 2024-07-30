package com.manager.appbanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.manager.appbanhang.R;
import com.manager.appbanhang.adapter.GioHangAdapter;
import com.manager.appbanhang.model.EventBus.TinhTongEvent;
import com.manager.appbanhang.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

public class GioHangActivity extends AppCompatActivity {
    TextView giohangtrong, tongtien;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btn_muahang;
    GioHangAdapter adapter;
    long tongtiensp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gio_hang);
        initView();
        initControl();
        tinTongTien();
    }

    private void tinTongTien() {
        tongtiensp = 0;

        for (int i = 0; i < Utils.mang_mua_hang.size(); i++) {
            tongtiensp = tongtiensp + (Utils.mang_mua_hang.get(i).getGiasp() * Utils.mang_mua_hang.get(i).getSoluong());
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        tongtien.setText(decimalFormat.format(tongtiensp));
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if (Utils.mang_gio_hang.size() == 0) {
            giohangtrong.setVisibility(View.VISIBLE);
        } else {
            adapter = new GioHangAdapter(getApplicationContext(), Utils.mang_gio_hang);
            recyclerView.setAdapter(adapter);
        }

        btn_muahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ThanhToanActivity.class);
                intent.putExtra("tongtien", tongtiensp);
                Utils.mang_gio_hang.clear();
                startActivity(intent);
            }
        });
    }

    private void initView() {
        giohangtrong = findViewById(R.id.txt_giohangtrong);
        tongtien = findViewById(R.id.txt_tongtien);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerview_giohang);
        btn_muahang = findViewById(R.id.btn_muahang);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventTinhTien(TinhTongEvent event) {
        if (event != null) {
            tinTongTien();
        }
    }
}