package pt.oitoo.portooculto.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;


import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.engine.implementation.SubmissionImpl;
import pt.oitoo.portooculto.engine.implementation.callback.SingleResponseCallback;
import pt.oitoo.portooculto.model.Submission;
import pt.oitoo.portooculto.util.ServiceUtil;
import pt.oitoo.portooculto.util.SnackbarMessage;
import pt.oitoo.portooculto.view.callback.OnProgressListener;

public class PostMoreInfoViewModel extends AndroidViewModel {

    private Submission submission;

    private SnackbarMessage snackbarMessage = new SnackbarMessage();
    private MutableLiveData<Bitmap> bitmap = new MutableLiveData<>();

    private SubmissionImpl submissionImpl = new SubmissionImpl();

    public SnackbarMessage getSnack() {
        if (snackbarMessage == null){
            snackbarMessage = new SnackbarMessage();
        }
        return snackbarMessage;
    }

    public PostMoreInfoViewModel(@NonNull Application application, Submission submission, Bitmap bitmap) {
        super(application);
        this.submission = submission;
        this.bitmap.setValue(bitmap);

    }

    private OnProgressListener onProgressListener;
    public void setOnProgressListener(OnProgressListener onProgressListener){
        this.onProgressListener = onProgressListener;
    }

    public void startPublishing(){
        if (ServiceUtil.isUserConnected(getApplication().getApplicationContext())) {

            submissionImpl.setCallback(new SingleResponseCallback() {

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


    public MutableLiveData<Bitmap> getBitmap() {
        return bitmap;
    }

    public void setBitmap(MutableLiveData<Bitmap> bitmap) {
        this.bitmap = bitmap;
    }

    public Submission getSubmission() {
        return submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public void addMoreInfo(){
        submissionImpl.addDetailedInfo(submission);
    }

}
