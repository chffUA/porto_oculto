package pt.oitoo.portooculto.view.ui.auth;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.databinding.FragmentRegistrationBinding;
import pt.oitoo.portooculto.util.AlertMessage;
import pt.oitoo.portooculto.util.Navigator;
import pt.oitoo.portooculto.view.anim.ButtonProgressAnimation;
import pt.oitoo.portooculto.view.anim.CircularRevealAnimation;
import pt.oitoo.portooculto.view.callback.OnProgressListener;
import pt.oitoo.portooculto.viewmodel.RegistrationViewModel;

public class RegistrationFragment extends Fragment {

    protected ButtonProgressAnimation buttonProgressAnimation;
    protected CircularRevealAnimation circularRevealAnimation;

    private FragmentRegistrationBinding binding;
    private RegistrationViewModel viewModel;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(RegistrationViewModel.class);
        buttonProgressAnimation = new ButtonProgressAnimation();
        circularRevealAnimation = new CircularRevealAnimation();
        setLoginValidationCallback();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false);
        binding.setViewModel(viewModel);
        binding.arrowBackButton.buttonCenterMap.setOnClickListener(v -> getActivity().onBackPressed());
        binding.setLifecycleOwner(this);
        observers();

        return binding.getRoot();
    }

    private void setLoginValidationCallback() {

        viewModel.setOnProgressListener(new OnProgressListener() {

            @Override
            public void start() {
                viewModel.getReg().setValidatingFields(true);
                buttonProgressAnimation.setOnAnimationEndListener(() -> viewModel.registerAuthEngine());
                buttonProgressAnimation.startAnimation(binding.frameSignin, binding.txtSignin, getActivity());
            }

            @Override
            public void onSuccess() {
                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                circularRevealAnimation.setOnAnimationEndListener(() -> welcome());
                circularRevealAnimation.startAnimation(binding.reveal, binding.frameSignin, R.dimen.btn_insert_animation, getActivity());
            }

            @Override
            public void onFailure(int message) {
                if (getActivity() != null) {
                    viewModel.getReg().setValidatingFields(false);
                    buttonProgressAnimation.resetAnimation(binding.frameSignin, binding.txtSignin);
                    binding.txtSignin.setText(R.string.signin_retry);
                    AlertMessage.showToast(getString(message), getActivity());
                    final Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(300);
                }
            }
        });
    }

    private void welcome() {
        success(binding.txtWelcome, binding.animationComplete);
        AlertMessage.showToast(getString(R.string.verification_email_dialog_msg), getActivity());
        final Handler handler = new Handler();
        handler.postDelayed(() -> Navigator.toAuth(getActivity()), 1500);
    }

    protected void success(TextView tv, LottieAnimationView animation) {
        tv.animate()
                .alpha(1f)
                .setDuration(300)
                .start();

        animation.playAnimation();
    }

    public void observers() {

        viewModel.getSnack().observe(this,
                (Observer<Integer>) resourceId ->
                        AlertMessage.showSnackbar(getString(resourceId), binding.coordinatorRegDialog,
                                getActivity(), 0));
    }
}
