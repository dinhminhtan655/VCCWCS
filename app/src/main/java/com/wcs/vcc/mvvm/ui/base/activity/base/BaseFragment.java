package com.wcs.vcc.mvvm.ui.base.activity.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewbinding.ViewBinding;

public abstract class BaseFragment<BINDING extends ViewBinding, VM extends BaseViewModel> extends DialogFragment {

    protected VM viewModel;
    protected BINDING binding;

    @NonNull
    protected abstract VM createViewModelFragment();

    @NonNull
    protected abstract BINDING createFragmentViewBinding(LayoutInflater inflater);


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = createFragmentViewBinding(getLayoutInflater());
        viewModel = createViewModelFragment();
        return binding.getRoot();
    }
}
