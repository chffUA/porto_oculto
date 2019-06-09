package pt.oitoo.portooculto.engine.implementation;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.engine.AdminDao;
import pt.oitoo.portooculto.engine.implementation.generic.GenericImplementation;
import pt.oitoo.portooculto.model.Submission;
import pt.oitoo.portooculto.util.FirebaseUtil;

public class AdminImpl extends GenericImplementation implements AdminDao {

    private CollectionReference pointsRef = FirebaseUtil.getMarkersReference();


    @Override
    public void listSubsmissions() {

        pointsRef
                .whereEqualTo("status", "Not moderated")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Submission> submissions = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            submissions.add(document.toObject(Submission.class));
                        }
                        getListResponseCallback().onSuccess(submissions);
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                        getListResponseCallback().onFailure();
                    }
                });
    }

    @Override
    public void approveSubsmission(Submission building, String approval) {
        OnFailureListener onFailureListener = e ->
                getSingleResponseCallback().onFailure(R.string.alert_add_photo_fail);

        OnSuccessListener onSuccessListener = s ->
                getSingleResponseCallback().onSuccess(0);


        if (building.getUid() != null) {
            FirebaseUtil.updatePointApprovalStatus(building.getUid(), approval)
                    .addOnSuccessListener(onSuccessListener)
                    .addOnFailureListener(onFailureListener);
        }
    }
}
