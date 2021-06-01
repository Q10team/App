package com.example.teamproject;

import com.android.volley.RequestQueue;

public class Global {
    public static RequestQueue requestQueue;
    public static String GetUrl(String page) {
        return "http://119.204.226.52/"+page+".php";
    }
}
