package com.example.retrofit_rxjava_volley.Volley;

import android.content.Context;
import android.net.Uri;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Request_Handler {

    private RequestQueue requestQueue;

    private static Request_Handler INSTANCE = null;

    private final String BASE_URL = "http://192.168.0.4/Bank/";


    private Request_Handler() {
    }


    public static Request_Handler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Request_Handler();
        }
        return (INSTANCE);
    }


    public void get_Request(String url, Context context, final VolleyCallback callback) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                BASE_URL + url,

                callback::onSuccess, callback::onFailure);

        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjReq);
    }


    public void post_Request(String url, Context context, JsonObject params, final VolleyCallback callback) {

        final String requestBody = params.toString();

        StringRequest string_Req = new StringRequest(Request.Method.POST, BASE_URL + url,
                callback::onSuccess,
                callback::onFailure) {

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    //     VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    //           requestBody, "utf-8");
                    return null;
                }
            }
        };
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(string_Req);
    }


    public void upload_File(final String fname, Uri uri, Context context, String url, Map<String, String> params, final VolleyCallback callback) {

        InputStream iStream = null;
        try {

            iStream = context.getContentResolver().openInputStream(uri);
            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, BASE_URL + url,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            requestQueue.getCache().clear();
                            callback.onSuccess(new String(response.data));
                        }
                    },
                    callback::onFailure) {

                @Override
                protected Map<String, String> getParams() {
                    return params;
                }

                /*
                 *pass files using below method
                 * */
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();

                    params.put("picture", new DataPart(fname, inputData));
                    return params;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(volleyMultipartRequest);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


}
