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


public class UpdateTodoList extends AppCompatActivity {
    private EditText et_utitle, et_ucontent, et_uimportance, et_uprocessHours, et_year, et_month, et_day;
    private Button btn_back, btn_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatetodolist);
        final TodoListLocalDAO localdb = new TodoListLocalDAO(getBaseContext());
        final TodoList todoList = (TodoList) getIntent().getSerializableExtra("todoList");
        final DateData date = (DateData) getIntent().getSerializableExtra("date");
        et_utitle = (EditText)findViewById(R.id.et_utitle);
        et_ucontent = (EditText)findViewById(R.id.et_ucontent);
        et_uimportance = (EditText)findViewById(R.id.et_uimportance);
        et_uprocessHours = (EditText)findViewById(R.id.et_uprocessHours);
        et_year = findViewById(R.id.et_year);
        et_month = findViewById(R.id.et_month);
        et_day = findViewById(R.id.et_day);
        this.btn_back = (Button)findViewById(R.id.btn_back);
        btn_update = (Button)findViewById(R.id.btn_update);


        et_utitle.setText(todoList.getTitle());
        et_ucontent.setText(todoList.getContent());
        et_uimportance.setText(String.valueOf(todoList.getImportance()));
        et_uprocessHours.setText(String.valueOf(todoList.getProcessHours()));
        String[] datesplit = todoList.getUploadDate().split("-");
        et_year.setText(datesplit[0]);
        et_month.setText(datesplit[1]);
        et_day.setText(datesplit[2]);
        et_utitle.setSelection(et_utitle.getText().length());
        et_ucontent.setSelection(et_ucontent.getText().length());
        et_uimportance.setSelection(et_uimportance.getText().length());
        et_uprocessHours.setSelection(et_uprocessHours.getText().length());
        et_year.setSelection(et_year.getText().length());
        et_month.setSelection(et_month.getText().length());
        et_day.setSelection(et_day.getText().length());

        this.btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateTodoList.this, ListDetail.class);
                intent.putExtra("todoList", todoList);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_utitle.getText().toString();
                String content = et_ucontent.getText().toString();
                String importance = et_uimportance.getText().toString();
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

                    if(localdb.Update(todoList)){
                        Toast.makeText(UpdateTodoList.this, "SUCCESS!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateTodoList.this, com.example.teamproject.TodoList.MainActivity.class);
                        intent.putExtra("date", date);
                        startActivity(intent);
                    }else{
                        Toast.makeText(UpdateTodoList.this, "FAILED!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(UpdateTodoList.this, "비어있는 칸이 있습니다.", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}
