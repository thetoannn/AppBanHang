package com.manager.appbanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.manager.appbanhang.R;
import com.manager.appbanhang.model.GioHang;
import com.manager.appbanhang.model.SanPhamMoi;
import com.manager.appbanhang.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class ChiTietActivity extends AppCompatActivity {
    TextView tensp, giasp, mota;
    Button btn_add;
    ImageView imghinhanh;
    Spinner spinner;
    Toolbar toolbar;
    SanPhamMoi sanPhamMoi;
    NotificationBadge badge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chi_tiet);
        initView();
        ActionToolbar();
        initData();
        initControl();
    }

    private void initControl() {
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themGioHang();
            }
        });
    }

    private void themGioHang() {
        if (Utils.mang_gio_hang.size() > 0) {
            boolean flag = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for (int i = 0; i < Utils.mang_gio_hang.size(); i++) {
                if (Utils.mang_gio_hang.get(i).getIdsp() == sanPhamMoi.getId()){
                    Utils.mang_gio_hang.get(i).setSoluong(soluong + Utils.mang_gio_hang.get(i).getSoluong());
                    long gia = Long.parseLong(sanPhamMoi.getGiasp()) * Utils.mang_gio_hang.get(i).getSoluong();
                    Utils.mang_gio_hang.get(i).setGiasp(gia);
                    flag = true;
                }
            }
            if (flag == false) {
                long gia = Long.parseLong(sanPhamMoi.getGiasp()) * soluong;
                GioHang gioHang = new GioHang();
                gioHang.setGiasp(gia);
                gioHang.setSoluong(soluong);
                gioHang.setIdsp(sanPhamMoi.getId());
                gioHang.setTensp(sanPhamMoi.getTensp());
                gioHang.setHinhsp(sanPhamMoi.getHinhanh());
                Utils.mang_gio_hang.add(gioHang);
            }
        } else {
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            long gia = Long.parseLong(sanPhamMoi.getGiasp()) * soluong;
            GioHang gioHang = new GioHang();
            gioHang.setGiasp(gia);
            gioHang.setSoluong(soluong);
            gioHang.setIdsp(sanPhamMoi.getId());
            gioHang.setTensp(sanPhamMoi.getTensp());
            gioHang.setHinhsp(sanPhamMoi.getHinhanh());
            Utils.mang_gio_hang.add(gioHang);
        }
        int totalItem = 0;
        for (int i = 0; i < Utils.mang_gio_hang.size(); i++) {
            totalItem = totalItem + Utils.mang_gio_hang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(totalItem));
    }

    private void initData() {
        sanPhamMoi = sanPhamMoi = (SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        tensp.setText(sanPhamMoi.getTensp());
        mota.setText(sanPhamMoi.getMota());
        Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(imghinhanh);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giasp.setText("Giá: " + decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasp())) + "Đ");
        Integer[] so = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapter_spin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, so);
        spinner.setAdapter(adapter_spin);
    }

    private void initView() {
        tensp = findViewById(R.id.txttensp);
        giasp = findViewById(R.id.txtgiasp);
        mota = findViewById(R.id.txtmotachitiet);
        btn_add = findViewById(R.id.btn_add);
        spinner = findViewById(R.id.spinner);
        imghinhanh = findViewById(R.id.imgchitiet);
        toolbar = findViewById(R.id.toolbar);
        badge = findViewById(R.id.menu_sl);
        FrameLayout frameLayoutgiohang = findViewById(R.id.frame_giohang);
        frameLayoutgiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent giohang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(giohang);
            }
        });

        if (Utils.mang_gio_hang != null) {
            int totalItem = 0;
            for (int i = 0; i < Utils.mang_gio_hang.size(); i++) {
                totalItem = totalItem + Utils.mang_gio_hang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(totalItem));
        }
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.mang_gio_hang != null) {
            int totalItem = 0;
            for (int i = 0; i < Utils.mang_gio_hang.size(); i++) {
                totalItem = totalItem + Utils.mang_gio_hang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(totalItem));
        }
    }
}