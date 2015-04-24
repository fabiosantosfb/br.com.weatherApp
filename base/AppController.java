package br.com.weatherapp.base;

import android.app.Application;
import android.util.Log;

public class AppController extends Application {

    private static AppController instance;

    public AppController() {
        Log.d("AppController","construtor");
        instance = this;
    }

    public static synchronized AppController getInstance() {
        Log.d("AppController","getInstance()");
        if (instance == null) {
            Log.d("AppController","instance == null");
            new AppController();
        }

        return instance;
    }

}
