package com.github.oshhepkov.app;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import java.util.List;

public class MainViewModel extends ViewModel {

    private final LiveData<List<CommonItem>> mContent;
    private final MutableLiveData<String> mQuery = new MutableLiveData<>();

    public MainViewModel() {
        final ApiDelegate repository = App.apiDelegate;
        mContent = Transformations.switchMap(mQuery, new Function<String, LiveData<List<CommonItem>>>() {
            @Override
            public LiveData<List<CommonItem>> apply(String input) {
                return repository.findUsers(input);
            }
        });
    }

    public void makeQuery(String query) {
        mQuery.setValue(query);
    }

    public LiveData<List<CommonItem>> getContent() {
        return mContent;
    }
}
