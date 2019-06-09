package pt.oitoo.portooculto.engine.implementation.generic;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import pt.oitoo.portooculto.engine.implementation.callback.ListResponseCallback;
import pt.oitoo.portooculto.engine.implementation.callback.SingleResponseCallback;

import static org.junit.Assert.*;

public class GenericImplementationTest {

    @Mock
    SingleResponseCallback mockSRC = Mockito.mock(SingleResponseCallback.class);
    ListResponseCallback mockLRC = Mockito.mock(ListResponseCallback.class);

    private SingleResponseCallback testSingleResponseCallback;
    private ListResponseCallback listResponseCallback;

    GenericImplementation testGenericImplementation = new GenericImplementation();

    @Test
    public void setCallback() {
        testGenericImplementation.setCallback(testSingleResponseCallback);
        assertEquals(testGenericImplementation.getSingleResponseCallback(),testGenericImplementation.getSingleResponseCallback());
    }

    @Test
    public void getSingleResponseCallback() {

        testGenericImplementation.setCallback(mockSRC);
        assertEquals(mockSRC,testGenericImplementation.getSingleResponseCallback());
    }

    @Test
    public void getListResponseCallback() {
        assertEquals(listResponseCallback,testGenericImplementation.getListResponseCallback());
    }

    @Test
    public void setListResponseCallback() {
        testGenericImplementation.setListResponseCallback(mockLRC);
        assertEquals(mockLRC,testGenericImplementation.getListResponseCallback());
    }
}