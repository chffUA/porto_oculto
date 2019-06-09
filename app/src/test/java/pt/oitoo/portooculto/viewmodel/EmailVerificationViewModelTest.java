package pt.oitoo.portooculto.viewmodel;

import android.app.Application;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import pt.oitoo.portooculto.util.SingleLiveEvent;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class EmailVerificationViewModelTest {


    @Mock
    Application mockApplication = Mockito.mock(Application.class);
    SingleLiveEvent resend = Mockito.mock(SingleLiveEvent.class);
    SingleLiveEvent<Boolean> verify = Mockito.mock(SingleLiveEvent.class);


    private EmailVerificationViewModel testEmailVVM = new EmailVerificationViewModel(mockApplication);

    @Test
    public void getResend() {

        boolean isSingleLiveEvent = testEmailVVM.getResend() instanceof SingleLiveEvent;

        assertTrue(isSingleLiveEvent);
    }

    @Test
    public void getVerify() {

        boolean isSingleLiveEvent2 = testEmailVVM.getResend() instanceof SingleLiveEvent;

        assertTrue(isSingleLiveEvent2);
    }

    @Test
    public void resend() {

        when(resend.getValue()).thenReturn(true);

        assertEquals(resend.getValue(),true);
    }

    @Test
    public void verify() {
        when(verify.getValue()).thenReturn(true);
        assertEquals(verify.getValue(),true);
    }
}