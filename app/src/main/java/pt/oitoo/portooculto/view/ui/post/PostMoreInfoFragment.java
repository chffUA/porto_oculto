package pt.oitoo.portooculto.view.ui.post;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.databinding.FragmentPostMoreinfoBinding;
import pt.oitoo.portooculto.engine.implementation.SubmissionImpl;
import pt.oitoo.portooculto.model.Submission;
import pt.oitoo.portooculto.util.AlertMessage;
import pt.oitoo.portooculto.util.Navigator;
import pt.oitoo.portooculto.view.anim.ButtonProgressAnimation;
import pt.oitoo.portooculto.view.anim.CircularRevealAnimation;
import pt.oitoo.portooculto.view.callback.OnBackPressed;
import pt.oitoo.portooculto.view.callback.OnProgressListener;
import pt.oitoo.portooculto.viewmodel.PostMoreInfoViewModel;

import static pt.oitoo.portooculto.BaseConstants.CAMERA_BITMAP_KEY;
import static pt.oitoo.portooculto.BaseConstants.REPLACE_WITH_ANIM;

public class PostMoreInfoFragment extends Fragment implements OnBackPressed {

    private PostMoreInfoViewModel viewModel;
    private FragmentPostMoreinfoBinding binding;

    protected ButtonProgressAnimation buttonProgressAnimation;
    protected CircularRevealAnimation circularRevealAnimation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        buttonProgressAnimation = new ButtonProgressAnimation();
        circularRevealAnimation = new CircularRevealAnimation();

        if (bundle != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(bundle.getByteArray(CAMERA_BITMAP_KEY), 0, bundle.getByteArray(CAMERA_BITMAP_KEY).length);
            viewModel = ViewModelProviders.of(this, new PostMoreInfoViewModelFactory(getActivity().getApplication(),
                    bundle.getParcelable("PostDetails"),
                    bmp
            )).get(PostMoreInfoViewModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_moreinfo, container,false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.floatingActionEvaluate.setBackgroundTintList(retrieveTerrainColor());
        binding.arrowBackButton.buttonCenterMap.setOnClickListener(v -> onBackPressed());
        setMoreInfoPostingValidationCallback();
        return binding.getRoot();
    }

    private ColorStateList retrieveTerrainColor() {

        String buildingStatus = viewModel.getSubmission().getBuildingStatus();
        int color = R.color.transparent;
        if(buildingStatus != null)
        switch(buildingStatus){
            case "DEGRADED":
            case "DEGRADADO":
                color = R.color.oitoo_orange;
                break;
            case "RUINS":
            case "RUÍNA":
                color = R.color.oitoo_red;
                break;
            case "TERRAIN":
            case "TERRENO":
                color = R.color.oitoo_green;
                break;
            case "GOOD CONDITION":
            case "BOM ESTADO":
                color = R.color.oitoo_yellow;
                break;
            default:
                break;
        }

        return ColorStateList.valueOf(getResources().getColor(color));
    }

    private void setMoreInfoPostingValidationCallback() {

        viewModel.setOnProgressListener(new OnProgressListener() {

            @Override
            public void start() {
                viewModel.getSubmission().setValidatingFields(true);
                buttonProgressAnimation.setOnAnimationEndListener(() -> viewModel.addMoreInfo());
                buttonProgressAnimation.startAnimation(binding.frameUpload, binding.txtUpload, getActivity());
            }

            @Override
            public void onSuccess() {
                circularRevealAnimation.setOnAnimationEndListener(() -> showUploadSuccessful());
                circularRevealAnimation.startAnimation(binding.reveal, binding.frameUpload, R.dimen.btn_insert_animation, getActivity());
            }

            @Override
            public void onFailure(int message) {
                if (getActivity() != null) {
                    viewModel.getSubmission().setValidatingFields(false);
                    buttonProgressAnimation.resetAnimation(binding.frameUpload, binding.txtUpload);
                    binding.txtUpload.setText(R.string.signin_retry);
                    AlertMessage.showToast(getString(message), getActivity());
                    final Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(300);
                }
            }
        });
    }

    protected void success(TextView tv, LottieAnimationView animation) {
        tv.animate()
                .alpha(1f)
                .setDuration(300)
                .start();

        animation.playAnimation();
    }

    private void showUploadSuccessful() {
        success(binding.txtWelcome, binding.animationComplete);
        final Handler handler = new Handler();
        handler.postDelayed(() ->  Navigator.toMain(getActivity()), 1500);
    }

    @Override
    public boolean onBackPressed() {
        Navigator.removeFragment(this, getActivity());
        return false;
    }

    class PostMoreInfoViewModelFactory implements ViewModelProvider.Factory {

        private Application application;
        private Submission submission;
        private Bitmap bitmap;

        PostMoreInfoViewModelFactory(Application application, Submission submission, Bitmap bitmap) {
            this.application = application;
            this.submission = submission;
            this.bitmap = bitmap;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new PostMoreInfoViewModel(application, submission, bitmap);
        }
    }

}
