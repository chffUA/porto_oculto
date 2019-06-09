package pt.oitoo.portooculto.model;

import android.databinding.Bindable;

import pt.oitoo.portooculto.BR;

public class SignInValidation extends SignIn {

    private String firstname;
    private boolean validatingFields;

    public SignInValidation(String email, String password, String firstname, boolean validatingFields) {
        super(email, password);
        this.firstname = firstname;
        this.validatingFields = validatingFields;
    }

    @Bindable
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;

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
