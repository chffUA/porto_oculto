package pt.oitoo.portooculto.view.ui.auth;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.databinding.FragmentEmailVerificationBinding;
import pt.oitoo.portooculto.util.AlertMessage;
import pt.oitoo.portooculto.util.FirebaseUtil;
import pt.oitoo.portooculto.util.Navigator;
import pt.oitoo.portooculto.util.ServiceUtil;
import pt.oitoo.portooculto.viewmodel.EmailVerificationViewModel;

public class EmailVerificationFragment extends Fragment {

    private final String TAG = EmailVerificationFragment.class.getSimpleName();

    FragmentEmailVerificationBinding binding;
    EmailVerificationViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(EmailVerificationViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_email_verification, container,false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        viewModel.getResend().observe(this, o -> {
            if (ServiceUtil.isUserConnected(getActivity())) {
                user.reload();

                if (user.isEmailVerified()) {
                    Navigator.toMain(getActivity());
                } else {
                    user.sendEmailVerification()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");
                                    AlertMessage.showToast(getString(R.string.email_was_sent, user.getEmail()), getActivity());
                                } else {
                                    Log.d(TAG, "Email not sent.");
                                    AlertMessage.showToast(getString(R.string.email_fail), getActivity());
                                }
                            });
                }
            } else {
                AlertMessage.showSnackbar(getString(R.string.alert_message_no_connection), binding.clEmailVerification, getActivity(), AlertMessage.NO_CONNECTION);
            }
        });

        viewModel.getVerify().observe(this, o -> {
            if (ServiceUtil.isUserConnected(getActivity())) {
                user.reload();

                if (user.isEmailVerified()) {

                    Map<String, Object> user_map = new HashMap<>();
                    user_map.put("fullName", user.getDisplayName());
                    user_map.put("email", user.getEmail());
                    user_map.put("UUID", user.getUid());
                    user_map.put("profile", "user");

                    FirebaseUtil.getFirestoreReference().collection("users").document(user.getUid())
                            .set(user_map)
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                            .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));

                    Navigator.toMain(getActivity());
                } else {
                    AlertMessage.showToast(getString(R.string.email_not_verified), getActivity());
                }
            } else {
                AlertMessage.showSnackbar(getString(R.string.alert_message_no_connection), binding.clEmailVerification, getActivity(), AlertMessage.NO_CONNECTION);
            }
        });

    }

}
