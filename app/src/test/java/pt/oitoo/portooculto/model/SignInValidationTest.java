package pt.oitoo.portooculto.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class SignInValidationTest {

    private String testFirstName = "test";
    private boolean testValidatingFields = false;

    private SignInValidation testSIValidation= new SignInValidation("testUsername","test",testFirstName,testValidatingFields);

    @Test
    public void getFirstname() {
        assertEquals("test",testSIValidation.getFirstname());
    }


    @Test
    public void setFirstname() {
    testSIValidation.setFirstname("test2");
    assertEquals("test2",testSIValidation.getFirstname());

    }

    @Test
    public void isValidatingFields() {
    testSIValidation.setValidatingFields(true);
    assertEquals(testSIValidation.isValidatingFields(),true);
    }

    @Test
    public void setValidatingFields() {
    testSIValidation.setValidatingFields(false);
    assertEquals(false,testSIValidation.isValidatingFields());

    }
}