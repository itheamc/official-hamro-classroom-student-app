package com.itheamc.hamroclassroom_student.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.itheamc.hamroclassroom_student.R;
import com.itheamc.hamroclassroom_student.adapters.ClassesAdapter;
import com.itheamc.hamroclassroom_student.callbacks.FirestoreCallbacks;
import com.itheamc.hamroclassroom_student.callbacks.SubjectCallbacks;
import com.itheamc.hamroclassroom_student.databinding.FragmentClassesBinding;
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


public class ClassesFragment extends Fragment implements SubjectCallbacks, FirestoreCallbacks {
    private static final String TAG = "ClassesFragment";
    private FragmentClassesBinding classesBinding;
    private NavController navController;
    private MainViewModel viewModel;
    private ClassesAdapter classesAdapter;
    private List<Subject> rawSubjectsList;
    private List<Subject> subjectList;
    private int position;



    public ClassesFragment() {
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
        classesBinding = FragmentClassesBinding.inflate(inflater, container, false);
        return classesBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initializing NavController and ViewModel
        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Initialize Arraylist
        rawSubjectsList = new ArrayList<>();
        subjectList = new ArrayList<>();

        classesAdapter = new ClassesAdapter(this);
        classesBinding.recyclerView.setAdapter(classesAdapter);

        // Calling function to checks and load data
        checksUser();

        // Handling back button icon
        classesBinding.backButton.setOnClickListener(v -> {
            navController.popBackStack();
        });

        // Handling add button
        classesBinding.addSubjectButton.setOnClickListener(v -> {
            navController.navigate(R.id.action_classesFragment_to_subjectsFragment);
        });

        /*
        Setting OnRefreshListener on the swipe-refresh layout
         */
        classesBinding.classesSwipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.setSubjects(null);
            checksUser();

        });
    }

    /**
     * Function to checks whether the user is already stored in viewmodel or not
     */
    private void checksUser() {
        User user = viewModel.getUser();
        if (user == null) {
            if (getActivity() != null) {
                FirestoreHandler.getInstance(this).getUser(LocalStorage.getInstance(getActivity()).getUserId());
                ViewUtils.showProgressBar(classesBinding.classesOverlayLayLayout);
            }
        } else {
            retrieveSubjects();
        }
    }

    /**
     * Function to get the subjects list according to the student's class and school
     */
    private void retrieveSubjects() {
        User user = viewModel.getUser();
        List<Subject> subjects = viewModel.getSubjects();
        if (subjects != null && !subjects.isEmpty()) {
            ViewUtils.hideProgressBar(classesBinding.classesOverlayLayLayout);
            classesAdapter.submitList(Subject.filterSubjects(subjects));
            return;
        }

        FirestoreHandler.getInstance(this).getSubjects(user.get_school_ref(), user.get_class());
        ViewUtils.showProgressBar(classesBinding.classesOverlayLayLayout);
    }



    /**
     * -----------------------------------------------------------------------------
     * These are the methods overrided from the SubjectCallbacks
     * @param _position - It is the position of the clicked item
     */

    @Override
    public void onClick(int _position) {

    }

    @Override
    public void onJoinClassClick(int _position) {
        List<Subject> subjects =  Subject.filterSubjects(viewModel.getSubjects());
        Subject subject= subjects.get(_position);
        String joinUrl = null;
        if (subject == null) return;

        joinUrl = subject.get_join_url();

        if (joinUrl == null || joinUrl.isEmpty()) return;

        if (!joinUrl.contains("https")) joinUrl = "https://" + joinUrl;

        Uri uri = Uri.parse(joinUrl);
        Intent intent= new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }

    @Override
    public void onCopyClick(int _position) {
        // Gets a handle to the clipboard service.
        if (getActivity() == null) return;

        List<Subject> subjects =  Subject.filterSubjects(viewModel.getSubjects());
        Subject subject= subjects.get(_position);
        String joinUrl = null;
        if (subject == null) return;

        joinUrl = subject.get_join_url();

        if (joinUrl == null || joinUrl.isEmpty()) return;

        if (!joinUrl.contains("https")) joinUrl = "https://" + joinUrl;

        // Link Code
        String linkCode = joinUrl;
        if (joinUrl.contains("google")) linkCode = linkCode.substring(24);

        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        // Creates a new text clip to put on the clipboard
        ClipData clip = ClipData.newPlainText("link-code", linkCode);
        clipboard.setPrimaryClip(clip);
        NotifyUtils.showToast(getContext(), "Copied");
    }

    @Override
    public void onLongClick(int _position) {

    }

    @Override
    public void onAddClick(int _position) {

    }


    /**
     * -------------------------------------------------------------------------
     * These are the methods overrided from the FirestoreCallbacks
     * @param user - it is the instance of the user
     * @param teacher - it is the instance of the teacher
     * @param school - it is the instance of the school
     * @param schools - it is the instance of the List<School>
     * @param subjects - it is the instance of the List<Subject>
     * @param assignments - it is the instance of the List<Assignment>
     * @param submissions - it is the instance of the List<Submission>
     * @param notices - it is the instance of the List<Notice>
     */

    @Override
    public void onSuccess(User user, Teacher teacher, School school, List<School> schools, List<Subject> subjects, List<Assignment> assignments, Submission submissions, List<Notice> notices) {
        if (classesBinding == null) return;

        // If User retrieved from the Firestore
        if (user != null) {
            viewModel.setUser(user);
            retrieveSubjects();
            return;
        }

        // If Subjects retrieve from the firestore
        if (subjects != null) {
            ViewUtils.hideProgressBar(classesBinding.classesOverlayLayLayout);
            ViewUtils.handleRefreshing(classesBinding.classesSwipeRefreshLayout);
            if (!subjects.isEmpty()) {
                rawSubjectsList.addAll(subjects);
                handleTeacher();
            }
            return;
        }

        // If Assignment is retrieved
        if (teacher != null) {
            Subject subject = rawSubjectsList.get(position);
            subject.set_teacher(teacher);
            subjectList.add(subject);
            position += 1;
            handleTeacher();
            return;
        }

        ViewUtils.hideProgressBar(classesBinding.classesOverlayLayLayout);
        ViewUtils.handleRefreshing(classesBinding.classesSwipeRefreshLayout);
    }

    @Override
    public void onFailure(Exception e) {
        if (classesBinding == null) return;
        if (getContext() != null) NotifyUtils.showToast(getContext(), e.getMessage());
        ViewUtils.handleRefreshing(classesBinding.classesSwipeRefreshLayout);
        ViewUtils.hideProgressBar(classesBinding.classesOverlayLayLayout);
    }


    /**
     * Function to retrieve teacher and add to the subject
     */
    private void handleTeacher() {
        if (position < rawSubjectsList.size()) {
            String teacher_ref = rawSubjectsList.get(position).get_teacher_ref();
            FirestoreHandler.getInstance(this).getTeacher(teacher_ref);
            return;
        }

        position = 0;
        ViewUtils.hideProgressBar(classesBinding.classesOverlayLayLayout);
        ViewUtils.handleRefreshing(classesBinding.classesSwipeRefreshLayout);
        handleSubjects(rawSubjectsList);
    }


    /**
     * ----------------------------------------------------------------------
     * Function to handle subjects data
     */
    private void handleSubjects(List<Subject> subjects) {
        User u = viewModel.getUser();
        List<Subject> processedSubjects =  Subject.processedSubjects(subjects, u);
        viewModel.setSubjects(processedSubjects);
        classesAdapter.submitList(Subject.filterSubjects(processedSubjects));
    }


    /*
    This is the method overrided to handle the view destroy
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        classesBinding = null;
    }
}