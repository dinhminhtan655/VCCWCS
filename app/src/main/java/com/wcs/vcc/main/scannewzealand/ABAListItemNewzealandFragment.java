package com.wcs.vcc.main.scannewzealand;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wcs.wcs.R;
import com.wcs.vcc.api.MyRetrofit2;
import com.wcs.vcc.main.scannewzealand.adapter.ABAListItemAdapter;
import com.wcs.vcc.main.scannewzealand.adapter.ABASpinnerSupplierAdapter;
import com.wcs.vcc.main.scannewzealand.model.XDockOutboundPackingViewSupplierProductsNewZealandABA;
import com.wcs.vcc.main.scannewzealand.model.XDockVinOutboundPackingViewSupplierNewZealandABA;
import com.wcs.vcc.preferences.SpinCusPrefVin;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;


public class ABAListItemNewzealandFragment extends DialogFragment {

    Unbinder unbinder;
    View view;

    @BindView(R.id.btn_update)
    Button btnUpdate;
    @BindView(R.id.btn_close)
    Button btnClose;
    @BindView(R.id.spinSupplier)
    Spinner spinSupplier;
    @BindView(R.id.rcSupplierProduct)
    RecyclerView rc;
    List<XDockVinOutboundPackingViewSupplierNewZealandABA> list;
    List<XDockOutboundPackingViewSupplierProductsNewZealandABA> list2;
    String strSupplierCode = "";

    ABASpinnerSupplierAdapter adapter;
    ABAListItemAdapter listItemAdapter;

    public static ABAListItemNewzealandFragment ABAnewInstance(String data, String userChia) {
        ABAListItemNewzealandFragment dialog = new ABAListItemNewzealandFragment();
        Bundle args = new Bundle();
        args.putString("data", data);
        args.putString("userchia", userChia);
        dialog.setArguments(args);
        return dialog;
    }

    public interface ABAOnInputListener2 {
        void sendInput(String input, String itemName, String supplierID);
    }

    public ABAOnInputListener2 onInputListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_a_b_a_list_item_newzealand, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String data = getArguments().getString("data", "");
        String userChia = getArguments().getString("userchia", "");

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadSupplierProduct(data, userChia, strSupplierCode);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Delivery_Date", data);
        jsonObject.addProperty("User_Chia_Item", userChia);

        MyRetrofit2.initRequest2().loadSupplierABANEWZEALAND(jsonObject).enqueue(new retrofit2.Callback<List<XDockVinOutboundPackingViewSupplierNewZealandABA>>() {
            @Override
            public void onResponse(Call<List<XDockVinOutboundPackingViewSupplierNewZealandABA>> call, retrofit2.Response<List<XDockVinOutboundPackingViewSupplierNewZealandABA>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    list = new ArrayList<>();

                    list = response.body();
                    adapter = new ABASpinnerSupplierAdapter(getContext(), list);
                    spinSupplier.setAdapter(adapter);

                    spinSupplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            adapterView.getChildAt(0).setBackgroundResource(R.color.bigcLightGreen);
                            SpinCusPrefVin.SaveIntVin(getContext(), i);
                            strSupplierCode = list.get(i).Supplier_Code;
                            Toast.makeText(getContext(), list.get(i).Supplier_Code, Toast.LENGTH_SHORT).show();
                            loadSupplierProduct(data, userChia, strSupplierCode);


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    if (SpinCusPrefVin.LoadInt(getContext()) >= list.size()) {
                        spinSupplier.setSelection(0);
                    } else {
                        spinSupplier.setSelection(SpinCusPrefVin.LoadInt(getContext()));
                    }


                }
            }

            @Override
            public void onFailure(Call<List<XDockVinOutboundPackingViewSupplierNewZealandABA>> call, Throwable t) {
                Toast.makeText(getContext(), "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadSupplierProduct(String data, String userChia, String strSupplierCode) {
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("Delivery_Date", data);
        jsonObject1.addProperty("User_Chia_Item", userChia);
        jsonObject1.addProperty("Supplier_Code", strSupplierCode);

        MyRetrofit2.initRequest2().loadSupplierProductsABANEWZEALAND(jsonObject1).enqueue(new retrofit2.Callback<List<XDockOutboundPackingViewSupplierProductsNewZealandABA>>() {
            @Override
            public void onResponse(Call<List<XDockOutboundPackingViewSupplierProductsNewZealandABA>> call, retrofit2.Response<List<XDockOutboundPackingViewSupplierProductsNewZealandABA>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    list2 = new ArrayList<>();
                    list2.addAll(response.body());
                    listItemAdapter = new ABAListItemAdapter(new RecyclerViewItemOrderListener<XDockOutboundPackingViewSupplierProductsNewZealandABA>() {
                        @Override
                        public void onClick(XDockOutboundPackingViewSupplierProductsNewZealandABA item, int position, int order) {
                            switch (order) {
                                case 0:
                                    String strItemCode = item.Item_Code;
                                    onInputListener.sendInput(strItemCode, item.Item_Name,strSupplierCode);
                                    getDialog().dismiss();
                                    break;
                            }
                        }

                        @Override
                        public void onLongClick(XDockOutboundPackingViewSupplierProductsNewZealandABA item, int position, int order) {

                        }
                    }, list2);
                    listItemAdapter.replace(response.body());
                    rc.setAdapter(listItemAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<XDockOutboundPackingViewSupplierProductsNewZealandABA>> call, Throwable t) {
                Toast.makeText(getContext(), "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onInputListener = (ABAOnInputListener2) getActivity();
        } catch (ClassCastException e) {
            Log.e("nono", "onAttach: ClassCastException: " + e.getMessage());
        }
    }
}