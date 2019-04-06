package com.github.oshhepkov.app;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

public class InfoViewModel extends ViewModel {

    private final LiveData<ConcreteUserStatus> userStatus;
    private final MutableLiveData<String> mQuery = new MutableLiveData<>();

    public InfoViewModel() {
        final ApiDelegate repository = App.apiDelegate;
        userStatus = Transformations.switchMap(mQuery, new Function<String, LiveData<ConcreteUserStatus>>() {
            @Override
            public LiveData<ConcreteUserStatus> apply(String input) {
                return repository.getConcreteUser(input);
            }
        });
    }

    public void setAccountId(String id) {
        mQuery.setValue(id);
    }

    public LiveData<ConcreteUserStatus> getContent() {
        return userStatus;
    }
}