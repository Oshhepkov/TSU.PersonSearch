package com.github.oshhepkov.app;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiDelegate {

    private final ApiService apiService;

    ApiDelegate() {
        apiService = createService();
    }

    private ApiService createService() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        Retrofit client = new Retrofit.Builder()
                .baseUrl("https://persona.tsu.ru")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return client.create(ApiService.class);
    }

    public LiveData<List<CommonItem>> findUsers(String term) {
        final MutableLiveData<List<CommonItem>> result = new MutableLiveData<>();
        if (term == null || term.isEmpty()) {
            result.setValue(CommonItem.FirstTimeItem.create());
            return result;
        }
        result.setValue(CommonItem.LoadItem.create());
        apiService.getUsersByTerm(term).enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserModel>> call, @NonNull Response<List<UserModel>> response) {
                if (response.isSuccessful()) {
                    result.setValue(CommonItem.UserItem.create(response.body()));
                } else {
                    result.setValue(CommonItem.ErrorItem.create(new Exception("Ошибка сервера: \""
                            + response.message() + "\" (" + response.code() + ")")));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UserModel>> call, @NonNull Throwable t) {
                result.setValue(CommonItem.ErrorItem.create(new Exception("Опаньки, похоже у Вас закончился интернет :(")));
            }
        });
        return result;
    }

    public LiveData<ConcreteUserStatus> getConcreteUser(String accountId) {
        final MutableLiveData<ConcreteUserStatus> result = new MutableLiveData<>();
        result.setValue(ConcreteUserStatus.loading());
        apiService.getUserIdByAccountId(accountId).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (response.isSuccessful()) {
                    apiService.getUserInfo(response.body()).enqueue(new Callback<ConcreteUserModel>() {
                        @Override
                        public void onResponse(@NonNull Call<ConcreteUserModel> call, @NonNull Response<ConcreteUserModel> response) {
                            if (response.isSuccessful()) {
                                result.setValue(ConcreteUserStatus.success(response.body()));
                            } else {
                                result.setValue(ConcreteUserStatus.error("Ошибка сервера: \""
                                        + response.message() + "\" (" + response.code() + ")"));
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ConcreteUserModel> call, @NonNull Throwable t) {
                            result.setValue(ConcreteUserStatus.error("Опаньки, похоже у Вас закончился интернет :("));
                        }
                    });
                } else {
                    result.setValue(ConcreteUserStatus.error("Ошибка сервера: \""
                            + response.message() + "\" (" + response.code() + ")"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                result.setValue(ConcreteUserStatus.error("Опаньки, похоже у Вас закончился интернет :("));
            }
        });
        return result;
    }
}
