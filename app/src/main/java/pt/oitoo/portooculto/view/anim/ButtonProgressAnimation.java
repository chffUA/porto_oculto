package pt.oitoo.portooculto.view.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.view.callback.OnAnimationEndListener;


public class ButtonProgressAnimation {

    private static int BUTTON_WIDTH;

    private OnAnimationEndListener onAnimationEndListener;

    public void setOnAnimationEndListener(OnAnimationEndListener onAnimationEndListener){
        this.onAnimationEndListener =  onAnimationEndListener;
    }

    public void startAnimation(final FrameLayout button, TextView textView, Context context){
        BUTTON_WIDTH = button.getMeasuredWidth();
        ValueAnimator anim = ValueAnimator.ofInt(BUTTON_WIDTH, (int) context.getResources().getDimension(R.dimen.btn_insert_animation));

        anim.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
            layoutParams.width = val;
            button.requestLayout();
        });

        anim.setDuration(300);
        anim.start();

        textView.animate()
                .alpha(0f)
                .setDuration(250)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        onAnimationEndListener.onComplete();
                    }
                })
                .start();
    }


    public void resetAnimation(final FrameLayout button, TextView textView){

        if(button.getMeasuredWidth() < BUTTON_WIDTH){

            ValueAnimator anim = ValueAnimator.ofInt(button.getMeasuredWidth(), BUTTON_WIDTH);

            anim.addUpdateListener(valueAnimator -> {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
                layoutParams.width = val;
                button.requestLayout();
            });

            anim.setDuration(300);
            anim.start();

            textView.animate()
                    .alpha(1f)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                        }
                    })
                    .start();

        }

    }
}
