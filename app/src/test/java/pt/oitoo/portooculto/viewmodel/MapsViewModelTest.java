package pt.oitoo.portooculto.viewmodel;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pt.oitoo.portooculto.util.MarkerEntity;
import pt.oitoo.portooculto.util.SingleLiveEvent;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class MapsViewModelTest {

    @Mock

    MapsViewModel mockMVM = Mockito.mock(MapsViewModel.class);
    QuerySnapshot mockQuerySnapshot = Mockito.mock(QuerySnapshot.class);
    Object documentSnapshotToPass = Mockito.mock(DocumentSnapshot.class);
    Geocoder mockGeocoder = Mockito.mock(Geocoder.class);
    Address mockAddress = Mockito.mock(Address.class);
    Address mockAddressToFail = Mockito.mock(Address.class);


    private SingleLiveEvent<Boolean> testCityNotAllowed = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> testCloseDialog = new SingleLiveEvent<>();
    private SingleLiveEvent<Location> testSendLocation = new SingleLiveEvent<>();
    private SingleLiveEvent<MarkerEntity> testNewMarker = new SingleLiveEvent<>();


    private GeoPoint testGeo = new GeoPoint(41.1607, -8.5824);
    private String testName = "testLocation";
    private String[] testStatus = {"Accepted", "Declined", "Not moderated"};
    private ArrayList<String> testPhotoIds = new ArrayList<>();


    @Test
    public void getSendLocation() {

        when(mockMVM.getSendLocation()).thenReturn(testSendLocation);
        assertNotNull(mockMVM.getSendLocation());
        assertTrue(mockMVM.getSendLocation() instanceof SingleLiveEvent);

    }

    @Test
    public void getCityNotAllowed() {
        when(mockMVM.getCityNotAllowed()).thenReturn(testCityNotAllowed);
        assertNotNull(mockMVM.getCityNotAllowed());
        assertTrue(mockMVM.getCityNotAllowed() instanceof SingleLiveEvent);

    }

    @Test
    public void getCloseDialog() {

        when(mockMVM.getCloseDialog()).thenReturn(testCloseDialog);
        assertNotNull(mockMVM.getCloseDialog());
        assertTrue(mockMVM.getCloseDialog() instanceof SingleLiveEvent);
    }

    @Test
    public void getNewMarker() {
        when(mockMVM.getNewMarker()).thenReturn(testNewMarker);
        assertNotNull(mockMVM.getNewMarker());
        assertTrue(mockMVM.getNewMarker() instanceof SingleLiveEvent);
    }


    @Test
    public void getSelectedMarker() {
        when(((DocumentSnapshot) documentSnapshotToPass).getGeoPoint("geoLocation")).
                thenReturn(testGeo);
        when(((DocumentSnapshot) documentSnapshotToPass).getString("name")).
                thenReturn(testName);
        when(((DocumentSnapshot) documentSnapshotToPass).getString("status1")).
                thenReturn(testStatus[0]);


        GeoPoint geo = ((DocumentSnapshot) documentSnapshotToPass).getGeoPoint("geoLocation");
        String name = ((DocumentSnapshot) documentSnapshotToPass).getString("name");
        String status = ((DocumentSnapshot) documentSnapshotToPass).getString("status1");
        ArrayList<String> photoIds = (ArrayList<String>) ((DocumentSnapshot) documentSnapshotToPass)
                .get("photos");
        String pointId = ((DocumentSnapshot) documentSnapshotToPass).getId();

        String bitmapDescriptor = getDescriptorStatus(status);

        double lat = geo.getLatitude();
        double lng = geo.getLongitude();
        LatLng testLatLng = new LatLng(lat, lng);


        MarkerEntity testMarkerEntity = new MarkerEntity(new MarkerOptions().position(testLatLng)
                .title(name).icon(null), photoIds, pointId);

        mockMVM.setSelectedMarker(testMarkerEntity);
        when(mockMVM.getSelectedMarker()).thenReturn(testMarkerEntity);

        assertNotNull(mockMVM.getSelectedMarker());
        assertEquals(mockMVM.getSelectedMarker(), testMarkerEntity);

    }

    @Test
    public void setSelectedMarker() {

        when(((DocumentSnapshot) documentSnapshotToPass).getGeoPoint("geoLocation")).
                thenReturn(testGeo);
        when(((DocumentSnapshot) documentSnapshotToPass).getString("name")).
                thenReturn(testName);
        when(((DocumentSnapshot) documentSnapshotToPass).getString("status1")).
                thenReturn(testStatus[0]);


        GeoPoint geo = ((DocumentSnapshot) documentSnapshotToPass).getGeoPoint("geoLocation");
        String name = ((DocumentSnapshot) documentSnapshotToPass).getString("name");
        String status = ((DocumentSnapshot) documentSnapshotToPass).getString("status1");
        ArrayList<String> photoIds = (ArrayList<String>) ((DocumentSnapshot) documentSnapshotToPass)
                .get("photos");
        String pointId = ((DocumentSnapshot) documentSnapshotToPass).getId();

        String bitmapDescriptor = getDescriptorStatus(status);

        double lat = geo.getLatitude();
        double lng = geo.getLongitude();
        LatLng testLatLng = new LatLng(lat, lng);


        MarkerEntity testMarkerEntity = new MarkerEntity(new MarkerOptions().position(testLatLng)
                .title(name).icon(null), photoIds, pointId);


        when(mockMVM.getSelectedMarker()).thenReturn(testMarkerEntity);


        assertEquals(mockMVM.getSelectedMarker(), testMarkerEntity);


    }

    @Test
    public void getCompleteAddress() {
        List<Address> testAddressList = new ArrayList<Address>();
        List<Address> testAddressList2 = new ArrayList<Address>();
        Locale testLocale = Locale.FRANCE;
        Address testAddress = new Address(testLocale);
        //testAddressList.add(testAddress);
        testAddressList.add(mockAddress);
        testAddressList.add(mockAddressToFail);
        when(mockAddress.getAddressLine(anyInt())).thenReturn("Rua Passos Manuel 211, 4200-111 Porto");
        when(mockAddressToFail.getAddressLine(anyInt())).thenReturn("Street Test");
        boolean noAddresses = false;
        boolean noAddress = false;
        String strAdd = "";
        String strAdd2 = "";

        try {
            when(mockGeocoder.getFromLocation(anyDouble(), anyDouble(), anyInt())).thenReturn(testAddressList);
            Address returnedAddress = testAddressList.get(0);
            strAdd = returnedAddress.getAddressLine(0).substring(0, returnedAddress.getAddressLine(0).lastIndexOf(","));

            try {
                Address returnedAddressToFail = testAddressList.get(1);
                strAdd2 = returnedAddressToFail.getAddressLine(0).substring(0, returnedAddress.getAddressLine(0).lastIndexOf(","));
            } catch (StringIndexOutOfBoundsException e) {
                noAddress = true;
            }


        } catch (IOException e) {
            noAddresses = true;
        }

        assertEquals(strAdd, "Rua Passos Manuel 211");
        assertEquals("", strAdd2);
        assertTrue(noAddress);
        assertFalse(noAddresses);

    }

    @Test
    public void getMarkers() {


        testPhotoIds.add("testId");

        when(((DocumentSnapshot) documentSnapshotToPass).getGeoPoint("geoLocation")).
                thenReturn(testGeo);
        when(((DocumentSnapshot) documentSnapshotToPass).getString("name")).
                thenReturn(testName);
        when(((DocumentSnapshot) documentSnapshotToPass).getString("status1")).
                thenReturn(testStatus[0]);


        Object document = mockQuerySnapshot;
        assertTrue(document instanceof QuerySnapshot);


        GeoPoint geo = ((DocumentSnapshot) documentSnapshotToPass).getGeoPoint("geoLocation");
        String name = ((DocumentSnapshot) documentSnapshotToPass).getString("name");
        String status = ((DocumentSnapshot) documentSnapshotToPass).getString("status1");
        ArrayList<String> photoIds = (ArrayList<String>) ((DocumentSnapshot) documentSnapshotToPass)
                .get("photos");
        String pointId = ((DocumentSnapshot) documentSnapshotToPass).getId();

        String bitmapDescriptor = getDescriptorStatus(status);

        double lat = geo.getLatitude();
        double lng = geo.getLongitude();
        LatLng testLatLng = new LatLng(lat, lng);


        MarkerEntity testMarkerEntity = new MarkerEntity(new MarkerOptions().position(testLatLng)
                .title(name).icon(null), photoIds, pointId);

        assertNotNull(testMarkerEntity);
        assertEquals(testMarkerEntity.getPhotoIds(), photoIds);
        assertEquals(testMarkerEntity.getMarkerOptions().getPosition(), testLatLng);
        assertEquals(testMarkerEntity.getPointId(), pointId);
        assertEquals(testMarkerEntity.getPhotoIds(), photoIds);
        assertEquals(bitmapDescriptor, "BitmapDescriptorFactory.HUE_ORANGE");

        status = "Declined";
        bitmapDescriptor = getDescriptorStatus(status);

        assertEquals(bitmapDescriptor, "BitmapDescriptorFactory.HUE_ORANGE");

        status = "Not moderated";
        bitmapDescriptor = getDescriptorStatus(status);

        assertEquals(bitmapDescriptor, "BitmapDescriptorFactory.HUE_MAGENTA");

    }

    public String getDescriptorStatus(String status) {

        String bitmapDescriptor = null;
        switch (status) {
            case "Accepted":
                bitmapDescriptor = "BitmapDescriptorFactory.HUE_ORANGE";
                break;
            case "Declined":
                bitmapDescriptor = "BitmapDescriptorFactory.HUE_ORANGE";
                break;
            case "Not moderated":
                bitmapDescriptor = "BitmapDescriptorFactory.HUE_MAGENTA";
                break;
            default:
                break;
        }
        return bitmapDescriptor;
    }

}