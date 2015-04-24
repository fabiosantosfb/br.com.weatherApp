package ceducarneiro.com.br.weatherapp.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.List;

import ceducarneiro.com.br.weatherapp.model.WeatherAppDBContract;
import ceducarneiro.com.br.weatherapp.model.bean.Alert;
import ceducarneiro.com.br.weatherapp.model.bean.Place;

public class AlertDAO extends BaseDAO<Alert> {

    private PlaceDAO placeDAO;

    public AlertDAO() {
        super(Alert.class);
        Log.d("PlaceDAO", "AlertDAO()");

        placeDAO = new PlaceDAO();
    }

    @Override
    protected void fillContent(ContentValues content, Alert object) {
        Log.d("PlaceDAO", "fillContent(ContentValues content, Alert object)");

        content.put(WeatherAppDBContract.Alert.COLUMN_OPTION, object.getOption().ordinal());
        content.put(WeatherAppDBContract.Alert.COLUMN_CONDITION, object.getCondition().ordinal());
        content.put(WeatherAppDBContract.Alert.COLUMN_VALUE, object.getValue());
        content.put(WeatherAppDBContract.Alert.COLUMN_PLACE_ID, object.getPlace().getId());
    }

    @Override
    protected void fillObject(Alert object, Cursor cursor) {
        Log.d("PlaceDAO", "fillObject(Alert object, Cursor cursor)");

        object.setId(cursor.getLong(cursor.getColumnIndexOrThrow(WeatherAppDBContract.Alert.COLUMN_ID)));
        object.setOption(Alert.AlertOption.values()[cursor.getInt(cursor.getColumnIndexOrThrow(WeatherAppDBContract.Alert.COLUMN_OPTION))]);
        object.setCondition(Alert.AlertCondition.values()[cursor.getInt(cursor.getColumnIndexOrThrow(WeatherAppDBContract.Alert.COLUMN_CONDITION))]);
        object.setValue(cursor.getDouble(cursor.getColumnIndexOrThrow(WeatherAppDBContract.Alert.COLUMN_VALUE)));

        long placeId = cursor.getLong(cursor.getColumnIndexOrThrow(WeatherAppDBContract.Alert.COLUMN_PLACE_ID));
        object.setPlace(placeDAO.findById(placeId));
    }

    @Override
    protected String getTableName() {
        Log.d("PlaceDAO", "getTableName()");

        return WeatherAppDBContract.Alert.TABLE_NAME;
    }

    public List<Alert> filterByPlace(Place place) {
        Log.d("PlaceDAO", "filterByPlace(Place place)");

        return filter(String.format("%s = ?", WeatherAppDBContract.Alert.COLUMN_PLACE_ID),
                new String[] { String.valueOf(place.getId()) });
    }

    public void deleteByPlace(Place place) {
        Log.d("PlaceDAO", "deleteByPlace(Place place)");

        List<Alert> alerts = filterByPlace(place);

        for (Alert alert : alerts) {
            Log.d("PlaceDAO", "for (Alert alert : alerts)");

            delete(alert);
        }
    }
}
