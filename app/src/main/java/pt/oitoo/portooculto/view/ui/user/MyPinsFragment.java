package pt.oitoo.portooculto.view.ui.user;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.databinding.FragmentMyPinsBinding;
import pt.oitoo.portooculto.util.AlertMessage;
import pt.oitoo.portooculto.view.adapter.MySubmissionsAdapter;
import pt.oitoo.portooculto.view.callback.OnProgressListener;
import pt.oitoo.portooculto.viewmodel.MySubmissionsViewModel;

public class MyPinsFragment extends Fragment {

    private MySubmissionsViewModel viewModel;
    private FragmentMyPinsBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MySubmissionsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_pins, container, false);

        MySubmissionsAdapter adapter = new MySubmissionsAdapter(getActivity());
        viewModel.getSubmissionsList().observe(this, submissionsList -> {
            binding.emptyCart.setVisibility(submissionsList != null && submissionsList.isEmpty() ? View.VISIBLE : View.INVISIBLE);
            adapter.setSubmissionsList(submissionsList);
        });
        binding.recyclerItems.addItemDecoration(new DividerItemDecoration(binding.recyclerItems.getContext(), DividerItemDecoration.VERTICAL));
        binding.recyclerItems.setAdapter(adapter);

        configRecyclerView();
        setSubmissionsListProgress();

        return binding.getRoot();

    }

    private void configRecyclerView() {
        binding.recyclerItems.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_fall_down);
        binding.recyclerItems.setLayoutAnimation(animation);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getCartList();
    }

    private void setSubmissionsListProgress() {
        viewModel.setOnProgressListener(new OnProgressListener() {

            @Override
            public void start() {
                binding.progressbarCart.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess() {
                binding.progressbarCart.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(int message) {
                if(getActivity() != null){
                    binding.progressbarCart.setVisibility(View.INVISIBLE);
                    AlertMessage.showToast(getString(message), getActivity());
                    final Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(300);
                }
            }
        });
    }
}
