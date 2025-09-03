package com.wcs.vcc.main.bigcqa;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LifecycleRegistryOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.wcs.wcs.BuildConfig;
import com.wcs.wcs.R;import com.wcs.wcs.databinding.DialogFragmentQapoProductUpdateBinding;
import com.wcs.vcc.main.postiamge.GridImage;
import com.wcs.vcc.main.postiamge.ImageUtils;
import com.wcs.vcc.main.postiamge.Thumb;
import com.wcs.vcc.main.postiamge.ThumbImageAdapter;
import com.wcs.vcc.main.vo.QaPoProduct;
import com.wcs.vcc.utilities.ParseNumberHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class QaPoProductUpdateDialogFragment extends DialogFragment implements LifecycleRegistryOwner {
    public final int CODE_CAMERA = 123;
    public final int CODE_READ_EXTERNAL_STORAGE = 124;
    public final int CODE_PICK_IMAGE = 101;
    public final int CODE_CAPTURE_IMAGE = 102;
    public Uri imageCapturedUri;
    private LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private QaPoProductViewModel viewModel;
    private DialogFragmentQapoProductUpdateBinding binding;
    private boolean[] remarkSelectList;
    private CharSequence[] remarkList;
    private Double qtyQADamageVH;
    private Double qtyQADamageH;
    private Double qtyQADamageL;
    private Integer qtyQA;
    private Double qtyDetailDamage;
    private Double qtyDetailDamageError;
    private String strPurchasingOrderProductNumber;
    private TextWatcher watcher;
    private boolean isAddTextChanged;
    public File outputMediaFile;
    public static ArrayList<File> filess = new ArrayList<>();
    private ThumbImageAdapter gridImageAdapter;

    @NonNull
    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_qapo_product_update, container, false);
        gridImageAdapter = new ThumbImageAdapter(getContext(), new ArrayList<Thumb>());
        binding.gridImage.setAdapter(gridImageAdapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct();
            }
        });

        binding.btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        remarkList = getContext().getResources().getTextArray(R.array.remark);
        remarkSelectList = new boolean[remarkList.length];

        binding.txtRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setMultiChoiceItems(remarkList, remarkSelectList, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                remarkSelectList[which] = isChecked;
                            }
                        })
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StringBuilder remark = new StringBuilder();
                                int selectedCount = 0;
                                for (int i = 0, n = remarkList.length; i < n; i++) {
                                    if (remarkSelectList[i]) {
                                        remark.append(remarkList[i]).append("; ");
                                        selectedCount++;
                                    }
                                }
                                if (selectedCount > 0) {
                                    remark.delete(remark.length() - 2, remark.length());
                                }
                                binding.txtRemark.setText(remark.toString());
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .create();
                alertDialog.show();
            }
        });

        watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                parseData();
                calculateDamage();
                updateDamageUI();
            }
        };

        View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (watcher != null && !isAddTextChanged) {
                    binding.editDamageL.addTextChangedListener(watcher);
                    binding.editQtyQa.addTextChangedListener(watcher);
                    binding.editDamageVh.addTextChangedListener(watcher);
                    binding.editDamageH.addTextChangedListener(watcher);

                    isAddTextChanged = true;
                }
            }
        };

        binding.editDamageL.setOnFocusChangeListener(focusChangeListener);
        binding.editQtyQa.setOnFocusChangeListener(focusChangeListener);
        binding.editDamageVh.setOnFocusChangeListener(focusChangeListener);
        binding.editDamageH.setOnFocusChangeListener(focusChangeListener);
    }

    private void updateProduct() {
        parseData();

        binding.getProduct().setQtyQA(qtyQA);
        binding.getProduct().setQtyQADamageVH(qtyQADamageVH);
        binding.getProduct().setQtyQADamageH(qtyQADamageH);
        binding.getProduct().setQtyQADamageL(qtyQADamageL);
        binding.getProduct().setPoDetailDamage(qtyDetailDamage);
        binding.getProduct().setPoDetailDamageError(qtyDetailDamageError);
        binding.getProduct().setPurchasingOrderProductNumber(binding.textView8.getText().toString());
        binding.getProduct().setRemark(
                concatRemark(binding.txtRemark.getText().toString(),
                        binding.editOtherRemark.getText().toString()));

        ((QaPoProductActivity) getActivity()).updateProduct(binding.getProduct());
        dismiss();
    }


    private void imageChooser() {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.chon_nguon_anh).setItems(new CharSequence[]{getString(R.string.chon_hinh_tu_may_anh), getString(R.string.chon_hinh_tu_bo_suu_tap)},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        checkCaptureImage();
                                        break;
                                    case 1:
                                        checkPickImage();
                                        break;
                                }
                            }
                        })
                .create();
        dialog.show();
    }


    public void checkCaptureImage() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CODE_CAMERA);
        } else
            intentCaptureImage();
    }

    public void checkPickImage() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_READ_EXTERNAL_STORAGE);
        } else
            intentPickImage();
    }

    public void intentPickImage() {
        Intent getImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getImageIntent.setType("image/*");
        getImageIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(getImageIntent, CODE_PICK_IMAGE);
    }

    public void intentCaptureImage() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        outputMediaFile = ImageUtils.getOutputMediaFile();
        if (outputMediaFile == null) {
            Toast.makeText(getContext().getApplicationContext(), "Không thể tạo tệp để lưu ảnh", Toast.LENGTH_LONG).show();
            return;
        }

        filess.add(outputMediaFile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageCapturedUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".fileprovider", outputMediaFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            if (outputMediaFile != null) {
                imageCapturedUri = Uri.fromFile(outputMediaFile);
            } else {
                Toast.makeText(getContext().getApplicationContext(), "Không thể tạo tệp để lưu ảnh", Toast.LENGTH_LONG).show();
            }
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCapturedUri);
        startActivityForResult(intent, CODE_CAPTURE_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_CAPTURE_IMAGE && resultCode == RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                filess = GridImage.updateGridImage(outputMediaFile.getPath(), gridImageAdapter);
            } else {
                filess = GridImage.updateGridImage(imageCapturedUri.getPath(), gridImageAdapter);
            }
        } else if (requestCode == CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            filess = GridImage.updateGridImage(getContext(), data, gridImageAdapter);
        }else if (resultCode == RESULT_CANCELED){
            Toast.makeText(getContext(), "Thất bại ", Toast.LENGTH_LONG).show();
        }
    }

    private void parseData() {
        qtyQADamageVH = ParseNumberHelper.parseDouble(binding.editDamageVh.getText().toString());
        qtyQADamageH = ParseNumberHelper.parseDouble(binding.editDamageH.getText().toString());
        qtyQADamageL = ParseNumberHelper.parseDouble(binding.editDamageL.getText().toString());
       // qtyDetailDamage = ParseNumberHelper.parseDouble(binding.editPoDetailDamage.getText().toString());
        qtyDetailDamageError = ParseNumberHelper.parseDouble(binding.editDamageError.getText().toString());
        qtyQA = ParseNumberHelper.parseInt(binding.editQtyQa.getText().toString());

    }

    private void calculateDamage() {
//        qtyDetailDamageError = 0d;
//        if (qtyQADamageVH > 0) {
//            qtyDetailDamage = 100d;
//        } else {
//            Double temp = (qtyQADamageH + qtyQADamageL / 2) / qtyQA * 100;
//            qtyDetailDamageError = temp;
//            if (temp <= 15d) {
//                qtyDetailDamage = temp;
//            } else {
//                qtyDetailDamage = 100d;
//            }
//        }
        qtyDetailDamage = qtyQADamageVH*100/qtyQA;
    }

    private void updateDamageUI() {
        binding.editPoDetailDamage.setText(String.format(Locale.getDefault(), "%.3f", qtyDetailDamage));
        binding.editDamageError.setText(String.format(Locale.getDefault(), "%.3f", qtyDetailDamageError));
    }

    private String concatRemark(String one, String two) {
        String semicolon = "";
        if (one.length() > 0 && two.trim().length() > 0) {
            semicolon = "; ";
        }
        return one + semicolon + two;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(QaPoProductViewModel.class);
        viewModel.getQaPoProduct().observe(this, new Observer<QaPoProduct>() {
            @Override
            public void onChanged(@Nullable QaPoProduct product) {
                if (product != null) {
                    binding.setProduct(product);
                }
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (watcher != null) {
            binding.editDamageL.removeTextChangedListener(watcher);
            binding.editQtyQa.removeTextChangedListener(watcher);
            binding.editDamageVh.removeTextChangedListener(watcher);
            binding.editDamageH.removeTextChangedListener(watcher);
            watcher = null;
        }
    }
}
