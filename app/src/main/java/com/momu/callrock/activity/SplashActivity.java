package com.momu.callrock.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.momu.callrock.R;
import com.momu.callrock.constant.CConstants;
import com.momu.callrock.utility.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 스플레시 페이지
 * Created by songm on 2017-07-09.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getObserve();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
////                startActivity(new Intent(SplashActivity.this, MainActivity.class));
////                finish();
//            }
//        }, 500);
    }


    private void getObserve() {
        RequestQueue queue = Volley.newRequestQueue(this);

        final SharedPreferences preferences = getSharedPreferences("Pref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, CConstants.URL_STATION_INFO, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonObject = response.getJSONArray("list");
                    LogHelper.e("@@@", jsonObject.toString());
                    JSONArray result = new JSONArray();

                    if (jsonObject != null) {
                        for (int i = 0; i < jsonObject.length(); i++) {
                            JSONObject o = jsonObject.getJSONObject(i);

                            JSONObject curObs = new JSONObject();
                            curObs.put("addr", o.getString("addr"));
                            curObs.put("dmX", o.getDouble("dmX"));
                            curObs.put("dmY", o.getDouble("dmY"));
                            curObs.put("oper", o.getString("oper"));
                            curObs.put("stationName", o.getString("stationName"));

                            result.put(curObs);
                        }

                        editor.putString("Observe", result.toString()).apply();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });

        queue.add(jsonRequest);
    }
}
