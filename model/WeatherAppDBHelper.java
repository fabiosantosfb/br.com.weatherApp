package br.com.weatherapp.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WeatherAppDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weatherapp.db";
    private static final int DATABASE_VERSION = 1;

    private static WeatherAppDBHelper instance;

    private WeatherAppDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        Log.d("WeatherAppDBHelper", "WeatherAppDBHelper(Context context)");
    }

    public static synchronized WeatherAppDBHelper getInstance(Context context) {
        Log.d("WeatherAppDBHelper", "getInstance(Context context)");

        if (instance == null) {
            Log.d("WeatherAppDBHelper", "instance == null");

            instance = new WeatherAppDBHelper(context);
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("WeatherAppDBHelper", "onCreate(SQLiteDatabase db)");

        createTablePlace(db);
        createTableWeather(db);
        createTableAlert(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("WeatherAppDBHelper", "onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)");
    }

    private void createTablePlace(SQLiteDatabase db) {
        Log.d("WeatherAppDBHelper", "createTablePlace(SQLiteDatabase db)");

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, " +
                        "%s REAL, %s REAL, %s INTEGER)",
                WeatherAppDBContract.Place.TABLE_NAME, WeatherAppDBContract.Place.COLUMN_ID,
                WeatherAppDBContract.Place.COLUMN_CITY, WeatherAppDBContract.Place.COLUMN_COUNTRY,
                WeatherAppDBContract.Place.COLUMN_LATITUDE, WeatherAppDBContract.Place.COLUMN_LONGITUDE,
                WeatherAppDBContract.Place.COLUMN_WEATHER_ID));
    }

    private void createTableWeather(SQLiteDatabase db) {
        Log.d("WeatherAppDBHelper", "createTableWeather(SQLiteDatabase db)");

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s INTEGER, %s TEXT, " +
                        "%s REAL, %s INTEGER, %s REAL, %s REAL, %s REAL, %s INTEGER)",
                WeatherAppDBContract.Weather.TABLE_NAME, WeatherAppDBContract.Weather.COLUMN_ID,
                WeatherAppDBContract.Weather.COLUMN_CODE, WeatherAppDBContract.Weather.COLUMN_ICON,
                WeatherAppDBContract.Weather.COLUMN_TEMPERATURE, WeatherAppDBContract.Weather.COLUMN_HUMIDITY,
                WeatherAppDBContract.Weather.COLUMN_PRESSURE, WeatherAppDBContract.Weather.COLUMN_WIND_DEGREE,
                WeatherAppDBContract.Weather.COLUMN_WIND_SPEED, WeatherAppDBContract.Weather.COLUMN_LAST_UPDATE));
    }

    private void createTableAlert(SQLiteDatabase db) {
        Log.d("WeatherAppDBHelper", "createTableAlert(SQLiteDatabase db)");

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s INTEGER, " +
                        "%s INTEGER, %s REAL, %s INTEGER)",
                WeatherAppDBContract.Alert.TABLE_NAME, WeatherAppDBContract.Alert.COLUMN_ID,
                WeatherAppDBContract.Alert.COLUMN_OPTION, WeatherAppDBContract.Alert.COLUMN_CONDITION,
                WeatherAppDBContract.Alert.COLUMN_VALUE, WeatherAppDBContract.Alert.COLUMN_PLACE_ID));
    }

}
