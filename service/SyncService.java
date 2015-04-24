package ceducarneiro.com.br.weatherapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ceducarneiro.com.br.weatherapp.R;
import ceducarneiro.com.br.weatherapp.base.AppController;
import ceducarneiro.com.br.weatherapp.controller.ListenerController;
import ceducarneiro.com.br.weatherapp.controller.SyncListener;
import ceducarneiro.com.br.weatherapp.controller.WeatherController;
import ceducarneiro.com.br.weatherapp.model.bean.Place;
import ceducarneiro.com.br.weatherapp.model.dao.PlaceDAO;
import ceducarneiro.com.br.weatherapp.ws.OpenWeather;
import ceducarneiro.com.br.weatherapp.ws.OpenWeatherList;
import ceducarneiro.com.br.weatherapp.ws.OpenWeatherWS;

public class SyncService extends IntentService {

    private static boolean running = false;

    public SyncService() {
        super("SyncService");

        Log.d("SyncService", "SyncService()");
    }

    public static boolean isRunning() {
        Log.d("SyncService", "isRunning()");

        return running;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("SyncService", "onHandleIntent(Intent intent)");

        running = true;

        ListenerController.getInstance().notifySyncListeners(
                SyncListener.ON_SYNC_START, null);

        OpenWeatherWS openWeatherWS = OpenWeatherWS.getInstance();
        PlaceDAO placeDAO = new PlaceDAO();


        List<Place> places = placeDAO.getAll();

        if (places.size() > 0) {
            Log.d("SyncService", "places.size() > 0");

            List<Long> ids = new ArrayList<>();

            for (Place place : places) {
                Log.d("SyncService", " for (Place place : places)");

                ids.add(place.getId());
            }

            OpenWeatherList weathers = openWeatherWS.getWeatherByIds(AppController
                    .getInstance().getString(R.string.default_units), ids);

            if (weathers != null) {
                Log.d("SyncService", "weathers != null");

                for (OpenWeather weather : weathers.list) {
                    Log.d("SyncService", " for (OpenWeather weather : weathers.list)");

                    WeatherController.getInstance().createOrUpdatePlace(weather);
                }
            }
        }

        ListenerController.getInstance().notifySyncListeners(
                SyncListener.ON_SYNC_FINISH, null);

        running = false;
    }
}
