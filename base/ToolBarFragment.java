package com.br.weatherapp.base;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import com.br.weatherapp.R;

public class ToolBarFragment extends Fragment {

    @Optional
    @InjectView(R.id.placeholder)
    protected View placeholder;

    protected void setPlaceholderVisible(boolean visible) {
        Log.d("ToolBarFragment", "setPlaceholderVisible(boolean visible)");

        if (placeholder != null) {
            Log.d("ToolBarFragment", "placeholder != null");
            placeholder.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    protected void setPlaceholderMessage(String message) {
        Log.d("ToolBarFragment", "setPlaceholderMessage(String message)");

        if (placeholder != null) {
            Log.d("ToolBarFragment", "placeholder != null");

            TextView txMessage = ButterKnife.findById(placeholder, R.id.placeholder_message);
            txMessage.setText(message);
        }
    }

    protected void setPlaceholderMessage(int messageResId) {
        Log.d("ToolBarFragment", "setPlaceholderMessage(int messageResId)");

        setPlaceholderMessage(getString(messageResId));
    }

    protected boolean isTablet() {
        Log.d("ToolBarFragment", "isTablet()");

        return getResources().getBoolean(R.bool.isTablet);
    }

}
