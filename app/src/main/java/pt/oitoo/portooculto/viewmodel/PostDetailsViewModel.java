package pt.oitoo.portooculto.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.engine.implementation.AdminImpl;
import pt.oitoo.portooculto.engine.implementation.callback.SingleResponseCallback;
import pt.oitoo.portooculto.model.Submission;
import pt.oitoo.portooculto.util.ServiceUtil;
import pt.oitoo.portooculto.util.SnackbarMessage;
import pt.oitoo.portooculto.view.callback.OnProgressListener;

public class PostDetailsViewModel extends AndroidViewModel {

    private AdminImpl adminImpl = new AdminImpl();
    private SnackbarMessage snackbarMessage = new SnackbarMessage();

    private Submission submission;

    public SnackbarMessage getSnack() {
        if (snackbarMessage == null){
            snackbarMessage = new SnackbarMessage();
        }
        return snackbarMessage;
    }

    private OnProgressListener onProgressListener;
    public void setOnProgressListener(OnProgressListener onProgressListener){
        this.onProgressListener = onProgressListener;
    }

    public PostDetailsViewModel(@NonNull Application application, Submission submission) {
        super(application);
        this.submission = submission;
    }

    public Submission getSubmission() {
        return submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public void startApproval(){
        if (ServiceUtil.isUserConnected(getApplication().getApplicationContext())) {
            adminImpl.setCallback(new SingleResponseCallback() {

                @Override
                public void onSuccess(Object o) {
                    onProgressListener.onSuccess();
                }

                @Override
                public void onFailure(int message) {
                    onProgressListener.onFailure(message);
                }
            });
            onProgressListener.start();
        } else {
            getSnack().setValue(R.string.alert_message_no_connection);
        }
    }

    public void approve(String approval){
        adminImpl.approveSubsmission(submission, approval);
    }


}
