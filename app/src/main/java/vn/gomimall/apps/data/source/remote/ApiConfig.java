package vn.gomimall.apps.data.source.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.gomimall.apps.BuildConfig;

/**
 * Created by KHOI LE on 3/13/2020.
 */
public class ApiConfig {

    public static int HTTP_CONNECTION_TIME_OUT = 10; // 10 seconds
    public static int HTTP_READ_TIME_OUT = 60; // 60 seconds, 0 -> no time out
    public static int HTTP_WRITE_TIME_OUT = 60; // 60 seconds, 0 - > no time mout

    public static OkHttpClient createHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(HTTP_CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(HTTP_READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(HTTP_WRITE_TIME_OUT, TimeUnit.SECONDS);
        return builder.build();

    }

    public static OkHttpClient createHttpLoggingClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(HTTP_CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(HTTP_READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(HTTP_WRITE_TIME_OUT, TimeUnit.SECONDS);

        builder.addInterceptor(logging);
        return builder.build();
    }

    public static Retrofit createRetrofit(final String baseUrl) {
        if (BuildConfig.DEBUG) {
            return createLoggingRetrofit(baseUrl);
        }

        return new Retrofit.Builder()
                .client(createHttpClient())
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static final Retrofit createLoggingRetrofit(final String baseUrl) {
        return new Retrofit.Builder()
                .client(createHttpLoggingClient())
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    public static final ApiService getClient() {
        Retrofit retrofit = createRetrofit(EndPoint.BASE_URL);
        return retrofit.create(ApiService.class);
    }

    public static final AgoraService getAgoraClient() {
        Retrofit retrofit = createRetrofit(AgoraEndPoint.BASE_URL);
        return retrofit.create(AgoraService.class);
    }
}
