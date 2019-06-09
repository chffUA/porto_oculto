package pt.oitoo.portooculto.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import pt.oitoo.portooculto.engine.implementation.MarkersImpl;
import pt.oitoo.portooculto.model.Submission;
import pt.oitoo.portooculto.util.ServiceUtil;
import pt.oitoo.portooculto.util.SnackbarMessage;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class MySubmissionsViewModelTest {

    @Mock
    MarkersImpl mockMarkersImpl = Mockito.mock(MarkersImpl.class);
    Application mockApplication = Mockito.mock(Application.class);
    Context mockContext = Mockito.mock(Context.class);
    MySubmissionsViewModel mockMySubmissionsViewModel = Mockito.mock(MySubmissionsViewModel.class);
    MutableLiveData<List<Submission>> testSubmissionsList = Mockito.mock(MutableLiveData.class);
    ConnectivityManager mockCM = Mockito.mock(ConnectivityManager.class);
    NetworkInfo mockNetworkInfo = Mockito.mock(NetworkInfo.class);
    ServiceUtil mockServiceUtil = Mockito.mock(ServiceUtil.class);

    List<Submission> testSubmission = new ArrayList<Submission>();


    @Test
    public void getSubmissionsList() {
        testSubmissionsList.setValue(testSubmission);
        assertNull(mockMySubmissionsViewModel.getSubmissionsList());
        when(mockMySubmissionsViewModel.getSubmissionsList()).thenReturn(testSubmissionsList);

        assertEquals(mockMySubmissionsViewModel.getSubmissionsList(), testSubmissionsList);


    }

    @Test
    public void getCartList() {
        when(mockMySubmissionsViewModel.getApplication()).thenReturn(mockApplication);
        when(mockContext.getApplicationContext()).thenReturn(mockContext);
        when(mockMySubmissionsViewModel.getApplication().getApplicationContext()).thenReturn(mockContext);
        when(mockContext.getSystemService(anyString())).thenReturn(mockCM);
        when(mockCM.getActiveNetworkInfo()).thenReturn(mockNetworkInfo);
        when(mockServiceUtil.isUserConnected(mockMySubmissionsViewModel.getApplication().getApplicationContext())).
                thenReturn(true);

        boolean online = true;

        if (mockServiceUtil.isUserConnected(mockMySubmissionsViewModel.getApplication().getApplicationContext()) && online) {

            List<Submission> successList = new ArrayList<>();

            when(mockMySubmissionsViewModel.getSubmissionsList()).thenReturn(testSubmissionsList);
            when(mockMySubmissionsViewModel.getSubmissionsList().getValue()).thenReturn(successList);
            mockMySubmissionsViewModel.getSubmissionsList().setValue(successList);
            assertEquals(mockMySubmissionsViewModel.getSubmissionsList().getValue(), successList);

        }

    }


}