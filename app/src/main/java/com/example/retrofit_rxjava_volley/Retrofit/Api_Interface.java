package com.example.retrofit_rxjava_volley.Retrofit;

import com.google.gson.JsonObject;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface Api_Interface {
    @GET("get_employee.php")
    Single<List<EmployeeModel>> get_List();

    @POST("add_employee.php")
    Single<String> add_Employee(@Body JsonObject body);

    @Multipart
    @POST("upload.php")
    Single<ResponseBody> upload(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file
    );
}
