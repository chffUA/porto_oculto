package pt.oitoo.portooculto.view.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.Log;
import android.view.ViewAnimationUtils;
import android.widget.FrameLayout;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.view.callback.OnAnimationEndListener;

import static android.view.View.VISIBLE;

public class CircularRevealAnimation {

    private OnAnimationEndListener onAnimationEndListener;

    public void setOnAnimationEndListener(OnAnimationEndListener onAnimationEndListener){
        this.onAnimationEndListener =  onAnimationEndListener;
    }

    public void startAnimation(FrameLayout view, FrameLayout button, int resDimension, Context context){

            button.setElevation(0f);
            view.setVisibility(VISIBLE);

            int cx = view.getWidth();
            int cy = view.getHeight();

            int [] location = new int[2];
            button.getLocationOnScreen(location);
            int x = location[0] + 25;
            int y = location[1];
            float finalRadius = Math.max(cx, cy) * 1.2f;

            Animator reveal = ViewAnimationUtils
                    .createCircularReveal(view, x, y, (int) context.getResources().getDimension(resDimension), finalRadius);

            reveal.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            onAnimationEndListener.onComplete();
                    }
            });
            reveal.setDuration(400);
            reveal.start();
        }

}
