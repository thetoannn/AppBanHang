package com.manager.appbanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.manager.appbanhang.R;
import com.manager.appbanhang.retrofit.ApiBanHang;
import com.manager.appbanhang.retrofit.RetrofitClient;
import com.manager.appbanhang.utils.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {
    TextView txt_dangky, txt_resetpass;
    EditText email, password;
    AppCompatButton button_dangnhap;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_nhap);
        initView();
        initControl();

    }

    private void initControl() {
        txt_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DangKyActivity.class);
                startActivity(intent);
            }
        });
        txt_resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ResetPassActivity.class);
                startActivity(intent);
            }
        });


        button_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = email.getText().toString().trim();
                String str_password = password.getText().toString().trim();
                if (TextUtils.isEmpty(str_email)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Email", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(str_password)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Password", Toast.LENGTH_SHORT).show();
                } else {
                    // save
                    Paper.book().write("email", str_email);
                    Paper.book().write("password", str_password);
                    dangNhap(str_email, str_password);
                }
            }
        });
    }

    private void initView() {
        Paper.init(this);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        txt_dangky = findViewById(R.id.txt_dangky);
        txt_resetpass = findViewById(R.id.txt_resetpass);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        button_dangnhap = findViewById(R.id.btn_dangnhap);

        // read data
        if (Paper.book().read("email") != null && Paper.book().read("password") != null) {
            email.setText(Paper.book().read("email"));
            password.setText(Paper.book().read("password"));
            if (Paper.book().read("isLogin") != null) {
                boolean flag = Paper.book().read("isLogin");
                if (flag) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            dangNhap(Paper.book().read("email"), Paper.book().read("password"));
                        }
                    }, 1000);
                }
            }
        }
    }

    private void dangNhap(String email, String password) {
        compositeDisposable.add(apiBanHang.dangNhap(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()) {
                                isLogin = true;
                                Paper.book().write("isLogin", isLogin);

                                Utils.user_current = userModel.getResult().get(0);
                                // luu lai thong tin nguoi dung
                                Paper.book().write("user", userModel.getResult().get(0));
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.user_current.getEmail() != null && Utils.user_current.getPass() != null) {
            email.setText(Utils.user_current.getEmail());
            password.setText(Utils.user_current.getPass());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}