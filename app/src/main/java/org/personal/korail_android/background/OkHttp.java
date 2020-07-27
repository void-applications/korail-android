package org.personal.korail_android.background;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttp {

    public String getData(String url) {

        String result = null;
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = okHttpClient.newCall(request).execute();

            result = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
