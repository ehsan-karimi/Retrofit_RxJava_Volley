package com.example.retrofit_rxjava_volley.Retrofit;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofit_rxjava_volley.R;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Get_Retrofit_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Api_Service apiService;
    private Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_retrofit);
        initialise();
        show_List();
    }

    private void initialise() {
        apiService = new Api_Service();
    }

    private void show_List(){
        apiService.get_List()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Model>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Get_Retrofit_Activity.this.disposable = d;
                    }

                    @Override
                    public void onSuccess(List<Model> models) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(Get_Retrofit_Activity.this,"Error !!!!",Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null){
            disposable.dispose();
        }
    }
}
