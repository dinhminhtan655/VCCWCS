package com.wcs.vcc.main.containerandtruckinfor;

import android.Manifest;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.wcs.vcc.main.vesinhantoan.model.ImageThumb;
import com.wcs.vcc.utilities.Utilities;
import com.wcs.wcs.databinding.FragmentViewPagerImageBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerImageFragment extends DialogFragment {

    private static final String ARG_PARAM1 = "arg_param1";
    private static final String ARG_PARAM2 = "arg_param2";
    private List<ImageThumb> list;

    private FragmentViewPagerImageBinding binding;
    private ViewPagerImageAdapter adapter;
    private int position;

    // TODO: Rename and change types and number of parameters
    public static ViewPagerImageFragment newInstance(List<ImageThumb> param1, int position) {
        ViewPagerImageFragment fragment = new ViewPagerImageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, (Serializable) param1);
        args.putInt(ARG_PARAM2, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentViewPagerImageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Bundle bundle = this.getArguments();
        list = new ArrayList<>();
        if (bundle != null) {
            position = bundle.getInt(ARG_PARAM2, 0);
            list = (List<ImageThumb>) bundle.getSerializable(ARG_PARAM1);
        }

        Log.d("printsize", list.size() + "");

        adapter = new ViewPagerImageAdapter(list, getContext(), new ViewPagerImageAdapter.OnClickItem() {
            @Override
            public void onClick(ImageThumb imageThumb) {

            }

            @Override
            public void onLongClick(ImageThumb imageThumb) {
                Dexter.withActivity(getActivity()).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Bitmap bitmap = imageThumb.getbImageThumb();
                            Utilities.saveBitmapToMedia(getActivity(), bitmap);
                        } else {
                            Utilities.showSettingsDialog(getActivity());
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
            }
        });
        binding.vpImage.setAdapter(adapter);

        binding.vpImage.setCurrentItem(position, false);


        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
//            dialog.setCancelable(false);
        }
    }


}