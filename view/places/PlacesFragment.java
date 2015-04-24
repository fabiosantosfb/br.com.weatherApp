package br.com.weatherapp.view.places;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import br.com.weatherapp.R;
import br.com.weatherapp.base.ToolBarFragment;
import br.com.weatherapp.model.bean.Place;
import br.com.weatherapp.model.dao.PlaceDAO;

public class PlacesFragment extends ToolBarFragment implements SearchView.OnQueryTextListener {

    @InjectView(R.id.listPlaces)
    ListView listPlaces;

    SearchView searchView;
    MenuItem searchItem;

    private PlacesListener listener;

    public static PlacesFragment newInstance() {
        Log.d("PlacesFragment", "newInstance()");

        return new PlacesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("PlacesFragment", "onCreateView()");

        View view = inflater.inflate(R.layout.places_list, null);

        ButterKnife.inject(this, view);
        setPlaceholderMessage(getString(R.string.places_placeholder_message));

        if (isTablet()) {
            Log.d("PlacesFragment", "isTablet()");
            placeholder.setBackgroundColor(0xCCCCCC);
        }

        updatePlacesList();

        return view;
    }

    public void updatePlacesList() {
        Log.d("PlacesFragment", "updatePlacesList()");

        String filter = "";

        if (searchView != null) {
            Log.d("PlacesFragment", "searchView != null");

            filter = searchView.getQuery().toString();
        }

        updatePlacesList(new PlaceDAO().filterByName(filter));
    }

    private void updatePlacesList(List<Place> placeList) {
        Log.d("PlacesFragment", " updatePlacesList()");

        if (placeList.size() > 0) {
            Log.d("PlacesFragment", "placeList.size() > 0");

            listPlaces.setAdapter(new PlaceAdapter(placeList));
            setPlaceholderVisible(false);
        } else {
            Log.d("PlacesFragment", "placeList.size() <= 0");

            setPlaceholderVisible(true);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("PlacesFragment", "onActivityCreated");

        setHasOptionsMenu(true);

        listener = (PlacesListener) getActivity();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d("PlacesFragment", "onCreateOptionsMenu()");

        inflater.inflate(R.menu.places_menu, menu);
        searchItem = menu.findItem(R.id.action_search);

        if (searchItem != null) {
            Log.d("PlacesFragment", "searchItem != null");

            searchView = (SearchView) MenuItemCompat
                    .getActionView(searchItem);

            if (searchView != null)
                Log.d("PlacesFragment", "searchView != null");

                searchView.setOnQueryTextListener(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("PlacesFragment", "onOptionsItemSelected(MenuItem item)");

        boolean result = false;

        if (item.getItemId() == R.id.action_add) {
            Log.d("PlacesFragment", "item.getItemId() == R.id.action_add");

            listener.onAddClicked();
            result = true;
        }

        return result;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Log.d("PlacesFragment", "onQueryTextSubmit(String s)");

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        Log.d("PlacesFragment", "onQueryTextChange(String s)");

        updatePlacesList(new PlaceDAO().filterByName(s));
        return true;
    }

    public interface PlacesListener {

        void onPlaceClicked(Place place);
        void onPlaceLongClicked(Place place);
        void onAddClicked();
    }

    @OnItemClick(R.id.listPlaces)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("PlacesFragment", "onItemClick()");

        view.setSelected(true);
        listener.onPlaceClicked(new PlaceDAO().findById(id));
    }

    @OnItemLongClick(R.id.listPlaces)
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("PlacesFragment", "onItemLongClick()");

        listener.onPlaceLongClicked(new PlaceDAO().findById(id));
        return true;
    }

}
