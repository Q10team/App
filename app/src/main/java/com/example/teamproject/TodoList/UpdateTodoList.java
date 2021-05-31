package com.example.teamproject.TodoList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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


public class UpdateTodoList extends AppCompatActivity {
    private EditText et_utitle, et_ucontent, et_uprocessHours, et_year, et_month, et_day;
    RatingBar rb_uimportance;
    private Button btn_back, btn_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatetodolist);
        final TodoListLocalDAO localdb = new TodoListLocalDAO(getBaseContext());
        TodoListServerDAO serverdb = new TodoListServerDAO();
        final TodoList todoList = (TodoList) getIntent().getSerializableExtra("todoList");
        final DateData date = (DateData) getIntent().getSerializableExtra("date");
        final String userID = (String) getIntent().getSerializableExtra("userID");
        et_utitle = (EditText)findViewById(R.id.et_utitle);
        et_ucontent = (EditText)findViewById(R.id.et_ucontent);
        rb_uimportance = (RatingBar) findViewById(R.id.rb_uimportance);
        et_uprocessHours = (EditText)findViewById(R.id.et_uprocessHours);
        et_year = findViewById(R.id.et_year);
        et_month = findViewById(R.id.et_month);
        et_day = findViewById(R.id.et_day);
        this.btn_back = (Button)findViewById(R.id.btn_back);
        btn_update = (Button)findViewById(R.id.btn_update);


        et_utitle.setText(todoList.getTitle());
        et_ucontent.setText(todoList.getContent());
        rb_uimportance.setRating((float)todoList.getImportance());
        et_uprocessHours.setText(String.valueOf(todoList.getProcessHours()));
        String[] datesplit = todoList.getUploadDate().split("-");
        et_year.setText(datesplit[0]);
        et_month.setText(datesplit[1]);
        et_day.setText(datesplit[2]);
        et_utitle.setSelection(et_utitle.getText().length());
        et_ucontent.setSelection(et_ucontent.getText().length());
        et_uprocessHours.setSelection(et_uprocessHours.getText().length());
        et_year.setSelection(et_year.getText().length());
        et_month.setSelection(et_month.getText().length());
        et_day.setSelection(et_day.getText().length());

        final String[] tempimportance = {"0"};

        rb_uimportance.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rb_uimportance.setRating(rating);
                tempimportance[0] = String.valueOf((int)rating);
            }
        });


        this.btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateTodoList.this, ListDetail.class);
                intent.putExtra("todoList", todoList);
                intent.putExtra("date", date);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_utitle.getText().toString();
                String content = et_ucontent.getText().toString();
                String importance = tempimportance[0];
                String processHours = et_uprocessHours.getText().toString();
                String syear = et_year.getText().toString();
                String smonth = et_month.getText().toString();
                String sday = et_day.getText().toString();


                if(!title.equals("")){
                    todoList.setTitle(title);
                    todoList.setContent(content);
                    if(!importance.equals(""))
                        todoList.setImportance(Integer.parseInt(importance));
                    if(!processHours.equals(""))
                        todoList.setProcessHours(Integer.parseInt(processHours));
                    if(!syear.equals("")&&!smonth.equals("")||!sday.equals("")) {
                        if(smonth.charAt(0) != '0' && Integer.parseInt(smonth) < 10)
                            smonth = "0" + smonth;
                        if(sday.charAt(0) != '0' && Integer.parseInt(sday) < 10)
                            sday = "0" + sday;
                        todoList.setUploadDate(syear+"-"+smonth+"-"+sday);
                    }


                    if(userID == null) {
                        if(localdb.Update(todoList)){
                            Toast.makeText(UpdateTodoList.this, "SUCCESS!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UpdateTodoList.this, com.example.teamproject.TodoList.MainActivity.class);
                            intent.putExtra("date", date);
                            startActivity(intent);
                        }else{
                            Toast.makeText(UpdateTodoList.this, "FAILED!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
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
                                                Toast.makeText(getApplicationContext(),"Successfully Updated",Toast.LENGTH_SHORT).show();
                                                Intent intent =  new Intent(UpdateTodoList.this, com.example.teamproject.TodoList.MainActivity.class);
                                                intent.putExtra("userID", userID);
                                                intent.putExtra("todoList", todoList);
                                                intent.putExtra("date", date);
                                                startActivity(intent);
                                            } else { // 실패한 경우
                                                Toast.makeText(UpdateTodoList.this, "Local Database Error", Toast.LENGTH_SHORT).show();
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
                    }
                }else{
                    Toast.makeText(UpdateTodoList.this, "비어있는 칸이 있습니다.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        if(Global.requestQueue == null){
            Global.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
    }
}
