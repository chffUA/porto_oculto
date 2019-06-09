package pt.oitoo.portooculto.view.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.databinding.DialogWarningBinding;
import pt.oitoo.portooculto.view.BaseDialog;
import pt.oitoo.portooculto.viewmodel.MainViewModel;
import pt.oitoo.portooculto.viewmodel.MapsViewModel;

public class WarningDialogFragment extends BaseDialog {

    private DialogWarningBinding binding;
    private MapsViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MapsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_warning, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getCloseDialog().observe(this, close -> dismiss());
    }
}
