package com.itheamc.hamroclassroom_student.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.itheamc.hamroclassroom_student.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private ActivityLoginBinding loginBinding;
    private NavController navController;
    private NavHostFragment navHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(loginBinding.loginFragmentContainerView.getId());

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
//            NavigationUI.setupActionBarWithNavController(this, navController);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        navController.navigateUp();
        return super.onSupportNavigateUp();
    }
}