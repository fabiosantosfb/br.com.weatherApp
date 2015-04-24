package br.com.weatherapp.model.bean;

import android.util.Log;

import br.com.weatherapp.R;
import br.com.weatherapp.base.AppController;

public class Alert extends BaseBean {

    private AlertOption option;
    private AlertCondition condition;
    private double value;
    private Place place;

    public Alert() {
        Log.d("Alert", "Alert()");

        place = new Place();
    }

    public AlertOption getOption() {
        Log.d("Alert", "AlertOption getOption()");

        return option;
    }

    public void setOption(AlertOption option) {
        Log.d("Alert", "setOption(AlertOption option)");

        this.option = option;
    }

    public AlertCondition getCondition() {
        Log.d("Alert", "AlertCondition getCondition()");

        return condition;
    }

    public void setCondition(AlertCondition condition) {
        Log.d("Alert", "setCondition(AlertCondition condition)");

        this.condition = condition;
    }

    public double getValue() {
        Log.d("Alert", "getValue()");

        return value;
    }

    public void setValue(double value) {
        Log.d("Alert", "setValue(double value)");

        this.value = value;
    }

    public Place getPlace() {
        Log.d("Alert", "Place getPlace()");

        return place;
    }

    public void setPlace(Place place) {
        Log.d("Alert", "setPlace(Place place)");

        this.place = place;
    }

    public enum AlertOption {

        Temperature,
        Humidity,
        Pressure,
        WindSpeed;

        @Override
        public String toString() {
            Log.d("Alert", "enum AlertOption toString()");

            AppController appController = AppController.getInstance();
            return appController.getResources().getStringArray(
                    R.array.alerts_options_array)[ordinal()];
        }
    }

    public enum AlertCondition {
        LessThan,
        GreaterThan;

        @Override
        public String toString() {
            Log.d("Alert", "enum AlertCondition toString()");

            AppController appController = AppController.getInstance();
            return appController.getResources().getStringArray(
                    R.array.alerts_conditions_array)[ordinal()];
        }
    }

}
