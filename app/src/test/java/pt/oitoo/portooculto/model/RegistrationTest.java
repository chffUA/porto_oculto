package pt.oitoo.portooculto.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class RegistrationTest {

    private String username = "user";
    private String passwordConfirm = "passwordConfirm";
    private boolean validatingFields = true;


    Registration testRegistration = new Registration("test", "test2", "test@test.com", "test2", false);

    @Test
    public void getUsername() {
        assertEquals(testRegistration.getUsername(),"test");
    }

    @Test
    public void setUsername() {
        testRegistration.setUsername("new_user");
        assertEquals(testRegistration.getUsername(),"new_user");
    }

    @Test
    public void getPasswordConfirm() {
       assertEquals( testRegistration.getPasswordConfirm(),"test2");
    }

    @Test
    public void setPasswordConfirm() {
        testRegistration.setPasswordConfirm("test3");
        assertEquals(testRegistration.getPasswordConfirm(),"test3");
    }

    @Test
    public void isValidatingFields() {

        testRegistration.setValidatingFields(true);
        assertTrue(testRegistration.isValidatingFields());

    }

    @Test
    public void setValidatingFields() {
        testRegistration.setValidatingFields(false);
        assertFalse(testRegistration.isValidatingFields());
    }
}