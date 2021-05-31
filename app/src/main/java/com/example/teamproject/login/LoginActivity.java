package com.example.teamproject.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.teamproject.Global;
import com.example.teamproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText et_ID, et_PW;
    private Button btn_login, btn_register, btn_noregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        et_ID = findViewById(R.id.et_ID);
        et_PW = findViewById(R.id.et_PW);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        btn_noregister = findViewById(R.id.btn_noregister);

        btn_noregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, com.example.teamproject.TodoList.MainActivity.class));
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, com.example.teamproject.login.RegisterActivity.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = et_ID.getText().toString();
                String PW = et_PW.getText().toString();
                if(TextUtils.isEmpty(ID)) {
                    Toast.makeText(getApplicationContext(),"아이디를 입력하십시오.",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(PW)) {
                    Toast.makeText(getApplicationContext(),"비밀번호를 입력하십시오.",Toast.LENGTH_SHORT).show();
                }
                else {
                    StringRequest request = new StringRequest(
                            Request.Method.POST,
                            Global.GetUrl("login"),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean success = jsonObject.getBoolean("success");
                                        String Name = jsonObject.getString("Name");
                                        if (success) { // 로그인에 성공한 경우;
                                            Toast.makeText(getApplicationContext(),Name+"님 환영합니다.",Toast.LENGTH_SHORT).show();
                                            Intent intent =  new Intent(LoginActivity.this, com.example.teamproject.TodoList.MainActivity.class);
                                            intent.putExtra("userID", ID);
                                            startActivity(intent);
                                        } else { // 로그인에 실패한 경우
                                            Toast.makeText(getApplicationContext(),"로그인에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                                            return;
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
                            param.put("ID", ID);
                            param.put("PW", PW);

                            return param;
                        }
                    };
                    request.setShouldCache(false);
                    Global.requestQueue.add(request);
                }
            }
        });

        if(Global.requestQueue == null){
            Global.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
    }
}