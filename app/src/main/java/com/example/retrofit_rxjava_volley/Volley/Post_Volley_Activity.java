package com.example.retrofit_rxjava_volley.Volley;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.retrofit_rxjava_volley.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class Post_Volley_Activity extends AppCompatActivity {

    private TextInputEditText firstNameEt;
    private TextInputEditText lastNameEt;
    private TextInputEditText phoneNumberEt;
    private View saveBtn;
    private Request_Handler volleyRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);
        initialise();
        setOnClick();
    }

    private void initialise() {
        volleyRequest = Request_Handler.getInstance();
        firstNameEt = findViewById(R.id.et_addNewEmployee_firstName);
        lastNameEt = findViewById(R.id.et_addNewEmployee_lastName);
        phoneNumberEt = findViewById(R.id.et_addNewEmployee_phoneNumber);
        saveBtn = findViewById(R.id.fab_addNewEmployee_save);
    }

    private void setOnClick() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToServer();
            }
        });
    }

    private void sendToServer() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("first_name", firstNameEt.getText().toString());
        params.put("last_name", lastNameEt.getText().toString());
        params.put("phone_number", phoneNumberEt.getText().toString());

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("first_name", firstNameEt.getText().toString());
        jsonObject.addProperty("last_name", lastNameEt.getText().toString());
        jsonObject.addProperty("phone_number", phoneNumberEt.getText().toString());

        volleyRequest.post_Request_With_Params("add_employee.php", Post_Volley_Activity.this, jsonObject, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("status :", result);
                Toast.makeText(Post_Volley_Activity.this, "successfully inserted", Toast.LENGTH_LONG).show();
            }
        });
    }


}
