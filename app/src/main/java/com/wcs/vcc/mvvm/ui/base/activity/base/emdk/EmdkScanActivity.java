package com.wcs.vcc.mvvm.ui.base.activity.base.emdk;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wcs.vcc.main.emdk.EmdkHelper;
import com.wcs.vcc.main.emdk.EmdkWrapper;
import com.wcs.vcc.main.emdk.ScanListener;
import com.wcs.vcc.mvvm.ui.base.activity.base.BaseActivity;
import com.wcs.vcc.mvvm.ui.base.activity.base.BaseViewModel;

public abstract class EmdkScanActivity<A extends ViewBinding, C extends BaseViewModel> extends BaseActivity implements ScanListener {

    protected C viewModel;
    protected A binding;

    @NonNull
    protected abstract C createViewModel();

    @NonNull
    protected abstract A createViewBinding(LayoutInflater inflater);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = createViewBinding(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        viewModel = createViewModel();
        if (EmdkHelper.isEmdkAvailable()) {
            new EmdkWrapper(this, getLifecycle(), this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                String text = result.getContents();
                onData(text.trim());
            }
        }
    }
}
