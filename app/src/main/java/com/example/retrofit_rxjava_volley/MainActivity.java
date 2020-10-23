package com.example.retrofit_rxjava_volley;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.retrofit_rxjava_volley.Retrofit.Api_Service;
import com.example.retrofit_rxjava_volley.Retrofit.Get_Retrofit_Activity;
import com.example.retrofit_rxjava_volley.Retrofit.Post_Retrofit_Activity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;

import io.reactivex.Observer;
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
    private static final int PICK_IMAGE = 1;
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
                //path off external storage
                File path = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);

                // use the FileUtils to get the actual file by uri
                File file = new File(path, String.valueOf(FileUtils.getFileMetaData(MainActivity.this, data.getData())));

                // create RequestBody instance from file
                RequestBody requestFile =
                        RequestBody.create(
                                MediaType.parse(getContentResolver().getType(data.getData())),
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
                                //you can parse json and show success or failed
                                //in this sample i just show success
                                Toast.makeText(MainActivity.this, "Successfully Uploaded!!!", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("MainActivity Upload", "onError: " + e.getMessage());
                            }
                        });
            }


        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}