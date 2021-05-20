package com.example.teamproject.TodoList;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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


public class ListDetail extends AppCompatActivity {
    private TextView tv_dtitle, tv_dcontent, tv_dimportance, tv_dprocesshours;
    private Button btn_back, btn_confirm,  btn_edit, btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_detail);

        final TodoList todoList = (TodoList)getIntent().getSerializableExtra("todoList");
        final DateData date = (DateData) getIntent().getSerializableExtra("date");
        final String userID = (String) getIntent().getSerializableExtra("userID");

        tv_dtitle = (TextView)findViewById(R.id.tv_dtitle);
        tv_dcontent = (TextView)findViewById(R.id.tv_dcontent);
        tv_dimportance = (TextView)findViewById(R.id.tv_dimportance);
        tv_dprocesshours = (TextView)findViewById(R.id.tv_dprocessHours);
        this.btn_back = (Button)findViewById(R.id.btn_back);
        btn_confirm = (Button)findViewById(R.id.btn_comfirm);
        if(todoList.getIsAchieved() == 1) {
            btn_confirm.setEnabled(false);
        }
        btn_edit = (Button)findViewById(R.id.btn_edit);
        btn_delete = (Button)findViewById(R.id.btn_delete);
        tv_dtitle.setText(todoList.getTitle());
        tv_dcontent.setText(todoList.getContent());
        tv_dimportance.setText(String.valueOf(todoList.getImportance()));
        tv_dprocesshours.setText(String.valueOf(todoList.getProcessHours()));

        this.btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListDetail.this, MainActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("달성 확인");
                builder.setMessage("정말 달성하셨습니까?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TodoListLocalDAO localdb = new TodoListLocalDAO(getBaseContext());
                        todoList.setIsAchieved(1);
                        if(localdb.Update(todoList)){
                            btn_confirm.setEnabled(false);
                            //로컬이라 포인트 미지급
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
                            builder.setMessage("취소되었습니다.");
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.create().show();
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListDetail.this, UpdateTodoList.class);
                intent.putExtra("todoList", todoList);
                intent.putExtra("date", date);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("삭제 확인");
                builder.setMessage("정말 삭제하시겠습니까?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TodoListLocalDAO localdb = new TodoListLocalDAO(getBaseContext());
                        TodoListServerDAO serverdb = new TodoListServerDAO();

                        if(userID == null) {
                            if(localdb.Delete(todoList.getListID())){
                                Intent intent = new Intent(ListDetail.this, MainActivity.class);
                                intent.putExtra("date", date);
                                intent.putExtra("userID", userID);
                                startActivity(intent);
                            }else{
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getBaseContext());
                                builder1.setMessage("삭제 실패");
                                builder1.setCancelable(false);
                                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder1.create().show();
                            }
                        }
                        else {
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
                                                    Toast.makeText(getApplicationContext(),"Successfully Deleted",Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(ListDetail.this, MainActivity.class);
                                                    intent.putExtra("date", date);
                                                    intent.putExtra("userID", userID);
                                                    startActivity(intent);
                                                } else { // 실패한 경우
                                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getBaseContext());
                                                    builder1.setMessage("삭제 실패");
                                                    builder1.setCancelable(false);
                                                    builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                                    builder1.create().show();
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
                                    param.put("listID", String.valueOf(todoList.getListID()));
                                    return param;
                                }
                            };
                            request.setShouldCache(false);
                            Global.requestQueue.add(request);
                        }

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });

        if(Global.requestQueue == null){
            Global.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
    }
}
