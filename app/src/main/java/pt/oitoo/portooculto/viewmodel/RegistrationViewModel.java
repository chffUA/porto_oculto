package pt.oitoo.portooculto.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.engine.implementation.SignInImpl;
import pt.oitoo.portooculto.engine.implementation.callback.SingleResponseCallback;
import pt.oitoo.portooculto.model.Registration;
import pt.oitoo.portooculto.util.ServiceUtil;
import pt.oitoo.portooculto.util.SingleLiveEvent;
import pt.oitoo.portooculto.util.SnackbarMessage;
import pt.oitoo.portooculto.util.TextUtil;
import pt.oitoo.portooculto.view.callback.OnProgressListener;

public class RegistrationViewModel extends AndroidViewModel {

    private SignInImpl authEngine = new SignInImpl(getApplication().getApplicationContext());
    private SnackbarMessage snackbarMessage = new SnackbarMessage();
    private Registration reg;

    public RegistrationViewModel(@NonNull Application application) {
        super(application);
    }

    public SnackbarMessage getSnack() {
        if (snackbarMessage == null){
            snackbarMessage = new SnackbarMessage();
        }
        return snackbarMessage;
    }

    public Registration getReg() {
        if(reg == null){
            reg = new Registration("", "", "",
                    "",  false);
        }
        return reg;
    }

    private OnProgressListener onProgressListener;
    public void setOnProgressListener(OnProgressListener onProgressListener){
        this.onProgressListener = onProgressListener;
    }

    public void register() {
        if(TextUtil.isInvalidString(getReg().getUsername())
                || TextUtil.isInvalidString(getReg().getEmail())
                || TextUtil.isInvalidString(getReg().getPassword())
                || TextUtil.isInvalidString(getReg().getPasswordConfirm())) {
            getSnack().setValue(R.string.alert_message_fill_in);
            cleanPasswords();
            return;
        }
        if (! getReg().getPassword().equals(getReg().getPasswordConfirm())) {
            getSnack().setValue(R.string.alert_message_password_not_match);
            cleanPasswords();
            return;
        }
        if (ServiceUtil.isUserConnected(getApplication().getApplicationContext())) {
            onProgressListener.start();
            authEngine.setCallback(new SingleResponseCallback() {

                @Override
                public void onSuccess(Object o) {
                    onProgressListener.onSuccess();
                }

                @Override
                public void onFailure(int message) {
                    onProgressListener.onFailure(message);
                }

            });
        } else {
            getSnack().setValue(R.string.alert_message_no_connection);
            cleanPasswords();
        }

    }

    private void cleanPasswords() {
        // getReg().setPassword(null);
        // getReg().setPasswordConfirm(null);
        // TODO update view
    }

    public void setReg(Registration auth) {
        this.reg = reg;
    }

    public void registerAuthEngine() {
        authEngine.register(getReg());
    }
}