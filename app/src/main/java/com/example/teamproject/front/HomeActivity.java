package com.example.teamproject.front;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.teamproject.R;

public class HomeActivity extends AppCompatActivity {

    Button btn1, btn2, btn3, btn4, btn5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        btn1 = (Button)findViewById(R.id.under_1);
        btn2 = (Button)findViewById(R.id.under_2);
        btn3 = (Button)findViewById(R.id.under_3);
        btn4 = (Button)findViewById(R.id.under_4);
        btn5 = (Button)findViewById(R.id.under_5);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FragHome fraghome = new FragHome();
        transaction.replace(R.id.frame, fraghome);
        transaction.addToBackStack(null);
        transaction.commit();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                FragHome fraghome = new FragHome();
                transaction.replace(R.id.frame, fraghome);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, com.example.teamproject.TodoList.MainActivity.class));
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                FragRank fragrank = new FragRank();
                transaction.replace(R.id.frame, fragrank);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                FragSet fragset = new FragSet();
                transaction.replace(R.id.frame, fragset);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
