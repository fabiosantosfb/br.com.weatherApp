package ceducarneiro.com.br.weatherapp.model.bean;

import android.util.Log;

public class Place extends BaseBean {

    private String city;
    private String country;
    private Double latitude;
    private Double longitude;
    private Weather weather;

    public Place() {
        Log.d("Place", "Place()");

        weather = new Weather();
    }

    public String getCity() {
        Log.d("Place", "getCity()");

        return city;
    }

    public void setCity(String city) {
        Log.d("Place", "setCity(String city)");

        this.city = city;
    }

    public String getCountry() {
        Log.d("Place", "getCountry()");

        return country;
    }

    public void setCountry(String country) {
        Log.d("Place", "setCountry(String country)");

        this.country = country;
    }

    public Double getLatitude() {
        Log.d("Place", "getLatitude()");

        return latitude;
    }

    public void setLatitude(Double latitude) {
        Log.d("Place", "setLatitude(Double latitude)");

        this.latitude = latitude;
    }

    public Double getLongitude() {
        Log.d("Place", "getLongitude()");

        return longitude;
    }

    public void setLongitude(Double longitude) {
        Log.d("Place", "setLongitude(Double longitude)");

        this.longitude = longitude;
    }

    public Weather getWeather() {
        Log.d("Place", "getWeather()");

        return weather;
    }

    public void setWeather(Weather weather) {
        Log.d("Place", "setWeather(Weather weather)");

        this.weather = weather;
    }

}
