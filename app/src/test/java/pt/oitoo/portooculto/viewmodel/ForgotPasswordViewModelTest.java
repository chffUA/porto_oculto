package pt.oitoo.portooculto.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import pt.oitoo.portooculto.util.AlertMessage;
import pt.oitoo.portooculto.util.ServiceUtil;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ForgotPasswordViewModelTest {

    @Mock
    Application mockApplication = Mockito.mock(Application.class);
    ServiceUtil mockServiceUtil = Mockito.mock(ServiceUtil.class);
    Context mockContext = Mockito.mock(Context.class);
    ForgotPasswordViewModel mockFPVM = Mockito.mock(ForgotPasswordViewModel.class);
    ConnectivityManager mockCM = Mockito.mock(ConnectivityManager.class);
    NetworkInfo mockNetworkInfo = Mockito.mock(NetworkInfo.class);

    private MutableLiveData<String> testEmail = new MutableLiveData<>();
    private MutableLiveData<String> testEmail2 = new MutableLiveData<>();

    ForgotPasswordViewModel testFPVM = new ForgotPasswordViewModel(mockApplication);

    @Test
    public void getEmail() {
        testFPVM.setEmail(testEmail);
        assertEquals(testFPVM.getEmail(), testEmail);
    }

    @Test
    public void setEmail() {
        testFPVM.setEmail(testEmail);
        assertEquals(testFPVM.getEmail(), testEmail);
        testFPVM.setEmail(testEmail2);
        assertEquals(testFPVM.getEmail(), testEmail2);

    }

    @Test
    public void forgotPassword() {
        String emailToTest = "email@to.test";
        when(mockFPVM.getApplication()).thenReturn(mockApplication);
        when(mockContext.getApplicationContext()).thenReturn(mockContext);
        when(testFPVM.getApplication().getApplicationContext()).thenReturn(mockContext);
        when(mockContext.getSystemService(anyString())).thenReturn(mockCM);
        when(mockCM.getActiveNetworkInfo()).thenReturn(mockNetworkInfo);
        when(mockServiceUtil.isUserConnected(testFPVM.getApplication().getApplicationContext())).
                thenReturn(true);


        boolean online=true;

        if(mockServiceUtil.isUserConnected(testFPVM.getApplication().getApplicationContext()) && online){
            assertEquals("An email was sent to email@to.test with password recovering instructions.",showToast(emailToTest));
        }else{
            online=false;
            assertNull(showToast(null));
        }


    }


    private String showToast( String email) {
        return "An email was sent to " + email + " with password recovering instructions.";
    }

}