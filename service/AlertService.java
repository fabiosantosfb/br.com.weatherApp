package br.com.weatherapp.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.weatherapp.R;
import br.com.weatherapp.base.AppController;
import br.com.weatherapp.broadcast.AlarmReceiver;
import br.com.weatherapp.controller.WeatherController;
import br.com.weatherapp.model.bean.Alert;
import br.com.weatherapp.model.bean.Place;
import br.com.weatherapp.model.dao.AlertDAO;
import br.com.weatherapp.model.dao.PlaceDAO;
import br.com.weatherapp.view.main.MainActivity;
import br.com.weatherapp.ws.OpenWeather;
import br.com.weatherapp.ws.OpenWeatherList;
import br.com.weatherapp.ws.OpenWeatherWS;

public class AlertService extends IntentService {

    private static boolean running = false;

    public AlertService() {
        super("i");

        Log.d("AlertService", "AlertService()");
    }

    public static boolean isRunning() {
        Log.d("AlertService", " isRunning()");

        return running;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("AlertService", "onHandleIntent(Intent intent)");

        running = true;

        List<Place> places = new PlaceDAO().getAllWithAlert();

        if (!places.isEmpty()) {
            Log.d("AlertService", "! places.isEmpty()");

            List<Long> ids = new ArrayList<>();

            for (Place place : places) {
                Log.d("AlertService", "for (Place place : places) ");

                ids.add(place.getId());
            }

            OpenWeatherList weathers = OpenWeatherWS.getInstance()
                    .getWeatherByIds(AppController.getInstance()
                            .getString(R.string.default_units), ids);

            if (weathers != null) {
                Log.d("AlertService", "weathers != null");

                for (OpenWeather weather : weathers.list) {
                    Log.d("AlertService", "for (OpenWeather weather : weathers.list)");

                    WeatherController.getInstance().createOrUpdatePlace(weather);
                }
            }
        }

        List<Alert> alerts = new AlertDAO().getAll();

        for (Alert alert : alerts) {
            Log.d("AlertService", "for (Alert alert : alerts)");

            checkAlert(alert);
        }

        if (alerts.isEmpty()) {
            Log.d("AlertService", "alerts.isEmpty() == true");

            AlarmReceiver.cancelAlarm(this);
        }

        running = false;
    }

    private void checkAlert(Alert alert) {
        Log.d("AlertService", "checkAlert(Alert alert)");

        Place place = alert.getPlace();

        Alert.AlertOption option = alert.getOption();
        Alert.AlertCondition condition = alert.getCondition();
        double value = alert.getValue();
        double valueToCheck = 0;

        switch (option) {
            case Humidity: valueToCheck = place.getWeather().getHumidity();Log.d("AlertService", "option == Humidity"); break;
            case Pressure: valueToCheck = place.getWeather().getPressure();Log.d("AlertService", "option == Pressure"); break;
            case Temperature: valueToCheck = place.getWeather().getTemperature();Log.d("AlertService", "option == Temperature"); break;
            case WindSpeed: valueToCheck = place.getWeather().getWindSpeed();Log.d("AlertService", "option == WindSpeed"); break;
            default: Log.d("AlertService", "option == default"); /* Empty */
        }

        boolean needAlert;

        if (condition == Alert.AlertCondition.GreaterThan) {
            Log.d("AlertService", "condition == Alert.AlertCondition.GreaterThan");

            needAlert = valueToCheck > value;
        } else {
            Log.d("AlertService", "condition != Alert.AlertCondition.GreaterThan");

            needAlert = valueToCheck < value;
        }

        if (needAlert) {
            Log.d("AlertService", "needAlert == true");

            sendNotification(place);
        }
    }

    private void sendNotification(Place place) {
        Log.d("AlertService", "sendNotification(Place place)");

        String contentText = String.format("%s: %s",
                getString(R.string.climate_alert), place.getCity());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(contentText)
                .setTicker(contentText);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        Intent it = new Intent(this, MainActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        it.putExtra(MainActivity.ALERT_KEY, true);
        it.putExtra(MainActivity.PLACE_KEY, place);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) place.getId(),
                it, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify((int) place.getId(), notification);
    }
}
