package pt.oitoo.portooculto.view.ui.main;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import pt.oitoo.portooculto.R;

import pt.oitoo.portooculto.databinding.FragmentMainBinding;
import pt.oitoo.portooculto.databinding.NavHeaderMainBinding;
import pt.oitoo.portooculto.util.AlertMessage;
import pt.oitoo.portooculto.util.FirebaseUtil;
import pt.oitoo.portooculto.util.Navigator;
import pt.oitoo.portooculto.util.SharedPreference;
import pt.oitoo.portooculto.view.ui.admin.ModerationFragment;
import pt.oitoo.portooculto.view.ui.user.MyPinsFragment;
import pt.oitoo.portooculto.viewmodel.MainViewModel;

import static pt.oitoo.portooculto.BaseConstants.ADD;
import static pt.oitoo.portooculto.BaseConstants.ADMIN_PROFILE;
import static pt.oitoo.portooculto.BaseConstants.REPLACE;

public class MainFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener{

    private FragmentMainBinding binding;
    private NavHeaderMainBinding navBinding;

    private MainViewModel viewModel;

    private float lastTranslate = 0.0f;

    private FragmentActivity context;

    private String profile = "";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        navBinding = DataBindingUtil.bind(binding.navView.getHeaderView(0));
        binding.setViewModel(viewModel);
        profile = getActivity().getIntent().getStringExtra("profile");
        if((profile != null && !profile.equals("admin")) || !SharedPreference.readStringSharedPreference(getActivity(), ADMIN_PROFILE, "user").equals("admin")){
                binding.navView.getMenu().removeItem(R.id.nav_admin);
        }

        if (savedInstanceState == null) { //display intro fragment
            Navigator.changeFragment(R.id.navigation_container, new MapsFragment(), getActivity(), false, "",ADD);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(),
                binding.drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

                @SuppressLint("NewApi")
                public void onDrawerSlide(View drawerView, float slideOffset)
                {
                    super.onDrawerSlide(drawerView, slideOffset);
                    float moveFactor = (binding.navView.getWidth() * slideOffset);

                    if(slideOffset == 1){
                        binding.drawerOpenClose.imgDrawerMenu.setImageResource(R.drawable.ic_close);
                    }  else if (slideOffset == 0) {
                        binding.drawerOpenClose.imgDrawerMenu.setImageResource(R.drawable.ic_menu);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    {
                        binding.drawerOpenClose.drawerOpenClose.setTranslationX(-moveFactor);
                    }
                    else
                    {
                        TranslateAnimation anim = new TranslateAnimation(lastTranslate, -moveFactor, 0.0f, 0.0f);
                        anim.setDuration(0);
                        anim.setFillAfter(true);
                        binding.drawerOpenClose.drawerOpenClose.startAnimation(anim);

                        lastTranslate = -moveFactor;
                    }

                }
            };
        binding.drawerLayout.addDrawerListener(toggle);


        binding.navLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            if(FirebaseAuth.getInstance().getCurrentUser() == null){
                Navigator.toAuth(context);
            } else {
                AlertMessage.showSnackbar("There was a problem on sign out, please try again.", binding.coordinatorFragmentMain, context, -1);
            }
        });

        binding.drawerOpenClose.drawerOpenClose.setOnClickListener(v -> {
            if (binding.drawerLayout.isDrawerOpen(Gravity.END)) {
                binding.drawerLayout.closeDrawer(Gravity.END);
                binding.drawerOpenClose.imgDrawerMenu.setImageResource(R.drawable.ic_menu);
            } else {
                binding.drawerLayout.openDrawer(Gravity.END);
                binding.drawerOpenClose.imgDrawerMenu.setImageResource(R.drawable.ic_close);
            }
        });

        toggle.syncState();
        binding.navView.setNavigationItemSelectedListener(this);
        return binding.getRoot();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && item.getItemId() == android.R.id.home) {
            if (binding.drawerLayout.isDrawerOpen(Gravity.END)) {
                binding.drawerLayout.closeDrawer(Gravity.END);
            } else {
                binding.drawerLayout.openDrawer(Gravity.END);
            }
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_admin) {
            Navigator.changeFragment(R.id.navigation_container, new ModerationFragment(), context, false, "",REPLACE);
        } else if (id == R.id.nav_map) {
            Navigator.changeFragment(R.id.navigation_container, new MapsFragment(), context, false, "",REPLACE);
        } else if (id == R.id.nav_mypins) {
            Navigator.changeFragment(R.id.navigation_container, new MyPinsFragment(), context, false, "",REPLACE);
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            if(FirebaseAuth.getInstance().getCurrentUser() == null){
                Navigator.toAuth(context);
            } else {
                AlertMessage.showSnackbar("There was a problem on sign out, please try again.", binding.coordinatorFragmentMain, context, -1);
            }
		} else if (id == R.id.nav_login) {
        } else if (id == R.id.nav_settings) {
        }
        binding.drawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }


}
