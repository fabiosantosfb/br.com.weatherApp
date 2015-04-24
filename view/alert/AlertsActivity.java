package ceducarneiro.com.br.weatherapp.view.alert;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnItemLongClick;
import butterknife.OnItemSelected;
import ceducarneiro.com.br.weatherapp.R;
import ceducarneiro.com.br.weatherapp.base.AppController;
import ceducarneiro.com.br.weatherapp.base.ToolBarActvitiy;
import ceducarneiro.com.br.weatherapp.broadcast.AlarmReceiver;
import ceducarneiro.com.br.weatherapp.model.bean.Alert;
import ceducarneiro.com.br.weatherapp.model.bean.Place;
import ceducarneiro.com.br.weatherapp.model.dao.AlertDAO;

public class AlertsActivity extends ToolBarActvitiy {

    public static final String PLACE_KEY = "place_id_key";

    @InjectView(R.id.optionsSpinner)
    Spinner optionsSpinner;

    @InjectView(R.id.conditionsSpinner)
    Spinner conditionsSpinner;

    @InjectView(R.id.edValue)
    EditText edValue;

    @InjectView(R.id.btAdd)
    ImageButton btAdd;

    @InjectView(R.id.listAlerts)
    ListView listAlerts;

    private int optionSelected;
    private int conditionSelected;
    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alerts_activity);

        Log.d("AlertsActivity", "onCreate(Bundle savedInstanceState)");

        place = (Place) getIntent().getSerializableExtra(PLACE_KEY);

        setPlaceholderMessage(R.string.alerts_placeholder_message);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.alerts_options_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionsSpinner.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this,
                R.array.alerts_conditions_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionsSpinner.setAdapter(adapter);

        edValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("AlertsActivity", "beforeTextChanged");
                /* Empty */
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("AlertsActivity", "onTextChanged");

                btAdd.setVisibility(edValue.getText().length() > 0 ?
                        View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("AlertsActivity", "afterTextChanged");
                /* Empty */
            }
        });

        updateAlertsList(new AlertDAO().filterByPlace(place));
    }

    @OnItemLongClick(R.id.listAlerts)
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("AlertsActivity", "onItemLongClick");

        final Alert alert = new AlertDAO().findById(id);

        AlertDialog dialog = new AlertDialog.Builder(
                new ContextThemeWrapper(this, R.style.AppTheme)).create();
        dialog.setMessage(getString(R.string.alert_exclude_confirmation));
        dialog.setCancelable(true);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {
                new AlertDAO().delete(alert);
                updateAlertsList(new AlertDAO().filterByPlace(place));
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {
                /* Empty */
            }
        });

        dialog.show();

        return true;
    }

    private void updateAlertsList(List<Alert> alertsList) {
        Log.d("AlertsActivity", "updateAlertsList");

        if (alertsList.size() > 0) {
            Log.d("AlertsActivity", "alertsList.size() > 0");

            listAlerts.setAdapter(new AlertAdapter(alertsList));
            setPlaceholderVisible(false);
        } else {
            Log.d("AlertsActivity", "alertsList.size() <= 0");

            setPlaceholderVisible(true);
        }
    }

    @OnItemSelected(R.id.optionsSpinner)
    public void onOptionsItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("AlertsActivity", "onOptionsItemSelected");

        optionSelected = position;
    }

    @OnItemSelected(R.id.conditionsSpinner)
    public void onConditionItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("AlertsActivity", "onConditionItemSelected");

        conditionSelected = position;
    }

    public void onAddClick(View v) {
        Log.d("AlertsActivity", "onAddClick(View v)");

        Alert alert = new Alert();
        AlertDAO alertDAO = new AlertDAO();

        alert.setOption(Alert.AlertOption.values()[optionSelected]);
        alert.setCondition(Alert.AlertCondition.values()[conditionSelected]);
        alert.setValue(Double.parseDouble(edValue.getText().toString()));
        alert.setPlace(place);

        alertDAO.insertOrUpdate(alert);
        AlarmReceiver.scheduleAlarm(this);
        updateAlertsList(new AlertDAO().filterByPlace(place));
    }

}
