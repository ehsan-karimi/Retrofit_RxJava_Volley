package com.example.retrofit_rxjava_volley.Retrofit;

import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api_Interface {
    @GET("get_employee.php")
    Single<List<EmployeeModel>> get_List();

    @POST("add_employee.php")
    Single<String> add_Employee(@Body JsonObject body);
}
