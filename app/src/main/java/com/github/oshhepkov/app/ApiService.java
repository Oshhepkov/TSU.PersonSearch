package com.github.oshhepkov.app;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("api/User")
    Call<List<UserModel>> getUsersByTerm(@Query("term") String term);

    @GET("/api/User/{id}")
    Call<ConcreteUserModel> getUserInfo(@Path("id") Integer id);

    @GET("api/User")
    Call<Integer> getUserIdByAccountId(@Query("accountId") String accountId);

}
