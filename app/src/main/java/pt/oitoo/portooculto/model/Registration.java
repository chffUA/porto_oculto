package pt.oitoo.portooculto.model;

import android.databinding.Bindable;

import pt.oitoo.portooculto.BR;

public class Registration extends SignIn {

    private String username;
    private String passwordConfirm;
    private boolean validatingFields;

    public Registration(String username, String password, String email,
                        String passwordConfirm, boolean validatingFields) {
        super(email, password);
        this.username = username;
        this.passwordConfirm = passwordConfirm;
        this.validatingFields = validatingFields;
        notifyPropertyChanged(BR.validatingFields);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    @Bindable
    public boolean isValidatingFields() {
        return validatingFields;
    }

    public void setValidatingFields(boolean validatingFields) {
        this.validatingFields = validatingFields;
        notifyPropertyChanged(BR.validatingFields);
    }


}
