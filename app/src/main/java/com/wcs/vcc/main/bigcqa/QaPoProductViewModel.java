package com.wcs.vcc.main.bigcqa;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wcs.vcc.main.vo.QaPoProduct;

public class QaPoProductViewModel extends ViewModel {
    private MutableLiveData<QaPoProduct> qaPoProduct = new MutableLiveData<>();

    public LiveData<QaPoProduct> getQaPoProduct() {
        return qaPoProduct;
    }

    public void setQaPoProduct(QaPoProduct product) {
        qaPoProduct.setValue(product);
    }
}
