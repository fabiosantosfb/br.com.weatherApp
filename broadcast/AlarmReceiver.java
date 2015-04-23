package ceducarneiro.com.br.weatherapp.broadcast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ceducarneiro.com.br.weatherapp.service.AlertService;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "onReceive");

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Log.d("AlarmReceiver", "BOOT_COMPLETED == true");

            scheduleAlarm(context);
        } else {
            Log.d("AlarmReceiver", "BOOT_COMPLETED == false");

            if (!AlertService.isRunning()) {
                Log.d("AlarmReceiver", "!AlertService.isRunning()");

                Intent service = new Intent(context, AlertService.class);
                context.startService(service);
            }
        }
    }

    public static void scheduleAlarm(Context context) {
        Log.d("AlarmReceiver", "scheduleAlarm(Context context)");

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long firstMillis = System.currentTimeMillis();
        int intervalMillis = 1 * 60 * 1000; /* 1 minuto */
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                intervalMillis, pIntent);
    }

    public static void cancelAlarm(Context context) {
        Log.d("AlarmReceiver", "cancelAlarm(Context context)");

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }

}
