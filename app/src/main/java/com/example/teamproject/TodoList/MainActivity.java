package com.example.teamproject.TodoList;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.teamproject.Global;
import com.example.teamproject.R;
import com.example.teamproject.TodoList.adapter.TodoListAdapter;
import com.example.teamproject.TodoList.local.TodoListLocalDAO;
import com.example.teamproject.TodoList.server.TodoListServerDAO;
import com.example.teamproject.login.LoginActivity;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.MonthDay;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView tv_date , tv_point;
    private DatePickerDialog.OnDateSetListener callbackMethod;

    int year, month, day;
    TodoListLocalDAO localdb;
    Button btn_add;
    ImageButton imgbtnback;
    ListView lv_list;

    List<TodoList> todoLists = new ArrayList<TodoList>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Calendar cal = Calendar.getInstance();
        year=cal.get(Calendar.YEAR);
        month=cal.get(Calendar.MONTH);
        day=cal.get(Calendar.DATE);

        final String userID = (String) getIntent().getSerializableExtra("userID");
        final DateData datedata = (DateData) getIntent().getSerializableExtra("date");

        if(Global.requestQueue == null){
            Global.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        DateData date = new DateData(year, month, day);
        if(datedata!=null) {
            date.setYear(datedata.getYear());
            date.setMonth(datedata.getMonth());
            date.setDay(datedata.getDay());
            year = datedata.getYear();
            month = datedata.getMonth();
            day = datedata.getDay();
        }

        localdb = new TodoListLocalDAO(this);
        btn_add = (Button)findViewById(R.id.btn_add);
        lv_list = (ListView)findViewById(R.id.lv_list);
        tv_date = (TextView)findViewById(R.id.tv_date);
        tv_date.setText(year + "??? " + (month+1) + "??? " + day + "???");
        tv_point = (TextView)findViewById(R.id.tv_point);
        imgbtnback = (ImageButton)findViewById(R.id.imgbtnback);

        com.example.teamproject.TodoList.adapter.TodoListAdapter todoListAdapter = new com.example.teamproject.TodoList.adapter.TodoListAdapter(getApplicationContext(), todoLists);
        lv_list.setAdapter(todoListAdapter);

        if(userID!=null){
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    Global.GetUrl("getpoint"),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                Integer point = jsonObject.getInt("point");
                                if (success) { // ???????????? ????????? ??????;
                                    tv_point.setText(String.valueOf(point));
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
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError
                {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("ID", userID);

                    return param;
                }
            };
            request.setShouldCache(false);
            Global.requestQueue.add(request);
        }

        todoLists.clear();
        if(userID==null) {
            todoLists = localdb.Read(date);
            todoListAdapter.updateDataSet(todoLists);
            todoListAdapter.notifyDataSetChanged();
        }
        else {
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
                        }
                        todoListAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Something went wrong",Toast.LENGTH_LONG).show();
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
        }

        todoListAdapter.sort(new Comparator<TodoList>() {
            @Override
            public int compare(TodoList o1, TodoList o2) {
                return String.valueOf(o1.getListID()).compareTo(String.valueOf(o2.getListID()));
            }
        });
        lv_list.setAdapter(todoListAdapter);

        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int Year, int monthOfYear, int dayOfMonth)
            {
                tv_date.setText(Year + "??? " + (monthOfYear+1) + "??? " + dayOfMonth + "???");
                year=Year;
                month=monthOfYear;
                day=dayOfMonth;
                date.setYear(year);
                date.setMonth(month);
                date.setDay(day);
                OnDateSetListener();
            }
            public void OnDateSetListener() {
                todoLists.clear();
                if(userID==null) {
                    todoLists = localdb.Read(date);
                    todoListAdapter.notifyDataSetChanged();
                }
                else {
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
                                }
                                todoListAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "Something went wrong",Toast.LENGTH_LONG).show();
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
                }
                todoListAdapter.sort(new Comparator<TodoList>() {
                    @Override
                    public int compare(TodoList o1, TodoList o2) {
                        return String.valueOf(o1.getListID()).compareTo(String.valueOf(o2.getListID()));
                    }
                });
                lv_list.setAdapter(todoListAdapter);
            }
        };

        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TodoList todoList;
                if(userID == null)
                    todoList = localdb.Read(date).get(position);
                else
                    todoList = todoLists.get(position);
                Intent intent = new Intent(MainActivity.this, ListDetail.class);
                intent.putExtra("userID", userID);
                intent.putExtra("todoList", todoList);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });

        if(Global.requestQueue == null){
            Global.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MakeTodoList.class);
                intent.putExtra("date", date);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        imgbtnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.example.teamproject.front.HomeActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
    }

    public void OnClickDayHandler(View view) {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, year, month, day);

        dialog.show();
    }
}

