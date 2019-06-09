package pt.oitoo.portooculto.view.ui.auth;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import pt.oitoo.portooculto.BaseConstants;
import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.databinding.FragmentTermsconditionsBinding;
import pt.oitoo.portooculto.util.Navigator;
import pt.oitoo.portooculto.util.SharedPreference;

import static pt.oitoo.portooculto.BaseConstants.REPLACE_WITH_ANIM;

public class TermsConditionsFragment extends Fragment {

    private FragmentTermsconditionsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_termsconditions, container, false);
        observers();
        return binding.getRoot();
    }

    public void observers() {

        binding.tabs.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab != null){
                    tabHandler(tab.getPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if(tab != null){
                    tabHandler(tab.getPosition());
                }
            }
        });
    }

    private void tabHandler(int tabPosition){
        switch(tabPosition){
            case 0:
                SharedPreference.saveStringSharedPreference(getActivity(), BaseConstants.SETTINGS_FIRST_RUN, "1");
                Navigator.changeFragment(R.id.auth_container, new AuthFragment(), getActivity(), false, "",REPLACE_WITH_ANIM);
                break;
            case 1:
            default:
                Objects.requireNonNull(getActivity()).finish();
                break;
        }

    }
}
