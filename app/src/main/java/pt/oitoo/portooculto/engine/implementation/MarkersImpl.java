package pt.oitoo.portooculto.engine.implementation;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import pt.oitoo.portooculto.engine.MarkersDao;
import pt.oitoo.portooculto.engine.implementation.generic.GenericImplementation;
import pt.oitoo.portooculto.model.Submission;
import pt.oitoo.portooculto.util.FirebaseUtil;

public class MarkersImpl extends GenericImplementation implements MarkersDao {

    private CollectionReference pointsRef = FirebaseUtil.getMarkersReference();

    @Override
    public void retrieveMarkers() {
        pointsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                getSingleResponseCallback().onSuccess(task.getResult());
            } else {
                getSingleResponseCallback().onFailure(-1);
            }
        });
    }

    @Override
    public void retrieveMarkersByUser(String uid) {

        pointsRef
                .whereEqualTo("uploaderUUID", uid)
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
}
