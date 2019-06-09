package pt.oitoo.portooculto.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.facebook.CallbackManager;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import pt.oitoo.portooculto.engine.implementation.SignInImpl;
import pt.oitoo.portooculto.engine.implementation.callback.SingleResponseCallback;
import pt.oitoo.portooculto.model.SignIn;
import pt.oitoo.portooculto.model.SignInValidation;
import pt.oitoo.portooculto.util.ServiceUtil;
import pt.oitoo.portooculto.util.SingleLiveEvent;
import pt.oitoo.portooculto.util.SnackbarMessage;
import pt.oitoo.portooculto.util.TextUtil;
import pt.oitoo.portooculto.view.callback.OnProgressListener;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class SignInViewModelTest {

    @Mock
    Application mockApplication = Mockito.mock(Application.class);
    OnProgressListener mockOPL = Mockito.mock(OnProgressListener.class);
    ServiceUtil mockServiceUtil = Mockito.mock(ServiceUtil.class);
    Context mockContext = Mockito.mock(Context.class);
    ServiceUtil mockSU = Mockito.mock(ServiceUtil.class);
    ConnectivityManager mockCM = Mockito.mock(ConnectivityManager.class);
    NetworkInfo mockNI = Mockito.mock(NetworkInfo.class);
    SignInImpl mockLoginEngine = Mockito.mock(SignInImpl.class);
    CallbackManager mockCallbackManager = Mockito.mock(CallbackManager.class);



    SingleLiveEvent<Boolean> verify = Mockito.mock(SingleLiveEvent.class);
    SnackbarMessage testSnack = new SnackbarMessage();
    SignInValidation testSIValidator = new SignInValidation("testUser", "testPass",
            "testFirstName", false);
    SignInViewModel testSIVM = new SignInViewModel(mockApplication);

    @Test
    public void getSnack() {
        assertTrue(testSIVM.getSnack() instanceof SnackbarMessage);
        assertNotNull(testSIVM.getSnack());

    }

    @Test
    public void getSignIn() {
        assertTrue(testSIVM.getSignIn() instanceof SignInValidation);
        assertNotNull(testSIVM.getSignIn());

    }

    @Test
    public void setOnProgressListener() {
        assertNull(testSIVM.getOnProgressListener());
        testSIVM.setOnProgressListener(mockOPL);

        assertNotNull(testSIVM.getOnProgressListener());
        assertEquals(testSIVM.getOnProgressListener(), mockOPL);
    }

    @Test
    public void signin() {

        when(mockContext.getSystemService(anyString())).thenReturn(mockCM);
        when(mockCM.getActiveNetworkInfo()).thenReturn(mockNI);
        when(mockSU.isUserConnected(mockContext)).thenReturn(true);


        boolean invalid = false;
        boolean valid = true;

        SignInValidation toFailSIV = new SignInValidation("", "", "",
                false);


        if (TextUtil.isInvalidString(toFailSIV.getPassword()) || TextUtil.isInvalidString
                (toFailSIV.getEmail())) {
            invalid = true;
        }

        if (TextUtil.isInvalidString(testSIValidator.getPassword()) || TextUtil.isInvalidString
                (testSIValidator.getEmail())) {
            valid = false;
        }


        assertTrue(invalid);
        assertTrue(valid);

        boolean successConnecting = false;

        if (valid == true) {
            if (mockSU.isUserConnected(mockContext)) {
                successConnecting = true;
                assertTrue(successConnecting);
            }
        }


    }

    @Test
    public void insertUserData() {

        boolean signedIn = false;
        SignInValidation testSignInV = new SignInValidation("test@test.com", "test", "test", false);

        try {
            mockLoginEngine.insertUserData(testSignInV);
            signedIn = true;
        } catch (Exception e) {
            assertFalse(signedIn);
        }
        assertTrue(signedIn);
    }

    @Test
    public void resendVerificationEmail() {
        boolean sent = false;

        try {
            mockLoginEngine.resendVerificationEmail();
            sent = true;
        } catch (Exception e) {
            assertFalse(sent);
        }

        assertTrue(sent);
    }

    @Test
    public void login() {

        SignIn testSignIn = new SignIn("test@test.com", "test");
        boolean loggedIn = false;

        try {
            mockLoginEngine.signIn(testSignIn);
            loggedIn = true;
        } catch (Exception e) {
            assertFalse(loggedIn);
        }

        assertTrue(loggedIn);
    }




    @Test
    public void facebookLogin() {


        boolean sucess = false;

        try {
            mockLoginEngine.facebookLogin(mockCallbackManager);
            sucess = true;
        } catch (Exception e) {
            sucess = false;
        }

        assertEquals(sucess, true);
    }

    @Test
    public void getOnProgressListener() {

        testSIVM.setOnProgressListener(mockOPL);
        assertEquals(testSIVM.getOnProgressListener(), mockOPL);

       assertNotNull(testSIVM.getOnProgressListener());



    }
}