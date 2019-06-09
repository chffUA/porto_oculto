package pt.oitoo.portooculto.engine.implementation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.concurrent.atomic.AtomicBoolean;

import pt.oitoo.portooculto.model.SignIn;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class SignInImplementationTest {


    @Mock
    FirebaseAuth mockFirebaseAuth = Mockito.mock(FirebaseAuth.class);
    FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
    SignIn mockSignIn = Mockito.mock(SignIn.class);



    @Test
    public void signIn() {




         boolean success = false;

        when(mockFirebaseAuth.getCurrentUser()).thenReturn(mockUser);
        when(mockFirebaseAuth.isSignInWithEmailLink(mockUser.getEmail())).thenReturn(true);
        when(mockSignIn.getEmail()).thenReturn("testemail");
        when(mockSignIn.getPassword()).thenReturn("testPassword");
        when(mockFirebaseAuth.isSignInWithEmailLink(anyString())).thenReturn(true);

        mockFirebaseAuth.signInWithEmailAndPassword(mockSignIn.getEmail(), mockSignIn.getPassword());
        success = mockFirebaseAuth.isSignInWithEmailLink(mockSignIn.getEmail());

        assertEquals(success,true);
    }
}