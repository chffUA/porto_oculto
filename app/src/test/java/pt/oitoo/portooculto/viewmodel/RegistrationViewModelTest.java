package pt.oitoo.portooculto.viewmodel;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.model.Registration;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;

import pt.oitoo.portooculto.util.ServiceUtil;
import pt.oitoo.portooculto.util.SingleLiveEvent;
import pt.oitoo.portooculto.util.TextUtil;
import pt.oitoo.portooculto.view.callback.OnProgressListener;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class RegistrationViewModelTest {

    @Mock

    Application mockApplication = Mockito.mock(Application.class);
    RegistrationViewModel mockRVM = Mockito.mock(RegistrationViewModel.class);

    private RegistrationViewModel testRVM = new RegistrationViewModel(mockApplication);
    private Registration testReg = new Registration("", "", "",
            "", false);


    @Test
    public void getSnack() {
        assertTrue(testRVM.getSnack() instanceof SingleLiveEvent);
        assertNotNull(testRVM.getSnack() instanceof SingleLiveEvent);
    }

    @Test
    public void getReg() {
        testRVM.setReg(testReg);
        assertNotNull(testRVM.getReg());
        assertEquals(testRVM.getReg().getEmail(), testReg.getEmail());
        assertEquals(testRVM.getReg().getEmail(), testReg.getEmail());
        assertEquals(testRVM.getReg().getPasswordConfirm(), testReg.getPasswordConfirm());

    }


    @Test
    public void register() {

        boolean passed = true;


        Registration testReg1 = new Registration("", "", "",
                "", false);

        Registration testReg2 = new Registration("aaa", "bbb",
                "ccc@gmail.com", "bbb", false);


        testRVM.setReg(testReg1);

        if (TextUtil.isInvalidString(testRVM.getReg().getEmail())
                || TextUtil.isInvalidString(testRVM.getReg().getPassword())
                || TextUtil.isInvalidString(testRVM.getReg().getPasswordConfirm())) {
            passed = false;
        }
        assertEquals(passed, false);

        passed = true;


        if (!testRVM.getReg().getPassword().equals(testRVM.getReg().getPasswordConfirm())) {
            assertEquals(passed, false);
        }

        testRVM.setReg(testReg2);

        if (testRVM.getReg().getPassword().equals(testRVM.getReg().getPasswordConfirm()) &&
                testRVM.getReg().getEmail().matches("[a-z]+@[a-z]+.[a-z][a-z][a-z]?")) {
            passed = true;

        }
        assertEquals(passed, true);

    }

    @Test
    public void setReg() {

        Registration testReg1 = new Registration("aaa", "bbb",
                "ccc@gmail.com", "bbb", false);

        when(mockRVM.getReg()).thenReturn(testReg1);

        mockRVM.setReg(testReg1);

        assertEquals(mockRVM.getReg().getEmail(), testReg1.getEmail());
        assertEquals(mockRVM.getReg().getPasswordConfirm(), testReg1.getPasswordConfirm());
    }

}