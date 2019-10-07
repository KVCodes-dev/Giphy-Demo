package com.example.atcdemo.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.gson.Gson;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class ServiceCallback<T> implements Callback<T> {

    private static final String TAG = "ServiceCallback";
    private Context context;

    public ServiceCallback(Context context) {
        this.context = context;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onSuccess(response.body());
        } else {
            try {
                String errorJson = response.errorBody().string();
                Log.e("Error", "Errorrrrr : " + errorJson);
                ServiceError error = new Gson().fromJson(errorJson, ServiceError.class);
                error.setStatusCode(response.code());


                JSONObject job = new JSONObject(errorJson);


            } catch (Exception e) {
                onFail(ServiceError.UNKNOWN);
            }
        }
    }
    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFail(ServiceError.from(t));
        t.printStackTrace();
        Log.e("Fail", "Faillllllll " + t.toString());
    }

    public abstract void onFail(ServiceError error);
    public abstract void onSuccess(T response);
}
