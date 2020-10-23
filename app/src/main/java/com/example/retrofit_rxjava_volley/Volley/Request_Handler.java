package com.example.retrofit_rxjava_volley.Volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Request_Handler {

    private RequestQueue requestQueue;

    private static Request_Handler INSTANCE = null;

    private final String BASE_URL = "http://192.168.0.3/Bank/";

    // other instance variables can be here

    private Request_Handler() {
    }


    public static Request_Handler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Request_Handler();
        }
        return (INSTANCE);
    }

    //  private VolleyCallback callback;

    //request to server with get method
    public void get_Request(String url, Context context, final VolleyCallback callback) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                BASE_URL + url,

                response -> {
                    //       Log.v("volley said: ", response);
                    callback.onSuccess(response);
                }, error -> VolleyLog.d("volley error said: ", error.toString()));

        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjReq);
    }

    // request to server with post method and body
    public void post_Request(String url, Context context, JSONObject jsonObject, final VolleyCallback callback) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        final String requestBody = jsonObject.toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                BASE_URL + url, null, response -> {
            callback.onSuccess(response.toString());
            // VolleyLog.d("volley said: ", response.toString());
        }, error -> VolleyLog.e("volley error said: ", String.valueOf(error.networkResponse))) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

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
        requestQueue.add(jsonObjectRequest);
    }

    // request to server with post method and params
    public void post_Request_With_Params(String url, Context context, Map<String, String> params, final VolleyCallback callback) {

        StringRequest string_Req = new StringRequest(Request.Method.POST, BASE_URL + url, response -> {
            callback.onSuccess(response);
            //   Log.d("volley said: ", response);
        }, error -> Log.d("volley error said: ", error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(string_Req);
    }

    // request to server with post method and body
    public void test(String url, Context context, JSONObject jsonObject, final VolleyCallback callback) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        final String requestBody = jsonObject.toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                "https://fcm.googleapis.com/v1/projects/myproject-b5ae1/messages:send", null, response -> {
            callback.onSuccess(response.toString());
            //  VolleyLog.d("volley said: ", response.toString());
        }, error -> VolleyLog.e("volley error said: ", String.valueOf(error.networkResponse))) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();

                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + "AIzaSyC9MdzIcZtSbL98FaeeKe1QZyOgprrDicg");

                return headers;
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    //   VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    //           requestBody, "utf-8");
                    return null;
                }
            }
        };
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    public void post_Request_Image(String url, Context context, Map<String, String> params, final VolleyCallback callback) {

        StringRequest string_Req = new StringRequest(Request.Method.POST, BASE_URL + url, response -> {
            callback.onSuccess(response);
            // Log.d("volley said: ", response);
        }, error -> Log.d("volley error said: ", error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        string_Req.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(string_Req);
    }

    public void post_Request_With_Params_Custom_url(String url, Context context, Map<String, String> params, final VolleyCallback callback) {

        StringRequest string_Req = new StringRequest(Request.Method.POST, url, response -> {
            callback.onSuccess(response);
            //     Log.d("volley said: ", response);
        }, error -> Log.d("volley error said: ", error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(string_Req);
    }

//    public void upload_File(final String pdfname, Uri pdffile, Context context, String url, Map<String, String> params, final VolleyCallback callback) {
//
//        InputStream iStream = null;
//        try {
//            Bitmap bitmap = Utils.get_bitmap(pdffile, context, "lol");
//
//
//            iStream = context.getContentResolver().openInputStream(getImageUri(context, bitmap));
//            final byte[] inputData = getBytes(iStream);
//
//            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, BASE_URL + url,
//                    new Response.Listener<NetworkResponse>() {
//                        @Override
//                        public void onResponse(NetworkResponse response) {
//                            //   Log.d("ressssssoo",new String(response.data));
//                            requestQueue.getCache().clear();
//                            callback.onSuccess(new String(response.data));
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            //   Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
//                            try {
//                                Log.e("volley error", error.toString());
//                            } catch (Exception e) {
//                            }
//                        }
//                    }) {
//
//                /*
//                 * If you want to add more parameters with the image
//                 * you can do it here
//                 * here we have only one parameter with the image
//                 * which is tags
//                 * */
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    //    Map<String, String> params = new HashMap<>();
//                    // params.put("tags", "ccccc");  add string parameters
//                    return params;
//                }
//
//                /*
//                 *pass files using below method
//                 * */
//                @Override
//                protected Map<String, DataPart> getByteData() {
//                    Map<String, DataPart> params = new HashMap<>();
//
//                    params.put("filename", new DataPart(pdfname, inputData));
//                    return params;
//                }
//            };
//
//
//            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
//                    0,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            requestQueue = Volley.newRequestQueue(context);
//            requestQueue.add(volleyMultipartRequest);
//
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }


}
