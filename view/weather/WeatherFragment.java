package br.com.weatherapp.view.weather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;
import br.com.weatherapp.R;
import br.com.weatherapp.base.ToolBarFragment;
import br.com.weatherapp.model.bean.Place;
import br.com.weatherapp.model.dao.PlaceDAO;

public class WeatherFragment extends ToolBarFragment {

    private static String PLACE_KEY = "place_key";

    TextView txHumidity;
    TextView txPressure;
    TextView txWindDirection;
    TextView txWindSpeed;

    @InjectView(R.id.imgWeather)
    ImageView imgWeather;

    @InjectView(R.id.txCity)
    TextView txCity;

    @InjectView(R.id.txTemperature)
    TextView txTemperature;

    @InjectView(R.id.txWeather)
    TextView txWeather;

    @InjectView(R.id.txUpdate)
    TextView txUpdate;

    private Place place;
    private WeatherListener listener;

    public static WeatherFragment newInstance() {
        Log.d("WeatherListener", "newInstance()");

        return new WeatherFragment();
    }

    public static WeatherFragment newInstance(Place place) {
        Log.d("WeatherListener", "newInstance(Place place)");

        WeatherFragment fragment = new WeatherFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(PLACE_KEY, place);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("WeatherListener", "onCreateView()");

        View view = inflater.inflate(R.layout.weather_detail, null);

        ButterKnife.inject(this, view);
        setPlaceholderMessage(getString(R.string.weather_placeholder_message));

        ViewGroup layoutHumidity = ButterKnife.findById(view, R.id.layoutHumidity);
        ViewGroup layoutPressure = ButterKnife.findById(view, R.id.layoutPressure);
        ViewGroup layoutWindDirection = ButterKnife.findById(view, R.id.layoutWindDirection);
        ViewGroup layoutWindSpeed = ButterKnife.findById(view, R.id.layoutWindSpeed);

        ((TextView) layoutHumidity.getChildAt(0)).setText(getString(R.string.humidity));
        ((TextView) layoutPressure.getChildAt(0)).setText(getString(R.string.pressure));
        ((TextView) layoutWindDirection.getChildAt(0)).setText(getString(R.string.direction));
        ((TextView) layoutWindSpeed.getChildAt(0)).setText(getString(R.string.Speed));

        txHumidity = (TextView) layoutHumidity.getChildAt(1);
        txPressure = (TextView) layoutPressure.getChildAt(1);
        txWindDirection = (TextView) layoutWindDirection.getChildAt(1);
        txWindSpeed = (TextView) layoutWindSpeed.getChildAt(1);

        Bundle args = getArguments();

        if (args != null) {
            Log.d("WeatherListener", "args != null");

            place = (Place) getArguments().getSerializable(PLACE_KEY);
            if (place != null) {
                Log.d("WeatherListener", "place != null");

                updateWeatherDetail(place);
            }
        }

        if (savedInstanceState != null) {
            Log.d("WeatherListener", "savedInstanceState != null");

            place = (Place) savedInstanceState.getSerializable(PLACE_KEY);
            if (place != null) {
                Log.d("WeatherListener", "place != null");

                updateWeatherDetail(place);
            }
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d("WeatherListener", "onCreateOptionsMenu()");

        inflater.inflate(R.menu.weather_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.d("WeatherListener", "onPrepareOptionsMenu()");

        menu.findItem(R.id.action_alerts).setVisible(place != null);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("WeatherListener", "onOptionsItemSelected()");

        if (item.getItemId() == R.id.action_alerts) {
            Log.d("WeatherListener", "item.getItemId() == R.id.action_alerts");

            listener.onAlertsClicked(place);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        Log.d("WeatherListener", "onActivityCreated()");

        listener = (WeatherListener) getActivity();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(PLACE_KEY, place);
        super.onSaveInstanceState(outState);

        Log.d("WeatherListener", "onSaveInstanceState(Bundle outState)");
    }

    public void updateWeatherDetail() {
        Log.d("WeatherListener", "updateWeatherDetail()");

        if (place != null) {
            Log.d("WeatherListener", "place != null");

            updateWeatherDetail(new PlaceDAO().findById(place.getId()));
        }
    }

    public void updateWeatherDetail(Place place) {
        Log.d("WeatherListener", " updateWeatherDetail(Place place) ");

        this.place = place;

        if (place != null) {
            Log.d("WeatherListener", "place != null");

            txCity.setText(place.getCity());
            txTemperature.setText(String.format("%.1f%s", place.getWeather().getTemperature(),
                    getString(R.string.degress)));
            txWeather.setText(place.getWeather().getDescription());
            imgWeather.setImageResource(place.getWeather().getResBackground());

            SimpleDateFormat dtFormatter = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat tmFormatter = new SimpleDateFormat("HH:mm");

            txUpdate.setText(String.format("%s: %s %s %s", getString(R.string.last_update),
                    dtFormatter.format(place.getWeather().getLastUpdate()),
                    getString(R.string.date_at),
                    tmFormatter.format(place.getWeather().getLastUpdate())));

            txHumidity.setText(String.format("%s%%", place.getWeather().getHumidity()));
            txPressure.setText(String.format("%.2f hPa", place.getWeather().getPressure()));
            txWindDirection.setText(String.format("%.2fº", place.getWeather().getWindDegree()));
            txWindSpeed.setText(String.format("%.2f %s", place.getWeather().getWindSpeed() *
                            getResources().getFraction(R.fraction.wind_coefficient, 1, 1),
                    getString(R.string.wind_unit)));

            setPlaceholderVisible(false);
            getActivity().supportInvalidateOptionsMenu();
        } else {
            Log.d("WeatherListener", "place == null");

            setPlaceholderVisible(true);
            getActivity().supportInvalidateOptionsMenu();
        }
    }

    public interface WeatherListener {
        void onAlertsClicked(Place place);
    }

}
