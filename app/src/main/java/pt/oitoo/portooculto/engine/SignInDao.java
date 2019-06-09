package pt.oitoo.portooculto.engine;

import com.facebook.CallbackManager;

import pt.oitoo.portooculto.model.Registration;
import pt.oitoo.portooculto.model.SignIn;
import pt.oitoo.portooculto.model.SignInValidation;

public interface SignInDao {
    void signIn(SignIn signIn);
    void facebookLogin(CallbackManager callbackManager);
    void insertUserData();
    void insertFacebookUserData();
    void resendVerificationEmail();
    void register(Registration reg);
    void forgotPassword(String email);
}
