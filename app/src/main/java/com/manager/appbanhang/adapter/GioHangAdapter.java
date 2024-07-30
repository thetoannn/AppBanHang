package com.manager.appbanhang.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.manager.appbanhang.Interface.ImageClickListener;
import com.manager.appbanhang.R;
import com.manager.appbanhang.model.EventBus.TinhTongEvent;
import com.manager.appbanhang.model.GioHang;
import com.manager.appbanhang.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    Context context;
    List<GioHang> gioHangList;

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        holder.item_giohang_tensp.setText(gioHang.getTensp());
        holder.item_giohang_soluong.setText(gioHang.getSoluong() + " ");
        Glide.with(context).load(gioHang.getHinhsp()).into(holder.item_giohang_image);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.item_giohang_gia.setText("Giá: " + decimalFormat.format((gioHang.getGiasp())) );
        long gia = gioHang.getSoluong() * gioHang.getGiasp();
        holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Utils.mang_mua_hang.add(gioHang);
                    EventBus.getDefault().postSticky(new TinhTongEvent());
                } else {
                    for (int i = 0; i < Utils.mang_mua_hang.size(); i++) {
                        if (Utils.mang_mua_hang.get(i).getIdsp() == gioHang.getIdsp()) {
                            Utils.mang_mua_hang.remove(i);
                            EventBus.getDefault().postSticky(new TinhTongEvent());
                        }
                    }
                }
            }
        });

        holder.setListener(new ImageClickListener() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                Log.d("TAG", "onImageClick: " + pos + "..." + giatri);
                if (giatri == 1) {
                    if (gioHangList.get(pos).getSoluong() > 1){
                        int soluongmoi = gioHangList.get(pos).getSoluong() - 1;
                        gioHangList.get(pos).setSoluong(soluongmoi);

                        holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong() + " ");
                        long gia = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiasp();
                        holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
                        EventBus.getDefault().postSticky(new TinhTongEvent());
                    } else if (gioHangList.get(pos).getSoluong() == 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Utils.mang_gio_hang.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTongEvent());
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    }
                } else if (giatri == 2) {
                    if (gioHangList.get(pos).getSoluong() < 11){
                        int soluongmoi = gioHangList.get(pos).getSoluong() + 1;
                        gioHangList.get(pos).setSoluong(soluongmoi);
                    }
                    holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong() + " ");
                    long gia = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiasp();
                    holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
                    EventBus.getDefault().postSticky(new TinhTongEvent());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_giohang_image, img_tru, img_cong;
        TextView item_giohang_tensp, item_giohang_gia, item_giohang_soluong, item_giohang_giasp2;
        ImageClickListener listener;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_giohang_image = itemView.findViewById(R.id.item_giohang_image);
            item_giohang_tensp = itemView.findViewById(R.id.item_giohang_tensp);
            item_giohang_gia = itemView.findViewById(R.id.item_giohang_gia);
            item_giohang_soluong = itemView.findViewById(R.id.item_giohang_soluong);
            item_giohang_giasp2 = itemView.findViewById(R.id.item_giohang_giasp2);
            img_tru = itemView.findViewById(R.id.item_giohang_tru);
            img_cong = itemView.findViewById(R.id.item_giohang_cong);
            checkBox = itemView.findViewById(R.id.item_giohang_cb);

            // event click
            img_cong.setOnClickListener(this);
            img_tru.setOnClickListener(this);

        }

        public void setListener(ImageClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            if (view == img_tru) {
                listener.onImageClick(view, getAdapterPosition(), 1);
            } else if (view == img_cong) {
                listener.onImageClick(view, getAdapterPosition(), 2);
            }
        }
    }
}
