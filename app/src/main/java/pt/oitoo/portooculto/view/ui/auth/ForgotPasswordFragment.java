package pt.oitoo.portooculto.view.ui.auth;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.databinding.FragmentForgotPasswordBinding;
import pt.oitoo.portooculto.viewmodel.ForgotPasswordViewModel;

public class ForgotPasswordFragment extends Fragment {

    private ForgotPasswordViewModel viewModel;
    private FragmentForgotPasswordBinding binding;

    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ForgotPasswordViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container, false);
        binding.setViewModel(viewModel);
        binding.arrowBackButton.buttonCenterMap.setOnClickListener(v -> getActivity().onBackPressed());
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }
}
