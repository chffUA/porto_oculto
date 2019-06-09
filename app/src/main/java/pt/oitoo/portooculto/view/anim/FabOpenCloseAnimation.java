package pt.oitoo.portooculto.view.anim;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import java.util.ArrayList;

import pt.oitoo.portooculto.R;

public class FabOpenCloseAnimation {

    private Animation fabOpenAnimation;
    private Animation fabCloseAnimation;
    private boolean isFabMenuOpen;

    public FabOpenCloseAnimation(Context context) {
            fabOpenAnimation = AnimationUtils.loadAnimation(context, R.anim.fab_open);
            fabCloseAnimation = AnimationUtils.loadAnimation(context, R.anim.fab_close);
            isFabMenuOpen = false;
    }

    public void toogleFabMenu(final ArrayList<FloatingActionButton> fabs, final ArrayList<LinearLayout> wrappers) {

    /*    if(!isFabMenuOpen) {
            ViewCompat.animate(fabs.get(0)).rotation(45.0F).withLayer().setDuration(300).setInterpolator(new OvershootInterpolator(10.0F)).start();
        } else {
            ViewCompat.animate(fabs.get(0)).rotation(0.0F).withLayer().setDuration(300).setInterpolator(new OvershootInterpolator(10.0F)).start();
        }*/

        for(LinearLayout wrapper : wrappers){
            wrapper.startAnimation(!isFabMenuOpen ? fabOpenAnimation : fabCloseAnimation);
        }

        //We skip the first fab, because it's our main one and we don't want it to stop being clickable
        for(int i = 1; i < fabs.size(); i++){
            fabs.get(i).setClickable(!isFabMenuOpen);
        }

        isFabMenuOpen = !isFabMenuOpen;

    }

}
