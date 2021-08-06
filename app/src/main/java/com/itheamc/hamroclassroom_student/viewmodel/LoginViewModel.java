package com.itheamc.hamroclassroom_student.viewmodel;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.itheamc.hamroclassroom_student.models.User;

public class LoginViewModel extends ViewModel {
    private FirebaseUser firebaseUser;
    private User user;

    // Getters and Setters for Firebase User
    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

    // Getters and Setters for user i.e. Teacher
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
