package br.com.weatherapp.controller;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ListenerController {

    private static ListenerController instance;

    private List<WeatherListener> weatherListeners;
    private List<SyncListener> syncListeners;

    private ListenerController() {
        Log.d("ListenerController", "ListenerController()");

        weatherListeners = new ArrayList<>();
        syncListeners = new ArrayList<>();
    }

    public static synchronized ListenerController getInstance() {
        Log.d("ListenerController", "getInstance()");

        if (instance == null) {
            Log.d("ListenerController", "instance == null");

            instance = new ListenerController();
        }

        return instance;
    }

    public void addWeatherListener(WeatherListener listener) {
        Log.d("ListenerController", "addWeatherListener(WeatherListener listener)");

        weatherListeners.remove(listener);
        weatherListeners.add(listener);
    }

    public void removeWeatherListener(WeatherListener listener) {
        Log.d("ListenerController", "removeWeatherListener(WeatherListener listener)");

        weatherListeners.remove(listener);
    }

    public void addSyncListener(SyncListener listener) {
        Log.d("ListenerController", "addSyncListener(SyncListener listener)");

        syncListeners.remove(listener);
        syncListeners.add(listener);
    }

    public void removeSyncListener(SyncListener listener) {
        Log.d("ListenerController", "removeSyncListener(SyncListener listener)");

        syncListeners.remove(listener);
    }

    public void notifyWeatherListeners(String event, Object... args) {
        Log.d("ListenerController", "notifyWeatherListeners(String event, Object... args)");

        List param = new ArrayList();

        if (args != null) {
            Log.d("ListenerController", "args != null");

            for (Object arg : args) {
                param.add(arg);
            }
        }

        for (WeatherListener listener : weatherListeners) {
            Log.d("ListenerController", "for (WeatherListener listener : weatherListeners)");

            callMethod(listener, event, param);
        }
    }

    public void notifySyncListeners(String event, Object... args) {
        Log.d("ListenerController", "notifySyncListeners(String event, Object... args)");

        List param = new ArrayList();

        if (args != null) {
            Log.d("ListenerController", "args != null");

            for (Object arg : args) {
                Log.d("ListenerController", "for (Object arg : args)");

                param.add(arg);
            }
        }

        for (SyncListener listener : syncListeners) {
            Log.d("ListenerController", "for (SyncListener listener : syncListeners)");

            callMethod(listener, event, param);
        }
    }

    private void callMethod(Object obj, String methodName, List param) {
        try {
            Log.d("ListenerController", "try {callMethod(Object obj, String methodName, List param)}");

            Method method = obj.getClass().getMethod(methodName, List.class);
            method.invoke(obj, param);
        } catch (Exception ignored) {
            Log.d("ListenerController", "catch (Exception ignored)");
            /* Empty */
        }
    }

}
