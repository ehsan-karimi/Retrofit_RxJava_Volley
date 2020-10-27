package com.example.retrofit_rxjava_volley.Volley;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.example.retrofit_rxjava_volley.EmployeeModel;
import com.example.retrofit_rxjava_volley.R;
import com.example.retrofit_rxjava_volley.Retrofit.EmployeeAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Get_Volley_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmployeeAdapter employeeAdapter;
    private Request_Handler volleyRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_employee);
        initialise();
        show_List();
    }

    private void initialise() {
        volleyRequest = Request_Handler.getInstance();
        recyclerView = findViewById(R.id.rc_get_retrofit);
    }

    private void show_List() {
        volleyRequest.get_Request("get_employee.php", Get_Volley_Activity.this, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {


                java.util.List<EmployeeModel> employeeModels = new Gson().fromJson(result, new TypeToken<java.util.List<EmployeeModel>>() {
                }.getType());

                recyclerView.setLayoutManager(new LinearLayoutManager(Get_Volley_Activity.this, RecyclerView.VERTICAL, false));
                employeeAdapter = new EmployeeAdapter(employeeModels);
                recyclerView.setAdapter(employeeAdapter);

            }

            @Override
            public void onFailure(VolleyError volleyError) {
                Toast.makeText(Get_Volley_Activity.this, "Unspecified Error!!!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}