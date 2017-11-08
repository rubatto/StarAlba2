package com.rubatto.staralba;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.User;

public class LoginActiviy extends AppCompatActivity {

    private final String TAG = "LoginActivity";

    private View mProgressView;
    private SignInButton mSignInBt;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions mGoogleSignInOptions;

    private static int GOOGLE_LOGIN_OPEN = 100;

    private FirebaseAuth mAuth;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activiy);

        mProgressView = (ProgressBar) findViewById(R.id.login_progress);
        mSignInBt = (SignInButton) findViewById(R.id.login_google_sign_in_button);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserRef = mFirebaseDatabase.getReference("users");

        // [START config_signin]
        // Configure Google Sign In
        mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        // 실패 시 처리하는 부분
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                .build();

        mSignInBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_LOGIN_OPEN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_LOGIN_OPEN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.e(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete()) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.e(TAG, "signInWithCredential:success");

                                final FirebaseUser firebaseUser = mAuth.getCurrentUser();

                                User user = new User();
                                user.setEmail(firebaseUser.getEmail());
                                user.setName(firebaseUser.getDisplayName());
                                user.setUid(firebaseUser.getUid());
                                if (null != firebaseUser.getPhotoUrl()) {
                                    user.setProfileUrl(firebaseUser.getPhotoUrl().toString());
                                }

                                mUserRef.child(firebaseUser.getUid()).setValue(user, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError == null) {
                                            Bundle eventBundle = new Bundle();
                                            eventBundle.putString("email", firebaseUser.getEmail());
                                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, eventBundle);
                                        }

                                    }
                                });

                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.e(TAG, "signInWithCredential:failure", task.getException());
                                Snackbar.make(mProgressView, "로그인에 실패하였습니다.", Snackbar.LENGTH_LONG).show();
                                //updateUI(null);
                            }
                        }
                    }
                });
    }
}
