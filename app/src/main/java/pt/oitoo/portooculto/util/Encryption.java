package pt.oitoo.portooculto.util;

import android.util.Base64;

public class Encryption {

    private Encryption(){}

    public static String encodeStringBase64(String input) {
        if(input.isEmpty()){
            return null;
        } else {
            return Base64.encodeToString(input.getBytes(), Base64.DEFAULT) ;
        }
    }

    public static String decodeStringBase64(String input){
        if(input == null){
            return null;
        } else {
            return new String(Base64.decode(input, Base64.DEFAULT));
        }
    }

}
