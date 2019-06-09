package pt.oitoo.portooculto.view.ui.main;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.databinding.ActivityMainBinding;
import pt.oitoo.portooculto.model.Submission;
import pt.oitoo.portooculto.util.AlertMessage;
import pt.oitoo.portooculto.util.Navigator;
import pt.oitoo.portooculto.util.PictureHandler;
import pt.oitoo.portooculto.view.callback.OnBackPressed;
import pt.oitoo.portooculto.view.callback.OnMarkerViewSelectedListener;
import pt.oitoo.portooculto.view.callback.OnSubmissionSelectedListener;
import pt.oitoo.portooculto.view.ui.admin.PostDetailsFragment;
import pt.oitoo.portooculto.view.ui.auth.NoGpsFragment;
import pt.oitoo.portooculto.view.callback.PermissionClickListener;
import pt.oitoo.portooculto.view.ui.post.PostDetailsMarkerViewFragment;
import pt.oitoo.portooculto.view.ui.post.PostFragment;

import static pt.oitoo.portooculto.BaseConstants.ADD;
import static pt.oitoo.portooculto.BaseConstants.CAMERA_BITMAP_KEY;
import static pt.oitoo.portooculto.BaseConstants.REPLACE;
import static pt.oitoo.portooculto.BaseConstants.REPLACE_WITH_ANIM;
import static pt.oitoo.portooculto.BaseConstants.REQUEST_IMAGE_CAPTURE;

public class MainActivity extends AppCompatActivity implements PermissionClickListener, OnSubmissionSelectedListener, OnMarkerViewSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding binding;
    public PictureHandler pictureHandler;
    private String lastMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (savedInstanceState == null) {
            requestGpsPermission();
        }
    }

    private void requestGpsPermission() {

        Fragment f = MainActivity.this.getSupportFragmentManager().findFragmentById(R.id.main_container);

        MultiplePermissionsListener multiplePermissionsListener = new  MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    if (f != null) {
                        Navigator.changeFragment(R.id.main_container, new MainFragment(),MainActivity.this, false, "",REPLACE);
                    } else
                        Navigator.changeFragment(R.id.main_container, new MainFragment(), MainActivity.this, false,"", ADD);
                } else {
                    if (!(f instanceof NoGpsFragment) && f != null) {
                        Navigator.changeFragment(R.id.main_container, new NoGpsFragment(),MainActivity.this, false, "",REPLACE);
                    } else if (f == null) {
                        Navigator.changeFragment(R.id.main_container, new NoGpsFragment(),MainActivity.this, false, "",ADD);
                    }
                    AlertMessage.showSnackbar("You must provide GPS access to start using the app.",
                            binding.coordinatorMain,
                            MainActivity.this,
                            AlertMessage.NO_GPS_ACCESS);
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        };

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(multiplePermissionsListener).check();
    }

    @Override
    public void onClick() {
        requestGpsPermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            //todo: error handling when bmp == null

            //todo: IMPORTANT
            //todo: play around with quality level in PostViewModel#postBuilding and here,
            //todo: a quality level of 100 in both results in a pic of 3-5 MB size being uploaded
            //todo: 50-50 looks decent and creates 700 KB pics
            Bitmap bmp = null;
            try {
                bmp = pictureHandler.createBitmapAfterCameraActivity(this);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d(TAG, String.valueOf(bmp.getWidth()+"x"+bmp.getHeight()));

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] byteArray = stream.toByteArray();

            PostFragment postFragment = new PostFragment();
            Bundle args = new Bundle();
            args.putByteArray(CAMERA_BITMAP_KEY, byteArray);
            args.putAll(getIntent().getExtras());
            postFragment.setArguments(args);
            Navigator.changeFragment(R.id.main_container, postFragment, this, true, "",REPLACE);

        }
    }

    public void setPictureHandler(PictureHandler pictureHandler) {
        this.pictureHandler = pictureHandler;
    }

    @Override
    public void onSubmissionSelected(Submission submission) {
        Intent intent = new Intent(this, PostDetailsFragment.class);
        intent.putExtra("Submission", submission);
        PostDetailsFragment postDetailsFragment = new PostDetailsFragment();
        postDetailsFragment.setArguments(intent.getExtras());
        Navigator.changeFragment(R.id.main_container, postDetailsFragment, this, true, "",REPLACE_WITH_ANIM);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
        if (!(fragment instanceof OnBackPressed) || !((OnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onMarkerViewSelected(Submission submission) {
        Intent intent = new Intent(this, PostDetailsMarkerViewFragment.class);
        intent.putExtra("Submission", submission);
        PostDetailsMarkerViewFragment postDetailsMarkerViewFragment = new PostDetailsMarkerViewFragment();
        postDetailsMarkerViewFragment.setArguments(intent.getExtras());
        Navigator.changeFragment(R.id.main_container, postDetailsMarkerViewFragment, this, true, "",REPLACE_WITH_ANIM);
    }
}