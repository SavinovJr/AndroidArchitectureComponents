package com.example.retrofitdemo.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit sRetrofit = null;

    private static String BASE_URL = "http://www.groupkt.com/";

    public static CountryService getService() {
        if (sRetrofit == null) {
            sRetrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return sRetrofit.create(CountryService.class);

    }
}
