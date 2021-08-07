package com.itheamc.hamroclassroom_student.ui;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.os.HandlerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.UploadTask;
import com.itheamc.hamroclassroom_student.R;
import com.itheamc.hamroclassroom_student.adapters.ImageAdapter;
import com.itheamc.hamroclassroom_student.adapters.SliderAdapter;
import com.itheamc.hamroclassroom_student.callbacks.FirestoreCallbacks;
import com.itheamc.hamroclassroom_student.callbacks.ImageCallbacks;
import com.itheamc.hamroclassroom_student.callbacks.StorageCallbacks;
import com.itheamc.hamroclassroom_student.databinding.FragmentSubmissionBinding;
import com.itheamc.hamroclassroom_student.handlers.FirestoreHandler;
import com.itheamc.hamroclassroom_student.handlers.StorageHandler;
import com.itheamc.hamroclassroom_student.models.Assignment;
import com.itheamc.hamroclassroom_student.models.Notice;
import com.itheamc.hamroclassroom_student.models.School;
import com.itheamc.hamroclassroom_student.models.Subject;
import com.itheamc.hamroclassroom_student.models.Submission;
import com.itheamc.hamroclassroom_student.models.Teacher;
import com.itheamc.hamroclassroom_student.models.User;
import com.itheamc.hamroclassroom_student.utils.IdGenerator;
import com.itheamc.hamroclassroom_student.utils.ImageUtils;
import com.itheamc.hamroclassroom_student.utils.NotifyUtils;
import com.itheamc.hamroclassroom_student.utils.ViewUtils;
import com.itheamc.hamroclassroom_student.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SubmissionFragment extends Fragment {
    private static final String TAG = "SubmissionFragment";
    private FragmentSubmissionBinding submissionBinding;
    private MainViewModel viewModel;
    private NavController navController;
    private SliderAdapter sliderAdapter;


    // Constructor
    public SubmissionFragment() {
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
        submissionBinding = FragmentSubmissionBinding.inflate(inflater, container, false);
        return submissionBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initializing NavController and ViewModel
        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);


        // Initializing slider adapter...
        sliderAdapter = new SliderAdapter();
        submissionBinding.submissionViewPager.setAdapter(sliderAdapter);

        Submission submission = viewModel.getSubmission();
        if (submission != null) {
            submissionBinding.setSubmission(submission);
            if (submission.get_images().size() > 0) sliderAdapter.submitList(submission.get_images());
        }
    }

    /*
    Overrided function to view destroy
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        submissionBinding = null;
    }
}