package com.example.teamproject.front;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.teamproject.Global;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.teamproject.R;
import com.example.teamproject.TodoList.DateData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FragHome extends Fragment {

    ImageButton btncbnu;
    Button btnad1, btnad2, btnad3;
    int year, month, day;
    ListView lv_tdlist, lv_fulist;
    ArrayList<String> todayLists, futureLists;


    public FragHome() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fraghome, container, false);
        btncbnu = (ImageButton) rootView.findViewById(R.id.imgbtncbnu);
        btnad1 = (Button) rootView.findViewById(R.id.btn_ad1);
        btnad2 = (Button) rootView.findViewById(R.id.btn_ad2);
        btnad3 = (Button) rootView.findViewById(R.id.btn_ad3);
        lv_tdlist = (ListView) rootView.findViewById(R.id.lv_tdlist);
        lv_fulist = (ListView) rootView.findViewById(R.id.lv_fulist);

        Calendar cal = Calendar.getInstance();
        year=cal.get(Calendar.YEAR);
        month=cal.get(Calendar.MONTH);
        day=cal.get(Calendar.DATE);
        DateData date = new DateData(year, month, day);
        final String userID = (String) getActivity().getIntent().getSerializableExtra("userID");
        todayLists = new ArrayList<>();
        futureLists = new ArrayList<>();

        if(Global.requestQueue == null){
            Global.requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        }

        final ArrayAdapter<String> homelistadapter1 = new ArrayAdapter<String>(
                getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1,
                todayLists
        );
        final ArrayAdapter<String> homelistadapter2 = new ArrayAdapter<String>(
                getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1,
                futureLists
        );

        if(userID!=null){
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    Global.GetUrl("read"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i=0; i<jsonArray.length(); i++) {
                            if(i>=3)
                                break;
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String title = jsonObject.getString("title");
                            todayLists.add(title);
                        }
                        homelistadapter1.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> parameters = new HashMap<>();
                    parameters.put("userID", userID);
                    String smonth = "", sday = "";
                    if(date.getMonth()< 9)
                        smonth +="0";
                    if(date.getDay() < 10)
                        sday +="0";
                    String sdfstr = date.getYear() +"-"+smonth+ (date.getMonth() + 1) +"-"+sday+ date.getDay();
                    parameters.put("uploadDate", sdfstr);
                    return parameters;
                }
            };
            request.setShouldCache(false);
            Global.requestQueue.add(request);

            StringRequest request2 = new StringRequest(
                    Request.Method.POST,
                    Global.GetUrl("read"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i=0; i<jsonArray.length(); i++) {
                            if(i>=3)
                                break;
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String title = jsonObject.getString("title");
                            futureLists.add(title);
                        }
                        homelistadapter2.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> parameters = new HashMap<>();
                    parameters.put("userID", userID);
                    String smonth = "", sday = "";
                    if(date.getMonth()< 9)
                        smonth +="0";
                    if(date.getDay() < 10)
                        sday +="0";
                    String sdfstr = date.getYear() +"-"+smonth+ (date.getMonth() + 1) +"-"+sday+ (date.getDay()+1);
                    parameters.put("uploadDate", sdfstr);
                    return parameters;
                }
            };
            request2.setShouldCache(false);
            Global.requestQueue.add(request2);

            lv_tdlist.setAdapter(homelistadapter1);
            lv_fulist.setAdapter(homelistadapter2);
        }




        btncbnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.chungbuk.ac.kr/site/www/main.do"));
                startActivity(intent);
            }
        });

        btnad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.chungbuk.ac.kr/site/70th/main.do"));
                startActivity(intent);
            }
        });
        btnad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://software.cbnu.ac.kr/"));
                startActivity(intent);
            }
        });
        btnad3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://eis.cbnu.ac.kr/"));
                startActivity(intent);
            }
        });
        return rootView;
    }
}
