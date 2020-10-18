package com.example.retrofit_rxjava_volley.Retrofit;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface Api_Interface {
    @GET("get_employee.php")
    Single<List<EmployeeModel>> get_List();
}
