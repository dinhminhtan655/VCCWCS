package com.wcs.vcc.mvvm.data.repository.masscyclecount;

import com.google.gson.JsonObject;
import com.wcs.vcc.mvvm.data.api.WCSApi;
import com.wcs.vcc.mvvm.data.mapper.LocationsMapper;
import com.wcs.vcc.mvvm.data.model.MassCycleCount.MassCycleCountRemote;
import com.wcs.vcc.mvvm.data.model.MassCycleCount.pallet.PalletRemote;
import com.wcs.vcc.mvvm.data.model.MassCycleCountDetail.MassCycleCountDetailRemote;
import com.wcs.vcc.mvvm.data.model.MassCycleCountDetail.product.Product;
import com.wcs.vcc.mvvm.data.model.locations.Locations;
import com.wcs.vcc.mvvm.data.repository.masscyclecount.randomcyclecountfragmentrepository.RandomCycleCountInsertFragmentRepository;
import com.wcs.vcc.mvvm.data.repository.masscyclecountdetail.MassCycleCountDetailRepository;
import com.wcs.vcc.mvvm.data.repository.masscyclecountdetail.massmyclemountdetailupdatefragmentrepository.MassCycleCountDetailUpdateFramentRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MassCycleCountRemoteDataSource implements MassCycleCountDataSource.Remote, MassCycleCountDataSource.MassCycleCountDetailRemote,
        MassCycleCountDataSource.MassCycleCountDetaillUpdate, MassCycleCountDataSource.RandomCycleCountInsert {

    private static MassCycleCountRemoteDataSource sInstance;

    private final WCSApi wcsApi;


    private MassCycleCountRemoteDataSource(WCSApi wcsApi) {
        this.wcsApi = wcsApi;
    }

    public static MassCycleCountRemoteDataSource getInstance(WCSApi wcsApi) {
        if (sInstance == null) {
            sInstance = new MassCycleCountRemoteDataSource(wcsApi);
        }
        return sInstance;
    }


    @Override
    public void getMassCycleCountData(MassCycleCountRepository.loadMassCycleCountCallback callback, JsonObject jsonObject) {
        wcsApi.getMassCycleCount(jsonObject).enqueue(new Callback<List<MassCycleCountRemote>>() {
            @Override
            public void onResponse(Call<List<MassCycleCountRemote>> call, Response<List<MassCycleCountRemote>> response) {
                if (response.isSuccessful()) {
                    List<MassCycleCountRemote> list = response.body() != null ? response.body() : null;
                    callback.onMassCycleCountLoaded(list);
                    callback.onMassCycleCountNormal(getFilterList(list, 0));
                    callback.onMassCycleCountBlind(getFilterList(list, 1));
                    callback.onMassCycleCountRandom(getFilterList(list, 2));
                    if (list == null && list.isEmpty())
                        callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<List<MassCycleCountRemote>> call, Throwable t) {
                callback.onError();
            }
        });


    }

    private List<MassCycleCountRemote> getFilterList(List<MassCycleCountRemote> list, int choice) {
        List<MassCycleCountRemote> list1 = new ArrayList<>();
        if (choice == 1) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isCycleCountHideQty() && !list.get(i).getCustomerNumber().equals("")) {
                    list1.add(new MassCycleCountRemote(list.get(i).getCustomerNumber(),
                            list.get(i).getCustomerName(),
                            list.get(i).getStockMovementMassDate(),
                            list.get(i).getStockMovementMassRemark(),
                            list.get(i).isStockMovementMassConfirm(),
                            list.get(i).getStockMovementMassNumber(),
                            list.get(i).getCreatedTime(),
                            list.get(i).getStockMovementMassID(),
                            list.get(i).isCycleCountHideQty()));
                }
            }
        } else if (choice == 0) {
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).isCycleCountHideQty()&&  !list.get(i).getCustomerNumber().equals("")) {
                    list1.add(new MassCycleCountRemote(list.get(i).getCustomerNumber(),
                            list.get(i).getCustomerName(),
                            list.get(i).getStockMovementMassDate(),
                            list.get(i).getStockMovementMassRemark(),
                            list.get(i).isStockMovementMassConfirm(),
                            list.get(i).getStockMovementMassNumber(),
                            list.get(i).getCreatedTime(),
                            list.get(i).getStockMovementMassID(),
                            list.get(i).isCycleCountHideQty()));
                }
            }
        } else if (choice == 2) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getCustomerNumber().equals("")) {
                    list1.add(new MassCycleCountRemote(list.get(i).getCustomerNumber(),
                            list.get(i).getCustomerName(),
                            list.get(i).getStockMovementMassDate(),
                            list.get(i).getStockMovementMassRemark(),
                            list.get(i).isStockMovementMassConfirm(),
                            list.get(i).getStockMovementMassNumber(),
                            list.get(i).getCreatedTime(),
                            list.get(i).getStockMovementMassID(),
                            list.get(i).isCycleCountHideQty()));
                }
            }
        }

        return list1;
    }

    @Override
    public void getFindPallet(MassCycleCountRepository.loadFindPalletIDCallback callback, JsonObject jsonObject) {
        wcsApi.getPalletRemote(jsonObject).enqueue(new Callback<List<PalletRemote>>() {
            @Override
            public void onResponse(Call<List<PalletRemote>> call, Response<List<PalletRemote>> response) {
                if (response.isSuccessful()) {
                    List<PalletRemote> list = response.body() != null ? response.body() : null;
                    callback.onFindPalletIDLoaded(list);
                    if (list == null && list.isEmpty())
                        callback.onDataNoteAvailable();
                }
            }

            @Override
            public void onFailure(Call<List<PalletRemote>> call, Throwable t) {
                callback.onError();
            }
        });
    }


    @Override
    public void getMassCycleCountDetailData(MassCycleCountDetailRepository.loadMassCycleCountDetailCallback callback, JsonObject jsonObject) {
        wcsApi.getMassCycleCountDetail(jsonObject).enqueue(new Callback<List<MassCycleCountDetailRemote>>() {
            @Override
            public void onResponse(Call<List<MassCycleCountDetailRemote>> call, Response<List<MassCycleCountDetailRemote>> response) {
                if (response.isSuccessful()) {
                    List<MassCycleCountDetailRemote> list = response.body() != null ? response.body() : null;
                    callback.onMassCycleCountDetailLoaded(list);
                    if (list == null && list.isEmpty())
                        callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<List<MassCycleCountDetailRemote>> call, Throwable t) {
                callback.onError();
            }
        });
    }


    @Override
    public void updateMassCycleCountDetail(MassCycleCountDetailUpdateFramentRepository.updateMassCycleCountDetailCallback callback, JsonObject jsonObject) {
        wcsApi.updateMassCycleCountDetail(jsonObject).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                    if (response.body() == null)
                        callback.onMassCycleCountDetailUpdatedFailed("Thất bại, vui lòng thử lại");
                    else
                        callback.onMassCycleCountDetailUpdated("Thành công");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onUpdatedError();
            }
        });
    }

    @Override
    public void getMassCycleCountDetailProductList(MassCycleCountDetailUpdateFramentRepository.loadMassCycleCountDetailProductListCallback callback, JsonObject jsonObject) {
        wcsApi.getProductListCycleCountDetail(jsonObject).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() == 0)
                        callback.onLoadFailed("Không có sản phẩm");
                    else
                        callback.onLoaded(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                callback.onError();
            }
        });
    }


    @Override
    public void confirmMassCycleCountDetail(MassCycleCountDetailRepository.confirmMassCycleCountDetailCallback callback, JsonObject jsonObject) {
        wcsApi.confirmMassCycleCountDetail(jsonObject).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body().equals("OK"))
                        callback.onConfirmedCycleCountDetailUpdated("Đóng phiếu thành công");
                    else {
                        callback.onConfirmedCycleCountDetailFailed(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onConfirmError();
            }
        });
    }

    @Override
    public void insertRandomCycleCount(RandomCycleCountInsertFragmentRepository.insertRandomCycleCountCallback callback, JsonObject jsonObject) {
        wcsApi.insertRandomCycleCount(jsonObject).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (!response.body().startsWith("NO"))
                        callback.onRandomCycleCountInserted(response.body());
                    else
                        callback.onRandomCycleCountInsertedFailed("NO");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onError();
            }
        });
    }

    @Override
    public void getLocations(MassCycleCountDetailUpdateFramentRepository.loadLocationsCallback callback) {
        wcsApi.getLocations().enqueue(new Callback<List<Locations>>() {
            @Override
            public void onResponse(Call<List<Locations>> call, Response<List<Locations>> response) {
                List<Locations> locations = response.body() != null ? response.body() : null;
                if (locations != null && !locations.isEmpty()) {
                    final List<com.wcs.vcc.mvvm.data.domain.Locations> locationDomain = new ArrayList<>();
                    for (Locations locations1 : locations) {
                        locationDomain.add(LocationsMapper.toLocationDomain(locations1));
                    }
                    callback.onLoaded(locationDomain);
                } else
                    callback.onLoadFailed("Không có dữ liệu");
            }

            @Override
            public void onFailure(Call<List<Locations>> call, Throwable t) {
                callback.onError();
            }
        });
    }


}
