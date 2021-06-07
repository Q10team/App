package com.example.teamproject.front;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.teamproject.Global;
import com.example.teamproject.R;
import com.example.teamproject.TodoList.MakeTodoList;
import com.example.teamproject.TodoList.TodoList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FrdActivity extends AppCompatActivity {
    ImageButton imgbtn;
    Button btnadd;
    TextView txtName;
    EditText et_frd;
    LinearLayout frd5;
    ArrayList<String> friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frd);


        et_frd = findViewById(R.id.et_frd);
        btnadd = (Button)findViewById(R.id.btn_frdadd);
        txtName = (TextView)findViewById(R.id.btn_frd5);
        frd5 = (LinearLayout)findViewById(R.id.frd_5);
        final String userID = (String) getIntent().getSerializableExtra("userID");
        friends = new ArrayList<String>();


        frd5.setVisibility(View.INVISIBLE);
        imgbtn = (ImageButton)findViewById(R.id.ibtn_back);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                Global.GetUrl("friend"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for(int i=0; i<jsonArray.length();i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String friend = jsonObject.getString("friendID");
                                friends.add(friend); //friends 리스트에 친구 이름들 모아뒀습니다. onclick은 미적용했습니다. 세부정보 조회 불가능
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
                param.put("userID", userID);
                return param;
            }
        };
        request.setShouldCache(false);
        Global.requestQueue.add(request);


        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FrdActivity.this, com.example.teamproject.front.HomeActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }

        });
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ID = et_frd.getText().toString();
                //txtName.setText(ID);
                //frd5.setVisibility(View.VISIBLE);
                if(!userID.equals(ID)) {
                    StringRequest request = new StringRequest(
                            Request.Method.POST,
                            Global.GetUrl("insertfriend"),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        int success = jsonObject.getInt("success");
                                        if (success == 1) { // 성공한 경우;
                                            Toast.makeText(getApplicationContext(), "친구 추가 완료", Toast.LENGTH_SHORT).show();
                                            et_frd.setText("");
                                        } else if (success == 2) { // 실패한 경우
                                            Toast.makeText(getApplicationContext(), "이미 추가된 계정입니다.", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "없는 계정입니다.", Toast.LENGTH_SHORT).show();
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
                    )  {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> param = new HashMap<>();
                            param.put("sendID", userID);
                            param.put("recieveID", ID);
                            return param;
                        }
                    };
                    request.setShouldCache(false);
                    Global.requestQueue.add(request);
                }
                else {
                    Toast.makeText(getApplicationContext(), "자신의 계정은 추가할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    };
}



