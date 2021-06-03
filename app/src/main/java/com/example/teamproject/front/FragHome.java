package com.example.teamproject.front;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.teamproject.R;

public class FragHome extends Fragment {

    ImageButton btncbnu;
    Button btnad1, btnad2, btnad3;

    public FragHome() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fraghome, container, false);
        btncbnu = (ImageButton) rootView.findViewById(R.id.imgbtncbnu);
        btnad1 = (Button) rootView.findViewById(R.id.btn_ad1);
        btnad2 = (Button) rootView.findViewById(R.id.btn_ad2);
        btnad3 = (Button) rootView.findViewById(R.id.btn_ad3);

        btncbnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.chungbuk.ac.kr/site/www/main.do"));
                startActivity(intent);
            }
        });

        btnad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.chungbuk.ac.kr/site/70th/main.do"));
                startActivity(intent);
            }
        });
        btnad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://software.cbnu.ac.kr/"));
                startActivity(intent);
            }
        });
        btnad3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://eis.cbnu.ac.kr/"));
                startActivity(intent);
            }
        });
        return rootView;
    }
}
