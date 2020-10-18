package com.example.retrofit_rxjava_volley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.retrofit_rxjava_volley.Retrofit.Get_Retrofit_Activity;

public class MainActivity extends AppCompatActivity {

    private Button btn_get_retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialise();
        set_OnClick();
    }

    private void initialise(){
        btn_get_retrofit = findViewById(R.id.btn_get_retrofit);
    }

    private void set_OnClick(){
        btn_get_retrofit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Get_Retrofit_Activity.class);
                startActivity(intent);
            }
        });
    }
}