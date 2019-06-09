package pt.oitoo.portooculto.viewmodel;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.model.Post;
import pt.oitoo.portooculto.util.FirebaseUtil;
import pt.oitoo.portooculto.util.ServiceUtil;
import pt.oitoo.portooculto.util.SingleLiveEvent;
import pt.oitoo.portooculto.util.SnackbarMessage;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PostViewModelTest {

    @Mock
    SingleLiveEvent resend = Mockito.mock(SingleLiveEvent.class);
    SingleLiveEvent<Boolean> verify = Mockito.mock(SingleLiveEvent.class);
    Application mockApplication = Mockito.mock(Application.class);
    Bitmap mockBitmap = Mockito.mock(Bitmap.class);
    Location mockLocation = Mockito.mock(Location.class);


    private String mockPointId = "mockpointid";
    private PostViewModel testPVM = new PostViewModel(mockApplication, mockBitmap, mockLocation, mockPointId,"address");
    private ByteArrayOutputStream testBaos = new ByteArrayOutputStream();


    @Test
    public void getSnack() {
        assertTrue(testPVM.getSnack() instanceof SnackbarMessage);
        assertNotNull(testPVM.getSnack());

    }

    @Test
    public void getPost() {
        assertTrue(testPVM.getPost() instanceof Post);
        assertNotNull(testPVM.getPost());
    }

    @Test
    public void postBuilding() {

        //TODO check how to test methods of this type
      //  StorageReference photosRef = FirebaseUtil.getPhotoRef(null);

      //  Boolean teste = new Boolean(true);
        // when(testServiceUtil.isUserConnected(mockContext)).thenReturn(teste);

      //  mockPhotosRef.putBytes(testData);

        //ServiceUtil.isUserConnected(mockContext) ;
        }
    }