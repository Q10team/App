package com.example.teamproject.front;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.teamproject.MainActivity;
import com.example.teamproject.R;

public class FragSet extends Fragment {

    TextView ID;
    Button setbtn;
    String userID;
    public FragSet(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragset, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            userID = bundle.getString("userID");
        }
        ID = (TextView)rootView.findViewById(R.id.textView13);
        ID.setText(userID);

        setbtn = (Button)rootView.findViewById(R.id.btn_set6);
        setbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), com.example.teamproject.login.LoginActivity.class));
            }
        });
        return rootView;
    }


}
