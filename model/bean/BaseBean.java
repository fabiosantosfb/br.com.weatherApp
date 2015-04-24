package br.com.weatherapp.model.bean;

import android.util.Log;

import java.io.Serializable;

public class BaseBean implements Serializable {

    private long id;

    public long getId() {
        Log.d("BaseBean", "getId()");

        return id;
    }

    public void setId(long id) {
        Log.d("BaseBean", "setId(long id)");

        this.id = id;
    }
}
