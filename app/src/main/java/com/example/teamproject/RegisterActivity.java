package com.example.teamproject;

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

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText et_ID, et_PW, et_Name, et_Email;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        et_ID = findViewById(R.id.et_ID);
        et_PW = findViewById(R.id.et_PW);
        et_Name = findViewById(R.id.et_Name);
        et_Email = findViewById(R.id.et_Eamil);
        btn_register = findViewById(R.id.btn_register);


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = et_ID.getText().toString();
                String PW = et_PW.getText().toString();
                String Name = et_Name.getText().toString();
                String Email = et_Email.getText().toString();
                if(TextUtils.isEmpty(ID)) {
                    Toast.makeText(getApplicationContext(),"아이디를 입력하십시오.",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(PW)) {
                    Toast.makeText(getApplicationContext(),"비밀번호를 입력하십시오.",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(Name)) {
                    Toast.makeText(getApplicationContext(),"이름를 입력하십시오.",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(Email)) {
                    Toast.makeText(getApplicationContext(),"이메일를 입력하십시오.",Toast.LENGTH_SHORT).show();
                }
                else {
                    StringRequest request = new StringRequest(
                            Request.Method.POST,
                            Global.GetUrl("register"),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(getApplicationContext(),"가입되었습니다.",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
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
                            param.put("Name", Name);
                            param.put("Email", Email);

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