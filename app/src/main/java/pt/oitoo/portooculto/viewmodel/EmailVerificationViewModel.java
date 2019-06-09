package pt.oitoo.portooculto.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import pt.oitoo.portooculto.util.SingleLiveEvent;

public class EmailVerificationViewModel extends AndroidViewModel {

    private SingleLiveEvent<Boolean> resend;
    private SingleLiveEvent<Boolean> verify;

    public SingleLiveEvent<Boolean> getResend() {
        return resend;
    }

    public SingleLiveEvent<Boolean> getVerify() {
        return verify;
    }

    public EmailVerificationViewModel(@NonNull Application application) {
        super(application);
        resend  = new SingleLiveEvent();
        verify  = new SingleLiveEvent();
    }


    public void resend(){
        resend.setValue(true);
    }


    public void verify(){
        verify.setValue(true);
    }


}
