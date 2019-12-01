package com.example.mydoctor.di.modules.appmodule;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.mydoctor.BuildConfig;
import com.example.mydoctor.R;
import com.example.mydoctor.di.MyDoctorApplication;
import com.example.mydoctor.utils.Tls12SocketFactory;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.SSLContext;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    private static final String TAG = "ApiModule";
    private static final String HEADER_PRAGMA = "Pragma";
    private static final String HEADER_CACHE_CONTROL = "Cache-Control" ;
    private final String BASE_URL = "https://thenyakamau.000webhostapp.com/helpline/";//"https://"; //base url
    private static final long cacheSize = 10*1024*1024;  //10mb

    @Singleton
    @Provides
    static RequestOptions requestOptions(){

        return RequestOptions
                .placeholderOf(R.drawable.white_background)
                .error(R.drawable.white_background);

    }

    @Singleton
    @Provides
    static RequestManager requestManager(Application application, RequestOptions requestOptions){

        return Glide.with(application)
                .setDefaultRequestOptions(requestOptions);

    }

    @Singleton
    @Provides
    static Drawable provideAppDrawable(Application application){

        return ContextCompat.getDrawable(application, R.drawable.doctor);

    }

    @Singleton
    @Provides
    Retrofit provideRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getNewHttpClient())
                .build();
    }

    @Provides
    @Singleton
    Picasso providePicasso(Application app, OkHttpClient client) {
        return new Picasso.Builder(app)
                //.downloader(new OkHttpDownloader(client))
                .build();
    }

    private static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 22) {
            try {
                SSLContext sc = SSLContext.getInstance("TLSv1.2");
                sc.init(null, null, null);
                client.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()));

                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build();

                List<ConnectionSpec> specs = new ArrayList<>();
                specs.add(cs);
                specs.add(ConnectionSpec.COMPATIBLE_TLS);
                specs.add(ConnectionSpec.CLEARTEXT);

                client.connectionSpecs(specs);
            } catch (Exception exc) {
                Log.d(TAG, "enableTls12OnPreLollipop: ");
            }
        }

        return client;
    }

    private OkHttpClient getNewHttpClient() {

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .cache(cache())
                .addInterceptor(chain -> {
                    Request request = chain.request();

                    Request.Builder builder = request.newBuilder()
                            .addHeader("Accept", "application/json")
                            .addHeader("Connection", "close");

                    request = builder.build();

                    return chain.proceed(request);
                })
                .addInterceptor(httpLoggingInterceptor())//used if network is off or on
                .addNetworkInterceptor(networkInterceptor()) //only used if network is on
                .addInterceptor(offlineInterceptor()) //only used if network is off
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG){

            client.addNetworkInterceptor(new StethoInterceptor());

        }

        return enableTls12OnPreLollipop(client).build();
    }

    private static Cache cache(){

        return new Cache(new File(MyDoctorApplication.getInstance().getCacheDir(), "fittieMobileCache"), cacheSize);

    }

    private static HttpLoggingInterceptor httpLoggingInterceptor(){


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return logging;
    }

    private static Interceptor offlineInterceptor(){

        return chain -> {

            Request request = chain.request();

            //prevents caching when network is on.

            if (!MyDoctorApplication.hasNetwork()) {

                CacheControl cacheControl = new CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build();

                request = request.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .cacheControl(cacheControl)
                        .build();

            }

            return chain.proceed(request);

        };

    }

    private static Interceptor networkInterceptor(){

        return (chain) -> {

            Response response = chain.proceed(chain.request());

            CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge(10, TimeUnit.SECONDS)
                    .build();


            return response.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                    .build();

        };

    }



}
