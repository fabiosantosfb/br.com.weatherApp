package ceducarneiro.com.br.weatherapp.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ceducarneiro.com.br.weatherapp.model.WeatherAppDBContract;
import ceducarneiro.com.br.weatherapp.model.bean.Place;

public class PlaceDAO extends BaseDAO<Place> {

    private WeatherDAO weatherDAO;

    public PlaceDAO() {
        super(Place.class);
        Log.d("PlaceDAO", "PlaceDAO()");

        weatherDAO = new WeatherDAO();
    }

    @Override
    public long insertOrUpdate(Place object) {
        Log.d("PlaceDAO", "insertOrUpdate(Place object)");

        weatherDAO.insertOrUpdate(object.getWeather());
        return super.insertOrUpdate(object);
    }

    @Override
    protected void fillContent(ContentValues content, Place object) {
        Log.d("PlaceDAO", "fillContent(ContentValues content, Place object)");

        content.put(WeatherAppDBContract.Place.COLUMN_ID, object.getId());
        content.put(WeatherAppDBContract.Place.COLUMN_CITY, object.getCity());
        content.put(WeatherAppDBContract.Place.COLUMN_COUNTRY, object.getCountry());
        content.put(WeatherAppDBContract.Place.COLUMN_LATITUDE, object.getLatitude());
        content.put(WeatherAppDBContract.Place.COLUMN_LONGITUDE, object.getLongitude());
        content.put(WeatherAppDBContract.Place.COLUMN_WEATHER_ID, object.getWeather().getId());
    }

    @Override
    protected void fillObject(Place object, Cursor cursor) {
        Log.d("PlaceDAO", "fillObject(Place object, Cursor cursor)");

        object.setId(cursor.getLong(cursor.getColumnIndexOrThrow(WeatherAppDBContract.Place.COLUMN_ID)));
        object.setCity(cursor.getString(cursor.getColumnIndexOrThrow(WeatherAppDBContract.Place.COLUMN_CITY)));
        object.setCountry(cursor.getString(cursor.getColumnIndexOrThrow(WeatherAppDBContract.Place.COLUMN_COUNTRY)));
        object.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(WeatherAppDBContract.Place.COLUMN_LATITUDE)));
        object.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(WeatherAppDBContract.Place.COLUMN_LONGITUDE)));

        long weatherId = cursor.getLong(cursor.getColumnIndexOrThrow(WeatherAppDBContract.Place.COLUMN_WEATHER_ID));
        object.setWeather(weatherDAO.findById(weatherId));
    }

    public List<Place> filterByName(String name) {
        Log.d("PlaceDAO", "filterByName(String name)");

        return filter(String.format("%s like ?",
                        WeatherAppDBContract.Place.COLUMN_CITY), new String[]{"%" + name + "%"},
                String.format("%s ASC", WeatherAppDBContract.Place.COLUMN_CITY));
    }

    @Override
    protected String getTableName() {
        Log.d("PlaceDAO", "getTableName()");

        return WeatherAppDBContract.Place.TABLE_NAME;
    }

    public List<Place> getAllWithAlert() {
        Log.d("PlaceDAO", "ist<Place> getAllWithAlert()");

        List<Place> places = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = String.format("select * from %s where %s in (select %s from %s)",
                WeatherAppDBContract.Place.TABLE_NAME,
                WeatherAppDBContract.Place.COLUMN_ID,
                WeatherAppDBContract.Alert.COLUMN_PLACE_ID,
                WeatherAppDBContract.Alert.TABLE_NAME);
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            Log.d("PlaceDAO", "while (cursor.moveToNext())");

            Place place = new Place();
            fillObject(place, cursor);
            places.add(place);
        }

        db.close();

        return places;
    }

    @Override
    public boolean delete(Place object) {
        Log.d("PlaceDAO", "delete(Place object)");

        weatherDAO.delete(object.getWeather());
        new AlertDAO().deleteByPlace(object);

        return super.delete(object);
    }
}
