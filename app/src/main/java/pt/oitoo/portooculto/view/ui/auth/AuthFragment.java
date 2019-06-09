package pt.oitoo.portooculto.view.ui.auth;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.facebook.CallbackManager;
import com.google.firebase.auth.FirebaseUser;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.databinding.FragmentSigninBinding;
import pt.oitoo.portooculto.model.SignInValidation;
import pt.oitoo.portooculto.util.AlertMessage;
import pt.oitoo.portooculto.util.FirebaseUtil;
import pt.oitoo.portooculto.util.Navigator;
import pt.oitoo.portooculto.view.anim.ButtonProgressAnimation;
import pt.oitoo.portooculto.view.anim.CircularRevealAnimation;
import pt.oitoo.portooculto.view.callback.OnProgressListener;
import pt.oitoo.portooculto.view.ui.main.MainActivity;
import pt.oitoo.portooculto.viewmodel.SignInViewModel;

import static pt.oitoo.portooculto.BaseConstants.REPLACE_WITH_ANIM;



public class AuthFragment extends Fragment {

    protected ButtonProgressAnimation buttonProgressAnimation;
    protected CircularRevealAnimation circularRevealAnimation;

    CallbackManager callbackManager;
    private FragmentSigninBinding binding;
    private SignInViewModel viewModel;

    private final static String TAG = AuthFragment.class.getSimpleName();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(SignInViewModel.class);
        buttonProgressAnimation = new ButtonProgressAnimation();
        circularRevealAnimation = new CircularRevealAnimation();
        setLoginValidationCallback();
        callbackManager = CallbackManager.Factory.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signin, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        observers();
        addHideKeyboardListener(binding.getRoot());
        binding.register.setOnClickListener(v -> Navigator.changeFragment(R.id.auth_container,
                new RegistrationFragment(), getActivity(), true, "",REPLACE_WITH_ANIM));
        binding.forgotPassword.setOnClickListener(v -> Navigator.changeFragment(R.id.auth_container,
                new ForgotPasswordFragment(), getActivity(), true, "",REPLACE_WITH_ANIM));
        binding.loginButton.setReadPermissions("email", "public_profile");
        binding.loginButton.setFragment(this);

        return binding.getRoot();
    }

    public void observers() {
        viewModel.getSnack().observe(this, (Observer<Integer>) resourceId -> AlertMessage.showSnackbar(getString(resourceId), binding.coordinatorAuthDialog, getActivity(), AlertMessage.NO_CONNECTION));
    }

    private void setLoginValidationCallback() {

        viewModel.setOnProgressListener(new OnProgressListener() {

            @Override
            public void start() {
                viewModel.getSignIn().setValidatingFields(true);
                buttonProgressAnimation.setOnAnimationEndListener(() -> viewModel.login());
                buttonProgressAnimation.startAnimation(binding.frameSignin, binding.txtSignin, getActivity());
            }

            @Override
            public void onSuccess() {
                checkEmailVerification();
            }

            @Override
            public void onFailure(int message) {
                if (getActivity() != null) {
                    viewModel.getSignIn().setValidatingFields(false);
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
        final Handler handler = new Handler();

        handler.postDelayed(() -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("profile", viewModel.getProfile());
            getActivity().startActivity(intent);
            getActivity().finish();
        }, 1500);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        viewModel.facebookLogin(callbackManager);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    protected void success(TextView tv, LottieAnimationView animation) {
        tv.animate()
                .alpha(1f)
                .setDuration(300)
                .start();

        animation.playAnimation();
    }

    private void checkEmailVerification() {
        FirebaseUser user = FirebaseUtil.getCurrentUser();


        boolean facebookProvider = user.getProviderData().get(1) != null && user.getProviderData().get(1).getProviderId().contains("facebook.com");
        if (FirebaseUtil.userHasVerifiedEmail() || facebookProvider) {
          transitionToMainActivity();
        } else {
            viewModel.resendVerificationEmail();
            Navigator.changeFragment(R.id.auth_container, new EmailVerificationFragment(), getActivity(), true, "",REPLACE_WITH_ANIM);
        }
    }

    private void transitionToMainActivity() {
        circularRevealAnimation.setOnAnimationEndListener(() -> welcome());
        circularRevealAnimation.startAnimation(binding.reveal, binding.frameSignin, R.dimen.btn_insert_animation, getActivity());
    }

    private void addHideKeyboardListener(View v) {
        v.setOnTouchListener((v1, event) -> {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            if(imm != null && getActivity().getCurrentFocus() != null){
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
            return true;
        });
    }

}
