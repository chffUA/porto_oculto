package pt.oitoo.portooculto.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.engine.implementation.SubmissionImpl;
import pt.oitoo.portooculto.engine.implementation.callback.SingleResponseCallback;
import pt.oitoo.portooculto.model.Post;
import pt.oitoo.portooculto.util.FirebaseUtil;
import pt.oitoo.portooculto.util.ServiceUtil;
import pt.oitoo.portooculto.util.SingleLiveEvent;
import pt.oitoo.portooculto.util.SnackbarMessage;
import pt.oitoo.portooculto.view.callback.OnProgressListener;

public class PostViewModel extends AndroidViewModel {

    private SnackbarMessage snackbarMessage = new SnackbarMessage();
    private SubmissionImpl submissionImpl = new SubmissionImpl();

    public SingleLiveEvent<Boolean> goToAddMoreInfo = new SingleLiveEvent<>();

    public SnackbarMessage getSnack() {
        if (snackbarMessage == null){
            snackbarMessage = new SnackbarMessage();
        }
        return snackbarMessage;
    }

    public Post getPost() {
        if(post == null){
            post = new Post(null, false);
        }
        return post;
    }

    private Post post;

    private OnProgressListener onProgressListener;
    public void setOnProgressListener(OnProgressListener onProgressListener){
        this.onProgressListener = onProgressListener;
    }


    public PostViewModel(@NonNull Application application, Bitmap bitmap, Location location, String pointId, String address) {
        super(application);
        getPost().setCameraBitmap(bitmap);
        getPost().setLocation(location);
        getPost().setPointId(pointId);
        getPost().setAddress(address);
    }

    public void startPublishing(){
        if (ServiceUtil.isUserConnected(getApplication().getApplicationContext())) {
            onProgressListener.start();
            submissionImpl.setCallback(new SingleResponseCallback<Post>() {

                @Override
                public void onSuccess(Post post) {
                    PostViewModel.this.post.setPointId(post.getPointId());
                    onProgressListener.onSuccess();
                }

                @Override
                public void onFailure(int message) {
                    onProgressListener.onFailure(message);
                }
            });
        } else {
            getSnack().setValue(R.string.alert_message_no_connection);
        }
    }

    public void goToAddMoreInfo(){
        goToAddMoreInfo.setValue(true);
    }



    public void postBuilding(){
        submissionImpl.uploadNewBuilding(getPost());
    }
}
