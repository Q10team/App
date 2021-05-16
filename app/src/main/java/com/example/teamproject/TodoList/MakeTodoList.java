package com.example.teamproject.TodoList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teamproject.R;
import com.example.teamproject.TodoList.local.TodoListLocalDAO;

public class MakeTodoList extends AppCompatActivity {
    TodoListLocalDAO localdb;
    EditText et_regtitle, et_regcontent, et_regimportance, et_regprocessHours, et_year, et_month, et_day;
    Button btn_regsave, btn_regback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maketodolist);

        final DateData date = (DateData) getIntent().getSerializableExtra("date");

        localdb = new TodoListLocalDAO(this);

        et_regtitle = (EditText)findViewById(R.id.et_title);
        et_regcontent = (EditText)findViewById(R.id.et_content);
        et_regimportance = (EditText)findViewById(R.id.et_importance);
        et_regprocessHours = (EditText)findViewById(R.id.et_processHours);
        et_year = findViewById(R.id.et_year);
        et_month = findViewById(R.id.et_month);
        et_day = findViewById(R.id.et_day);
        btn_regsave = (Button)findViewById(R.id.btn_regsave);
        btn_regback = (Button)findViewById(R.id.btn_regback);

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

                if(!title.equals("")){
                    TodoList todoList = new TodoList();
                    todoList.setTitle(title);
                    todoList.setContent(content);
                    if(!importance.equals(""))
                        todoList.setImportance(Integer.parseInt(importance));
                    if(!processHours.equals(""))
                        todoList.setProcessHours(Integer.parseInt(processHours));
                    if(!syear.equals("")&&!smonth.equals("")||!sday.equals("")) {
                        if(Integer.parseInt(smonth) < 10)
                            smonth = "0" + smonth;
                        if(Integer.parseInt(sday) < 10)
                            sday = "0" + sday;
                        todoList.setUploadDate(syear+"-"+smonth+"-"+sday);
                    }

                    if (localdb.Insert(todoList)) { //case local . 로그인 상태면 외부로 intent~
                        Toast.makeText(MakeTodoList.this, "Successfully Inserted", Toast.LENGTH_SHORT).show();
                        et_regtitle.setText("");
                        et_regcontent.setText("");
                        et_regimportance.setText("");
                        et_regprocessHours.setText("");
                        et_year.setText("");
                        et_month.setText("");
                        et_year.setText("");
                        Intent intent = new Intent(MakeTodoList.this, MainActivity.class);
                        intent.putExtra("date", date);
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(MakeTodoList.this, "비어있는 칸이 있습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_regback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeTodoList.this, com.example.teamproject.TodoList.MainActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });
    }
}
