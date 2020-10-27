package com.example.retrofit_rxjava_volley.Retrofit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.retrofit_rxjava_volley.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Post_Retrofit_Activity extends AppCompatActivity {

    private Api_Service apiService;
    private TextInputEditText firstNameEt;
    private TextInputEditText lastNameEt;
    private TextInputEditText phoneNumberEt;
    private View saveBtn;
    private Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);
        initialise();
        setOnClick();
    }

    private void initialise() {
        apiService = new Api_Service();
        firstNameEt = findViewById(R.id.et_addNewEmployee_firstName);
        lastNameEt = findViewById(R.id.et_addNewEmployee_lastName);
        phoneNumberEt = findViewById(R.id.et_addNewEmployee_phoneNumber);
        saveBtn = findViewById(R.id.fab_addNewEmployee_save);
    }

    private void setOnClick() {
        saveBtn.setOnClickListener(view -> sendToServer());
    }

    private void sendToServer() {
        if (firstNameEt.length() > 0 &&
                lastNameEt.length() > 0 &&
                phoneNumberEt.length() > 0) {
            apiService.saveStudent((Objects.requireNonNull(firstNameEt.getText())).toString(),
                    (Objects.requireNonNull(lastNameEt.getText())).toString(),
                    (Objects.requireNonNull(phoneNumberEt.getText())).toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Post_Retrofit_Activity.this.disposable = d;
                        }

                        @Override
                        public void onSuccess(String s) {
                            Toast.makeText(Post_Retrofit_Activity.this, "successfully inserted", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", "onError: " + e.getMessage() );
                            Toast.makeText(Post_Retrofit_Activity.this, "Unspecified Error!!!", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
