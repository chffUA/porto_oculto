package pt.oitoo.portooculto.view.ui.auth;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.databinding.FragmentNoGpsAccessBinding;
import pt.oitoo.portooculto.view.callback.PermissionClickListener;
import pt.oitoo.portooculto.viewmodel.NoGpsViewModel;


public class NoGpsFragment extends Fragment {

    private FragmentNoGpsAccessBinding binding;
    private NoGpsViewModel viewModel;

    PermissionClickListener activityCallback;

    private PermissionClickListener clickCallback = () -> activityCallback.onClick();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(NoGpsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_no_gps_access, container, false);
        binding.setLifecycleOwner(this);
        binding.setCallback(clickCallback);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }


    /* Makes sure we are implementing On*/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            clickCallback = (PermissionClickListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnRestaurantClickListener");
        }
    }
}

