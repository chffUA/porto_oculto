package pt.oitoo.portooculto.view.ui.auth;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.util.FirebaseUtil;
import pt.oitoo.portooculto.util.Navigator;
import pt.oitoo.portooculto.util.SharedPreference;

import static pt.oitoo.portooculto.BaseConstants.ADD;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Auth);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        FirebaseUser user = FirebaseUtil.getCurrentUser();

        if(SharedPreference.isFirstRun(this)) {
            if (savedInstanceState == null) { //display terms and conditions fragment
                Navigator.changeFragment(R.id.auth_container, new TermsConditionsFragment(), this, false, "", ADD);
            }
        } else if (user != null){
            if(user.isEmailVerified() || (user.getProviderData().get(1) != null && user.getProviderData().get(1).getProviderId().contains("facebook.com"))){
                Navigator.toMain(this);
            } else {
                Navigator.changeFragment(R.id.auth_container, new EmailVerificationFragment(), this, false,"", ADD);
            }
        } else {
            if (savedInstanceState == null) { //display intro fragment
                Navigator.changeFragment(R.id.auth_container, new AuthFragment(), this, false,"", ADD);
            }
        }
    }

    public void hideKeyboard(View v) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) { }
    }

    @Override
    protected void onStart() {

        super.onStart();

    }
}
