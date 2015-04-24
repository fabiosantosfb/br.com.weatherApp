package br.com.weatherapp.model.bean;

import android.util.Log;

import java.util.Date;

import br.com.weatherapp.R;
import br.com.weatherapp.base.AppController;

public class Weather extends BaseBean {

    private int code;
    private String icon;
    private double temperature;
    private int humidity;
    private double pressure;
    private double windDegree;
    private double windSpeed;
    private Date lastUpdate;

    public Weather() {
        Log.d("Weather", "Weather()");

        lastUpdate = new Date();
    }

    public int getCode() {
        Log.d("Weather", "getCode()");

        return code;
    }

    public void setCode(int code) {
        Log.d("Weather", "setCode(int code)");

        this.code = code;
    }

    public String getIcon() {
        Log.d("Weather", "getIcon()");

        return icon;
    }

    public void setIcon(String icon) {
        Log.d("Weather", "setIcon(String icon)");

        this.icon = icon;
    }

    public double getTemperature() {
        Log.d("Weather", "getTemperature()");

        return temperature;
    }

    public void setTemperature(double temperature) {
        Log.d("Weather", "setTemperature(double temperature)");

        this.temperature = temperature;
    }

    public int getHumidity() {
        Log.d("Weather", "getHumidity()");

        return humidity;
    }

    public void setHumidity(int humidity) {
        Log.d("Weather", "setHumidity(int humidity)");

        this.humidity = humidity;
    }

    public double getPressure() {
        Log.d("Weather", "getPressure()");

        return pressure;
    }

    public void setPressure(double pressure) {
        Log.d("Weather", "setPressure(double pressure)");

        this.pressure = pressure;
    }

    public double getWindDegree() {
        Log.d("Weather", "getWindDegree()");

        return windDegree;
    }

    public void setWindDegree(double windDegree) {
        Log.d("Weather", "setWindDegree(double windDegree)");

        this.windDegree = windDegree;
    }

    public double getWindSpeed() {
        Log.d("Weather", "getWindSpeed()");

        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        Log.d("Weather", "setWindSpeed(double windSpeed)");

        this.windSpeed = windSpeed;
    }

    public Date getLastUpdate() {
        Log.d("Weather", " getLastUpdate()");

        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        Log.d("Weather", "setLastUpdate(Date lastUpdate)");

        this.lastUpdate = lastUpdate;
    }

    public String getDescription() {
        Log.d("Weather", "getDescription()");

        String description = "";

        if (icon != null && icon.length() == 3) {
            Log.d("Weather", "icon != null && icon.length() == 3");

            description = String.format("condition_%s", icon.substring(0, 2));
            description = AppController.getInstance().getString(
                    AppController.getInstance().getResources()
                            .getIdentifier(description, "string",
                                    AppController.getInstance().getPackageName()));
        }

        return description;
    }

    public int getResIcon() {
        Log.d("Weather", "getResIcon()");

        String iconName = String.format("ic_condition_%s", icon);
        return AppController.getInstance().getResources()
                .getIdentifier(iconName, "drawable",
                        AppController.getInstance().getPackageName());
    }

    public int getResBackground() {
        Log.d("Weather", "getResBackground()");

        int res = 0;

        if (icon != null && icon.length() == 3) {
            Log.d("Weather", "icon != null && icon.length() == 3");
            int iconValue = Integer.parseInt(icon.substring(0, 2));

            if (iconValue == 1) {
                Log.d("Weather", "iconValue == 1");

                res = R.drawable.clear;
            } else if (iconValue == 2 || iconValue == 3) {
                Log.d("Weather", "iconValue == 2 || iconValue == 3");

                res = R.drawable.clouds;
            }else if (iconValue == 4 || iconValue == 50) {
                Log.d("Weather", "iconValue == 4 || iconValue == 50");

                res = R.drawable.cloudy;
            }else {
                Log.d("Weather", "else");

                res = R.drawable.rain;

            }
        }

        return res;
    }
}
