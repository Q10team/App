package com.example.teamproject.front;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.teamproject.Global;
import com.example.teamproject.R;
import com.example.teamproject.TodoList.MainActivity;
import com.example.teamproject.TodoList.MakeTodoList;
import com.example.teamproject.TodoList.TodoList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragRank extends Fragment {
    ImageButton imgbtn;
    String userID;
    Button btn_rank1, btn_rank2;
    ListView lv_ranks;
    List<User> rankLists;

    public FragRank() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragrank, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            userID = bundle.getString("userID");
        }

        btn_rank1 = (Button)rootView.findViewById(R.id.btn_rank1);
        btn_rank2 = (Button)rootView.findViewById(R.id.btn_rank2);
        lv_ranks = (ListView) rootView.findViewById(R.id.lv_ranks);
        rankLists = new ArrayList<User>();

        if(Global.requestQueue == null){
            Global.requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        }


        com.example.teamproject.front.userlistadapter rankadapter = new userlistadapter(getActivity().getApplicationContext(), rankLists);
        lv_ranks.setAdapter(rankadapter);

        if(userID!=null){
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    Global.GetUrl("friendrank"), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i=0; i<jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            User rankList = new User();
                            rankList.setUserNum(i+1);
                            rankList.setUserName(jsonObject.getString("name"));
                            rankList.setUserPoint(Integer.parseInt(jsonObject.getString("point")));
                            rankLists.add(rankList);
                        }
                        rankadapter.notifyDataSetChanged();
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
                    return parameters;
                }
            };
            request.setShouldCache(false);
            Global.requestQueue.add(request);

            rankadapter.sort(new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return String.valueOf(o1.getUserPoint()).compareTo(String.valueOf(o2.getUserPoint()));
                }
            });
            lv_ranks.setAdapter(rankadapter);
        }

        btn_rank1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rankLists.clear();
                if(userID!=null){
                    StringRequest request = new StringRequest(
                            Request.Method.POST,
                            Global.GetUrl("userfriendrank"), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i<jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    User rankList = new User();
                                    rankList.setUserNum(i+1);
                                    rankList.setUserName(jsonObject.getString("name"));
                                    rankList.setUserPoint(Integer.parseInt(jsonObject.getString("point")));
                                    rankLists.add(rankList);
                                }
                                rankadapter.notifyDataSetChanged();
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
                            return parameters;
                        }
                    };
                    request.setShouldCache(false);
                    Global.requestQueue.add(request);


                    rankadapter.sort(new Comparator<User>() {
                        @Override
                        public int compare(User o1, User o2) {
                            return String.valueOf(o1.getUserPoint()).compareTo(String.valueOf(o2.getUserPoint()));
                        }
                    });

                    lv_ranks.setAdapter(rankadapter);

                }
            }
        });

        btn_rank2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rankLists.clear();
                if(userID!=null){
                    StringRequest request = new StringRequest(
                            Request.Method.POST,
                            Global.GetUrl("userrank"), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i<jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    User rankList = new User();
                                    rankList.setUserNum(i+1);
                                    rankList.setUserName(jsonObject.getString("name"));
                                    rankList.setUserPoint(Integer.parseInt(jsonObject.getString("point")));
                                    rankLists.add(rankList);
                                }
                                rankadapter.notifyDataSetChanged();
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
                            return parameters;
                        }
                    };
                    request.setShouldCache(false);
                    Global.requestQueue.add(request);


                    rankadapter.sort(new Comparator<User>() {
                        @Override
                        public int compare(User o1, User o2) {
                            return String.valueOf(o1.getUserPoint()).compareTo(String.valueOf(o2.getUserPoint()));
                        }
                    });

                    lv_ranks.setAdapter(rankadapter);

                }
            }
        });



        return rootView;
    }
}