package com.example.teamproject.front;

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

public class userlistadapter extends ArrayAdapter<User> {
    private Context context;
    private List<User> userLists;

    public userlistadapter(Context context, List<User> userLists) {
        super(context, R.layout.ranklist_list, userLists);
        this.context = context;
        this.userLists = userLists;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view  = layoutInflater.inflate(R.layout.ranklist_list, parent, false);
        TextView tv_rank = (TextView)view.findViewById(R.id.tv_rank);
        tv_rank.setText(String.valueOf(position+1));
        TextView tv_name = (TextView)view.findViewById(R.id.tv_name);
        tv_name.setText(userLists.get(position).getUserName());
        TextView tv_point = (TextView)view.findViewById(R.id.tv_point);
        tv_point.setText(String.valueOf(userLists.get(position).getUserPoint()));

        return view;
    }
}
