package pt.oitoo.portooculto.viewmodel;

import android.Manifest;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.engine.implementation.MarkersImpl;
import pt.oitoo.portooculto.engine.implementation.callback.SingleResponseCallback;
import pt.oitoo.portooculto.model.Submission;
import pt.oitoo.portooculto.util.FirebaseUtil;
import pt.oitoo.portooculto.util.MarkerEntity;
import pt.oitoo.portooculto.util.SharedPreference;
import pt.oitoo.portooculto.util.SingleLiveEvent;

import static pt.oitoo.portooculto.BaseConstants.ADMIN_PROFILE;

public class MapsViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private MarkersImpl mapsEngine = new MarkersImpl();

    private SingleLiveEvent<Boolean> cityNotAllowed = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> closeDialog = new SingleLiveEvent<>();
    private SingleLiveEvent<Location> sendLocation = new SingleLiveEvent<>();
    private SingleLiveEvent<MarkerEntity> newMarker = new SingleLiveEvent<>();
    private Location userCurrentLocation;

    public SingleLiveEvent<Location> getSendLocation() { return sendLocation; }
    public SingleLiveEvent getCityNotAllowed() { return cityNotAllowed; }
    public SingleLiveEvent<Boolean> getCloseDialog() { return closeDialog; }
    public SingleLiveEvent<MarkerEntity> getNewMarker() { return newMarker; }

    public Location getUserCurrentLocation() {
        return userCurrentLocation;
    }

    private FusedLocationProviderClient mFusedLocationClient;

    private MarkerEntity selectedMarker = null;

    public Geocoder gcd = new Geocoder(getApplication().getApplicationContext(), Locale.getDefault());

    public MapsViewModel(@NonNull Application application) {
        super(application);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(application.getApplicationContext());
        getLocation();
    }

    public MarkerEntity getSelectedMarker() {
        return selectedMarker;
    }

    public void setSelectedMarker(MarkerEntity selectedMarker) {
        this.selectedMarker = selectedMarker;
    }

    public String getCompleteAddress(double latitude, double longitude) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getApplication().getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                strAdd = returnedAddress.getAddressLine(0).substring(0 , returnedAddress.getAddressLine(0).lastIndexOf(","));
            } else {
                Log.d(TAG, "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Canont get Address!");
        }
        return strAdd;
    }

    public String getCompleteAddressWithCity(double latitude, double longitude) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getApplication().getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                strAdd = returnedAddress.getAddressLine(0);
            } else {
                Log.d(TAG, "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Canont get Address!");
        }
        return strAdd;
    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(getApplication().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplication().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        userCurrentLocation = location;
                        List<Address> addresses = null;
                        try {
                            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (addresses != null && !addresses.isEmpty()) {
                            sendLocation.setValue(location);
                            if(addresses.get(0).getAdminArea() != null && !addresses.get(0).getAdminArea().equalsIgnoreCase("porto") && !addresses.get(0).getLocality().equalsIgnoreCase("porto")){
                                cityNotAllowed.setValue(true);
                            } else {
                                cityNotAllowed.setValue(false);
                            }
                        } else {
                            Log.d(TAG, "Address came out null.");
                            getLocation();
                        }
                    } else {
                        Log.d(TAG, "Address came out null.");
                        getLocation();
                    }
                });
    }

    public void getMarkers(){
        mapsEngine.setCallback(new SingleResponseCallback() {
            @Override
            public void onSuccess(Object document) {
                if(document instanceof QuerySnapshot){
                    for(Object documentSnapshot : (QuerySnapshot) document){
                        GeoPoint geo = ((DocumentSnapshot) documentSnapshot).getGeoPoint("geoLocation");
                        String name = ((DocumentSnapshot) documentSnapshot).getString("name");
                        String buildingStatus = ((DocumentSnapshot) documentSnapshot).getString("buildingStatus");
                        String status = ((DocumentSnapshot) documentSnapshot).getString("status");
                        String uploaderUUID = ((DocumentSnapshot) documentSnapshot).getString("uploaderUUID");
                        ArrayList<String> photoIds = (ArrayList<String>)((DocumentSnapshot) documentSnapshot).get("photos");
                        String pointId = ((DocumentSnapshot) documentSnapshot).getId();
                        BitmapDescriptor bitmapDescriptor = null;
                        Submission submission = getSubmission((DocumentSnapshot) documentSnapshot);
                       if((uploaderUUID != null && uploaderUUID.equals(FirebaseUtil.getCurrentUser().getUid()))
                               || SharedPreference.readStringSharedPreference(getApplication().getApplicationContext(), ADMIN_PROFILE, "user").equals("admin")
                                && status != null && !status.equals("Rejected")){

                           if(status != null && status.equals("Not moderated")){
                               bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_gray);
                           } else if(buildingStatus != null){
                               switch (buildingStatus){
                                   case "DEGRADED":
                                   case "DEGRADADO":
                                       bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                                       break;
                                   case "RUINS":
                                   case "RUÍNA":
                                       bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                                       break;
                                   case "TERRAIN":
                                   case "TERRENO":
                                       bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                                       break;
                                   case "GOOD CONDITION":
                                   case "BOM ESTADO":
                                       bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                                       break;
                                   default:
                                       bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);
                                       break;
                               }
                           }

                           if(geo != null){
                               double lat = geo.getLatitude();
                               double lng = geo.getLongitude();

                               LatLng latLng = new LatLng(lat, lng);
                               MarkerEntity markerEntity = new MarkerEntity(
                                       new MarkerOptions().position(latLng).title(name).icon(bitmapDescriptor),
                                       photoIds, pointId, submission);
                               addMarker(markerEntity);
                           }

                       }

                    }
                }
            }

            @Override
            public void onFailure(int message) {

            }
        });

        mapsEngine.retrieveMarkers();
    }

    private Submission getSubmission(DocumentSnapshot documentSnapshot) {

        int year = 0;
        if(documentSnapshot.getLong("year") != null){
            year =  documentSnapshot.getLong("year").intValue();
        }

        return new Submission(documentSnapshot.getId(),
                documentSnapshot.getString("name"),
                documentSnapshot.getString("status"),
                documentSnapshot.getGeoPoint("geoLocation"),
                documentSnapshot.getString("addressString"),
                documentSnapshot.getString("ownership"),
                documentSnapshot.getString("description"),
                year,
                documentSnapshot.getString("wishes"),
                documentSnapshot.getString("type"),
                documentSnapshot.getString("uploaderName"),
                documentSnapshot.getString("uploaderEmail"),
                documentSnapshot.getString("uploaderUUID"),
                (ArrayList<String>) documentSnapshot.get("photos"),
                documentSnapshot.getString("buildingStatus"),
                false);

    }

    private void addMarker(MarkerEntity marker) {
        newMarker.setValue(marker);
    }


    // method definition
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
}
