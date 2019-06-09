package pt.oitoo.portooculto.engine.implementation;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import pt.oitoo.portooculto.model.Submission;

import static org.junit.Assert.*;

public class AdminImplTest {

    @Mock
    CollectionReference mockPointsRef = Mockito.mock(CollectionReference.class);


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




    @Test
    public void listSubsmissions() {

        ArrayList<Submission> pointsRef = new ArrayList<>();
        ArrayList<Submission> pointsRef2 = new ArrayList<>();


        Submission testSubmission1 = new Submission(uid,name,"not moderated",mockGeoLocation,addressString,ownership,description,year,
                wishes,type,uploaderName,uploaderEmail,uploaderUUID,photos,status);
        Submission testSubmission2 = new Submission(uid,name,"moderated",mockGeoLocation,addressString,ownership,description,year,
                wishes,type,uploaderName,uploaderEmail,uploaderUUID,photos,status);
        Submission testSubmission3 = new Submission(uid,name,"not moderated",mockGeoLocation,addressString,ownership,description,year,
                wishes,type,uploaderName,uploaderEmail,uploaderUUID,photos,status);

        pointsRef.add(testSubmission1);
        pointsRef.add(testSubmission2);
        pointsRef.add(testSubmission3);

        ArrayList<Submission> validSubmissions = new ArrayList<>();

        for (Submission document:pointsRef) {

            if(document.getStatus() == "not moderated"){
                validSubmissions.add(document);
            }

        }

        ArrayList<Submission> validSubmissions2 = new ArrayList<>();

        for (Submission document:pointsRef2) {

            if(document.getStatus() == "not moderated"){
                validSubmissions2.add(document);
            }

        }

        assertEquals(validSubmissions.size(),2);
        assertTrue(validSubmissions.contains(testSubmission1) && validSubmissions.contains(testSubmission3));
        assertFalse(validSubmissions.contains(testSubmission2));

        assertEquals(validSubmissions2.size(),0);

    }

    @Test
    public void approveSubsmission() {

    }
}