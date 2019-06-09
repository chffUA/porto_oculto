package pt.oitoo.portooculto.util;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.when;

public class SingleLiveEventTest {

    private static final String testTAG = "test";

    private AtomicBoolean testMPending = new AtomicBoolean(true);

    private SingleLiveEvent testEvent = new SingleLiveEvent();


    @Mock
    Observer mockObserver = Mockito.mock(Observer.class);
    AtomicBoolean mockPending = Mockito.mock(AtomicBoolean.class);


    @Test
    public void observe() {


        boolean observerChanged = false;


        assertEquals(testMPending.get(), true);

        if (testMPending.compareAndSet(true, false)) {
            mockObserver.onChanged(testMPending);
            observerChanged = true;
        }

        assertEquals(testMPending.get(), false);
        assertEquals(observerChanged, true);
    }

    @Test
    public void setValue() {
        testMPending.set(false);
        assertEquals(testMPending.get(), false);
        testMPending.set(true);
        assertEquals(testMPending.get(), true);
    }


}