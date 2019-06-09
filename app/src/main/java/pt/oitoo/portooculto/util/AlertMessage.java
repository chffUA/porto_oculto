package pt.oitoo.portooculto.util;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.widget.Toast;

import pt.oitoo.portooculto.R;

public abstract class AlertMessage {

    public static final int NO_CONNECTION = 1;
    public static final int NO_GPS_ACCESS = 2;
    public static final int NO_CAMERA_ACCESS = 3;

    private AlertMessage(){}

    public static void showToast(String message, Context context){
        Toast.makeText(context,
                Html.fromHtml(message),
                Toast.LENGTH_LONG).show();
    }

    public static void showSnackbar(String message, final CoordinatorLayout coordinatorLayout, final Context context, int snackType) {
        Snackbar mSnackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);

        switch(snackType){
            case NO_CONNECTION:
                mSnackbar.setAction(R.string.alert_message_no_connection_connect_event, view -> context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)
                        .putExtra("extra_prefs_show_button_bar", true)
                        .putExtra("extra_prefs_set_back_text", R.string.alert_message_no_connection_back)
                        .putExtra("extra_prefs_set_next_text", "")));
                break;
            case NO_GPS_ACCESS:
                mSnackbar.setAction("SETTINGS", view -> context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        .putExtra("extra_prefs_show_button_bar", true)
                        .putExtra("extra_prefs_set_back_text", R.string.alert_message_no_connection_back)
                        .putExtra("extra_prefs_set_next_text", "")));
                break;
            case NO_CAMERA_ACCESS:
                mSnackbar.setAction("SETTINGS", view -> context.startActivity(new Intent(Settings.ACTION_SETTINGS)
                        .putExtra("extra_prefs_show_button_bar", true)
                        .putExtra("extra_prefs_set_back_text", R.string.alert_message_no_connection_back)
                        .putExtra("extra_prefs_set_next_text", "")));
                break;
            default:
                break;
        }
        mSnackbar.show();
    }

}
