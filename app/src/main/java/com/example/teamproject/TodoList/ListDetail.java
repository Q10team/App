package com.example.teamproject.TodoList;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teamproject.R;
import com.example.teamproject.TodoList.local.TodoListLocalDAO;


public class ListDetail extends AppCompatActivity {
    private TextView tv_dtitle, tv_dcontent, tv_dimportance, tv_dprocesshours;
    private Button btn_back, btn_confirm,  btn_edit, btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_detail);

        final TodoList todoList = (TodoList)getIntent().getSerializableExtra("todoList");
        final DateData date = (DateData) getIntent().getSerializableExtra("date");
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
                        TodoListLocalDAO database = new TodoListLocalDAO(getBaseContext());
                        if(database.Delete(todoList.getID())){
                            Intent intent = new Intent(ListDetail.this, MainActivity.class);
                            intent.putExtra("date", date);
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
    }
}
