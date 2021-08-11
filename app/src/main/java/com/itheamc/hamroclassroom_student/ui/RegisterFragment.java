package com.itheamc.hamroclassroom_student.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseUser;
import com.itheamc.hamroclassroom_student.R;
import com.itheamc.hamroclassroom_student.adapters.SchoolAdapter;
import com.itheamc.hamroclassroom_student.adapters.SpinnerAdapter;
import com.itheamc.hamroclassroom_student.callbacks.FirestoreCallbacks;
import com.itheamc.hamroclassroom_student.callbacks.SchoolCallbacks;
import com.itheamc.hamroclassroom_student.databinding.FragmentRegisterBinding;
import com.itheamc.hamroclassroom_student.databinding.SchoolBottomSheetBinding;
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
import com.itheamc.hamroclassroom_student.viewmodel.LoginViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RegisterFragment extends Fragment implements FirestoreCallbacks, SchoolCallbacks, AdapterView.OnItemSelectedListener {
    private static final String TAG = "RegisterFragment";
    private FragmentRegisterBinding registerBinding;
    private NavController navController;
    private LoginViewModel loginViewModel;
    private List<School> schools;
    private School school;

    /*
    For Bottom Sheet -- Schools list
    */
    private BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior;
    private SchoolBottomSheetBinding bottomSheetBinding;
    private SchoolAdapter schoolAdapter;

    // EditText
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText guardianEditText;
    private EditText addressEditText;
    private EditText schoolEditText;
    private EditText classEditText;
    private EditText sectionEditText;
    private EditText rollNumberEditText;

    /*
    For Gender Spinner
     */
    private SpinnerAdapter spinnerAdapter;
    private final String[] genders = new String[]{"Gender", "Male", "Female", "Other"};
    private int gender_position;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        registerBinding = FragmentRegisterBinding.inflate(inflater, container, false);
        return registerBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initializing view model and navController
        navController = Navigation.findNavController(view);
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

         /*
        Initializing Bottom Sheet and its views
         */
        ConstraintLayout bottomSheetLayout = (ConstraintLayout) registerBinding.schoolBottomSheetCoordinatorLayout.findViewById(R.id.schoolBottomSheetLayout);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBinding = SchoolBottomSheetBinding.bind(bottomSheetLayout);

        schoolAdapter = new SchoolAdapter(this);
        bottomSheetBinding.schoolRecyclerView.setAdapter(schoolAdapter);

        //Spinner Initialization
        // Spinner Adapter
        if (getContext() != null)
            spinnerAdapter = new SpinnerAdapter(getContext(), R.layout.custom_dropdown_item, genders);
        if (spinnerAdapter != null) registerBinding.spinnerGender.setAdapter(spinnerAdapter);
        registerBinding.spinnerGender.setOnItemSelectedListener(this);


        // Initializing the EditTexts
        nameEditText = registerBinding.editTextName;
        emailEditText = registerBinding.editTextEmail;
        phoneEditText = registerBinding.editTextPhone;
        guardianEditText = registerBinding.editTextGuardian;
        addressEditText = registerBinding.editTextAddress;
        schoolEditText = registerBinding.editTextSchool;
        classEditText = registerBinding.editTextClass;
        sectionEditText = registerBinding.editTextSection;
        rollNumberEditText = registerBinding.editTextRollNumber;

        // Calling function to set the known data got from firebase user to the edittext
        setKnownValue();

        // Setting on click listener on the continue button
        registerBinding.continueButton.setOnClickListener(v -> {
            handleUserStore();
        });

        // Setting onClickListener on school edit text
        schoolEditText.setOnClickListener(v -> {
            handleBottomSheet();
        });
    }

    // Setting some known value to the edit text
    private void setKnownValue() {
        FirebaseUser user = loginViewModel.getFirebaseUser();
        if (user != null) {
            if (user.getDisplayName() != null) nameEditText.setText(user.getDisplayName());
            if (user.getEmail() != null) {
                emailEditText.setText(user.getEmail());
                emailEditText.setEnabled(false);
            }
            if (user.getPhoneNumber() != null) phoneEditText.setText(user.getPhoneNumber());
        }
    }

    //----------------------------------------------------------------
    // Function to handle the storage of user data to the Firestore
    private void handleUserStore() {
        FirebaseUser firebaseUser = loginViewModel.getFirebaseUser();

        if (firebaseUser != null && isValidated()) {
            String _name = nameEditText.getText().toString().trim();
            String _phone = phoneEditText.getText().toString().trim();
            String _email = emailEditText.getText().toString().trim();
            String _guardian = guardianEditText.getText().toString().trim();
            String _address = addressEditText.getText().toString().trim();
            String _class = classEditText.getText().toString().trim();
            String _section = sectionEditText.getText().toString().trim();
            String _rollNo = rollNumberEditText.getText().toString().trim();

            User user = new User(
                    firebaseUser.getUid(),
                    _name,
                    genders[gender_position],
                    String.valueOf(firebaseUser.getPhotoUrl()),
                    _phone,
                    _email,
                    _address,
                    _guardian,
                    _class,
                    _section,
                    _rollNo,
                    school.get_id(),
                    null,
                    new ArrayList<>(),
                    null,
                    new ArrayList<>(),
                    null,
                    new Date()
            );

            FirestoreHandler.getInstance(this).storeUser(user);
            ViewUtils.showProgressBar(registerBinding.overlayLayout);
            ViewUtils.disableViews(
                    nameEditText,
                    phoneEditText,
                    emailEditText,
                    addressEditText,
                    guardianEditText,
                    registerBinding.spinnerGender,
                    schoolEditText,
                    classEditText,
                    sectionEditText,
                    rollNumberEditText,
                    registerBinding.continueButton);
        }

    }


    //-----------------------------------------------------
    // Function to validate the data
    private boolean isValidated() {
        if (getContext() == null) return false;

        String _name = nameEditText.getText().toString().trim();
        String _phone = phoneEditText.getText().toString().trim();
        String _email = emailEditText.getText().toString().trim();
        String _address = addressEditText.getText().toString().trim();
        String _guardian = guardianEditText.getText().toString().trim();
        String _school = schoolEditText.getText().toString().trim();
        String _class = classEditText.getText().toString().trim();
        String _section = sectionEditText.getText().toString().trim();
        String _rollNo = rollNumberEditText.getText().toString().trim();
        if (TextUtils.isEmpty(_name) ||
                TextUtils.isEmpty(_email) ||
                TextUtils.isEmpty(_phone) ||
                TextUtils.isEmpty(_address) ||
                TextUtils.isEmpty(_guardian) ||
                TextUtils.isEmpty(_school) ||
                TextUtils.isEmpty(_class) ||
                TextUtils.isEmpty(_section) ||
                TextUtils.isEmpty(_rollNo) ||
                gender_position == 0) {
            NotifyUtils.showToast(getContext(), "Please enter all the data");
            return false;
        }

        return true;

    }


    /**
     * ----------------------------------------------------------------------
     * Function to store info on local storage
     * ----------------------------------------------------------------------
     */
    private void storeInfo(User user) {
        LocalStorage localStorage = null;
        if (getActivity() != null) localStorage = LocalStorage.getInstance(getActivity());

        if (localStorage == null) return;
        localStorage.storeUserId(user.get_id());
    }

    /**
     * -------------------------------------------------------------------
     * These are the methods implemented from the FirestoreCallbacks
     * -------------------------------------------------------------------
     */
    @Override
    public void onSuccess(User user, List<School> schools, List<Teacher> teachers, List<Subject> subjects, List<Assignment> assignments, List<Submission> submissions, List<Notice> notices) {
        if (registerBinding == null) return;
        if (schools != null) {
            this.schools = new ArrayList<>();
            this.schools = schools;
            schoolAdapter.submitList(this.schools);
            ViewUtils.hideProgressBar(bottomSheetBinding.schoolOverlayLayout);
            return;
        }

        if (getActivity() != null) storeInfo(user);
        ViewUtils.hideProgressBar(registerBinding.overlayLayout);
        requireActivity().startActivity(new Intent(requireActivity(), MainActivity.class));
        requireActivity().finish();
    }

    @Override
    public void onFailure(Exception e) {
        if (registerBinding == null) return;

        NotifyUtils.showToast(getContext(), e.getMessage());
        ViewUtils.hideProgressBar(registerBinding.overlayLayout);
        ViewUtils.hideProgressBar(bottomSheetBinding.schoolOverlayLayout);
        ViewUtils.enableViews(
                nameEditText,
                phoneEditText,
                emailEditText,
                addressEditText,
                guardianEditText,
                registerBinding.spinnerGender,
                schoolEditText,
                classEditText,
                sectionEditText,
                rollNumberEditText,
                registerBinding.continueButton);
        if (getContext() != null)
            NotifyUtils.showToast(getContext(), getString(R.string.went_wrong_message));

    }


    /**
     * -----------------------------------------------------------------------
     * Function to show or hide bottomsheet
     */
    private void handleBottomSheet() {
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            if (schools != null && !schools.isEmpty()) {
                schoolAdapter.submitList(schools);
                return;
            }
            ViewUtils.showProgressBar(bottomSheetBinding.schoolOverlayLayout);
            FirestoreHandler.getInstance(this).getSchools();
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }


    /**
     * --------------------------------------------------------------------------
     * Method implemented from SchoolViewCallbacks
     *
     * @param _position - clicked item position in a list
     */
    @Override
    public void onClick(int _position) {
        school = schools.get(_position);
        schoolEditText.setText(school.get_name());
        ViewUtils.handleBottomSheet(bottomSheetBehavior);
    }

    // On Fragment Destroy
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        registerBinding = null;
    }

    /**
     * Methods implemented from the AdapterView.OnItemSelectedListener
     *
     * @param parent   -
     * @param view     -
     * @param position -
     * @param id       -
     */

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        gender_position = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        gender_position = 1;
    }
}