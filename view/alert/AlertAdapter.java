package ceducarneiro.com.br.weatherapp.view.alert;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import ceducarneiro.com.br.weatherapp.R;
import ceducarneiro.com.br.weatherapp.model.bean.Alert;

public class AlertAdapter extends BaseAdapter {

    private List<Alert> alertList;

    public AlertAdapter(List<Alert> alertList) {
        Log.d("AlertAdapter", "AlertAdapter(List<Alert> alertList)");

        this.alertList = alertList;
    }

    @Override
    public int getCount() {
        Log.d("AlertAdapter", "getCount()");

        return alertList.size();
    }

    @Override
    public Object getItem(int position) {
        Log.d("AlertAdapter", "getItem(int position)");

        return alertList.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.d("AlertAdapter", "getItemId(int posit");

        return alertList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("AlertAdapter", "getView");

        Alert alert = (Alert) getItem(position);
        ViewHolder holder;


        if (convertView == null) {
            Log.d("AlertAdapter", "convertView == null");

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            convertView = inflater.inflate(R.layout.alert_item, parent, false);

            holder = new ViewHolder();
            holder.txOption = ButterKnife.findById(convertView, R.id.txOption);
            holder.txCondition = ButterKnife.findById(convertView, R.id.txCondition);
            holder.txValue = ButterKnife.findById(convertView, R.id.txValue);

            convertView.setTag(holder);
        } else {
            Log.d("AlertAdapter", "convertView != null");

            holder = (ViewHolder) convertView.getTag();
        }

        holder.txOption.setText(alert.getOption().toString());
        holder.txCondition.setText(alert.getCondition().toString());
        holder.txValue.setText(String.format(Locale.getDefault(), "%.2f", alert.getValue()));

        return convertView;
    }

    static class ViewHolder {

        TextView txOption;
        TextView txCondition;
        TextView txValue;
    }

}
