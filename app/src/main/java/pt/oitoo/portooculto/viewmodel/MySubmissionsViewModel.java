package pt.oitoo.portooculto.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.engine.implementation.AdminImpl;
import pt.oitoo.portooculto.engine.implementation.MarkersImpl;
import pt.oitoo.portooculto.engine.implementation.callback.ListResponseCallback;
import pt.oitoo.portooculto.model.Submission;
import pt.oitoo.portooculto.util.FirebaseUtil;
import pt.oitoo.portooculto.util.ServiceUtil;
import pt.oitoo.portooculto.util.SnackbarMessage;
import pt.oitoo.portooculto.view.callback.OnProgressListener;

public class MySubmissionsViewModel extends AndroidViewModel {

    private static final String TAG = MySubmissionsViewModel.class.getSimpleName();

    private MutableLiveData<List<Submission>> submissionsList = new MutableLiveData<>();
    private MarkersImpl markersImpl = new MarkersImpl();
    private SnackbarMessage snackbarMessage = new SnackbarMessage();

    public MutableLiveData<Boolean> goToMain = new MutableLiveData<>();


    private OnProgressListener onProgressListener;
    public void setOnProgressListener(OnProgressListener onProgressListener){
        this.onProgressListener = onProgressListener;
    }


    public MySubmissionsViewModel(@NonNull Application application) {
        super(application);

    }

    public MutableLiveData<List<Submission>> getSubmissionsList() {
        return submissionsList;
    }

    public void getCartList() {
        if (ServiceUtil.isUserConnected(getApplication().getApplicationContext())) {
            onProgressListener.start();
            markersImpl.setListResponseCallback(new ListResponseCallback<Submission>() {


                @Override
                public void onSuccess(List<Submission> list) {
                    onProgressListener.onSuccess();
                    getSubmissionsList().setValue(list);
                }

                @Override
                public void onFailure() {
                    onProgressListener.onFailure(R.string.com_facebook_internet_permission_error_message);
                }

            });
            markersImpl.retrieveMarkersByUser(FirebaseUtil.getCurrentUser().getUid());
        } else {
            snackbarMessage.setValue(R.string.alert_message_no_connection);
        }
    }

    public void toMain(){
        goToMain.setValue(true);
    }
}
