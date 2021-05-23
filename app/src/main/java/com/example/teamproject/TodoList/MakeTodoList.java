package com.example.teamproject.TodoList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.teamproject.Global;
import com.example.teamproject.R;
import com.example.teamproject.TodoList.local.TodoListLocalDAO;
import com.example.teamproject.TodoList.server.TodoListServerDAO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MakeTodoList extends AppCompatActivity {
    TodoListLocalDAO localdb;
    TodoListServerDAO serverdb;
    EditText et_regtitle, et_regcontent, et_regimportance, et_regprocessHours, et_year, et_month, et_day;
    Button btn_regsave, btn_regback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maketodolist);

        final DateData date = (DateData) getIntent().getSerializableExtra("date");
        final String userID = (String) getIntent().getSerializableExtra("userID");

        localdb = new TodoListLocalDAO(this);
        serverdb = new TodoListServerDAO();

        et_regtitle = (EditText)findViewById(R.id.et_title);
        et_regcontent = (EditText)findViewById(R.id.et_content);
        et_regimportance = (EditText)findViewById(R.id.et_importance);
        et_regprocessHours = (EditText)findViewById(R.id.et_processHours);
        et_year = findViewById(R.id.et_year);
        et_month = findViewById(R.id.et_month);
        et_day = findViewById(R.id.et_day);
        btn_regsave = (Button)findViewById(R.id.btn_regsave);
        btn_regback = (Button)findViewById(R.id.btn_regback);

        et_year.setText(String.valueOf(date.getYear()));
        et_month.setText(String.valueOf(date.getMonth()+1));
        et_day.setText(String.valueOf(date.getDay()));

        btn_regsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_regtitle.getText().toString();
                String content = et_regcontent.getText().toString();
                String importance = et_regimportance.getText().toString();
                String processHours = et_regprocessHours.getText().toString();
                String syear = et_year.getText().toString();
                String smonth = et_month.getText().toString();
                String sday = et_day.getText().toString();

                if(!title.equals("")&&!syear.equals("")&&!smonth.equals("")&&!sday.equals("")){
                    TodoList todoList = new TodoList();
                    todoList.setTitle(title);
                    todoList.setContent(content);
                    if(!importance.equals(""))
                        todoList.setImportance(Integer.parseInt(importance));
                    if(!processHours.equals(""))
                        todoList.setProcessHours(Integer.parseInt(processHours));
                    if(Integer.parseInt(smonth) < 10)
                        smonth = "0" + smonth;
                    if(Integer.parseInt(sday) < 10)
                        sday = "0" + sday;
                    todoList.setUploadDate(syear+"-"+smonth+"-"+sday);

                    if(userID == null) {
                        if (localdb.Insert(todoList)) { //case local . 로그인 상태면 외부로 intent~
                            Toast.makeText(MakeTodoList.this, "Successfully Inserted", Toast.LENGTH_SHORT).show();
                            et_regtitle.setText("");
                            et_regcontent.setText("");
                            et_regimportance.setText("");
                            et_regprocessHours.setText("");
                            et_year.setText("");
                            et_month.setText("");
                            et_day.setText("");
                            Intent intent = new Intent(MakeTodoList.this, com.example.teamproject.TodoList.MainActivity.class);
                            intent.putExtra("date", date);
                            startActivity(intent);
                        }
                        else { Toast.makeText(MakeTodoList.this, "Local Database Error", Toast.LENGTH_SHORT).show(); }
                    }
                    else {
                        todoList.setUserID(userID);
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
                                                Toast.makeText(getApplicationContext(),"Successfully Inserted",Toast.LENGTH_SHORT).show();
                                                et_regtitle.setText("");
                                                et_regcontent.setText("");
                                                et_regimportance.setText("");
                                                et_regprocessHours.setText("");
                                                et_year.setText("");
                                                et_month.setText("");
                                                et_day.setText("");
                                                Intent intent =  new Intent(MakeTodoList.this, com.example.teamproject.TodoList.MainActivity.class);
                                                intent.putExtra("userID", userID);
                                                intent.putExtra("date", date);
                                                startActivity(intent);
                                            } else { // 실패한 경우
                                                Toast.makeText(MakeTodoList.this, "Server Database Error", Toast.LENGTH_SHORT).show();
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
                    }

                }else{
                    Toast.makeText(MakeTodoList.this, "비어있는 칸이 있습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(Global.requestQueue == null){
            Global.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        btn_regback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeTodoList.this, com.example.teamproject.TodoList.MainActivity.class);
                intent.putExtra("date", date);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
    }
}
