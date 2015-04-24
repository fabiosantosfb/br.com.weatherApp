package br.com.weatherapp.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.weatherapp.base.AppController;
import br.com.weatherapp.model.WeatherAppDBContract;
import br.com.weatherapp.model.WeatherAppDBHelper;
import br.com.weatherapp.model.bean.BaseBean;

public abstract class BaseDAO<T extends BaseBean> {

    protected WeatherAppDBHelper dbHelper;
    private final Class<T> clazz;

    public BaseDAO(Class<T> clazz) {
        Log.d("abstract class BaseDAO", "BaseDAO(Class<T> clazz)");

        dbHelper = WeatherAppDBHelper.getInstance(AppController.getInstance().getApplicationContext());
        this.clazz = clazz;
    }

    public long insertOrUpdate(T object) {
        Log.d("abstract class BaseDAO", "insertOrUpdate(T object)");

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues content = new ContentValues();
        fillContent(content, object);

        long id = object.getId();

        if (id == 0 || db.update(getTableName(), content, String.format("%s = ?", WeatherAppDBContract.TableContract.COLUMN_ID),
                new String[] { String.valueOf(id) }) == 0) {
            Log.d("abstract class BaseDAO", "id == 0 || db.update(getTableName(), content, String.format(\"%s = ?\", WeatherAppDBContract.TableContract.COLUMN_ID),\n" +
                    "                new String[] { String.valueOf(id) }) == 0t)");

            id = db.insert(getTableName(), null, content);
            if (id > 0){
                Log.d("abstract class BaseDAO", "id > 0");

                object.setId(id);
            }
        }

        db.close();

        return id;
    }

    public boolean delete(T object) {
        Log.d("abstract class BaseDAO", "delete(T object)0");

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        boolean deleted = db.delete(getTableName(), String.format("%s = ?", WeatherAppDBContract.TableContract.COLUMN_ID),
                new String[] { String.valueOf(object.getId()) }) == 1;

        db.close();

        return deleted;
    }

    public int delete() {
        Log.d("abstract class BaseDAO", "delete()");

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int deleted = db.delete(getTableName(), null, null);

        db.close();

        return deleted;
    }

    public T findById(long id) {
        Log.d("abstract class BaseDAO", "T findById(long id)");

        T object = null;

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(getTableName(), null,
                String.format("%s = ?", WeatherAppDBContract.TableContract.COLUMN_ID),
                new String[] { String.valueOf(id) }, null, null, null);

        if (cursor.moveToFirst()) {
            try {
                Log.d("abstract class BaseDAO", "if (cursor.moveToFirst()){try}");

                object = clazz.newInstance();
                fillObject(object, cursor);
            } catch (InstantiationException|IllegalAccessException ignored) {
                Log.d("abstract class BaseDAO", "if (cursor.moveToFirst()){catch}");

                /* Empty */
            }
        }

        db.close();

        return object;
    }

    public List<T> filter(String selection, String[] selectionArgs) {
        Log.d("abstract class BaseDAO", "List<T> filter(String selection, String[] selectionArgs)");

        return filter(selection, selectionArgs, null);
    }

    public List<T> filter(String selection, String[] selectionArgs, String orderBy) {
        Log.d("abstract class BaseDAO", "List<T> filter(String selection, String[] selectionArgs, String orderBy)");

        List<T> all = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(getTableName(), null, selection, selectionArgs,
                null, null, orderBy);

        if (cursor.moveToFirst()) {
            Log.d("abstract class BaseDAO", "if (cursor.moveToFirst())");

            do {
                try {
                    Log.d("abstract class BaseDAO", "do{try}");

                    T object = clazz.newInstance();
                    fillObject(object, cursor);
                    all.add(object);
                } catch (InstantiationException|IllegalAccessException ignored) {
                    Log.d("abstract class BaseDAO", "do{catch (InstantiationException|IllegalAccessException ignored)}");

                /* Empty */
                }
            } while (cursor.moveToNext());
        }

        db.close();

        return all;
    }

    public List<T> getAll(String orderBy) {
        Log.d("abstract class BaseDAO", "List<T> getAll(String orderBy)");

        return filter(null, null, orderBy);
    }

    public List<T> getAll() {
        Log.d("abstract class BaseDAO", "List<T> getAll()");

        return filter(null, null, null);
    }

    protected abstract void fillContent(ContentValues content, T object);
    protected abstract void fillObject(T object, Cursor cursor);
    protected abstract String getTableName();

}
