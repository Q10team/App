package com.example.teamproject.TodoList.server;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.teamproject.Global;
import com.example.teamproject.TodoList.DateData;
import com.example.teamproject.TodoList.TodoList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TodoListServerDAO extends AppCompatActivity {
    private boolean done = false;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if(Global.requestQueue == null){
            Global.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
    }

    public boolean Insert(TodoList todoList) {
        if(Global.requestQueue == null){
            Global.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        StringRequest request = new StringRequest(
                Request.Method.POST,
                Global.GetUrl("insert"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) { // 성공한 경우;
                                done = true;
                            } else { // 실패한 경우
                                done = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                        done = false;
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                HashMap<String, String> param = new HashMap<>();
                param.put("userID", todoList.getUserID());
                param.put("title", todoList.getTitle());
                param.put("content", todoList.getContent());
                param.put("importance", String.valueOf(todoList.getImportance()));
                param.put("processHours", String.valueOf(todoList.getProcessHours()));
                param.put("uploadDate", todoList.getUploadDate());


                return param;
            }
        };
        request.setShouldCache(false);
        Global.requestQueue.add(request);
        return done;
    }

    public List<TodoList> Read(String userID, DateData date) {
        if(Global.requestQueue == null){
            Global.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        List<TodoList> todoLists = new ArrayList<TodoList>();
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Global.GetUrl("read"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0; i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        TodoList todoList = new TodoList();
                        todoList.setListID(Integer.parseInt(jsonObject.getString("listID")));
                        todoList.setUserID(jsonObject.getString("userID"));
                        todoList.setTitle(jsonObject.getString("title"));
                        todoList.setContent(jsonObject.getString("content"));
                        todoList.setImportance(Integer.parseInt(jsonObject.getString("importance")));
                        todoList.setProcessHours(Integer.parseInt(jsonObject.getString("processHours")));
                        todoList.setUploadDate(jsonObject.getString("uploadDate"));
                        todoList.setIsAchieved(Integer.parseInt(jsonObject.getString("isAchieved")));
                        todoLists.add(todoList);
                        System.out.println(todoLists.size());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TodoListServerDAO.this, "Something went wrong",Toast.LENGTH_LONG).show();
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
        return todoLists;
    }

    public boolean Update(TodoList todoList) {
        if(Global.requestQueue == null){
            Global.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        StringRequest request = new StringRequest(
                Request.Method.POST,
                Global.GetUrl("update"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) { // 성공한 경우;
                                done = true;
                            } else { // 실패한 경우
                                done = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        done = false;
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                HashMap<String, String> param = new HashMap<>();
                param.put("listID", String.valueOf(todoList.getListID())); //list 식별용, 조건으로 달면 됨
                param.put("userID", todoList.getUserID());
                param.put("title", todoList.getTitle());
                param.put("content", todoList.getContent());
                param.put("importance", String.valueOf(todoList.getImportance()));
                param.put("processHours", String.valueOf(todoList.getProcessHours()));
                param.put("uploadDate", todoList.getUploadDate());
                param.put("isAchieved", String.valueOf(todoList.getIsAchieved()));

                return param;
            }
        };
        request.setShouldCache(false);
        Global.requestQueue.add(request);
        return done;
    }

    public boolean Delete(int listID) {
        if(Global.requestQueue == null){
            Global.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        StringRequest request = new StringRequest(
                Request.Method.POST,
                Global.GetUrl("delete"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) { // 성공한 경우;
                                done = true;
                            } else { // 실패한 경우
                                done = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        done = false;
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                HashMap<String, String> param = new HashMap<>();
                param.put("listID", String.valueOf(listID));
                return param;
            }
        };
        request.setShouldCache(false);
        Global.requestQueue.add(request);
        return done;
    }
}
