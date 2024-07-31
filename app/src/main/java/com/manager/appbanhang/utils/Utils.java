package com.manager.appbanhang.utils;

import com.manager.appbanhang.model.GioHang;
import com.manager.appbanhang.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String BASE_URL = "http://192.168.0.178:8888/banhang/";
    public static List<GioHang> mang_gio_hang;
    public static List<GioHang> mang_mua_hang = new ArrayList<>();
    public static User user_current = new User();

}
