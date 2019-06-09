package pt.oitoo.portooculto.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.engine.implementation.SignInImpl;
import pt.oitoo.portooculto.util.AlertMessage;
import pt.oitoo.portooculto.util.ServiceUtil;
import pt.oitoo.portooculto.util.SnackbarMessage;

public class ForgotPasswordViewModel extends AndroidViewModel {

    private SignInImpl signInImpl = new SignInImpl(getApplication().getApplicationContext());
    private SnackbarMessage snackbarMessage = new SnackbarMessage();

    private MutableLiveData<String> email = new MutableLiveData<>();

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public void setEmail(MutableLiveData<String> email) {
        this.email = email;
    }

    public ForgotPasswordViewModel(@NonNull Application application) {
        super(application);
    }


    public void forgotPassword(String email){
        if (ServiceUtil.isUserConnected(getApplication().getApplicationContext())) {
            AlertMessage.showToast("An email was sent to " + email + " with password recovering instructions.", getApplication().getApplicationContext());
            signInImpl.forgotPassword(email);
        } else {
            snackbarMessage.setValue(R.string.alert_message_no_connection);
        }
    }

}
