package br.com.weatherapp.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.Date;

import br.com.weatherapp.model.WeatherAppDBContract;
import br.com.weatherapp.model.bean.Weather;

public class WeatherDAO extends BaseDAO<Weather> {

    public WeatherDAO() {
        super(Weather.class);
        Log.d("WeatherDAO", "WeatherDAO()");
    }

    @Override
    protected void fillContent(ContentValues content, Weather object) {
        Log.d("WeatherDAO", "fillContent(ContentValues content, Weather object)");

        content.put(WeatherAppDBContract.Weather.COLUMN_CODE, object.getCode());
        content.put(WeatherAppDBContract.Weather.COLUMN_ICON, object.getIcon());
        content.put(WeatherAppDBContract.Weather.COLUMN_TEMPERATURE, object.getTemperature());
        content.put(WeatherAppDBContract.Weather.COLUMN_HUMIDITY, object.getHumidity());
        content.put(WeatherAppDBContract.Weather.COLUMN_PRESSURE, object.getPressure());
        content.put(WeatherAppDBContract.Weather.COLUMN_WIND_DEGREE, object.getWindDegree());
        content.put(WeatherAppDBContract.Weather.COLUMN_WIND_SPEED, object.getWindSpeed());
        content.put(WeatherAppDBContract.Weather.COLUMN_LAST_UPDATE, object.getLastUpdate().getTime());
    }

    @Override
    protected void fillObject(Weather object, Cursor cursor) {
        Log.d("WeatherDAO", "fillObject(Weather object, Cursor cursor)");

        object.setId(cursor.getLong(cursor.getColumnIndexOrThrow(WeatherAppDBContract.Weather.COLUMN_ID)));
        object.setCode(cursor.getInt(cursor.getColumnIndexOrThrow(WeatherAppDBContract.Weather.COLUMN_CODE)));
        object.setIcon(cursor.getString(cursor.getColumnIndexOrThrow(WeatherAppDBContract.Weather.COLUMN_ICON)));
        object.setTemperature(cursor.getDouble(cursor.getColumnIndexOrThrow(WeatherAppDBContract.Weather.COLUMN_TEMPERATURE)));
        object.setHumidity(cursor.getInt(cursor.getColumnIndexOrThrow(WeatherAppDBContract.Weather.COLUMN_HUMIDITY)));
        object.setPressure(cursor.getDouble(cursor.getColumnIndexOrThrow(WeatherAppDBContract.Weather.COLUMN_PRESSURE)));
        object.setWindDegree(cursor.getDouble(cursor.getColumnIndexOrThrow(WeatherAppDBContract.Weather.COLUMN_WIND_DEGREE)));
        object.setWindSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(WeatherAppDBContract.Weather.COLUMN_WIND_SPEED)));
        object.setLastUpdate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(WeatherAppDBContract.Weather.COLUMN_LAST_UPDATE))));
    }

    @Override
    protected String getTableName() {
        Log.d("WeatherDAO", "getTableName()");

        return WeatherAppDBContract.Weather.TABLE_NAME;
    }

}
