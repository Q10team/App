package com.example.teamproject.front;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.teamproject.R;

public class FragRank extends Fragment {
    ImageButton imgbtn;
    String userID;

    public FragRank() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragrank, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            userID = bundle.getString("userID");
        }
        return rootView;
    }
}