package com.github.oshhepkov.app;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class App extends Application {

    public static ApiDelegate apiDelegate;

    @Override
    public void onCreate() {
        super.onCreate();
        apiDelegate = new ApiDelegate();
    }
}
