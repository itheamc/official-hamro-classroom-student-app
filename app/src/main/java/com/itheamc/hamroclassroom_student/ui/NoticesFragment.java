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
import com.itheamc.hamroclassroom_student.adapters.NoticeAdapter;
import com.itheamc.hamroclassroom_student.callbacks.FirestoreCallbacks;
import com.itheamc.hamroclassroom_student.callbacks.NoticeCallbacks;
import com.itheamc.hamroclassroom_student.databinding.FragmentNoticesBinding;
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
import com.itheamc.hamroclassroom_student.utils.OtherUtils;
import com.itheamc.hamroclassroom_student.utils.ViewUtils;
import com.itheamc.hamroclassroom_student.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NoticesFragment extends Fragment implements FirestoreCallbacks, NoticeCallbacks {
    private static final String TAG = "NoticesFragment";
    private FragmentNoticesBinding noticesBinding;
    private NavController navController;
    private MainViewModel viewModel;
    private NoticeAdapter noticeAdapter;



    public NoticesFragment() {
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
        noticesBinding = FragmentNoticesBinding.inflate(inflater, container, false);
        return noticesBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initializing NavController and ViewModel
        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Initializing AssignmentAdapter and setting to recyclerview
        noticeAdapter = new NoticeAdapter(this);
        noticesBinding.noticesRecyclerView.setAdapter(noticeAdapter);


        // Setting swipe and refresh layout
        noticesBinding.noticesSwipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.setNotices(null);
            checksUser();
        });

        // Handling back button
        noticesBinding.backButton.setOnClickListener(v -> {
            navController.popBackStack();
        });



        // Checks User for assignment extraction
        if (viewModel.get_past_date() != 0) {
            long timeDiff = OtherUtils.timeDifference(viewModel.get_past_date());
            if (timeDiff < 90) {
                List<Notice> storedNotices = viewModel.getNotices();
                if (storedNotices != null && !storedNotices.isEmpty()) {
                    noticeAdapter.submitList(storedNotices);
                } else {
                    checksUser();
                }
            } else {
                checksUser();
            }
        } else {
            checksUser();
        }


    }


    /**
     * Function to checks whether the user is already stored in viewmodel or not
     */
    private void checksUser() {
        User user = viewModel.getUser();
        if (user == null) {
            if (getActivity() != null) {
                FirestoreHandler.getInstance(this).getUser(LocalStorage.getInstance(getActivity()).getUserId());
                showProgress();
            }
            return;
        }

        retrieveNotices(user);
        showProgress();
    }

    /**
     * Function to retrieve notices
     */
    private void retrieveNotices(@NonNull User user) {
        String schoolId = user.get_school_ref();
        String _class = user.get_class();
        if ((schoolId == null || schoolId.isEmpty()) || (_class == null || _class.isEmpty())) {
            hideProgress();
            return;
        }

        FirestoreHandler.getInstance(this).getNotices(schoolId, _class);
    }


    /*
    Function to show progressbar
     */
    private void showProgress() {
        ViewUtils.showProgressBar(noticesBinding.noticesOverlayLayLayout);
    }

    /*
    Function to hide progressbar
     */
    private void hideProgress() {
        ViewUtils.hideProgressBar(noticesBinding.noticesOverlayLayLayout);
        ViewUtils.handleRefreshing(noticesBinding.noticesSwipeRefreshLayout);
    }


    /**
     * Methods implemented from the FirestoreCallbacks
     * @param user -
     * @param teacher -
     * @param school -
     * @param schools -
     * @param subjects -
     * @param assignments -
     * @param submissions -
     * @param notices -
     */
    @Override
    public void onSuccess(User user, Teacher teacher, School school, List<School> schools, List<Subject> subjects, List<Assignment> assignments, Submission submissions, List<Notice> notices) {
        if (noticesBinding == null) return;
        if (user != null) {
            viewModel.setUser(user);
            retrieveNotices(user);
            return;
        }

        if (notices != null) {
            viewModel.setNotices(notices);
            noticeAdapter.submitList(notices);
            viewModel.set_past_date(new Date().getTime());
            hideProgress();
            return;
        }

        // If success but result is null
        hideProgress();
    }

    @Override
    public void onFailure(Exception e) {
        if (noticesBinding == null) return;
        hideProgress();
        NotifyUtils.showToast(getContext(), getString(R.string.went_wrong_message));
        NotifyUtils.logError(TAG, "onFailure()", e);
    }

    /**
     * Method overrided from the NoticeCallbacks
     * @param _position - a position of the clicked item in the recycler view
     */
    @Override
    public void onClick(int _position) {
        List<Notice> notices = viewModel.getNotices();
        if (notices == null) return;

        Notice notice = notices.get(_position);
        viewModel.setNotice(notice);
        navController.navigate(R.id.action_noticesFragment_to_noticeFragment);
    }
}