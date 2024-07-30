package com.manager.appbanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.manager.appbanhang.R;
import com.manager.appbanhang.retrofit.ApiBanHang;
import com.manager.appbanhang.retrofit.RetrofitClient;
import com.manager.appbanhang.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKyActivity extends AppCompatActivity {
    EditText email, password, cf_password, phone, username;
    AppCompatButton button;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ky);
        initView();
        initControl();
    }

    private void initControl() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangKy();
            }
        });
    }

    private void dangKy() {
        String str_email = email.getText().toString().trim();
        String str_password = password.getText().toString().trim();
        String str_cf_password = cf_password.getText().toString().trim();
        String str_phone = phone.getText().toString().trim();
        String str_username = username.getText().toString().trim();

        if (TextUtils.isEmpty(str_email)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_password)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_cf_password)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Confirm Password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_phone)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Phone", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_username)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Username", Toast.LENGTH_SHORT).show();
        } else {
            if (str_password.equals(str_cf_password)) {
                // post data
                compositeDisposable.add(apiBanHang.dangKy(str_email, str_password, str_username, str_phone)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userModel -> {
                                    if (userModel.isSuccess()) {
                                        Utils.user_current.setEmail(str_email);
                                        Utils.user_current.setEmail(str_password);
                                        Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                },
                                throwable ->  {
                                    Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        ));
            } else {
                Toast.makeText(getApplicationContext(), "Confirm Password chưa đúng", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        cf_password = findViewById(R.id.cf_password);
        phone = findViewById(R.id.phone);
        username = findViewById(R.id.username);
        button = findViewById(R.id.btn_dangky);
    }
}