package pt.oitoo.portooculto.model;

import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SubmissionTest {

    @Mock
    GeoPoint mockGeoLocation = Mockito.mock(GeoPoint.class);
    GeoPoint mockGeoLocation2 = Mockito.mock(GeoPoint.class);


    private String uid="testuid";
    private String name="testname";
    private String status="degraded";
    private String addressString = "123 fake street";
    private String ownership ="testOwnership";
    private String description ="empty" ;
    private int year =1946;
    private String wishes ="testwishes";
    private String type="testtype";
    private String uploaderName="zack hill";
    private String uploaderEmail="test@test.com";
    private String uploaderUUID="123test";
    private ArrayList<String> photos = new ArrayList<>();
    private String buildingStatus = "testsStatus";

    public Submission testSubmission = new Submission(uid,name,status,mockGeoLocation,addressString,ownership,description,year,
            wishes,type,uploaderName,uploaderEmail,uploaderUUID,photos,buildingStatus);


    @Test
    public void getUid() {
        assertEquals("testuid",testSubmission.getUid());
    }


    @Test
    public void setUid() {
        testSubmission.setUid("newUid");
        assertEquals("newUid",testSubmission.getUid());
    }

    @Test
    public void getName() {
        assertEquals("testname",testSubmission.getName());
    }

    @Test
    public void getStatus() {
        assertEquals("degraded",testSubmission.getStatus());

    }

    @Test
    public void getOwnership() {
        assertEquals("testOwnership",testSubmission.getOwnership());
    }

    @Test
    public void getDescription() {
        assertEquals("empty",testSubmission.getDescription());
    }

    @Test
    public void getYear() {
        assertEquals(1946,testSubmission.getYear());
    }

    @Test
    public void getWishes() {
        assertEquals("testwishes",testSubmission.getWishes());
    }

    @Test
    public void getType() {
        assertEquals("testtype",testSubmission.getType());
    }

    @Test
    public void getUploaderName() {
        assertEquals("zack hill",testSubmission.getUploaderName());
    }

    @Test
    public void setUploaderName() {
        testSubmission.setUploaderName("mc ride");
        assertEquals("mc ride",testSubmission.getUploaderName());
    }

    @Test
    public void getUploaderEmail() {
        assertEquals("test@test.com",testSubmission.getUploaderEmail());
    }

    @Test
    public void setUploaderEmail() {
        testSubmission.setUploaderEmail("test2@test2.com");
        assertEquals("test2@test2.com",testSubmission.getUploaderEmail());
    }

    @Test
    public void getUploaderUUID() {
        assertEquals("123test",testSubmission.getUploaderUUID());
    }

    @Test
    public void setUploaderUUID() {
        testSubmission.setUploaderUUID("testuid");
        assertEquals("testuid",testSubmission.getUploaderUUID());
    }

    @Test
    public void getAddressString() {
        assertEquals("123 fake street",testSubmission.getAddressString());
    }

    @Test
    public void setAddressString() {
        testSubmission.setAddressString("1234 real street");
        assertEquals("1234 real street",testSubmission.getAddressString());
    }

    @Test
    public void setName() {
        testSubmission.setName("new name");
        assertEquals("new name",testSubmission.getName());
    }

    @Test
    public void setStatus() {
    testSubmission.setStatus("new status");
    assertEquals("new status",testSubmission.getStatus());

    }

    @Test
    public void setOwnership() {
        testSubmission.setOwnership("new owner");
        assertEquals("new owner",testSubmission.getOwnership());
    }

    @Test
    public void setDescription() {
        testSubmission.setDescription("new description");
        assertEquals("new description",testSubmission.getDescription());
    }

    @Test
    public void setYear() {
        testSubmission.setYear(1996);
        assertEquals(1996,testSubmission.getYear());
    }

    @Test
    public void setWishes() {
        testSubmission.setWishes("new wishes");
        assertEquals("new wishes",testSubmission.getWishes());
    }

    @Test
    public void setType() {
        testSubmission.setType("new type");
        assertEquals("new type",testSubmission.getType());
    }

    @Test
    public void getPhotos() {
    assertEquals(photos,testSubmission.getPhotos());
    }

    @Test
    public void setPhotos() {
        ArrayList<String> newPhotos = new ArrayList<>();
        testSubmission.setPhotos(newPhotos);
        assertEquals(newPhotos,testSubmission.getPhotos());
    }

    @Test
    public void getGeoLocation() {
        assertEquals(mockGeoLocation,testSubmission.getGeoLocation());
    }

    @Test
    public void setGeoLocation() {
        testSubmission.setGeoLocation(mockGeoLocation2);
        assertEquals(mockGeoLocation2,testSubmission.getGeoLocation());

    }
}