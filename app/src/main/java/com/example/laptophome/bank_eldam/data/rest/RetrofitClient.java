package com.example.laptophome.bank_eldam.data.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static final String BaseUrl="http://ipda3.com/blood-bank/api/v1/";
    private static Retrofit retrofit=null;
    public static Retrofit getclient()
    {
        if(retrofit==null)
        {
            retrofit=new Retrofit.Builder()
                    .baseUrl(BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
