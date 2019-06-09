package pt.oitoo.portooculto.util;

import android.content.Context;
import android.content.SharedPreferences;

import pt.oitoo.portooculto.BaseConstants;
import pt.oitoo.portooculto.BuildConfig;

import static pt.oitoo.portooculto.BaseConstants.SETTINGS_FIRST_RUN;


public abstract class SharedPreference {

    private static final String PREFERENCES = BuildConfig.APPLICATION_ID + "_preferences";

    private SharedPreference(){}

    public static String readStringSharedPreference(Context context, String settingName, String defaultValue) {
        SharedPreferences sharedPref = setSharedPreferences(context);
        String passEncrypted = sharedPref.getString(Encryption.encodeStringBase64(settingName), Encryption.encodeStringBase64(defaultValue));
        return Encryption.decodeStringBase64(passEncrypted);
    }

    public static void saveStringSharedPreference(Context context, String settingName, String settingValue) {
        SharedPreferences sharedPref = setSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Encryption.encodeStringBase64(settingName), Encryption.encodeStringBase64(settingValue));
        editor.apply();
    }

    private static SharedPreferences setSharedPreferences(Context context) {
        return context.getSharedPreferences(getDefaultSharedPreferencesName(),
                getDefaultSharedPreferencesMode());
    }

    public static boolean isFirstRun(Context context) {
        return Boolean.valueOf(readStringSharedPreference(context, SETTINGS_FIRST_RUN, "true"));
    }

    private static String getDefaultSharedPreferencesName() {
        return PREFERENCES;
    }

    private static int getDefaultSharedPreferencesMode() {
        return Context.MODE_PRIVATE;
    }
}
