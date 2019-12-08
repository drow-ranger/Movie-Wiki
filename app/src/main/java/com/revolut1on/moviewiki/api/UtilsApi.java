package com.revolut1on.moviewiki.api;

/**
 * Created by Deo Fibrianico on 04,December,2019
 * Visit https://revolut1on.com
 */

public class UtilsApi {
    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    public static BaseApiService getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(BaseApiService.class);
    }
}
