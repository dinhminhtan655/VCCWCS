package com.wcs.vcc.main.scanvinmartv;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
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
import com.wcs.vcc.api.MyRetrofit;
import com.wcs.vcc.api.XDockVinOutboundPackingViewSupplierABA;
import com.wcs.vcc.api.XDockVinOutboundPackingViewSupplierProductsABA;
import com.wcs.vcc.preferences.SpinCusPrefVin;
import com.wcs.vcc.recyclerviewadapter.RecyclerViewItemOrderListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class ABAListItemFragment extends DialogFragment {

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
    List<XDockVinOutboundPackingViewSupplierABA> list;
    List<XDockVinOutboundPackingViewSupplierProductsABA> list2;
    String strSupplierCode = "";

    ABASpinnerSupplierAdapter adapter;
    ABAListItemAdapter listItemAdapter;

    public static ABAListItemFragment ABAnewInstance(String data, String userChia) {
        ABAListItemFragment dialog = new ABAListItemFragment();
        Bundle args = new Bundle();
        args.putString("data", data);
        args.putString("userchia", userChia);
        dialog.setArguments(args);
        return dialog;
    }

    public interface ABAOnInputListener {
        void sendInput(String input, String itemName);
    }

    public ABAOnInputListener onInputListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_a_b_a_list_item, container, false);
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


        MyRetrofit.initRequest(getContext()).loadSupplierABA(jsonObject).enqueue(new Callback<List<XDockVinOutboundPackingViewSupplierABA>>() {
            @Override
            public void onResponse(Response<List<XDockVinOutboundPackingViewSupplierABA>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
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
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(), "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadSupplierProduct(String data, String userChia, String strSupplierCode) {
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("Delivery_Date", data);
        jsonObject1.addProperty("User_Chia_Item", userChia);
        jsonObject1.addProperty("Supplier_Code", strSupplierCode);

        MyRetrofit.initRequest(getContext()).loadSupplierProductsABA(jsonObject1).enqueue(new Callback<List<XDockVinOutboundPackingViewSupplierProductsABA>>() {
            @Override
            public void onResponse(Response<List<XDockVinOutboundPackingViewSupplierProductsABA>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    list2 = new ArrayList<>();
                    list2.addAll(response.body());
                    listItemAdapter = new ABAListItemAdapter(new RecyclerViewItemOrderListener<XDockVinOutboundPackingViewSupplierProductsABA>() {
                        @Override
                        public void onClick(XDockVinOutboundPackingViewSupplierProductsABA item, int position, int order) {
                            switch (order) {
                                case 0:
                                    String strItemCode = item.Item_Code;
                                    onInputListener.sendInput(strItemCode, item.Item_Name);
                                    getDialog().dismiss();
                                    break;
                            }
                        }

                        @Override
                        public void onLongClick(XDockVinOutboundPackingViewSupplierProductsABA item, int position, int order) {

                        }
                    }, list2);
                    listItemAdapter.replace(response.body());
                    rc.setAdapter(listItemAdapter);
                }
            }

            @Override
            public void onFailure(Throwable t) {
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
            onInputListener = (ABAOnInputListener) getActivity();
        } catch (ClassCastException e) {
            Log.e("nono", "onAttach: ClassCastException: " + e.getMessage());
        }
    }
}
