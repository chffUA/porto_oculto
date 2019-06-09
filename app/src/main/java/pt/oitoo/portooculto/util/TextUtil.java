package pt.oitoo.portooculto.util;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

public class TextUtil {

    private static final Pattern INVALID_CHARS_PATTERN =
            Pattern.compile("^.*[~#@*+%{}<>\\[\\]|\"\\_].*$");

    private TextUtil(){}

    public static boolean isInvalidString(final String string){
        return string == null || string.isEmpty() || string.length() > 44;
    }

    public static boolean containsIllegals(String toExamine) {
        return INVALID_CHARS_PATTERN.matcher(toExamine).matches();
    }

    public static boolean isInvalidNumber(final long number){
        return number == 0 || number < 0;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e("Create URL", "Error with creating URL", exception);
            return null;
        }
        return url;
    }
}
