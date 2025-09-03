package com.wcs.vcc.mvvm.ui.base.activity.cyclecountdetail.dialog;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.wcs.vcc.mvvm.data.repository.masscyclecountdetail.massmyclemountdetailupdatefragmentrepository.MassCycleCountDetailUpdateFramentRepository;

public class CycleCountDetailUpdateViewModelFactory implements ViewModelProvider.Factory {

    private final MassCycleCountDetailUpdateFramentRepository repository;

    public CycleCountDetailUpdateViewModelFactory(MassCycleCountDetailUpdateFramentRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CycleCountDetailUpdateViewModel.class))
            return (T) new CycleCountDetailUpdateViewModel(repository);

        throw new IllegalArgumentException("Unknow viewmodel class");
    }
}
