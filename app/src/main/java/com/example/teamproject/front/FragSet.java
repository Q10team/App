package com.example.teamproject.front;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.teamproject.Global;
import com.example.teamproject.MainActivity;
import com.example.teamproject.R;
import com.example.teamproject.login.LoginActivity;
import com.example.teamproject.login.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FragSet extends AppCompatActivity {
    private EditText et_Avata, et_PW, et_Name, et_Email;
    private Button btn_modify, b_logout;
    private TextView t_ID, t_Name;
    private ImageButton img_Avata;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragset);

        final String userID = (String) getIntent().getSerializableExtra("userID");

        img_Avata = findViewById(R.id.ibtn_img);
        t_Name = findViewById(R.id.textView11);
        t_ID = findViewById(R.id.textView13);
        et_PW = findViewById(R.id.et_PW);
        et_Name = findViewById(R.id.et_Name);
        et_Email = findViewById(R.id.et_Eamil);
        et_Avata = findViewById(R.id.et_Avata);
        btn_modify = findViewById(R.id.btn_modify);
        b_logout = findViewById(R.id.btn_set6);

        t_ID.setText(userID);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                Global.GetUrl("fragset_get"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String userPW = jsonObject.getString("userPW");
                            String userName = jsonObject.getString("userName");
                            String userEmail = jsonObject.getString("userEmail");
                            int userAvata = jsonObject.getInt("userAvata");
                            if (success) { // 사용자 정보 가져오기 성공
                                et_PW.setText(userPW);
                                t_Name.setText(userName);
                                et_Name.setText(userName);
                                et_Email.setText(userEmail);
                                et_Avata.setText(String.valueOf(userAvata));
                                switch(userAvata) {
                                    case 0:
                                        img_Avata.setImageResource(R.drawable.user1); //user0...?
                                        break;
                                    case 1:
                                        img_Avata.setImageResource(R.drawable.user1);
                                        break;
                                    case 2:
                                        img_Avata.setImageResource(R.drawable.user2);
                                        break;
                                    case 3:
                                        img_Avata.setImageResource(R.drawable.user3);
                                        break;
                                    case 4:
                                        img_Avata.setImageResource(R.drawable.user3); //user4...?
                                        break;
                                    case 5:
                                        img_Avata.setImageResource(R.drawable.user3); //user5...?
                                        break;
                                    case 6:
                                        img_Avata.setImageResource(R.drawable.user3); //user6...?
                                        break;
                                    case 7:
                                        img_Avata.setImageResource(R.drawable.user3); //user7...?
                                        break;
                                }
                            } else { // 사용자 정보 가져오기 실패
                                Toast.makeText(getApplicationContext(),"사용자 정보 조희 실패",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("userID", userID);
                return param;
            }
        };
        request.setShouldCache(false);
        Global.requestQueue.add(request);

        b_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(FragSet.this, com.example.teamproject.login.LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Avata = et_Avata.getText().toString();
                String PW = et_PW.getText().toString();
                String Name = et_Name.getText().toString();
                String Email = et_Email.getText().toString();

                StringRequest request = new StringRequest(
                        Request.Method.POST,
                        Global.GetUrl("fragset"),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if (success) { // 사용자 정보 수정이 성공한 경우;
                                        Toast.makeText(getApplicationContext(),"수정되었습니다.",Toast.LENGTH_SHORT).show();
                                        Intent intent =  getIntent();
                                        finish();
                                        startActivity(intent);
                                    } else { // 사용자 정보 수정이 실패한 경우
                                        Toast.makeText(getApplicationContext(),"수정이 실패하였습니다.",Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> param = new HashMap<>();
                        param.put("userID", userID);
                        param.put("Avata", Avata);
                        param.put("PW", PW);
                        param.put("Name", Name);
                        param.put("Email", Email);

                        return param;
                    }
                };
                request.setShouldCache(false);
                Global.requestQueue.add(request);
            }
        });

        if (Global.requestQueue == null) {
            Global.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
    }

}
