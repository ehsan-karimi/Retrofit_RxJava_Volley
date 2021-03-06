package com.example.retrofit_rxjava_volley.Retrofit;

import com.example.retrofit_rxjava_volley.EmployeeModel;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.reactivex.Single;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api_Service {

    private final String BASE_URL = "http://192.168.0.4/Bank/";
    private Api_Interface api_interface;

    public Api_Service() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request oldRequest = chain.request();
                        Request.Builder newRequestBuilder = oldRequest.newBuilder();
                        newRequestBuilder.addHeader("Acccept", "application/json");
//                        newRequestBuilder.addHeader("Authorization","YOUR TOKEN");
                        return chain.proceed(newRequestBuilder.build());
                    }
                })
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        api_interface = retrofit.create(Api_Interface.class);
    }

    public Single<String> saveStudent(String firstName, String lastName, String phoneNumber) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("first_name", firstName);
        jsonObject.addProperty("last_name", lastName);
        jsonObject.addProperty("phone_number", phoneNumber);
        return api_interface.add_Employee(jsonObject);

    }

    public Single<List<EmployeeModel>> get_List() {
        return api_interface.get_List();
    }

    public Single<ResponseBody> uploadFile(File file, String textDescription, RequestBody requestFile) {

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        // add another part within the multipart request
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, textDescription);

        return api_interface.upload(description, body);
    }

}
