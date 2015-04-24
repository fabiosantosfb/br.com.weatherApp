package ceducarneiro.com.br.weatherapp.view.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnPageChange;
import butterknife.Optional;
import ceducarneiro.com.br.weatherapp.R;
import ceducarneiro.com.br.weatherapp.base.ToolBarActvitiy;
import ceducarneiro.com.br.weatherapp.broadcast.AlarmReceiver;
import ceducarneiro.com.br.weatherapp.controller.ListenerController;
import ceducarneiro.com.br.weatherapp.controller.SyncListener;
import ceducarneiro.com.br.weatherapp.model.bean.Place;
import ceducarneiro.com.br.weatherapp.model.dao.PlaceDAO;
import ceducarneiro.com.br.weatherapp.service.SyncService;
import ceducarneiro.com.br.weatherapp.view.alert.AlertsActivity;
import ceducarneiro.com.br.weatherapp.view.places.AddPlaceActivity;
import ceducarneiro.com.br.weatherapp.view.places.PlacesFragment;
import ceducarneiro.com.br.weatherapp.view.weather.WeatherFragment;

public class MainActivity extends ToolBarActvitiy implements PlacesFragment.PlacesListener,
        WeatherFragment.WeatherListener, SyncListener {

    public static final String ALERT_KEY = "alert_key";
    public static final String PLACE_KEY = "place_key";

    private static final int ADD_PLACE_REQUEST = 1;

    @Optional @InjectView(R.id.pager)
    ViewPager pager;

    private PlacesFragment placesFragment;
    private WeatherFragment weatherFragment;
    private PlacesPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "onCreate(Bundle savedInstanceState)");

        ListenerController.getInstance().addSyncListener(this);

        setContentView(R.layout.main_activity);

        loadView();

        boolean isAlert = getIntent().getBooleanExtra(ALERT_KEY, false);

        if (isAlert && (savedInstanceState == null)) {
            Log.d("MainActivity", "isAlert && (savedInstanceState == null)");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Place place = (Place) getIntent().getSerializableExtra(PLACE_KEY);
                    onPlaceClicked(place);
                }
            }, 500);
        }

        AlarmReceiver.scheduleAlarm(this);
    }

    @Override
    protected void onDestroy() {
        Log.d("MainActivity", "onDestroy()");

        ListenerController.getInstance().removeSyncListener(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("MainActivity", "onCreateOptionsMenu");

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MainActivity", "onOptionsItemSelected");

        if (item.getItemId() == R.id.action_update) {
            Log.d("MainActivity", "item.getItemId() == R.id.action_update");

            if (!SyncService.isRunning()) {
                Log.d("MainActivity", "!SyncService.isRunning()");

                startService(new Intent(this, SyncService.class));
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadView() {
        Log.d("MainActivity", "loadView()");

        if (isTablet()) {
            Log.d("MainActivity", "isTablet() == true");

            loadTablet();
        } else {
            Log.d("MainActivity", "isTablet() == false");

            loadSmartphone();
        }
    }

    private void loadSmartphone() {
        Log.d("MainActivity", "loadSmartphone()");

        pagerAdapter = new PlacesPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
    }

    private void loadTablet() {
        Log.d("MainActivity", "loadTablet()");

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.list, (placesFragment = PlacesFragment.newInstance()))
                .commitAllowingStateLoss();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail, (weatherFragment = WeatherFragment.newInstance()))
                .commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        Log.d("MainActivity", "onBackPressed()");

        if (!isTablet()) {
            Log.d("MainActivity", "!isTablet()");

            if (pager.getCurrentItem() == 0) {
                super.onBackPressed();

                Log.d("MainActivity", "pager.getCurrentItem() == 0");
            } else {
                Log.d("MainActivity", "pager.getCurrentItem() != 0");

                pager.setCurrentItem(pager.getCurrentItem() - 1);
            }
        }
    }

    @Override
    public void onPlaceClicked(Place place) {
        Log.d("MainActivity", "onPlaceClicked(Place place)");

        if (isTablet()) {
            Log.d("MainActivity", "isTablet() == true");

            weatherFragment.updateWeatherDetail(place);
        } else {
            Log.d("MainActivity", "isTablet() == false");

            pagerAdapter.updateWeatherDetail(place);
            pager.setCurrentItem(1);
        }
    }

    @Override
    public void onPlaceLongClicked(final Place place) {
        Log.d("MainActivity", "onPlaceLongClicked(final Place place)");

        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setMessage(getString(R.string.place_exclude_confirmation));
        dialog.setCancelable(true);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {
                new PlaceDAO().delete(place);
                if (isTablet()) {
                    Log.d("MainActivity", "isTablet() == true");

                    placesFragment.updatePlacesList();
                    weatherFragment.updateWeatherDetail();
                } else {
                    Log.d("MainActivity", "isTablet() == false");

                    pagerAdapter.updatePlacesList();
                    pagerAdapter.updateWeatherDetail();
                }
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {
                /* Empty */
            }
        });

        dialog.show();
    }

    @Override
    public void onAddClicked() {
        Log.d("MainActivity", "onAddClicked()");

        Intent it = new Intent(this, AddPlaceActivity.class);
        startActivityForResult(it, ADD_PLACE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MainActivity", "onActivityResult");

        if (requestCode == ADD_PLACE_REQUEST && resultCode == RESULT_OK) {
            Log.d("MainActivity", "requestCode == ADD_PLACE_REQUEST && resultCode == RESULT_OK");

            if (isTablet()) {
                Log.d("MainActivity", "isTablet == true");

                placesFragment.updatePlacesList();
            } else {
                Log.d("MainActivity", "isTablet == false");

                pagerAdapter.updatePlacesList();
            }
        }
    }

    @Override
    public void onAlertsClicked(Place place) {
        Log.d("MainActivity", "onAlertsClicked(Place place)");

        Intent it = new Intent(this, AlertsActivity.class);

        it.putExtra(AlertsActivity.PLACE_KEY, place);

        startActivity(it);
    }

    @Override
    public void onSyncStart(List args) {
        Log.d("MainActivity", "onSyncStart(List args)");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showLoading(true);
            }
        });
    }

    @Override
    public void onSyncFinish(List args) {
        Log.d("MainActivity", "onSyncFinish(List args)");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isTablet()) {
                    Log.d("MainActivity", "isTablet == true");

                    placesFragment.updatePlacesList();
                    weatherFragment.updateWeatherDetail();
                } else {
                    Log.d("MainActivity", "isTablet == false");

                    pagerAdapter.updatePlacesList();
                    pagerAdapter.updateWeatherDetail();
                }

                showLoading(false);
            }
        });
    }

}
