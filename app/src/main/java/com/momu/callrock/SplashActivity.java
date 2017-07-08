package com.momu.callrock;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songm on 2017-07-09.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getMsrstnList?pageNo=1&numOfRows=400&ServiceKey=NCPIDFE%2F7buZ0eIVTd6x6iqFLtZRkGcVW%2FZuKO1g%2BM9cCN8YBQBmPIKzaP%2B9MTfyyNMhXDS3SkK8%2FjiyINYe0A%3D%3D&_returnType=json";

        final SharedPreferences preferences =getSharedPreferences("Pref",MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonObject = response.getJSONArray("list");
                    JSONArray result = new JSONArray();

                    if(jsonObject!=null) {
                        for(int i = 0; i<jsonObject.length();i++){
                            JSONObject o = jsonObject.getJSONObject(i);

                            JSONObject curObs = new JSONObject();
                            curObs.put("addr", o.getString("addr"));
                            curObs.put("dmX",o.getDouble("dmX"));
                            curObs.put("dmY",o.getDouble("dmY"));
                            curObs.put("oper",o.getString("oper"));
                            curObs.put("stationName", o.getString("stationName"));

                            result.put(curObs);
                        }

                        editor.putString("Observe",result.toString());
                        editor.commit();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        queue.add(jsonRequest);
    }
}
