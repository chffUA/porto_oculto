package pt.oitoo.portooculto.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import pt.oitoo.portooculto.engine.implementation.AdminImpl;
import pt.oitoo.portooculto.model.Submission;
import pt.oitoo.portooculto.util.ServiceUtil;
import pt.oitoo.portooculto.util.SnackbarMessage;
import pt.oitoo.portooculto.view.callback.OnProgressListener;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ModerationViewModelTest {

    @Mock
    OnProgressListener mockOnProgressListener = Mockito.mock(OnProgressListener.class);
    Application mockApplication = Mockito.mock(Application.class);
    AdminImpl mockAdminImpl= Mockito.mock(AdminImpl.class);
    ModerationViewModel mockMVM = Mockito.mock(ModerationViewModel.class);
    Context mockContext = Mockito.mock(Context.class);
    ConnectivityManager mockCM = Mockito.mock(ConnectivityManager.class);
    NetworkInfo mockNetworkInfo = Mockito.mock(NetworkInfo.class);
    ServiceUtil mockServiceUtil = Mockito.mock(ServiceUtil.class);


    private MutableLiveData<List<Submission>> testSubmissionsList = new MutableLiveData<>();
    private SnackbarMessage testSnackbarMessage = new SnackbarMessage();

    public MutableLiveData<Boolean> goToMain = new MutableLiveData<>();


    @Test
    public void getSubmissionsList() {

        when(mockMVM.getSubmissionsList()).thenReturn(testSubmissionsList);

        assertNotNull(mockMVM.getSubmissionsList());
        assertTrue(mockMVM.getSubmissionsList() instanceof MutableLiveData);
        assertEquals(mockMVM.getSubmissionsList(),testSubmissionsList);
    }

    @Test
    public void getCartList() {
        when(mockMVM.getApplication()).thenReturn(mockApplication);
        when(mockContext.getApplicationContext()).thenReturn(mockContext);
        when(mockMVM.getApplication().getApplicationContext()).thenReturn(mockContext);
        when(mockContext.getSystemService(anyString())).thenReturn(mockCM);
        when(mockCM.getActiveNetworkInfo()).thenReturn(mockNetworkInfo);
        when(mockServiceUtil.isUserConnected(mockMVM.getApplication().getApplicationContext())).
                thenReturn(true);
        when(mockMVM.getSubmissionsList()).thenReturn(testSubmissionsList);

        boolean online = true;

        if (mockServiceUtil.isUserConnected(mockMVM.getApplication().getApplicationContext()) && online) {
            mockMVM.getCartList();
            mockMVM.getSubmissionsList();


            assertNotNull(mockMVM.getSubmissionsList());
            assertEquals(mockMVM.getSubmissionsList(),testSubmissionsList);

        }
        online=false;
        if (mockServiceUtil.isUserConnected(mockMVM.getApplication().getApplicationContext()) && online) {
        assertNull(mockMVM.getSubmissionsList());

        }


    }
}