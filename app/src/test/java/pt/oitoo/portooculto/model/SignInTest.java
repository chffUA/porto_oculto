package pt.oitoo.portooculto.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class SignInTest {

    private SignIn testSignIn = new SignIn("test@test.com","test");


    @Test
    public void getEmail() {
        assertEquals("test@test.com",testSignIn.getEmail());
    }

    @Test
    public void setEmail() {
        testSignIn.setEmail("test2@test.com");
        assertEquals("test2@test.com",testSignIn.getEmail());

    }

    @Test
    public void getPassword() {
        assertEquals("test",testSignIn.getPassword());
    }

    @Test
    public void setPassword() {
        testSignIn.setPassword("test2");
        assertEquals("test2",testSignIn.getPassword());
    }
}