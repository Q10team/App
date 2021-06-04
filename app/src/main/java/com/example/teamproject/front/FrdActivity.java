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

import com.example.teamproject.R;

public class FrdActivity extends AppCompatActivity {
    ImageButton imgbtn;
    Button btnadd;
    TextView txtName;
    EditText et_frd;
    LinearLayout frd5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frd);


        et_frd = findViewById(R.id.et_frd);
        btnadd = (Button)findViewById(R.id.btn_frdadd);
        txtName = (TextView)findViewById(R.id.btn_frd5);
        frd5 = (LinearLayout)findViewById(R.id.frd_5);
        final String userID = (String) getIntent().getSerializableExtra("userID");


        frd5.setVisibility(View.INVISIBLE);
        imgbtn = (ImageButton)findViewById(R.id.ibtn_back);
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
                if(ID.equals("hms")) {
                    txtName.setText(ID);
                    frd5.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "친구 추가 완료", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "없는 계정입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}



