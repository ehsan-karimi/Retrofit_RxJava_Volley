package com.example.retrofit_rxjava_volley;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.retrofit_rxjava_volley.Retrofit.Api_Service;
import com.example.retrofit_rxjava_volley.Retrofit.Get_Retrofit_Activity;
import com.example.retrofit_rxjava_volley.Retrofit.Post_Retrofit_Activity;
import com.example.retrofit_rxjava_volley.Volley.Get_Volley_Activity;
import com.example.retrofit_rxjava_volley.Volley.Post_Volley_Activity;
import com.example.retrofit_rxjava_volley.Volley.Request_Handler;
import com.example.retrofit_rxjava_volley.Volley.VolleyCallback;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private Button btn_get_retrofit;
    private Button btn_post_retrofit;
    private Button btn_upload_retrofit;
    private Button btn_get_volley;
    private Button btn_post_volley;
    private Button btn_upload_volley;
    private static final int PICK_IMAGE = 1;
    private Api_Service apiService;
    private Disposable disposable;
    private Request_Handler volleyRequest = Request_Handler.getInstance();
    private Boolean its_Retrofit;

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
        btn_get_volley = findViewById(R.id.btn_get_volley);
        btn_post_volley = findViewById(R.id.btn_post_volley);
        btn_upload_volley = findViewById(R.id.btn_upload_volley);

        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */
                        //   Toast.makeText(getApplicationContext(),"granted",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();
    }

    @SuppressLint("IntentReset")
    private void set_OnClick() {
        btn_get_retrofit.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Get_Retrofit_Activity.class);
            startActivity(intent);
        });

        btn_post_retrofit.setOnClickListener((v) -> {
            Intent intent = new Intent(MainActivity.this, Post_Retrofit_Activity.class);
            startActivity(intent);
        });

        btn_upload_retrofit.setOnClickListener(v -> {
            its_Retrofit = true;
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

            startActivityForResult(chooserIntent, PICK_IMAGE);

        });

        btn_get_volley.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Get_Volley_Activity.class);
            startActivity(intent);
        });

        btn_post_volley.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Post_Volley_Activity.class);
            startActivity(intent);
        });

        btn_upload_volley.setOnClickListener(v -> {
            its_Retrofit = false;
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

            startActivityForResult(chooserIntent, PICK_IMAGE);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {

            if (data != null) {
                if (its_Retrofit) {
                    //path off external storage
                    File path = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES);

                    // use the FileUtils to get the actual file by uri
                    File file = new File(path, String.valueOf(FileUtils.getFileMetaData(MainActivity.this, Objects.requireNonNull(data.getData()))));

                    // create RequestBody instance from file
                    RequestBody requestFile =
                            RequestBody.create(
                                    MediaType.parse(Objects.requireNonNull(getContentResolver().getType(data.getData()))),
                                    file
                            );
                    apiService.uploadFile(file, "description", requestFile)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SingleObserver<ResponseBody>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    MainActivity.this.disposable = d;
                                }

                                @Override
                                public void onSuccess(ResponseBody responseBody) {
                                    Toast.makeText(MainActivity.this, "Successfully Uploaded!!!", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(MainActivity.this, "Unspecified Error!!!", Toast.LENGTH_LONG).show();
                                }
                            });
                } else {
                    Map<String, String> params = new HashMap<>();
//                        params.put("id", "1");
                    volleyRequest.upload_File("displayName.jpg", data.getData(), MainActivity.this, "upload.php", params, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Toast.makeText(MainActivity.this, "Successfully Uploaded!!!", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(VolleyError volleyError) {
                            Toast.makeText(MainActivity.this, "Unspecified Error!!!", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }


        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}