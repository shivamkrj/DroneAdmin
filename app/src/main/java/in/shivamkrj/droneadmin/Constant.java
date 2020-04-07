package in.shivamkrj.droneadmin;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Constant {
    public static String apiFcm = "AAAArhrkbeI:APA91bHVpkHYhVDfkGBbapNMj4Cun1iKDnpGUJE--EtXWTy5FKGq6QrvUXxwi0mDABAS8Pb0PLQc3rTtt06DRBUg6n7AiY7w3oHA8M1LhAXwnyG9gy3MpHDd2zvP2IOqvmTtNj_4xkDf";
    public static String FCM_API_KEY = apiFcm;
    public static void sendGroupPush(Context context, String title, String body) {
        String msg = body;
        FCM_API_KEY = Constant.apiFcm;
        JSONArray regId = null;
        JSONObject to = null;
        JSONObject objData = null;
        JSONObject data = null;
        JSONObject notif = null;
        try {
//            regId = new JSONArray();
//            for (int i = 0; i < tokenlist.size(); i++) {
//                regId.put(tokenlist.get(i));
//            }
            data = new JSONObject();
            data.put("message", "hd");
            notif = new JSONObject();
            notif.put("title", title);
            notif.put("body", body);

            objData = new JSONObject();
            objData.put("to","/topics/all-users");
//            objData.put("registration_ids", regId);
//            objData.put("data", data);
            objData.put("notification", notif);
            Log.e("!_@rj@_group_PASS:>", objData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "https://fcm.googleapis.com/fcm/send";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, objData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("!_@rj@_@@_SUCESS", response + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("!_@rj@_@@_Errors--", error + "");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "key=" + FCM_API_KEY);
                params.put("Content-Type", "application/json");
                Log.e("!_@rj@_@@PUSH_headrers", "::> " + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest)  ;
    }
}
