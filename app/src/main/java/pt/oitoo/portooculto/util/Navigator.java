package pt.oitoo.portooculto.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import pt.oitoo.portooculto.BaseConstants;
import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.view.ui.auth.AuthActivity;
import pt.oitoo.portooculto.view.ui.main.MainActivity;

import static pt.oitoo.portooculto.BaseConstants.REMOVE;

public class Navigator {

    public static void changeFragment(int container,
                                      android.support.v4.app.Fragment fragment,
                                      FragmentActivity fragmentActivity,
                                      boolean addToBackStack,
                                      String tag,
                                      int mode){
        FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        switch(mode){
            case BaseConstants.ADD:
                transaction.add(container, fragment);
                if(addToBackStack){
                    transaction.addToBackStack(fragment.getClass().getSimpleName());
                }
                break;
            case BaseConstants.REPLACE_WITH_ANIM:
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.replace(container, fragment);
                if(addToBackStack){
                    transaction.addToBackStack(fragment.getClass().getSimpleName());
                }
                break;
            case BaseConstants.REPLACE_WITH_ANIM_RIGHT:
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
                transaction.replace(container, fragment);
                if(addToBackStack) {
                    transaction.addToBackStack(fragment.getClass().getSimpleName());
                }
                break;
            case BaseConstants.REPLACE:
                transaction.replace(container, fragment);
                if(addToBackStack){
                    transaction.addToBackStack(fragment.getClass().getSimpleName());
                }
                break;
            case REMOVE:
                transaction.remove(fragment);
                break;
            default:
                break;
        }

        transaction.commitAllowingStateLoss();

    }

    public static void removeFragment(android.support.v4.app.Fragment fragment,
                                     FragmentActivity fragmentActivity){
        FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        transaction.remove(fragment).commitAllowingStateLoss();

    }

    public static void toMain(Context context){
            context.startActivity(new Intent(context, MainActivity.class));
            ((Activity) context).finish();
    }

    public static void toAuth(Context context){
        context.startActivity(new Intent(context, AuthActivity.class));
        ((Activity) context).finish();
    }

}
