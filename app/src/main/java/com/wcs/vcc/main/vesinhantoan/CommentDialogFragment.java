package com.wcs.vcc.main.vesinhantoan;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.wcs.wcs.R;

/**
 * Created by tranxuanloc on 3/22/2016.
 */
public class CommentDialogFragment extends DialogFragment {
    //Flag = 0: Select tất cả comment của 1 QSHE
//Flag = 1: Insert
//Flag = 2: Update
//Flag = 3: Delete
    private boolean largeLayout;
    private static CommentDialogFragment dialogFragment;

    public static CommentDialogFragment getInstance() {
        if (dialogFragment == null)
            dialogFragment = new CommentDialogFragment();
        return dialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        largeLayout = getContext().getResources().getBoolean(R.bool.large_layout);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_qhse_comment, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void showDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (largeLayout) {
            show(fragmentManager, "comment");
        } else {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.add(android.R.id.content, dialogFragment).addToBackStack(null).commit();
        }
    }
}
