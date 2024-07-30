package com.manager.appbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.manager.appbanhang.R;
import com.manager.appbanhang.model.DonHang;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<DonHang> listDonHang;

    public DonHangAdapter(Context context, List<DonHang> listDonHang) {
        this.context = context;
        this.listDonHang = listDonHang;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DonHang donHang = listDonHang.get(position);
        holder.txt_donhang.setText("Đơn hàng: " + donHang.getId());
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.rv_chitiet.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(donHang.getItem().size());
        // adapter chi tiet
        ChiTietAdapter chiTietAdapter = new ChiTietAdapter(context, donHang.getItem());
        holder.rv_chitiet.setLayoutManager(layoutManager);
        holder.rv_chitiet.setAdapter(chiTietAdapter);
        holder.rv_chitiet.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return listDonHang.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_donhang;
        RecyclerView rv_chitiet;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_donhang = itemView.findViewById(R.id.id_donhang);
            rv_chitiet = itemView.findViewById(R.id.recycleview_chitiet);
        }
    }
}
