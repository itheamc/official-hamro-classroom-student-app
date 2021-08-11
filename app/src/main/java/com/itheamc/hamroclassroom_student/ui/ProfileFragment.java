package com.itheamc.hamroclassroom_student.ui;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.os.HandlerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.itheamc.hamroclassroom_student.callbacks.FirestoreCallbacks;
import com.itheamc.hamroclassroom_student.databinding.FragmentProfileBinding;
import com.itheamc.hamroclassroom_student.handlers.FirestoreHandler;
import com.itheamc.hamroclassroom_student.models.Assignment;
import com.itheamc.hamroclassroom_student.models.Notice;
import com.itheamc.hamroclassroom_student.models.School;
import com.itheamc.hamroclassroom_student.models.Subject;
import com.itheamc.hamroclassroom_student.models.Submission;
import com.itheamc.hamroclassroom_student.models.Teacher;
import com.itheamc.hamroclassroom_student.models.User;
import com.itheamc.hamroclassroom_student.utils.LocalStorage;
import com.itheamc.hamroclassroom_student.utils.NotifyUtils;
import com.itheamc.hamroclassroom_student.utils.ViewUtils;
import com.itheamc.hamroclassroom_student.viewmodel.MainViewModel;

import java.util.List;


public class ProfileFragment extends Fragment implements FirestoreCallbacks {
    private static final String TAG = "ProfileFragment";
    private FragmentProfileBinding profileBinding;
    private MainViewModel viewModel;
    private NavController navController;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        profileBinding = FragmentProfileBinding.inflate(inflater, container, false);
        return profileBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initializing NavController and ViewModel
        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Calling function to pass data
        passDataToBinding(viewModel.getUser());

        // Handling Back Button
        profileBinding.backButton.setOnClickListener(v -> {
            navController.popBackStack();
        });
    }

    // Function to pass data to the dataBinding
    private void passDataToBinding(User user) {
        if (user == null) {
            if (getActivity() != null) {
                FirestoreHandler.getInstance(this).getUser(LocalStorage.getInstance(getActivity()).getUserId());
                ViewUtils.showProgressBar(profileBinding.profileOverlayLayLayout);
            }
            return;
        }

        profileBinding.setUser(user);
        ViewUtils.visibleViews(profileBinding.idCardView);
    }




    /**
     * -------------------------------------------------------------------
     * These are the methods implemented from the FirestoreCallbacks
     * -------------------------------------------------------------------
     */
    @Override
    public void onSuccess(User user, List<School> schools, List<Teacher> teachers, List<Subject> subjects, List<Assignment> assignments, List<Submission> submissions, List<Notice> notices) {
        if (profileBinding == null) return;
        Log.d(TAG, "onSuccess: ");
        if (user != null) {
            viewModel.setUser(user);
            passDataToBinding(user);
        }
    }

    @Override
    public void onFailure(Exception e) {
        Log.d(TAG, "onFailure: ");
        ViewUtils.hideProgressBar(profileBinding.profileOverlayLayLayout);
    }
}