package pt.oitoo.portooculto.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.facebook.CallbackManager;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.engine.implementation.SignInImpl;
import pt.oitoo.portooculto.engine.implementation.callback.SingleResponseCallback;
import pt.oitoo.portooculto.model.Registration;
import pt.oitoo.portooculto.model.SignIn;
import pt.oitoo.portooculto.model.SignInValidation;
import pt.oitoo.portooculto.util.ServiceUtil;
import pt.oitoo.portooculto.util.SharedPreference;
import pt.oitoo.portooculto.util.SingleLiveEvent;
import pt.oitoo.portooculto.util.SnackbarMessage;
import pt.oitoo.portooculto.util.TextUtil;
import pt.oitoo.portooculto.view.callback.OnProgressListener;

import static pt.oitoo.portooculto.BaseConstants.ADMIN_PROFILE;

public class SignInViewModel extends AndroidViewModel {

    private SignInImpl loginEngine = new SignInImpl(getApplication().getApplicationContext());
    private SnackbarMessage snackbarMessage = new SnackbarMessage();
    private SignInValidation signIn;
    private String profile;
    private static final String EMAIL = "email";

    public String getProfile() {
        return profile;
    }

    public SignInViewModel(@NonNull Application application) {
        super(application);
    }

    public SnackbarMessage getSnack() {
        if (snackbarMessage == null){
            snackbarMessage = new SnackbarMessage();
        }
        return snackbarMessage;
    }

    public SignInValidation getSignIn() {
        if(signIn == null){
            signIn = new SignInValidation("","","",  false);
        }
        return signIn;
    }

    private OnProgressListener onProgressListener;
    public void setOnProgressListener(OnProgressListener onProgressListener){
        this.onProgressListener = onProgressListener;
    }

    public void signin() {  //databinded
        if(!TextUtil.isInvalidString(getSignIn().getEmail()) && !TextUtil.isInvalidString(getSignIn().getPassword())){ //only if not blank fields
            if (ServiceUtil.isUserConnected(getApplication().getApplicationContext())) {
                onProgressListener.start();
                loginEngine.setCallback(new SingleResponseCallback<String>() {

                    @Override
                    public void onSuccess(String s) {
                        profile = s;
                        SharedPreference.saveStringSharedPreference(getApplication().getApplicationContext(), ADMIN_PROFILE, s);
                        onProgressListener.onSuccess();

                    }

                    @Override
                    public void onFailure(int message) {
                        onProgressListener.onFailure(message);
                    }
                });
            } else {
                getSnack().setValue(R.string.alert_message_no_connection);
            }
        } else {
            getSnack().setValue(R.string.alert_empty_credentials);
        }
    }

    public void insertUserData(){
        loginEngine.insertUserData();
    }

    public void insertUserFacebookData(SignInValidation signIn){
        loginEngine.insertUserData();
    }

    public void resendVerificationEmail(){
        loginEngine.resendVerificationEmail();
    }

    public void login() {
        loginEngine.signIn(getSignIn());

    }

    public void facebookLogin(CallbackManager callbackManager) {
        loginEngine.setCallback(new SingleResponseCallback<String>() {

            @Override
            public void onSuccess(String s) {
                profile = s;
                SharedPreference.saveStringSharedPreference(getApplication().getApplicationContext(), ADMIN_PROFILE, s);
                onProgressListener.onSuccess();
            }

            @Override
            public void onFailure(int message) {
                onProgressListener.onFailure(message);
            }
        });
        loginEngine.facebookLogin(callbackManager);
    }

    public OnProgressListener getOnProgressListener(){
        return this.onProgressListener;
    }
}