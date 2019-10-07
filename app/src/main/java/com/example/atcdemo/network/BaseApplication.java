package com.example.atcdemo.network;

import android.content.Context;
import android.content.IntentFilter;
import android.os.StrictMode;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import com.example.atcdemo.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class BaseApplication extends MultiDexApplication {

    public static BaseApplication applicationCtx;
    private static BaseApplication defaultContext;

    static BaseApplication mInstance;
    private APIInterface apiService;
    private APIInterface customApiService;


    public static BaseApplication getDefaultContext() {
        return defaultContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        defaultContext = this;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());



    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        applicationCtx = this;
        MultiDex.install(this);
    }

    public APIInterface getApiService() {

        if (apiService == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request.Builder requestBuilder = chain.request().newBuilder();
                            requestBuilder.addHeader("Accept", "application/json");
                            return chain.proceed(requestBuilder.build());
                        }
                    })
                    .readTimeout(2, TimeUnit.MINUTES)
                    .writeTimeout(2, TimeUnit.MINUTES)
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getString(R.string.server_url))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();

            apiService = retrofit.create(APIInterface.class);
        }
        return apiService;

    }

    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }


}
