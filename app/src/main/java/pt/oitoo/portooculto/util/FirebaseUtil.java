package pt.oitoo.portooculto.util;

import android.location.Location;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.type.Date;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import pt.oitoo.portooculto.model.Post;
import pt.oitoo.portooculto.model.Submission;

public class FirebaseUtil {

    private static FirebaseFirestore firestore;
    private static FirebaseAuth firebaseAuth;
    private static StorageReference storage;

    public static FirebaseFirestore getFirestoreReference(){
        if( firestore == null ){
            firestore = FirebaseFirestore.getInstance();
        }
        return firestore;
    }

    public static FirebaseAuth getFirebaseAuth() {
        if(firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

    public static StorageReference getStorageReference() {
        if (storage == null) {
            storage = FirebaseStorage.getInstance().getReference();
        }
        return storage;
    }

    public static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static boolean userHasVerifiedEmail() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return false;
        return FirebaseAuth.getInstance().getCurrentUser().isEmailVerified();
    }

    public static CollectionReference getMarkersReference() {
        return getFirestoreReference().collection("points_po");
    }
    public static CollectionReference getUsersRef() {
        return getFirestoreReference().collection("users");
    }

    public static StorageReference getPhotoRef(String name) {
        if (name == null) {
            name = UUID.randomUUID().toString() + ".png";
        }
        return getStorageReference().child("Taken_Photos").child(name);
    }

    public static Task<Void> addPhotoToPoint(String pointId, String photoName) {
        return FirebaseUtil.getFirestoreReference().collection("points_po").document(pointId)
                .update("photos",  FieldValue.arrayUnion(photoName),
                        "modified", FieldValue.serverTimestamp());

    }

    public static Task<Void> updatePointApprovalStatus(String pointId, String approval) {
        return FirebaseUtil.getFirestoreReference().collection("points_po").document(pointId)
                .update("status",  approval);
    }

    public static Task<Void> addMoreInfo(Submission submission) {

        Map<String, Object> moreInfo = new HashMap<>();
        moreInfo.put("wishes", submission.getWishes());
        moreInfo.put("year", submission.getYear());

        return FirebaseUtil.getFirestoreReference().collection("points_po")
                .document(submission.getUid())
                .update(moreInfo);

    }

    public static Task<Void> createPoint(Post postData, String photoName, String uid) {

        Map<String, Object> post = new HashMap<>();
        post.put("geoLocation", new GeoPoint(postData.getLocation().getLatitude(), postData.getLocation().getLongitude()));
        post.put("uid", uid);
        post.put("status", "Not moderated");
        post.put("addressString", postData.getAddress());
        post.put("buildingStatus", postData.getBuildingStatus());
        post.put("uploaderName", getCurrentUser().getDisplayName());
        post.put("uploaderEmail", getCurrentUser().getEmail());
        post.put("uploaderUUID", getCurrentUser().getUid());
        post.put("photos", Arrays.asList(photoName));
        post.put("created", FieldValue.serverTimestamp());


        return FirebaseUtil.getFirestoreReference().collection("points_po")
                .document(uid).set(post);
    }

}
