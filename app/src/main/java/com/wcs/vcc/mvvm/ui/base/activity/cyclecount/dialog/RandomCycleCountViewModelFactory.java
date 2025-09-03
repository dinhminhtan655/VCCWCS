package com.wcs.vcc.mvvm.ui.base.activity.cyclecount.dialog;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.wcs.vcc.mvvm.data.repository.masscyclecount.randomcyclecountfragmentrepository.RandomCycleCountInsertFragmentRepository;

public class RandomCycleCountViewModelFactory implements ViewModelProvider.Factory {


    private final RandomCycleCountInsertFragmentRepository repository;

    public RandomCycleCountViewModelFactory(RandomCycleCountInsertFragmentRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RandomCycleCountViewModel.class))
            return (T) new RandomCycleCountViewModel(repository);

        throw new IllegalArgumentException("Unknow viewmodel class");
    }
}
