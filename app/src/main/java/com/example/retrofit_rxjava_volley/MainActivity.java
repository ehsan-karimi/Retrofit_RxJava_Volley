package com.example.retrofit_rxjava_volley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.retrofit_rxjava_volley.Retrofit.Api_Service;
import com.example.retrofit_rxjava_volley.Retrofit.Get_Retrofit_Activity;
import com.example.retrofit_rxjava_volley.Retrofit.Post_Retrofit_Activity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private Button btn_get_retrofit;
    private Button btn_post_retrofit;
    private Button btn_upload_retrofit;
    private Api_Service apiService;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialise();
        set_OnClick();
    }

    private void initialise() {
        apiService = new Api_Service();
        btn_get_retrofit = findViewById(R.id.btn_get_retrofit);
        btn_post_retrofit = findViewById(R.id.btn_post_retrofit);
        btn_upload_retrofit = findViewById(R.id.btn_upload_retrofit);
    }

    private void set_OnClick() {
        btn_get_retrofit.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Get_Retrofit_Activity.class);
            startActivity(intent);
        });

        btn_post_retrofit.setOnClickListener((v) -> {
            Intent intent = new Intent(MainActivity.this, Post_Retrofit_Activity.class);
            startActivity(intent);
        });

        btn_upload_retrofit.setOnClickListener(v ->{


        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
     //   disposable.dispose();
    }
}