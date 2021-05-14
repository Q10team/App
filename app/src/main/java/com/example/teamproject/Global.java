package com.example.teamproject;

import com.android.volley.RequestQueue;

public class Global {
    public static RequestQueue requestQueue;
    public static String GetUrl(String page) {
        return "http://125.143.93.19/"+page+".php";
    }
}