package com.example.teamproject.TodoList;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teamproject.R;
import com.example.teamproject.TodoList.local.TodoListLocalDAO;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.MonthDay;
import java.time.Year;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView tv_date;
    private DatePickerDialog.OnDateSetListener callbackMethod;

    int year, month, day;
    TodoListLocalDAO database;
    Button btn_add;
    ListView lv_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Calendar cal = Calendar.getInstance();
        year=cal.get(Calendar.YEAR);
        month=cal.get(Calendar.MONTH);
        day=cal.get(Calendar.DATE);
        final DateData datedata = (DateData) getIntent().getSerializableExtra("date");
        DateData date = new DateData(year, month, day);
        if(datedata!=null) {
            date.setYear(datedata.getYear());
            date.setMonth(datedata.getMonth());
            date.setDay(datedata.getDay());
            year = datedata.getYear();
            month = datedata.getMonth();
            day = datedata.getDay();
        }

        database = new TodoListLocalDAO(this);
        btn_add = (Button)findViewById(R.id.btn_add);
        lv_list = (ListView)findViewById(R.id.lv_list);
        tv_date = (TextView)findViewById(R.id.tv_date);
        tv_date.setText(year + "년" + (month+1) + "월" + day + "일");

        com.example.teamproject.TodoList.adapter.TodoListAdapter todoListAdapter = new com.example.teamproject.TodoList.adapter.TodoListAdapter(this, database.Read(date));
        todoListAdapter.sort(new Comparator<TodoList>() {
            @Override
            public int compare(TodoList o1, TodoList o2) {
                return o1.getUploadDate().compareTo(o2.getUploadDate());
            }
        });
        lv_list.setAdapter(todoListAdapter);

        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int Year, int monthOfYear, int dayOfMonth)
            {
                tv_date.setText(Year + "년" + (monthOfYear+1) + "월" + dayOfMonth + "일");
                year=Year;
                month=monthOfYear;
                day=dayOfMonth;
                date.setYear(year);
                date.setMonth(month);
                date.setDay(day);
                OnDateSetListener();
            }
            public void OnDateSetListener() {
                com.example.teamproject.TodoList.adapter.TodoListAdapter todoListAdapter = new com.example.teamproject.TodoList.adapter.TodoListAdapter(getApplicationContext(), database.Read(date));
                todoListAdapter.sort(new Comparator<TodoList>() {
                    @Override
                    public int compare(TodoList o1, TodoList o2) {
                        return o1.getUploadDate().compareTo(o2.getUploadDate());
                    }
                });
                lv_list.setAdapter(todoListAdapter);
            }
        };
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TodoList todoList = database.Read(date).get(position);
                Intent intent = new Intent(MainActivity.this, ListDetail.class);
                intent.putExtra("todoList", todoList);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MakeTodoList.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });
    }

    public void OnClickDayHandler(View view) {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, year, month, day);

        dialog.show();
    }
}

