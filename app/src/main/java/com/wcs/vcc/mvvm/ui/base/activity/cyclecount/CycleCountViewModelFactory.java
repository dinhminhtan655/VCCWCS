package com.wcs.vcc.mvvm.ui.base.activity.cyclecount;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.wcs.vcc.mvvm.data.repository.masscyclecount.MassCycleCountRepository;

public class CycleCountViewModelFactory implements ViewModelProvider.Factory {

    private final MassCycleCountRepository massCycleCountRepository;


    public CycleCountViewModelFactory(MassCycleCountRepository massCycleCountRepository) {
        this.massCycleCountRepository = massCycleCountRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CycleCountViewModel.class)) {
            return (T) new CycleCountViewModel(massCycleCountRepository);
        }
        throw new IllegalArgumentException("Unknown viewmodel class");
    }
}
