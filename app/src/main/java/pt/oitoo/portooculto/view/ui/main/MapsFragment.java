package pt.oitoo.portooculto.view.ui.main;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.List;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.databinding.FragmentMapsBinding;
import pt.oitoo.portooculto.util.AlertMessage;
import pt.oitoo.portooculto.util.FirebaseUtil;
import pt.oitoo.portooculto.util.MarkerEntity;
import pt.oitoo.portooculto.util.Navigator;
import pt.oitoo.portooculto.util.PictureHandler;
import pt.oitoo.portooculto.view.callback.OnMarkerViewSelectedListener;
import pt.oitoo.portooculto.view.callback.OnSubmissionSelectedListener;
import pt.oitoo.portooculto.view.custom.LockedBottomSheetBehavior;
import pt.oitoo.portooculto.view.ui.admin.PostDetailsFragment;
import pt.oitoo.portooculto.viewmodel.MapsViewModel;

import static pt.oitoo.portooculto.BaseConstants.ADDRESS_TO_POST;
import static pt.oitoo.portooculto.BaseConstants.LOCATION_LATITUDE;
import static pt.oitoo.portooculto.BaseConstants.LOCATION_LONGITUDE;
import static pt.oitoo.portooculto.BaseConstants.PHOTO_IDS;
import static pt.oitoo.portooculto.BaseConstants.POINT_ID;
import static pt.oitoo.portooculto.BaseConstants.REPLACE_WITH_ANIM;

public class MapsFragment extends Fragment implements  OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener {

    private OnMarkerViewSelectedListener activityCallback;
    private OnMarkerViewSelectedListener submissionClickListener = submission -> activityCallback.onMarkerViewSelected(submission);

    private FragmentMapsBinding binding;
    private MapsViewModel viewModel;

    private LockedBottomSheetBehavior bottomSheetBehavior;

    private Location location;

    private Marker draggableMarker;
    private BitmapDescriptor draggableMarkerDescriptor;

    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;

    private PictureHandler pictureHandler;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MapsInitializer.initialize(context);
        try {
            activityCallback = (OnMarkerViewSelectedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnRestaurantSelectedListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MapsViewModel.class);
        Bitmap draggableMarkerBitmap = getBitmapFromVectorDrawable(getContext(),R.drawable.ic_draggable_pin_orange);
        draggableMarkerDescriptor = BitmapDescriptorFactory.fromBitmap(draggableMarkerBitmap);
        pictureHandler = new PictureHandler();
        ((MainActivity) getActivity()).setPictureHandler(pictureHandler);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_maps, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        bottomSheetBehavior = LockedBottomSheetBehavior.from(binding.bottomSheet.bottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new LockedBottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        binding.buttonCamera.setOnClickListener(v -> positionMarker());
//        binding.addPhoto.setOnClickListener(v -> requestCameraPermission());
        binding.buttonCenterMap.setOnClickListener(v -> getMyLocation());

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        observers();
    }

    @Override
    public void onMapClick(LatLng latLng) {
       /* binding.markerView.setVisibility(View.INVISIBLE);
        cleanMarkerPreview();*/
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
      /*  binding.markerView.setVisibility(View.VISIBLE);
        cleanMarkerPreview();*/
        if(!(marker.getTag() instanceof Integer)){
            MarkerEntity selectedMarker = (MarkerEntity) marker.getTag();
            viewModel.setSelectedMarker(selectedMarker);
            submissionClickListener.onMarkerViewSelected(selectedMarker.getSubmission());
        }

        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        binding.bottomSheet.edtAddress.setText(viewModel.getCompleteAddress(marker.getPosition().latitude, marker.getPosition().longitude));
        getActivity().getIntent().putExtra(LOCATION_LATITUDE, marker.getPosition().latitude);
        getActivity().getIntent().putExtra(LOCATION_LONGITUDE, marker.getPosition().longitude);
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        binding.bottomSheet.edtAddress.setText(viewModel.getCompleteAddress(marker.getPosition().latitude, marker.getPosition().longitude));
        getActivity().getIntent().putExtra(LOCATION_LATITUDE, marker.getPosition().latitude);
        getActivity().getIntent().putExtra(LOCATION_LONGITUDE, marker.getPosition().longitude);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng porto = new LatLng(location.getLatitude(), location.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder().target(porto).zoom(14.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        this.googleMap.moveCamera(cameraUpdate);
        viewModel.getMarkers();
        this.googleMap.setOnMarkerClickListener(this);
        this.googleMap.setOnMarkerDragListener(this);
        this.googleMap.setOnMapClickListener(this);
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable =  AppCompatResources.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(200,
                200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private void positionMarker(){

        if(draggableMarker == null){
        LatLng latLng = new LatLng(viewModel.getUserCurrentLocation().getLatitude(),
                viewModel.getUserCurrentLocation().getLongitude());
            draggableMarker = googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Your position!")
                    .draggable(true)
                    .icon(draggableMarkerDescriptor));
            draggableMarker.setTag(0);
            binding.imgPin.setImageResource(R.drawable.ic_confirmation);
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                binding.bottomSheet.edtAddress.setText(viewModel.getCompleteAddress(latLng.latitude, latLng.longitude));
                getActivity().getIntent().putExtra(LOCATION_LATITUDE, draggableMarker.getPosition().latitude);
                getActivity().getIntent().putExtra(LOCATION_LONGITUDE, draggableMarker.getPosition().longitude);

            }
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            binding.imgPin.setImageResource(R.drawable.ic_draggable_pin);
            List<Address> addresses = null;
            try {
                addresses = viewModel.gcd.getFromLocation(draggableMarker.getPosition().latitude, draggableMarker.getPosition().longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(addresses.get(0).getAdminArea() != null &&
                    !addresses.get(0).getAdminArea().equalsIgnoreCase("porto") &&
                    !addresses.get(0).getLocality().equalsIgnoreCase("porto")){
                draggableMarker.remove();
                draggableMarker = null;
                new WarningDialogFragment().show(getFragmentManager(), "WarningDialogFragment");

            } else {
                requestCameraPermission();
            }
        }

    }

    private void observers() {
        viewModel.getCityNotAllowed().observe(this, o -> {
            if((boolean) o){
                new WarningDialogFragment().show(getFragmentManager(), "WarningDialogFragment");
            }
        });

        viewModel.getSendLocation().observe(this, location -> {
            if (mapFragment != null && location != null) {
                this.location = location;
                mapFragment.getMapAsync(this);
            }
        });

        viewModel.getNewMarker().observe(this, marker -> {
            if (googleMap != null) {
                googleMap.addMarker(marker.getMarkerOptions()).setTag(marker);
            }
        });
    }

    private void requestCameraPermission() {

        MultiplePermissionsListener multiplePermissionsListener = new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    if (viewModel.getSelectedMarker() != null) {
                        // update existing point
                        getActivity().getIntent().putExtra(PHOTO_IDS, viewModel.getSelectedMarker().getPhotoIds().toString());
                        getActivity().getIntent().putExtra(POINT_ID, viewModel.getSelectedMarker().getPointId());
                        getActivity().getIntent().putExtra(ADDRESS_TO_POST, ((EditText) getActivity().findViewById(R.id.edt_address)).getText().toString());
                    } else {
                        // create new point
                        getActivity().getIntent().putExtra(ADDRESS_TO_POST, ((EditText) getActivity().findViewById(R.id.edt_address)).getText().toString());
                    }
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        pictureHandler.startCameraActivity(getActivity());
                    }
                } else {
                    AlertMessage.showSnackbar("You must provide camera access to take pictures with the app.",
                            binding.coordinatorFragmentMain,
                            getActivity(),
                            AlertMessage.NO_CAMERA_ACCESS);
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        };

        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(multiplePermissionsListener).check();

    }

   /* private void cleanMarkerPreview() {
        viewModel.setSelectedMarker(null);
        binding.markerTitle.setText(null);
        binding.mainImage.setImageBitmap(null);
        binding.prevImage.setImageBitmap(null);
        binding.nextImage.setImageBitmap(null);
    }*/

    private void getMyLocation() {
        LatLng latLng = new LatLng(viewModel.getUserCurrentLocation().getLatitude(),viewModel.getUserCurrentLocation().getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        if(googleMap != null){
            googleMap.animateCamera(cameraUpdate);
        }
    }

    private void setImage(List<String> photoIds, ImageView image, int id) {
        if(photoIds.get(id) != null) {
            Task<byte[]> imageDownloadTask = FirebaseUtil.getPhotoRef(photoIds.get(id)).getBytes(50 * 1024 * 1024);
            imageDownloadTask.addOnCompleteListener(task -> {
                byte[] imageBytes = task.getResult();
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                image.setImageBitmap(imageBitmap);
            });
        }
    }


}
