package com.wcs.vcc.mvvm.ui.base.activity.cyclecountdetail;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.wcs.vcc.mvvm.data.repository.masscyclecountdetail.MassCycleCountDetailRepository;

public class CycleCountDetailViewModelFactory implements ViewModelProvider.Factory {

    private final MassCycleCountDetailRepository repository;

    public CycleCountDetailViewModelFactory(MassCycleCountDetailRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(CycleCountDetailViewModel.class))
            return (T) new CycleCountDetailViewModel(repository);

        throw new IllegalArgumentException("Unknow Viewmodel class");
    }
}
