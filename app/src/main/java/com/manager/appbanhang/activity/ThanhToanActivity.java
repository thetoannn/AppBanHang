package com.manager.appbanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.manager.appbanhang.R;
import com.manager.appbanhang.retrofit.ApiBanHang;
import com.manager.appbanhang.retrofit.RetrofitClient;
import com.manager.appbanhang.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txt_tongtien, txt_phone, txt_email;
    EditText edt_diachi;
    AppCompatButton btn_thanhtoan;
    long tongtien;
    int totalItem;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thanh_toan);
        initView();
        countItem();
        initControl();
    }

    private void countItem() {
        totalItem = 0;
        for (int i = 0; i < Utils.mang_mua_hang.size(); i++) {
            totalItem = totalItem + Utils.mang_mua_hang.get(i).getSoluong();
        }
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
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien = getIntent().getLongExtra("tongtien", 0);

        txt_tongtien.setText(decimalFormat.format(tongtien));
        txt_email.setText(Utils.user_current.getEmail());
        txt_phone.setText(Utils.user_current.getPhone());


        btn_thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_diachi = edt_diachi.getText().toString().trim();
                if (TextUtils.isEmpty(str_diachi)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
                } else {
                    // post data
                    String str_email = Utils.user_current.getEmail();
                    String str_sdt = Utils.user_current.getPhone();
                    int id = Utils.user_current.getId();
                    Log.d("test", new Gson().toJson(Utils.mang_mua_hang)); // kiem tra
                    compositeDisposable.add(apiBanHang.createOrder(str_email, str_sdt, String.valueOf(tongtien), id, str_diachi, totalItem, new Gson().toJson(Utils.mang_mua_hang))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        Toast.makeText(getApplicationContext(), "Thành công", Toast.LENGTH_SHORT).show();
                                        Utils.mang_mua_hang.clear();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            }
        });

    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toolbar);
        txt_tongtien = findViewById(R.id.txt_tienthanhtoan);
        txt_phone = findViewById(R.id.txt_phone);
        txt_email = findViewById(R.id.txt_email);
        edt_diachi = findViewById(R.id.edt_diachi);
        btn_thanhtoan = findViewById(R.id.btn_thanhtoan);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}