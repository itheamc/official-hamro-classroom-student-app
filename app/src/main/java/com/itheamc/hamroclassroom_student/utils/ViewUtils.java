package com.itheamc.hamroclassroom_student.utils;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class ViewUtils {
    /*
   Function to handle progressbar
    */
    public static void handleProgressBar(@NonNull LinearLayout progressBarLayout) {
        if (progressBarLayout.getVisibility() == View.GONE) {
            progressBarLayout.setVisibility(View.VISIBLE);
            return;
        }
        progressBarLayout.setVisibility(View.GONE);
    }

    /*
  Function to handle progressbar
   */
    public static void showProgressBar(@NonNull LinearLayout progressBarLayout) {
        if (progressBarLayout.getVisibility() == View.GONE) progressBarLayout.setVisibility(View.VISIBLE);
    }


    /*
  Function to handle progressbar
   */
    public static void hideProgressBar(@NonNull LinearLayout progressBarLayout) {
        if (progressBarLayout.getVisibility() == View.VISIBLE) progressBarLayout.setVisibility(View.GONE);
    }


    /*
    Function to handle noItemFoundLayout
     */
    public static void handleNoItemFound(@NonNull LinearLayout noItemLayout) {
        if (noItemLayout.getVisibility() == View.GONE) {
            noItemLayout.setVisibility(View.VISIBLE);
            return;
        }
        noItemLayout.setVisibility(View.GONE);
    }

    /*
   Function to handle Swipe and Refresh layout refreshing
    */
    public static void handleRefreshing(@NonNull SwipeRefreshLayout swipeRefreshLayout) {
        if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
    }

    /*
   --------------------------------------
   Function to show or hide bottomsheet
    */
    public static void handleBottomSheet(@NonNull BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior) {
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    /*
    FUnction to disable views
     */
    public static void disableViews(View... views) {
        if (views.length == 0) return;

        for (View view: views) {
            view.setEnabled(false);
        }
    }

    /*
    FUnction to enable views
     */
    public static void enableViews(View... views) {
        if (views.length == 0) return;

        for (View view: views) {
            view.setEnabled(true);
        }
    }

    /*
    FUnction to clear EditTexts views
     */
    public static void clearEditTexts(EditText... editTexts) {
        if (editTexts == null || editTexts.length == 0) return;

        for (EditText editText: editTexts) {
            if (editText != null) editText.setText("");
        }
    }

    /*
   FUnction to disable views
    */
    public static void visibleViews(View... views) {
        if (views.length == 0) return;

        for (View view: views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    /*
    FUnction to enable views
     */
    public static void hideViews(View... views) {
        if (views.length == 0) return;

        for (View view: views) {
            view.setVisibility(View.GONE);
        }
    }

}
