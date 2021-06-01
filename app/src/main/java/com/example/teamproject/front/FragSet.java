package com.example.teamproject.front;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.teamproject.MainActivity;
import com.example.teamproject.R;

public class FragSet extends Fragment {

    HomeActivity activity;
    Button setbtn;

    public FragSet(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragset, container, false);

        setbtn = (Button)rootView.findViewById(R.id.set_login);
        setbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), com.example.teamproject.login.LoginActivity.class));
            }
        });

        return rootView;
    }


}
