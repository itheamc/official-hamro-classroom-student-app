package com.itheamc.hamroclassroom_student.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.itheamc.hamroclassroom_student.R;
import com.itheamc.hamroclassroom_student.adapters.SubmissionAdapter;
import com.itheamc.hamroclassroom_student.callbacks.FirestoreCallbacks;
import com.itheamc.hamroclassroom_student.callbacks.SubmissionCallbacks;
import com.itheamc.hamroclassroom_student.databinding.FragmentSubmissionsBinding;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SubmissionsFragment extends Fragment implements FirestoreCallbacks, SubmissionCallbacks {
    private static final String TAG = "SubmissionsFragment";
    private FragmentSubmissionsBinding submissionsBinding;
    private NavController navController;
    private MainViewModel viewModel;
    private SubmissionAdapter submissionAdapter;

    /*
    Lists
     */
    private List<String> submissionsIds;
    private List<Submission> listOfSubmissions;
    private int position = 0;


    public SubmissionsFragment() {
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
        submissionsBinding = FragmentSubmissionsBinding.inflate(inflater, container, false);
        return submissionsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initializing NavController and ViewModel
        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Initializing AssignmentAdapter and setting to recyclerview
        submissionAdapter = new SubmissionAdapter(this);
        submissionsBinding.submissionsRecyclerView.setAdapter(submissionAdapter);

        // Initializing filteredSubjects
        submissionsIds = new ArrayList<>();
        listOfSubmissions = new ArrayList<>();


        // Setting swipe and refresh layout
        submissionsBinding.submissionsSwipeRefreshLayout.setOnRefreshListener(() -> {
            listOfSubmissions = new ArrayList<>();
            submissionsIds = new ArrayList<>();
            viewModel.setAllSubmissions(null);
            position = 0;
            ViewUtils.hideViews(submissionsBinding.noSubmissionLayout);
            checksUser();
        });

        // Handling back button
        submissionsBinding.backButton.setOnClickListener(v -> {
            navController.popBackStack();
        });


        // Checking if submissions are already stored in the viewModel or not
        List<Submission> storedSubmissions = viewModel.getAllSubmissions();
        if (storedSubmissions != null && !storedSubmissions.isEmpty()) {
            submissionAdapter.submitList(storedSubmissions);
        } else {
            checksUser();
        }
    }

    /*
    Function to check user first
     */
    private void checksUser() {
        User user = viewModel.getUser();
        if (user == null) {
            if (getActivity() != null) {
                FirestoreHandler.getInstance(this).getUser(LocalStorage.getInstance(getActivity()).getUserId());
                showProgressBar();
            }
            return;
        }

        checksSubmissions();
        showProgressBar();
    }

    /*
    Function to check whether user has any submissions or not
     */
    private void checksSubmissions() {
        User user = viewModel.getUser();
        submissionsIds = user.get_submissions();
        if (submissionsIds == null || submissionsIds.isEmpty()) {
            hideProgressBar();
            return;
        }

        retrieveSubmissions();
    }

    /*
    Function to retrieve submissions
     */
    private void retrieveSubmissions() {
        if (position >= submissionsIds.size()) {
            position = 0;
            hideProgressBar();
            if (listOfSubmissions != null && !listOfSubmissions.isEmpty()) {
                submissionAdapter.submitList(listOfSubmissions);
                viewModel.setAllSubmissions(listOfSubmissions);
                return;
            }

            ViewUtils.visibleViews(submissionsBinding.noSubmissionLayout);
            return;
        }

        String[] ids = submissionsIds.get(position).split("___");
        String subjectId = ids[0];
        String assignmentId = ids[1];
        String userId = viewModel.getUser().get_id();
        FirestoreHandler.getInstance(this).getSubmission(subjectId, assignmentId, userId);
    }

    /*
    Function to hide progressbar
     */
    private void hideProgressBar() {
        ViewUtils.hideProgressBar(submissionsBinding.submissionsOverlayLayLayout);
        ViewUtils.handleRefreshing(submissionsBinding.submissionsSwipeRefreshLayout);
    }

    /*
    Function to show progressbar
     */
    private void showProgressBar() {
        ViewUtils.showProgressBar(submissionsBinding.submissionsOverlayLayLayout);
        ViewUtils.handleRefreshing(submissionsBinding.submissionsSwipeRefreshLayout);
    }


    /**
     * --------------------------------------------------------------------------
     * Function Implemented from the FirestoreCallbacks
     * @param user - an user object got from the database
     * @param teacher - an teacher object got from the database
     * @param school - an school object got from the database
     * @param schools - list of schools got from the database
     * @param subjects - list of subjects got from the database
     * @param assignments - list of assignments got from the database
     * @param submissions - list of submissions got from the database
     * @param notices - list of notices got from the database
     */
    @Override
    public void onSuccess(User user, Teacher teacher, School school, List<School> schools, List<Subject> subjects, List<Assignment> assignments, Submission submissions, List<Notice> notices) {
        if (submissionsBinding == null) return;

        if (user != null) {
            viewModel.setUser(user);
            checksSubmissions();
            return;
        }

        // If submissions is retrieved
        if (submissions != null) {
            listOfSubmissions.add(submissions);
            position += 1;
            retrieveSubmissions();
            return;
        }

        hideProgressBar();
    }

    @Override
    public void onFailure(Exception e) {
        if (submissionsBinding == null) return;
        hideProgressBar();
        if (e.getMessage() == null) return;
        if (e.getMessage().toLowerCase(Locale.ROOT).contains("submission")) {
            ViewUtils.visibleViews(submissionsBinding.noSubmissionLayout);
            return;
        }
        if (getContext() != null) NotifyUtils.showToast(getContext(), e.getMessage());
    }

    /**
     * ---------------------------------------------------------------------------
     * Function implemented from the SubmissionCallbacks
     * @param _position - It is the position of the clicked item in the recyclerview
     */
    @Override
    public void onClick(int _position) {

    }


    // Overriding function to handle the destroy of view

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        submissionsBinding = null;
    }
}