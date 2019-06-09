package pt.oitoo.portooculto.view.ui.post;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.databinding.FragmentPostdetailsBinding;
import pt.oitoo.portooculto.databinding.FragmentPostdetailsmarkerviewBinding;
import pt.oitoo.portooculto.model.Submission;
import pt.oitoo.portooculto.util.AlertMessage;
import pt.oitoo.portooculto.util.Navigator;
import pt.oitoo.portooculto.view.anim.ButtonProgressAnimation;
import pt.oitoo.portooculto.view.anim.CircularRevealAnimation;
import pt.oitoo.portooculto.view.callback.OnProgressListener;
import pt.oitoo.portooculto.view.ui.admin.PostDetailsFragment;
import pt.oitoo.portooculto.viewmodel.PostDetailsViewModel;

import static pt.oitoo.portooculto.BaseConstants.CAMERA_BITMAP_KEY;
import static pt.oitoo.portooculto.BaseConstants.REPLACE_WITH_ANIM;

public class PostDetailsMarkerViewFragment extends Fragment {

    protected ButtonProgressAnimation buttonProgressAnimation;
    protected CircularRevealAnimation circularRevealAnimation;

    private FragmentPostdetailsmarkerviewBinding binding;
    private PostDetailsViewModel viewModel;
    private Context context;
    private String approvalResult;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        buttonProgressAnimation = new ButtonProgressAnimation();
        circularRevealAnimation = new CircularRevealAnimation();

        if (bundle != null) {
            viewModel = ViewModelProviders.of(this, new PostViewModelFactory(getActivity().getApplication(),
                    Objects.requireNonNull(bundle.getParcelable("Submission")))).get(PostDetailsViewModel.class);
        } else {

        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_postdetailsmarkerview, container,false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        binding.arrowBackButton.buttonCenterMap.setOnClickListener(v -> ((FragmentActivity) context).onBackPressed());

        binding.tabs.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab != null) {
                    tabHandler(tab.getPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab != null) {
                    tabHandler(tab.getPosition());
                }
            }
        });

        setApprovalValidationCallback();

        binding.arrowBackButton.buttonCenterMap.setOnClickListener(v -> getActivity().onBackPressed());

        return binding.getRoot();
    }


    private void tabHandler(int tabPosition) {
        switch (tabPosition) {
            case 0:
                PostMoreInfoFragment postMoreInfoFragment = new PostMoreInfoFragment();
                Bundle args = new Bundle();
                args.putParcelable("PostDetails",
                        viewModel.getSubmission());

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
                break;
            case 1:
            default:
                approvalResult = "Rejected";
                break;
        }

        //viewModel.startApproval();
    }

    private void setApprovalValidationCallback() {

        viewModel.setOnProgressListener(new OnProgressListener() {

            @Override
            public void start() {
                viewModel.approve(approvalResult);
            }

            @Override
            public void onSuccess() {
                circularRevealAnimation.setOnAnimationEndListener(() -> showUploadSuccessful());
                circularRevealAnimation.startAnimation(binding.reveal, binding.frameUpload, R.dimen.btn_insert_animation, getActivity());
            }

            @Override
            public void onFailure(int message) {
                if (getActivity() != null) {
                    AlertMessage.showToast(getString(message), getActivity());
                    final Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(300);
                }
            }
        });
    }


    private void showUploadSuccessful() {
        binding.txtWelcome.setText( getResources().getString(R.string.submission_has_been) + approvalResult.toLowerCase() + "!");
        success(binding.txtWelcome, binding.animationComplete);
        final Handler handler = new Handler();
        handler.postDelayed(() -> Navigator.toMain(getActivity()), 1500);
    }

    protected void success(TextView tv, LottieAnimationView animation) {
        tv.animate()
                .alpha(1f)
                .setDuration(300)
                .start();

        animation.playAnimation();
    }

    class PostViewModelFactory implements ViewModelProvider.Factory {

        private Application application;
        private Submission submission;

        PostViewModelFactory(Application application, Submission submission) {
            this.application = application;
            this.submission = submission;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new PostDetailsViewModel(application, submission);
        }
    }

}
