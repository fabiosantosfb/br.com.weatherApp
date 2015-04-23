package ceducarneiro.com.br.weatherapp.controller;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import ceducarneiro.com.br.weatherapp.R;
import ceducarneiro.com.br.weatherapp.base.AppController;
import ceducarneiro.com.br.weatherapp.model.bean.Place;
import ceducarneiro.com.br.weatherapp.model.bean.Weather;
import ceducarneiro.com.br.weatherapp.model.dao.PlaceDAO;
import ceducarneiro.com.br.weatherapp.ws.OpenWeather;
import ceducarneiro.com.br.weatherapp.ws.OpenWeatherWS;

public class WeatherController {

    private static WeatherController instance;

    private WeatherController() {
        Log.d("WeatherController", "WeatherController()");
    }

    public static synchronized WeatherController getInstance() {
        Log.d("WeatherController", "getInstance()");

        if (instance == null) {
            Log.d("WeatherController", "instance == null");

            instance = new WeatherController();
        }

        return instance;
    }

    public void getWeatherByLatLng(LatLng latLng) {
        Log.d("WeatherController", "getWeatherByLatLng(LatLng latLng) ");

        new WeatherLatLngTask().execute(latLng);
    }

    public void getWeatherByPlace(String place, String country) {
        Log.d("WeatherController", "getWeatherByPlace(String place, String country)");

        new WeatherPlaceTask().execute(place, country);
    }

    public Place createOrUpdatePlace(OpenWeather openWeather) {
        Log.d("WeatherController", "createOrUpdatePlace(OpenWeather openWeather)");

        PlaceDAO placeDAO = new PlaceDAO();

        Place place = placeDAO.findById(openWeather.id);

        if (place == null) {
            Log.d("WeatherController", "place == null");

            place = new Place();;
        }

        if (openWeather.weathers.size() > 0) {
            Log.d("WeatherController", "openWeather.weathers.size() > 0");

            place.getWeather().setCode(openWeather.weathers.get(0).code);
            place.getWeather().setIcon(openWeather.weathers.get(0).icon);
        }
        place.getWeather().setTemperature(openWeather.main.temp);
        place.getWeather().setHumidity(openWeather.main.humidity);
        place.getWeather().setPressure(openWeather.main.pressure);
        place.getWeather().setWindDegree(openWeather.wind.degree);
        place.getWeather().setWindSpeed(openWeather.wind.speed);
        place.getWeather().setLastUpdate(new Date(openWeather.timestamp *1000));

        place.setId(openWeather.id);
        place.setCity(openWeather.name);
        place.setCountry(openWeather.system.country);
        place.setLatitude(openWeather.coordinates.latitude);
        place.setLongitude(openWeather.coordinates.longitude);

        new PlaceDAO().insertOrUpdate(place);

        return place;
    }

    private class WeatherLatLngTask extends WeatherTask<LatLng, Void> {

        @Override
        protected OpenWeather doInBackground(LatLng... params) {
            Log.d("WeatherController class WeatherLatLngTask", "OpenWeather doInBackground(LatLng... params)");

            return OpenWeatherWS.getInstance().getWeatherByLatLng(
                    AppController.getInstance().getString(R.string.default_units),
                    params[0].latitude, params[0].longitude);
        }
    }

    private class WeatherPlaceTask extends WeatherTask<String, Void> {

        @Override
        protected OpenWeather doInBackground(String... params) {
            Log.d("WeatherController class WeatherPlaceTask", "OpenWeather doInBackground(String... params)");

            return OpenWeatherWS.getInstance().getWeatherByPlace(
                    AppController.getInstance().getString(R.string.default_units),
                    params[0], params[1]);
        }
    }

    private abstract class WeatherTask<a, b> extends AsyncTask<a, b, OpenWeather> {

        @Override
        protected void onPreExecute() {
            Log.d("WeatherController class WeatherTask", "onPreExecute()");

            ListenerController.getInstance().notifyWeatherListeners(
                    WeatherListener.ON_GET_WEATHER_START, null);
        }

        @Override
        protected void onPostExecute(OpenWeather weather) {
            Log.d("WeatherController class WeatherTask", " onPostExecute(OpenWeather weather)");

            ListenerController.getInstance().notifyWeatherListeners(
                    WeatherListener.ON_GET_WEATHER_FINISH, weather);
        }
    }

}
