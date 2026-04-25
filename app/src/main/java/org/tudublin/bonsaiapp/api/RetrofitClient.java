package org.tudublin.bonsaiapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://bonsaiapi-ewe7gdd0hfd8a8dv.westeurope-01.azurewebsites.net";

    private static Retrofit retrofit;
    private static BonsaiApiService service;

    public static BonsaiApiService getService() {
        if (service == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofit.create(BonsaiApiService.class);
        }
        return service;
    }
}