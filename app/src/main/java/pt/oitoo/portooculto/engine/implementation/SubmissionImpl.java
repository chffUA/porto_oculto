package pt.oitoo.portooculto.engine.implementation;

import android.graphics.Bitmap;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.engine.SubmissionsDao;
import pt.oitoo.portooculto.engine.implementation.generic.GenericImplementation;
import pt.oitoo.portooculto.model.Post;
import pt.oitoo.portooculto.model.Submission;
import pt.oitoo.portooculto.util.FirebaseUtil;

public class SubmissionImpl extends GenericImplementation implements SubmissionsDao {

    @Override
    public void uploadNewBuilding(Post post) {
        StorageReference photosRef = FirebaseUtil.getPhotoRef(null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        post.getCameraBitmap().compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = photosRef.putBytes(data);
        OnFailureListener onFailureListener = e ->
                getSingleResponseCallback().onFailure(R.string.alert_add_photo_fail);
        OnSuccessListener onSuccessListener = s ->
                getSingleResponseCallback().onSuccess(post);


        uploadTask.addOnSuccessListener(taskSnapshot -> {
            if (post.getPointId() != null) {
                FirebaseUtil.addPhotoToPoint(post.getPointId(), photosRef.getName())
                        .addOnSuccessListener(onSuccessListener)
                        .addOnFailureListener(onFailureListener);
            } else {
                String uid = UUID.randomUUID().toString();
                post.setPointId(uid);
                FirebaseUtil.createPoint(post, photosRef.getName(), uid)
                        .addOnSuccessListener(onSuccessListener)
                        .addOnFailureListener(onFailureListener);
            }
        }).addOnFailureListener(onFailureListener);

    }

    @Override
    public void addDetailedInfo(Submission submission) {

        OnFailureListener onFailureListener = e ->
                getSingleResponseCallback().onFailure(R.string.alert_add_photo_fail);

        OnSuccessListener onSuccessListener = s ->
                getSingleResponseCallback().onSuccess(0);


        if (submission.getUid() != null) {
            FirebaseUtil.addMoreInfo(submission)
                    .addOnSuccessListener(onSuccessListener)
                    .addOnFailureListener(onFailureListener);
        }
    }

}
