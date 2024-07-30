package com.manager.appbanhang.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.manager.appbanhang.R;
import com.manager.appbanhang.databinding.ActivityThemSanPhamBinding;

import java.util.ArrayList;
import java.util.List;

public class ThemSanPhamActivity extends AppCompatActivity {
    Spinner spinner;
    int loai = 0;
    ActivityThemSanPhamBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_them_san_pham);
        initView();
        initData();
    }

    private void initData() {
        List<String> stringList = new ArrayList<>();
        stringList.add("Vui lòng chọn loại");
        stringList.add("Loại 1");
        stringList.add("Loại 2");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stringList);
        spinner.setAdapter(adapter);
        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                loai = i;
            }
        });
    }

    private void initView() {
        spinner = findViewById(R.id.spinner_loai);
    }
}