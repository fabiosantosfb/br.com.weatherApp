package br.com.weatherapp.view.places;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import br.com.weatherapp.R;
import br.com.weatherapp.model.bean.Place;

public class PlaceAdapter extends BaseAdapter {

    private List<Place> placeList;

    public PlaceAdapter(List<Place> placeList) {
        Log.d("PlaceAdapter", "PlaceAdapter(List<Place> placeList)");

        this.placeList = placeList;
    }

    @Override
    public int getCount() {
        Log.d("PlaceAdapter", "getCount()");

        return placeList.size();
    }

    @Override
    public Object getItem(int position) {
        Log.d("PlaceAdapter", "getItem(int position)");

        return placeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.d("PlaceAdapter", "getItemId(int position)");

        return placeList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("PlaceAdapter", "View getView()");

        Place place = (Place) getItem(position);
        ViewHolder holder;


        if (convertView == null) {
            Log.d("PlaceAdapter", "convertView == null");

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            convertView = inflater.inflate(R.layout.place_item, parent, false);

            holder = new ViewHolder();
            holder.imgWeather = ButterKnife.findById(convertView, R.id.imgWeather);
            holder.txCity = ButterKnife.findById(convertView, R.id.txCity);
            holder.txWeather = ButterKnife.findById(convertView, R.id.txWeather);
            holder.txTemperature = ButterKnife.findById(convertView, R.id.txTemperature);

            convertView.setTag(holder);
        } else {
            Log.d("PlaceAdapter", "convertView != null");

            holder = (ViewHolder) convertView.getTag();
        }

        holder.imgWeather.setImageResource(place.getWeather().getResIcon());
        holder.txCity.setText(place.getCity()+ ", " + place.getCountry().toUpperCase());
        holder.txWeather.setText(place.getWeather().getDescription());
        holder.txTemperature.setText(String.format(Locale.getDefault(), "%.1fº", place.getWeather().getTemperature()));

        return convertView;
    }

    static class ViewHolder {
        ImageView imgWeather;
        TextView txCity;
        TextView txWeather;
        TextView txTemperature;
    }

}
