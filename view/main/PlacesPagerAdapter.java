package ceducarneiro.com.br.weatherapp.view.main;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import ceducarneiro.com.br.weatherapp.R;
import ceducarneiro.com.br.weatherapp.base.AppController;
import ceducarneiro.com.br.weatherapp.model.bean.Place;
import ceducarneiro.com.br.weatherapp.view.places.PlacesFragment;
import ceducarneiro.com.br.weatherapp.view.weather.WeatherFragment;

class PlacesPagerAdapter extends FragmentPagerAdapter {
    private PlacesFragment placesFragment;
    private WeatherFragment weatherFragment;

    public PlacesPagerAdapter(FragmentManager fm) {
        super(fm);

        Log.d("PlacesPagerAdapter", "PlacesPagerAdapter(FragmentManager fm)");
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("PlacesPagerAdapter", "getItem(int position)");

        return (position == 0) ? PlacesFragment.newInstance()
                : WeatherFragment.newInstance();
    }

    @Override
    public int getCount() {
        Log.d("PlacesPagerAdapter", "getCount()");

        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.d("PlacesPagerAdapter", " getPageTitle(int position)");

        return (position == 0) ? AppController.getInstance().getString(R.string.places)
                : AppController.getInstance().getString(R.string.weather);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d("PlacesPagerAdapter", "instantiateItem");

        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        if (position == 0){
            Log.d("PlacesPagerAdapter", "position == 0");

            placesFragment = (PlacesFragment) fragment;
        }
        if (position == 1){
            Log.d("PlacesPagerAdapter", "position == 1");

            weatherFragment = (WeatherFragment) fragment;
        }
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d("PlacesPagerAdapter", "destroyItem");

        if (position == 0){
            Log.d("PlacesPagerAdapter", "position == 0");

            placesFragment = null;
        }
        if (position == 1){
            Log.d("PlacesPagerAdapter", "position == 1");

            weatherFragment = null;
        }
        super.destroyItem(container, position, object);
    }

    public void updatePlacesList() {
        Log.d("PlacesPagerAdapter", "updatePlacesList()");

        if (placesFragment != null) {
            Log.d("PlacesPagerAdapter", "placesFragment != null");

            placesFragment.updatePlacesList();
        }
    }

    public void updateWeatherDetail() {
        Log.d("PlacesPagerAdapter", "updateWeatherDetail()");

        if (weatherFragment != null){
            Log.d("PlacesPagerAdapter", "weatherFragment != null");

            weatherFragment.updateWeatherDetail();
        }
    }

    public void updateWeatherDetail(Place place) {
        Log.d("PlacesPagerAdapter", "updateWeatherDetail(Place place)");

        if (weatherFragment != null){
            Log.d("PlacesPagerAdapter", "weatherFragment != null");

            weatherFragment.updateWeatherDetail(place);
        }
    }

}
