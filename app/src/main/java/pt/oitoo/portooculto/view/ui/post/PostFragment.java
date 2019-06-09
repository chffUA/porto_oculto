package pt.oitoo.portooculto.view.ui.post;

import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.ByteArrayOutputStream;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.databinding.FragmentPostBinding;
import pt.oitoo.portooculto.model.Submission;
import pt.oitoo.portooculto.util.AlertMessage;
import pt.oitoo.portooculto.util.Navigator;
import pt.oitoo.portooculto.view.anim.ButtonProgressAnimation;
import pt.oitoo.portooculto.view.anim.CircularRevealAnimation;
import pt.oitoo.portooculto.view.callback.OnProgressListener;
import pt.oitoo.portooculto.viewmodel.PostViewModel;

import static pt.oitoo.portooculto.BaseConstants.ADDRESS_TO_POST;
import static pt.oitoo.portooculto.BaseConstants.CAMERA_BITMAP_KEY;
import static pt.oitoo.portooculto.BaseConstants.LOCATION_LATITUDE;
import static pt.oitoo.portooculto.BaseConstants.LOCATION_LONGITUDE;
import static pt.oitoo.portooculto.BaseConstants.POINT_ID;
import static pt.oitoo.portooculto.BaseConstants.REPLACE_WITH_ANIM;

public class PostFragment extends Fragment {

    protected ButtonProgressAnimation buttonProgressAnimation;
    protected CircularRevealAnimation circularRevealAnimation;

    private FragmentPostBinding binding;
    private PostViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        buttonProgressAnimation = new ButtonProgressAnimation();
        circularRevealAnimation = new CircularRevealAnimation();

        if (bundle != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(bundle.getByteArray(CAMERA_BITMAP_KEY), 0, bundle.getByteArray(CAMERA_BITMAP_KEY).length);
            Location location = new Location(LocationManager.GPS_PROVIDER);
            location.setLatitude(bundle.getDouble(LOCATION_LATITUDE));
            location.setLongitude(bundle.getDouble(LOCATION_LONGITUDE));


            viewModel = ViewModelProviders.of(this, new PostViewModelFactory(getActivity().getApplication(),
                    bmp,
                    location,
                    bundle.getString(POINT_ID),
                    bundle.getString(ADDRESS_TO_POST)
            )).get(PostViewModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post, container,false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        observers();

        binding.floatingTerrain.setOnClickListener(v -> {
            binding.floatingActionEvaluate.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.oitoo_green)));
            binding.imgBuildingState.setImageResource(R.drawable.ic_terreno);
            binding.txtBuildingState.setText("TERRENO");
            viewModel.getPost().setBuildingStatus("TERRENO");
        });

        binding.floatingDegraded.setOnClickListener(v -> {
            binding.floatingActionEvaluate.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.oitoo_orange)));
            binding.imgBuildingState.setImageResource(R.drawable.ic_degradado);
            binding.txtBuildingState.setText("DEGRADADO");
            viewModel.getPost().setBuildingStatus("DEGRADADO");


        });

        binding.floatingGoodState.setOnClickListener(v -> {
            binding.floatingActionEvaluate.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.oitoo_yellow)));
            binding.imgBuildingState.setImageResource(R.drawable.ic_bom_estado);
            binding.txtBuildingState.setText("BOM ESTADO");
            viewModel.getPost().setBuildingStatus("BOM ESTADO");
        });

        binding.floatingRuin.setOnClickListener(v -> {
            binding.floatingActionEvaluate.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.oitoo_red)));
            binding.imgBuildingState.setImageResource(R.drawable.ic_ruina);
            binding.txtBuildingState.setText("RUÍNA");
            viewModel.getPost().setBuildingStatus("RUÍNA");
        });

        binding.arrowBackButton.buttonCenterMap.setOnClickListener(v -> getActivity().onBackPressed());

        return binding.getRoot();
    }

    private void observers() {
        setPostValidationCallback();
        viewModel.getSnack().observe(this,
                (Observer<Integer>) resourceId ->
                        AlertMessage.showSnackbar(getString(resourceId), binding.coordinatorPostDialog,
                                getActivity(), 0));
        viewModel.goToAddMoreInfo.observe(this, aBoolean -> {
            PostMoreInfoFragment postMoreInfoFragment = new PostMoreInfoFragment();
            Bundle args = new Bundle();
            args.putParcelable("PostDetails",
                    new Submission(viewModel.getPost().getPointId(),
                            binding.edtAddressPost.getText().toString(),
                            binding.txtBuildingState.getText().toString()));

            Bitmap bitmap = ((BitmapDrawable)binding.imgBuilding.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageInByte = baos.toByteArray();
            args.putByteArray(CAMERA_BITMAP_KEY, imageInByte);

            postMoreInfoFragment.setArguments(args);

            Navigator.changeFragment(R.id.main_container,
                    postMoreInfoFragment,
                    getActivity(),
                    false,
                    "", REPLACE_WITH_ANIM
            );
        });

    }

    private void setPostValidationCallback() {

        viewModel.setOnProgressListener(new OnProgressListener() {

            @Override
            public void start() {
                viewModel.getPost().setValidatingFields(true);
                buttonProgressAnimation.setOnAnimationEndListener(() -> viewModel.postBuilding());
                buttonProgressAnimation.startAnimation(binding.frameUpload, binding.txtUpload, getActivity());
            }

            @Override
            public void onSuccess() {
                circularRevealAnimation.setOnAnimationEndListener(() -> success(binding.txtWelcome, binding.animationComplete));
                circularRevealAnimation.startAnimation(binding.reveal, binding.frameUpload, R.dimen.btn_insert_animation, getActivity());
            }

            @Override
            public void onFailure(int message) {
                if (getActivity() != null) {
                    viewModel.getPost().setValidatingFields(false);
                    buttonProgressAnimation.resetAnimation(binding.frameUpload, binding.txtUpload);
                    binding.txtUpload.setText(R.string.signin_retry);
                    AlertMessage.showToast(getString(message), getActivity());
                    final Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(300);
                }
            }
        });
    }

    /*private void showUploadSuccessful() {
        success(binding.txtWelcome, binding.animationComplete);
        final Handler handler = new Handler();
        handler.postDelayed(() ->  Navigator.toMain(getActivity()), 4000);
    }*/

    protected void success(TextView tv, LottieAnimationView animation) {
        tv.animate()
                .alpha(1f)
                .setDuration(300)
                .start();

        animation.playAnimation();
    }

    class PostViewModelFactory implements ViewModelProvider.Factory {

        private Application application;
        private Bitmap cameraBitmap;
        private Location location;
        private String pointId;
        private String address;

        PostViewModelFactory(Application application, Bitmap cameraBitmap, Location location, String pointId, String address) {
            this.application = application;
            this.cameraBitmap = cameraBitmap;
            this.location = location;
            this.pointId = pointId;
            this.address = address;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new PostViewModel(application, cameraBitmap, location, pointId, address);
        }
    }

}
