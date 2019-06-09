package pt.oitoo.portooculto.engine.implementation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.engine.SignInDao;
import pt.oitoo.portooculto.engine.implementation.generic.GenericImplementation;
import pt.oitoo.portooculto.model.Registration;
import pt.oitoo.portooculto.model.SignIn;
import pt.oitoo.portooculto.model.SignInValidation;
import pt.oitoo.portooculto.util.FirebaseUtil;

public class SignInImpl extends GenericImplementation implements SignInDao {

    private static final String TAG = SignInImpl.class.getSimpleName();
    private FirebaseAuth mAuth;
    Context context;

    private CollectionReference usersRef = FirebaseUtil.getUsersRef();

    public SignInImpl(Context context) {
        this.context = context;

    }

    @Override
    public void signIn(SignIn signIn) {
        getMAuth().signInWithEmailAndPassword(signIn.getEmail(), signIn.getPassword())
                .addOnSuccessListener(authResult -> {
                    insertUserData();
                })
                .addOnFailureListener(e -> getSingleResponseCallback().onFailure(R.string.alert_wrong_credentials));
    }

    @Override
    public void insertUserData() {
        FirebaseUser user = FirebaseUtil.getCurrentUser();
        if(user != null){
            Map<String, Object> user_map = new HashMap<>();
            user_map.put("email", user.getEmail());
            user_map.put("UUID", user.getUid());
            user_map.put("fullName", user.getDisplayName());

            FirebaseUtil.getFirestoreReference().collection("users").document(user.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "Document exists!");
                            FirebaseUtil.getFirestoreReference().collection("users").document(user.getUid())
                                    .update(user_map)
                                    .addOnSuccessListener(aVoid -> {
                                        usersRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(taskInsert -> {
                                            if (taskInsert.isSuccessful()) {
                                                Log.d(TAG, "User data inserted successfully into Firestore");
                                                getSingleResponseCallback().onSuccess(document.get("profile"));
                                            } else {
                                                getSingleResponseCallback().onFailure(-1);
                                            }
                                        });
                                    })
                                    .addOnFailureListener(e -> Log.w(TAG, "Error writing document with user data to Firestore", e));
                        } else {
                            user_map.put("profile", "user");
                            FirebaseUtil.getFirestoreReference().collection("users").document(user.getUid())
                                    .set(user_map)
                                    .addOnSuccessListener(aVoid -> {
                                        usersRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(taskInsert -> {
                                            if (taskInsert.isSuccessful()) {
                                                Log.d(TAG, "User data inserted successfully into Firestore");
                                                getSingleResponseCallback().onSuccess(taskInsert.getResult().get("profile"));
                                            } else {
                                                getSingleResponseCallback().onFailure(-1);
                                            }
                                        });
                                    })
                                    .addOnFailureListener(e -> Log.w(TAG, "Error writing document with user data to Firestore", e));
                        }
                    } else {
                        Log.d(TAG, "Failed with: ", task.getException());
                    }
                }
            });

/*
            FirebaseUtil.getFirestoreReference().collection("users").document(user.getUid())
                    .set(user_map)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "User data inserted successfully into Firestore"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error writing document with user data to Firestore", e));*/
        }

    }

    @Override
    public void insertFacebookUserData() {

        FirebaseUser user = FirebaseUtil.getCurrentUser();
        if(user != null) {
            Map<String, Object> user_map = new HashMap<>();
            user_map.put("email", user.getEmail());
            user_map.put("UUID", user.getUid());
            user_map.put("fullName", user.getDisplayName());


            FirebaseUtil.getFirestoreReference().collection("users").document(user.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "Document exists!");
                            FirebaseUtil.getFirestoreReference().collection("users").document(user.getUid())
                                    .update(user_map)
                                    .addOnSuccessListener(aVoid -> {
                                        usersRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(taskInsert -> {
                                            if (taskInsert.isSuccessful()) {
                                                Log.d(TAG, "User data inserted successfully into Firestore");
                                                getSingleResponseCallback().onSuccess(document.get("profile"));
                                            } else {
                                                getSingleResponseCallback().onFailure(-1);
                                            }
                                        });
                                    })
                                    .addOnFailureListener(e -> Log.w(TAG, "Error writing document with user data to Firestore", e));
                        } else {
                            user_map.put("profile", "user");
                            FirebaseUtil.getFirestoreReference().collection("users").document(user.getUid())
                                    .set(user_map)
                                    .addOnSuccessListener(aVoid -> {
                                        usersRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(taskInsert -> {
                                            if (taskInsert.isSuccessful()) {
                                                Log.d(TAG, "User data inserted successfully into Firestore");
                                                getSingleResponseCallback().onSuccess(taskInsert.getResult().get("profile"));
                                            } else {
                                                getSingleResponseCallback().onFailure(-1);
                                            }
                                        });
                                    })
                                    .addOnFailureListener(e -> Log.w(TAG, "Error writing document with user data to Firestore", e));
                        }
                    } else {
                        Log.d(TAG, "Failed with: ", task.getException());
                    }
                }
            });
        }
    }

    @Override
    public void resendVerificationEmail() {
        FirebaseUtil.getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email sent.");
                    }
                    else{
                        Log.d(TAG, "Email not sent.");
                    }
                });
    }

    @Override
    public void register(Registration reg) {
        getMAuth().createUserWithEmailAndPassword(reg.getEmail(), reg.getPassword())
                .addOnSuccessListener( task -> {
                    FirebaseUser user = getMAuth().getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(reg.getUsername())
                            .build();
                    user.updateProfile(profileUpdates);
                    getSingleResponseCallback().onSuccess(user);
                })
                .addOnFailureListener(e -> {
                    if (e instanceof FirebaseAuthWeakPasswordException) {
                        getSingleResponseCallback().onFailure(R.string.alert_message_weak_password);
                    } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        getSingleResponseCallback().onFailure(R.string.alert_message_invalid_email);
                    } else if (e instanceof FirebaseAuthUserCollisionException) {
                        getSingleResponseCallback().onFailure(R.string.alert_message_already_exists);
                    } else {
                        getSingleResponseCallback().onFailure(R.string.alert_message_registration);
                    }
                });
    }

    @Override
    public void forgotPassword(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email sent.");
                    }
                });

    }

        @Override
    public void facebookLogin(CallbackManager callbackManager) {

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d(TAG, "facebook:onError", null);
                    }
                });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {

                    insertFacebookUserData();

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "signInWithCredential:failure" + e.toString());
            }
        });

    }


    private FirebaseAuth getMAuth() {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }
        return mAuth;
    }
}