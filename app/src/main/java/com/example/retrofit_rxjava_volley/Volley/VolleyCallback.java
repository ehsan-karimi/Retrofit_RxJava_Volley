package com.example.retrofit_rxjava_volley.Volley;

import com.android.volley.VolleyError;

public interface VolleyCallback {
    void onSuccess(String result);
    void onFailure(VolleyError volleyError);
}
