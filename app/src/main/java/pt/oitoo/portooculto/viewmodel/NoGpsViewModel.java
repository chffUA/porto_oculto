package pt.oitoo.portooculto.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import pt.oitoo.portooculto.util.SingleLiveEvent;

public class NoGpsViewModel extends AndroidViewModel {

    public NoGpsViewModel(@NonNull Application application) {
        super(application);
    }
}
