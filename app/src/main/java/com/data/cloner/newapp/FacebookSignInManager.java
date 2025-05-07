package com.data.cloner.newapp;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FacebookSignInManager {
    private static FacebookSignInManager instance=null;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private Context context;
    private Callback callback;
    private FacebookSignInManager(){

    }

    public static FacebookSignInManager getInstance(Context context){
        if(instance == null){
            instance = new FacebookSignInManager();
        }
        instance.init(context);
        return instance;
    }
    private void init(Context context){
        this.context=context;
        this.callback=(Callback) context;
    }

    public void setLoginButton(LoginButton loginButton){
        loginButton.setReadPermissions("email","public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(@NonNull FacebookException e) {

            }
        });
    }

    public void SetupFacebookAuth(){
        mCallbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();

        AccessTokenTracker accessTokenTracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(@Nullable AccessToken accessToken, @Nullable AccessToken accessToken1) {
                if(accessToken1 == null){
                    callback.updateUI();
                    FirebaseAuth.getInstance().signOut();

                }else {

                }
            }
        };
        accessTokenTracker.startTracking();
    }
    private void handleFacebookAccessToken(AccessToken token){
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    getProfile(user);
                }else {
                    Toast.makeText(context, "Authentication Failes", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
 void getProfile(FirebaseUser user){
        callback.getProfile(user);
 }
 boolean isUserAlreadySignedIn(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            Toast.makeText(context, "already signed in", Toast.LENGTH_SHORT).show();
            return true;
        }else {
            return false;
        }
 }
    CallbackManager getmCallbackManager(){return mCallbackManager;}

    public interface Callback {
        void getProfile(FirebaseUser user);
        void updateUI();
    }
}

