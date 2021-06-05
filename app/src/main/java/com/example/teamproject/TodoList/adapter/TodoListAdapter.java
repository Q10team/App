package com.example.teamproject.TodoList.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.teamproject.R;
import com.example.teamproject.TodoList.TodoList;

import java.util.List;


public class TodoListAdapter extends ArrayAdapter<TodoList> {
    private Context context;
    private List<TodoList> todoLists;

    public TodoListAdapter(Context context, List<TodoList> todoLists) {
        super(context, R.layout.todolist_list, todoLists);
        this.context = context;
        this.todoLists = todoLists;
    }

    public void updateDataSet(List<TodoList> todoLists) {
        this.todoLists = todoLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view  = layoutInflater.inflate(R.layout.todolist_list, parent, false);
        TextView tv_title = (TextView)view.findViewById(R.id.tv_title);
        tv_title.setText(todoLists.get(position).getTitle());

        return view;
    }
}
