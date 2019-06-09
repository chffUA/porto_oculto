package pt.oitoo.portooculto.engine.implementation;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.model.SignIn;
import pt.oitoo.portooculto.util.FirebaseUtil;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class SignInImplTest {

    @Mock
    FirebaseAuth mockFirebaseAuth = Mockito.mock(FirebaseAuth.class);
    FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
    FirebaseUtil mockUtil = Mockito.mock(FirebaseUtil.class);
    SignIn testSignIn = new SignIn("test@test.com", "test");
    Task mockTask = Mockito.mock(Task.class);
    FirebaseFirestore mockReference = Mockito.mock(FirebaseFirestore.class);
    FirebaseUser mockUser2 = null;



    @Test
    public void insertUserData() {
        when(mockUser.getEmail()).thenReturn(testSignIn.getEmail());
        when(mockUser.getUid()).thenReturn("1");
        when(mockFirebaseAuth.getCurrentUser()).thenReturn(mockUser);
        when(mockTask.isComplete()).thenReturn(true);
        when(mockTask.getResult()).thenReturn(false);

        if (mockUser != null) {
            mockFirebaseAuth.getCurrentUser();
            assertEquals(mockTask.isComplete(), true);
        }
        if (mockUser2 != null) {
            assertEquals(mockTask.getResult(), true);
        } else {
            assertEquals(mockTask.getResult(), false);
        }


    }

    @Test
    public void resendVerificationEmail() {

        when(mockUser.getEmail()).thenReturn(testSignIn.getEmail());
        when(mockUser.sendEmailVerification()).thenReturn(mockTask);
        when(mockTask.getResult()).thenReturn(true);

        assertEquals(mockUser.getEmail(), "test@test.com");
        mockUser.sendEmailVerification();
        assertEquals(mockTask.getResult(), true);

    }

    @Test
    public void getMAuthTest() {
        assertNotNull(mockFirebaseAuth);
    }
}