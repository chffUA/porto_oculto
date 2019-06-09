package pt.oitoo.portooculto.model;

import android.graphics.Bitmap;
import android.location.Location;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class PostTest {

    @Mock
    Bitmap cameraBitmap2 = Mockito.spy(Bitmap.class);
    Location location2 = Mockito.mock(Location.class);



    Bitmap cameraBitmap;
    Location location;
    String pointId;


    Post testPost = new Post(cameraBitmap,false);

    @Test
    public void getCameraBitmap() {
        assertEquals(cameraBitmap, testPost.getCameraBitmap());

    }

    @Test
    public void setCameraBitmap() {


        testPost.setCameraBitmap(cameraBitmap2);
        assertEquals(testPost.getCameraBitmap(), cameraBitmap2);

    }

    @Test
    public void setLocation() {
        testPost.setLocation(location);
        assertEquals(testPost.getLocation(), location);

    }

    @Test
    public void getLocation() {
        testPost.setLocation(location2);
        assertEquals(testPost.getLocation(), location2);
    }

    @Test
    public void getPointId() {
        testPost.setPointId(pointId);
        assertEquals(testPost.getPointId(),pointId);
    }

    @Test
    public void setPointId() {
        testPost.setPointId("test");
        assertEquals(testPost.getPointId(),"test");
    }
}