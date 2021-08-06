package com.itheamc.hamroclassroom_student.callbacks;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;

public interface LoginCallbacks {
    void onLoginSuccess(@NonNull FirebaseUser user);
    void onLoginFailure(@NonNull String errorMessage);
}
