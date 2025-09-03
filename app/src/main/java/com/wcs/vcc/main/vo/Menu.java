package com.wcs.vcc.main.vo;

import static com.wcs.vcc.main.vo.Group.BIGC_USER;
import static com.wcs.vcc.main.vo.Group.DILIVERYMAN;
import static com.wcs.vcc.main.vo.Group.DOCUMENT;
import static com.wcs.vcc.main.vo.Group.FORKLIFT_DRIVER;
import static com.wcs.vcc.main.vo.Group.LOWER_USER;
import static com.wcs.vcc.main.vo.Group.MANAGER;
import static com.wcs.vcc.main.vo.Group.NO_POSITION;
import static com.wcs.vcc.main.vo.Group.PRODUCT_CHECKER;
import static com.wcs.vcc.main.vo.Group.SUPERVISOR;
import static com.wcs.vcc.main.vo.Group.TECHNICAL;
import static com.wcs.vcc.main.vo.Group.TRANSPORTATION;
import static com.wcs.vcc.main.vo.Store.ABA_HA_NAM;
import static com.wcs.vcc.main.vo.Store.ABA_HA_NOI;
import static com.wcs.vcc.main.vo.Store.ABA_MD;
import static com.wcs.vcc.main.vo.Store.ABA_SAI_GON;

import com.wcs.vcc.main.len_ha_hang.PickPutOnlineActivity;
import com.wcs.vcc.main.logrecords.LogRecordsActivity;
import com.wcs.vcc.main.phieuhomnay.HomNayActivity;
import com.wcs.vcc.mvvm.ui.base.activity.cyclecount.CycleCountActivity;
import com.wcs.wcs.R;
import com.wcs.vcc.main.DeviceInfoActivity;
import com.wcs.vcc.main.capnhatphienban.CapNhatUngDungActivity;
import com.wcs.vcc.main.changepassword.ChangePasswordActivity;
import com.wcs.vcc.main.chuyenhang.ChuyenHangActivity;
import com.wcs.vcc.main.containerandtruckinfor.ContainerAndTruckInfoActivity;
import com.wcs.vcc.main.doichieu.difference_check.DifferenceCheckActivity;
import com.wcs.vcc.main.gskscan.GSKScanActivity;
import com.wcs.vcc.main.kiemcontainer.KiemContainerActivity;
import com.wcs.vcc.main.kiemvesinh.KiemVeSinhActivity;
import com.wcs.vcc.main.kiemvitri.KiemViTriNoEMDKActivity;
import com.wcs.vcc.main.nangsuat.NangSuatActivity;
import com.wcs.vcc.main.nangsuatnhanvien.NangSuatNhanVienActivity;
import com.wcs.vcc.main.packingscan.save_package.SavePackageHNActivity;
import com.wcs.vcc.main.palletcartonchecking.KiemPalletCartonActivity;
import com.wcs.vcc.main.phieucuatoi.PhieuCuaToiActivity;
import com.wcs.vcc.main.phieuhomnay.KhachHangActivity;
import com.wcs.vcc.main.pickship.CheckStatusPickPackShipActivity;
import com.wcs.vcc.main.register.RegisterActivity;
import com.wcs.vcc.main.resetpassword.resetpasswordActivity;
import com.wcs.vcc.main.scanbarcode.ScanBarcodeActivity;
import com.wcs.vcc.main.soki.GhiSoKiActivity;
import com.wcs.vcc.main.tonkho.khachhang.StockOnHandActivity;
import com.wcs.vcc.main.tripdelivery.TripDeliveryActivity;
import com.wcs.vcc.main.vesinhantoan.QHSEActivity;
import com.wcs.vcc.main.vitritrong.FreeLocationActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by aang on 16/10/2017.
 */

public class Menu {

    private static final String RO = "RO";
    private static final String DO = "DO";

    public static MenuItem menuItemQHSE;
    public static MenuItem menuItemPhieuCuaToi;
    public static MenuItem menuItemGiaoViec;
    public static MenuItem menuItemInfoTruckCont;
    public static MenuItem menuItemUpdateVersion;
    public static MenuItem menuItemCountTrip;

    public static List<MenuItem> createMenu(int store, int group) {
        switch (store) {
            case ABA_SAI_GON:
                return filter(createMenuSaiGon(), group);
            case ABA_HA_NOI:
                return filter(createMenuHaNoi(), group);
            case ABA_HA_NAM:
                return filter(createMenuHaNam(), group);
            case ABA_MD:
                return filter(createMenuDCMD(), group);
            default:
                return new ArrayList<>();
        }
    }

    private static List<MenuItem> createMenuSaiGon() {
        List<MenuItem> menuItems = new ArrayList<>();
//        menuItems.add(new MenuItem(R.string.xdoc_pickinglist, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), XdockingPickingListOrderActivity.class));
//        menuItems.add(new MenuItem(R.string.xdoc_input_carton, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), XdocInputCartonActivity.class));
//        menuItems.add(new MenuItem(R.string.pick_pack, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, SUPERVISOR, LOWER_USER), PickPackShipOrdersActivity.class));
        menuItems.add(new MenuItem(R.string.nhap, R.drawable.ic_menu_item_04, Arrays.asList(MANAGER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), HomNayActivity.class, RO));
        menuItems.add(new MenuItem(R.string.xuat, R.drawable.ic_menu_item_04, Arrays.asList(MANAGER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), HomNayActivity.class, DO));
        menuItems.add(new MenuItem(R.string.phieu_hom_nay, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), KhachHangActivity.class));
        menuItemPhieuCuaToi = new MenuItem(R.string.phieu_cua_toi, R.drawable.ic_menu_item_23, Arrays.asList(MANAGER, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), PhieuCuaToiActivity.class);
        menuItems.add(menuItemPhieuCuaToi);
        menuItems.add(new MenuItem(R.string.len_hang_online, R.drawable.ic_menu_item_17, Arrays.asList(MANAGER, FORKLIFT_DRIVER, PRODUCT_CHECKER, SUPERVISOR), PickPutOnlineActivity.class, RO));
        menuItems.add(new MenuItem(R.string.ha_hang_online, R.drawable.ic_menu_item_17, Arrays.asList(MANAGER, FORKLIFT_DRIVER, PRODUCT_CHECKER, SUPERVISOR), PickPutOnlineActivity.class, DO));
        menuItems.add(new MenuItem(R.string.lich_su_chuyen_hang, R.drawable.ic_menu_item_02, Arrays.asList(MANAGER, FORKLIFT_DRIVER, PRODUCT_CHECKER, SUPERVISOR), ChuyenHangActivity.class));
        menuItems.add(new MenuItem(R.string.kiem_vi_tri, R.drawable.ic_menu_item_15, Arrays.asList(MANAGER, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), KiemViTriNoEMDKActivity.class));
        menuItems.add(new MenuItem(R.string.kiem_pallet, R.drawable.ic_menu_item_19, Arrays.asList(MANAGER, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), KiemPalletCartonActivity.class));
        menuItems.add(new MenuItem(R.string.vi_tri_trong, R.drawable.ic_menu_item_17, Arrays.asList(MANAGER, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), FreeLocationActivity.class));
        menuItems.add(new MenuItem(R.string.ton_kho, R.drawable.ic_menu_item_04, Arrays.asList(MANAGER, FORKLIFT_DRIVER, PRODUCT_CHECKER, SUPERVISOR), StockOnHandActivity.class));
        menuItems.add(new MenuItem(R.string.cycle_count, R.drawable.ic_menu_item_04, Arrays.asList(MANAGER, FORKLIFT_DRIVER, PRODUCT_CHECKER, SUPERVISOR), CycleCountActivity.class));
        menuItemInfoTruckCont = new MenuItem(R.string.container_information, R.drawable.ic_menu_item_07, Arrays.asList(MANAGER, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR, TRANSPORTATION), ContainerAndTruckInfoActivity.class);
        menuItems.add(menuItemInfoTruckCont);
        menuItems.add(new MenuItem(R.string.log_records, R.drawable.ic_menu_item_07, Arrays.asList(MANAGER, SUPERVISOR), LogRecordsActivity.class));
        menuItems.add(new MenuItem(R.string.doi_mat_khau, R.drawable.ic_menu_item_08, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), ChangePasswordActivity.class));
        menuItems.add(new MenuItem(R.string.reset_mat_khau, R.drawable.ic_menu_item_14, Arrays.asList(MANAGER), resetpasswordActivity.class));
        menuItemUpdateVersion = new MenuItem(R.string.cap_nhat_moi, R.drawable.ic_menu_item_03, Arrays.asList(MANAGER, NO_POSITION, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR, TRANSPORTATION), CapNhatUngDungActivity.class);
        menuItems.add(menuItemUpdateVersion);
        menuItems.add(new MenuItem(R.string.gsk_scan, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, SUPERVISOR), GSKScanActivity.class));
        menuItems.add(new MenuItem(R.string.save_package, R.drawable.ic_menu_item_05, Arrays.asList(MANAGER,  SUPERVISOR), SavePackageHNActivity.class));
        menuItems.add(new MenuItem(R.string.scan_barcode, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, SUPERVISOR), ScanBarcodeActivity.class));

        menuItems.add(new MenuItem(R.string.phieu_da_xong, R.drawable.ic_menu_item_07, Arrays.asList(MANAGER, SUPERVISOR), PhieuCuaToiActivity.class));
        menuItems.add(new MenuItem(R.string.difference_check, R.drawable.ic_menu_item_09, Arrays.asList(MANAGER), DifferenceCheckActivity.class));
//        menuItems.add(new MenuItem(R.string.len_ha_hang, R.drawable.ic_menu_item_17, Arrays.asList(MANAGER, FORKLIFT_DRIVER, SUPERVISOR), UpDownActivity.class));
        // duong Add 07/01/2022
        // duong Add 07/01/2022
        //menuItems.add(new MenuItem(R.string.cap_dau, R.drawable.ic_menu_item_22, Arrays.asList(MANAGER, TRANSPORTATION), DistributionOrderViewActivity.class));
        //menuItemCountTrip = new MenuItem(R.string.delivery, R.drawable.ic_menu_item_11, Arrays.asList(MANAGER, DILIVERYMAN), TripDeliveryActivity.class);
        //menuItems.add(menuItemCountTrip);
//        menuItems.add(new MenuItem(R.string.pick_ship, R.drawable.ic_menu_item_11, Arrays.asList(MANAGER, SUPERVISOR, LOWER_USER), PickShipActivity.class));
//        menuItems.add(new MenuItem(R.string.pick_pack, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER), PickPackShipOrdersActivity.class));
//        menuItems.add(new MenuItem(R.string.nhap_ho_so, R.drawable.ic_menu_item_18, Arrays.asList(MANAGER, FORKLIFT_DRIVER, DOCUMENT, SUPERVISOR), NhapHoSoActivity.class));
//        menuItems.add(new MenuItem(R.string.giao_ho_so, R.drawable.ic_menu_item_13, Arrays.asList(MANAGER, FORKLIFT_DRIVER, DOCUMENT, SUPERVISOR), GiaoHoSoActivity.class));
       // menuItems.add(new MenuItem(R.string.giao_ho_so_offline, R.drawable.ic_menu_item_14, Arrays.asList(MANAGER), DispatchingOrderActivity.class));
        menuItemQHSE = new MenuItem(R.string.ve_sinh_an_toan, R.drawable.ic_menu_item_20, Arrays.asList(MANAGER,  SUPERVISOR), QHSEActivity.class);
        menuItems.add(menuItemQHSE);
        //menuItems.add(new MenuItem(R.string.qa_kiem, R.drawable.ic_menu_item_23, Arrays.asList(MANAGER), MetroQACheckingSuppliersActivity.class));
        menuItems.add(new MenuItem(R.string.kiem_container, R.drawable.ic_menu_item_22, Arrays.asList(MANAGER, SUPERVISOR), KiemContainerActivity.class));
//        menuItems.add(new MenuItem(R.string.kiem_ho_so, R.drawable.ic_menu_item_13, Arrays.asList(MANAGER, FORKLIFT_DRIVER, DOCUMENT, SUPERVISOR), KiemHoSoNoEMDKActivity.class));
        menuItems.add(new MenuItem(R.string.nang_suat, R.drawable.ic_menu_item_25, Arrays.asList(MANAGER,  SUPERVISOR), NangSuatActivity.class));
        menuItems.add(new MenuItem(R.string.label_nang_suat_nhan_vien, R.drawable.ic_menu_item_23, Arrays.asList(MANAGER, SUPERVISOR), NangSuatNhanVienActivity.class));
        //menuItems.add(new MenuItem(R.string.lich_su_ra_vao, R.drawable.ic_menu_item_24, Arrays.asList(MANAGER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), LichSuRaVaoActivity.class));
        //menuItems.add(new MenuItem(R.string.nhap_ngoai_gio, R.drawable.ic_menu_item_25, Arrays.asList(MANAGER, TECHNICAL, DOCUMENT, SUPERVISOR, TRANSPORTATION), ListOverTimeEntryActivity.class));
        //menuItemGiaoViec = new MenuItem(R.string.giao_viec, R.drawable.ic_menu_item_03, Arrays.asList(MANAGER, TECHNICAL, DOCUMENT, SUPERVISOR, TRANSPORTATION), AssignWorkActivity.class);
        //menuItems.add(menuItemGiaoViec);
        //menuItems.add(new MenuItem(R.string.lich_trinh, R.drawable.ic_menu_item_09, Arrays.asList(MANAGER, TECHNICAL), ScheduleJobActivity.class));
        //menuItems.add(new MenuItem(R.string.lich_lam_viec, R.drawable.ic_menu_item_07, Arrays.asList(MANAGER, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), LichLamViecActivity.class));
//        menuItems.add(new MenuItem(R.string.gps, R.drawable.ic_menu_item_15, Arrays.asList(MANAGER, DOCUMENT, SUPERVISOR), ListUserActivity.class));
        //menuItems.add(new MenuItem(R.string.fixed_asset, R.drawable.ic_menu_item_10, Arrays.asList(MANAGER), EquipmentInventoryNoEMDKActivity.class));
        //menuItems.add(new MenuItem(R.string.opportunity_manager, R.drawable.ic_menu_item_03, Arrays.asList(MANAGER), ListOpportunityActivity.class));
        //menuItems.add(new MenuItem(R.string.calendar, R.drawable.ic_menu_item_09, Arrays.asList(MANAGER, SUPERVISOR), CRMActivity.class));
        //menuItems.add(new MenuItem(R.string.label_maintenance, R.drawable.ic_menu_item_01, Arrays.asList(MANAGER, TECHNICAL), MaintenanceActivity.class));
        //menuItems.add(new MenuItem(R.string.label_bookings, R.drawable.ic_menu_item_06, Arrays.asList(MANAGER), BookingActivity.class));
        menuItems.add(new MenuItem(R.string.main_label_kvs, R.drawable.ic_menu_item_23, Arrays.asList(MANAGER, SUPERVISOR), KiemVeSinhActivity.class));
        menuItems.add(new MenuItem(R.string.label_register, R.drawable.ic_menu_item_07, Arrays.asList(MANAGER), RegisterActivity.class));

        menuItems.add(new MenuItem(R.string.label_device_info, R.drawable.ic_menu_item_05, Arrays.asList(MANAGER), DeviceInfoActivity.class));
//        menuItems.add(new MenuItem(R.string.receiving_vinmart, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), ScanVinmartDetailActivity.class));
//        menuItems.add(new MenuItem(R.string.packing_vinmart, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), GroupVinmartActivity.class));
//        menuItems.add(new MenuItem(R.string.packing_vinmart2, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), ABAGroupVinmartActivity.class));
//        menuItems.add(new MenuItem(R.string.packing_bhx, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), ABAGroupBHXActivity.class));
//        menuItems.add(new MenuItem(R.string.packing_newzealand, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), ABAGroupNewzealandActivity.class));
//        menuItems.add(new MenuItem(R.string.receiving_review_vinmart, R.drawable.ic_menu_item_07, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), ScanVinmartActivity.class));
//        menuItems.add(new MenuItem(R.string.packing_review_vinmart, R.drawable.ic_menu_item_07, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), PackingReviewActivity.class));
//        menuItems.add(new MenuItem(R.string.packing_order, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), DispatchingOrderPackingActivity.class));
//        menuItems.add(new MenuItem(R.string.check_out, R.drawable.ic_menu_item_22, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), CheckOutTripListActivity.class));
//        menuItems.add(new MenuItem(R.string.packing_satus, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), CheckStatusPickPackShipActivity.class));
//        menuItems.add(new MenuItem(R.string.packing_masan, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), ScanMasanActivity.class));
//        menuItems.add(new MenuItem(R.string.packing_chia_hang, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), CustomerActivity.class));
        menuItems.add(new MenuItem(R.string.ghi_so_ki, R.drawable.ic_menu_item_07, Arrays.asList(MANAGER, SUPERVISOR), GhiSoKiActivity.class));
//        menuItems.add(new MenuItem(R.string.test, R.drawable.ic_menu_item_07, Arrays.asList(MANAGER, SUPERVISOR), TestActivity.class));
//        menuItems.add(new MenuItem(R.string.shift_confirm, R.drawable.ic_menu_item_07, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), DoiChieuActivity.class));

        //menuItems.add(new MenuItem(R.string.QL_KhayRo, R.drawable.ic_baseline_shopping_basket_24, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), BasketRouteActivity.class));

        return menuItems;
    }

    private static List<MenuItem> createMenuHaNoi() {
        List<MenuItem> menuItems = new ArrayList<>();
        //        menuItems.add(new MenuItem(R.string.xdoc_pickinglist, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), XdockingPickingListOrderActivity.class));
//        menuItems.add(new MenuItem(R.string.xdoc_input_carton, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), XdocInputCartonActivity.class));
//        menuItems.add(new MenuItem(R.string.pick_pack, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, SUPERVISOR, LOWER_USER), PickPackShipOrdersActivity.class));
        menuItems.add(new MenuItem(R.string.nhap, R.drawable.ic_menu_item_04, Arrays.asList(MANAGER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), HomNayActivity.class, RO));
        menuItems.add(new MenuItem(R.string.xuat, R.drawable.ic_menu_item_04, Arrays.asList(MANAGER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), HomNayActivity.class, DO));
        menuItems.add(new MenuItem(R.string.phieu_hom_nay, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), KhachHangActivity.class));
        menuItemPhieuCuaToi = new MenuItem(R.string.phieu_cua_toi, R.drawable.ic_menu_item_23, Arrays.asList(MANAGER, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), PhieuCuaToiActivity.class);
        menuItems.add(menuItemPhieuCuaToi);
        menuItems.add(new MenuItem(R.string.len_hang_online, R.drawable.ic_menu_item_17, Arrays.asList(MANAGER, FORKLIFT_DRIVER,PRODUCT_CHECKER, SUPERVISOR), PickPutOnlineActivity.class, RO));
        menuItems.add(new MenuItem(R.string.ha_hang_online, R.drawable.ic_menu_item_17, Arrays.asList(MANAGER, FORKLIFT_DRIVER, PRODUCT_CHECKER,SUPERVISOR), PickPutOnlineActivity.class, DO));
        menuItems.add(new MenuItem(R.string.lich_su_chuyen_hang, R.drawable.ic_menu_item_02, Arrays.asList(MANAGER, FORKLIFT_DRIVER,PRODUCT_CHECKER, SUPERVISOR), ChuyenHangActivity.class));
        menuItems.add(new MenuItem(R.string.kiem_vi_tri, R.drawable.ic_menu_item_15, Arrays.asList(MANAGER, FORKLIFT_DRIVER, PRODUCT_CHECKER,PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), KiemViTriNoEMDKActivity.class));
        menuItems.add(new MenuItem(R.string.kiem_pallet, R.drawable.ic_menu_item_19, Arrays.asList(MANAGER, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), KiemPalletCartonActivity.class));
        menuItems.add(new MenuItem(R.string.vi_tri_trong, R.drawable.ic_menu_item_17, Arrays.asList(MANAGER, FORKLIFT_DRIVER,PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), FreeLocationActivity.class));
        menuItems.add(new MenuItem(R.string.ton_kho, R.drawable.ic_menu_item_04, Arrays.asList(MANAGER, FORKLIFT_DRIVER, SUPERVISOR), StockOnHandActivity.class));
        menuItems.add(new MenuItem(R.string.cycle_count, R.drawable.ic_menu_item_04, Arrays.asList(MANAGER, FORKLIFT_DRIVER, PRODUCT_CHECKER, SUPERVISOR), CycleCountActivity.class));
        menuItemInfoTruckCont = new MenuItem(R.string.container_information, R.drawable.ic_menu_item_07, Arrays.asList(MANAGER, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR, TRANSPORTATION), ContainerAndTruckInfoActivity.class);
        menuItems.add(menuItemInfoTruckCont);
        menuItems.add(new MenuItem(R.string.doi_mat_khau, R.drawable.ic_menu_item_08, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), ChangePasswordActivity.class));
        menuItems.add(new MenuItem(R.string.reset_mat_khau, R.drawable.ic_menu_item_14, Arrays.asList(MANAGER), resetpasswordActivity.class));
        menuItemUpdateVersion = new MenuItem(R.string.cap_nhat_moi, R.drawable.ic_menu_item_03, Arrays.asList(MANAGER, NO_POSITION, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR,TRANSPORTATION), CapNhatUngDungActivity.class);
        menuItems.add(menuItemUpdateVersion);
        menuItems.add(new MenuItem(R.string.gsk_scan, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, SUPERVISOR), GSKScanActivity.class));
        menuItems.add(new MenuItem(R.string.save_package, R.drawable.ic_menu_item_05, Arrays.asList(MANAGER,  SUPERVISOR), SavePackageHNActivity.class));
        menuItems.add(new MenuItem(R.string.scan_barcode, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, SUPERVISOR), ScanBarcodeActivity.class));

        menuItems.add(new MenuItem(R.string.phieu_da_xong, R.drawable.ic_menu_item_07, Arrays.asList(MANAGER, SUPERVISOR), PhieuCuaToiActivity.class));
        menuItems.add(new MenuItem(R.string.difference_check, R.drawable.ic_menu_item_09, Arrays.asList(MANAGER), DifferenceCheckActivity.class));
//        menuItems.add(new MenuItem(R.string.len_ha_hang, R.drawable.ic_menu_item_17, Arrays.asList(MANAGER, FORKLIFT_DRIVER, SUPERVISOR), UpDownActivity.class));
        // duong Add 07/01/2022
        // duong Add 07/01/2022
        //menuItems.add(new MenuItem(R.string.cap_dau, R.drawable.ic_menu_item_22, Arrays.asList(MANAGER, TRANSPORTATION), DistributionOrderViewActivity.class));
        //menuItemCountTrip = new MenuItem(R.string.delivery, R.drawable.ic_menu_item_11, Arrays.asList(MANAGER, DILIVERYMAN), TripDeliveryActivity.class);
        //menuItems.add(menuItemCountTrip);
//        menuItems.add(new MenuItem(R.string.pick_ship, R.drawable.ic_menu_item_11, Arrays.asList(MANAGER, SUPERVISOR, LOWER_USER), PickShipActivity.class));
//        menuItems.add(new MenuItem(R.string.pick_pack, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER), PickPackShipOrdersActivity.class));
//        menuItems.add(new MenuItem(R.string.nhap_ho_so, R.drawable.ic_menu_item_18, Arrays.asList(MANAGER, FORKLIFT_DRIVER, DOCUMENT, SUPERVISOR), NhapHoSoActivity.class));
//        menuItems.add(new MenuItem(R.string.giao_ho_so, R.drawable.ic_menu_item_13, Arrays.asList(MANAGER, FORKLIFT_DRIVER, DOCUMENT, SUPERVISOR), GiaoHoSoActivity.class));
        // menuItems.add(new MenuItem(R.string.giao_ho_so_offline, R.drawable.ic_menu_item_14, Arrays.asList(MANAGER), DispatchingOrderActivity.class));
        menuItemQHSE = new MenuItem(R.string.ve_sinh_an_toan, R.drawable.ic_menu_item_20, Arrays.asList(MANAGER, SUPERVISOR), QHSEActivity.class);
        menuItems.add(menuItemQHSE);
        //menuItems.add(new MenuItem(R.string.qa_kiem, R.drawable.ic_menu_item_23, Arrays.asList(MANAGER), MetroQACheckingSuppliersActivity.class));
        menuItems.add(new MenuItem(R.string.kiem_container, R.drawable.ic_menu_item_22, Arrays.asList(MANAGER, SUPERVISOR), KiemContainerActivity.class));

//        menuItems.add(new MenuItem(R.string.kiem_ho_so, R.drawable.ic_menu_item_13, Arrays.asList(MANAGER, FORKLIFT_DRIVER, DOCUMENT, SUPERVISOR), KiemHoSoNoEMDKActivity.class));
        menuItems.add(new MenuItem(R.string.nang_suat, R.drawable.ic_menu_item_25, Arrays.asList(MANAGER, NO_POSITION,  SUPERVISOR), NangSuatActivity.class));
        menuItems.add(new MenuItem(R.string.label_nang_suat_nhan_vien, R.drawable.ic_menu_item_23, Arrays.asList(MANAGER, SUPERVISOR), NangSuatNhanVienActivity.class));
        //menuItems.add(new MenuItem(R.string.lich_su_ra_vao, R.drawable.ic_menu_item_24, Arrays.asList(MANAGER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), LichSuRaVaoActivity.class));
        //menuItems.add(new MenuItem(R.string.nhap_ngoai_gio, R.drawable.ic_menu_item_25, Arrays.asList(MANAGER, TECHNICAL, DOCUMENT, SUPERVISOR, TRANSPORTATION), ListOverTimeEntryActivity.class));
        //menuItemGiaoViec = new MenuItem(R.string.giao_viec, R.drawable.ic_menu_item_03, Arrays.asList(MANAGER, TECHNICAL, DOCUMENT, SUPERVISOR, TRANSPORTATION), AssignWorkActivity.class);
        //menuItems.add(menuItemGiaoViec);
        //menuItems.add(new MenuItem(R.string.lich_trinh, R.drawable.ic_menu_item_09, Arrays.asList(MANAGER, TECHNICAL), ScheduleJobActivity.class));
        //menuItems.add(new MenuItem(R.string.lich_lam_viec, R.drawable.ic_menu_item_07, Arrays.asList(MANAGER, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), LichLamViecActivity.class));
//        menuItems.add(new MenuItem(R.string.gps, R.drawable.ic_menu_item_15, Arrays.asList(MANAGER, DOCUMENT, SUPERVISOR), ListUserActivity.class));
        //menuItems.add(new MenuItem(R.string.fixed_asset, R.drawable.ic_menu_item_10, Arrays.asList(MANAGER), EquipmentInventoryNoEMDKActivity.class));
        //menuItems.add(new MenuItem(R.string.opportunity_manager, R.drawable.ic_menu_item_03, Arrays.asList(MANAGER), ListOpportunityActivity.class));
        //menuItems.add(new MenuItem(R.string.calendar, R.drawable.ic_menu_item_09, Arrays.asList(MANAGER, SUPERVISOR), CRMActivity.class));
        //menuItems.add(new MenuItem(R.string.label_maintenance, R.drawable.ic_menu_item_01, Arrays.asList(MANAGER, TECHNICAL), MaintenanceActivity.class));
        //menuItems.add(new MenuItem(R.string.label_bookings, R.drawable.ic_menu_item_06, Arrays.asList(MANAGER), BookingActivity.class));
        menuItems.add(new MenuItem(R.string.main_label_kvs, R.drawable.ic_menu_item_23, Arrays.asList(MANAGER, SUPERVISOR), KiemVeSinhActivity.class));
        menuItems.add(new MenuItem(R.string.label_register, R.drawable.ic_menu_item_07, Arrays.asList(MANAGER), RegisterActivity.class));

        menuItems.add(new MenuItem(R.string.label_device_info, R.drawable.ic_menu_item_05, Arrays.asList(MANAGER), DeviceInfoActivity.class));
//        menuItems.add(new MenuItem(R.string.receiving_vinmart, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), ScanVinmartDetailActivity.class));
//        menuItems.add(new MenuItem(R.string.packing_vinmart, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), GroupVinmartActivity.class));
//        menuItems.add(new MenuItem(R.string.packing_vinmart2, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), ABAGroupVinmartActivity.class));
//        menuItems.add(new MenuItem(R.string.packing_bhx, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), ABAGroupBHXActivity.class));
//        menuItems.add(new MenuItem(R.string.packing_newzealand, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), ABAGroupNewzealandActivity.class));
//        menuItems.add(new MenuItem(R.string.receiving_review_vinmart, R.drawable.ic_menu_item_07, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), ScanVinmartActivity.class));
//        menuItems.add(new MenuItem(R.string.packing_review_vinmart, R.drawable.ic_menu_item_07, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), PackingReviewActivity.class));
//        menuItems.add(new MenuItem(R.string.packing_order, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), DispatchingOrderPackingActivity.class));
//        menuItems.add(new MenuItem(R.string.check_out, R.drawable.ic_menu_item_22, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), CheckOutTripListActivity.class));
//        menuItems.add(new MenuItem(R.string.packing_satus, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), CheckStatusPickPackShipActivity.class));
//        menuItems.add(new MenuItem(R.string.packing_masan, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), ScanMasanActivity.class));
//        menuItems.add(new MenuItem(R.string.packing_chia_hang, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), CustomerActivity.class));
        menuItems.add(new MenuItem(R.string.ghi_so_ki, R.drawable.ic_menu_item_07, Arrays.asList(MANAGER, SUPERVISOR), GhiSoKiActivity.class));
//        menuItems.add(new MenuItem(R.string.shift_confirm, R.drawable.ic_menu_item_07, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), DoiChieuActivity.class));

        //menuItems.add(new MenuItem(R.string.QL_KhayRo, R.drawable.ic_baseline_shopping_basket_24, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), BasketRouteActivity.class));
        return menuItems;
    }

    private static List<MenuItem> createMenuHaNam() {
        List<MenuItem> menuItems = new ArrayList<>();
//        menuItems.add(new MenuItem(R.string.pick_pack, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, SUPERVISOR, LOWER_USER), PickPackShipOrdersActivity.class));
        menuItems.add(new MenuItem(R.string.nhap, R.drawable.ic_menu_item_04, Arrays.asList(MANAGER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), HomNayActivity.class, RO));
        menuItems.add(new MenuItem(R.string.xuat, R.drawable.ic_menu_item_04, Arrays.asList(MANAGER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), HomNayActivity.class, DO));
        menuItems.add(new MenuItem(R.string.phieu_hom_nay, R.drawable.ic_menu_item_07, Arrays.asList(MANAGER, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), KhachHangActivity.class));
        menuItems.add(new MenuItem(R.string.save_package, R.drawable.ic_menu_item_05, Arrays.asList(MANAGER, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), SavePackageHNActivity.class));
        menuItemPhieuCuaToi = new MenuItem(R.string.phieu_cua_toi, R.drawable.ic_menu_item_01, Arrays.asList(MANAGER, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), PhieuCuaToiActivity.class);
        menuItems.add(menuItemPhieuCuaToi);
        menuItems.add(new MenuItem(R.string.delivery, R.drawable.ic_menu_item_11, Arrays.asList(MANAGER, SUPERVISOR, LOWER_USER, TRANSPORTATION), TripDeliveryActivity.class));
        menuItems.add(new MenuItem(R.string.difference_check, R.drawable.ic_menu_item_09, Arrays.asList(MANAGER), DifferenceCheckActivity.class));
        menuItems.add(new MenuItem(R.string.doi_mat_khau, R.drawable.ic_menu_item_24, Arrays.asList(MANAGER, NO_POSITION, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), ChangePasswordActivity.class));
        menuItemUpdateVersion = new MenuItem(R.string.cap_nhat_moi, R.drawable.ic_menu_item_03, Arrays.asList(MANAGER, NO_POSITION, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), CapNhatUngDungActivity.class);
        menuItems.add(menuItemUpdateVersion);
        menuItems.add(new MenuItem(R.string.label_device_info, R.drawable.ic_menu_item_11, Arrays.asList(MANAGER), DeviceInfoActivity.class));
        menuItems.add(new MenuItem(R.string.packing_satus, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, SUPERVISOR), CheckStatusPickPackShipActivity.class));
//        menuItems.add(new MenuItem(R.string.check_out, R.drawable.ic_menu_item_22, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), CheckOutTripListActivity.class));
//        menuItems.add(new MenuItem(R.string.packing_masan, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), ItemMasanActivity.class));
//        menuItems.add(new MenuItem(R.string.xdoc_pickinglist, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), XdockingPickingListOrderActivity.class));
        menuItems.add(new MenuItem(R.string.ghi_so_ki, R.drawable.ic_menu_item_07, Arrays.asList(MANAGER, SUPERVISOR), GhiSoKiActivity.class));
//        menuItems.add(new MenuItem(R.string.packing_3f, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), ABARegion3FActivity.class));
//        menuItems.add(new MenuItem(R.string.xdoc_input_carton, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), XdocInputCartonActivity.class));

//        menuItems.add(new MenuItem(R.string.QL_KhayRo, R.drawable.ic_menu_item_24, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), BasketRouteActivity.class));

        return menuItems;
    }

    private static List<MenuItem> createMenuDCMD() {
        List<MenuItem> menuItems = new ArrayList<>();
//        menuItems.add(new MenuItem(R.string.xdoc_pickinglist, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), XdockingPickingListOrderActivity.class));
//        menuItems.add(new MenuItem(R.string.packing_satus, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), CheckStatusPickPackShipActivity.class));
//        menuItems.add(new MenuItem(R.string.xdoc_input_carton, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), XdocInputCartonActivity.class));
        menuItemUpdateVersion = new MenuItem(R.string.cap_nhat_moi, R.drawable.ic_menu_item_03, Arrays.asList(MANAGER, NO_POSITION, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), CapNhatUngDungActivity.class);
        menuItems.add(menuItemUpdateVersion);
//        menuItems.add(new MenuItem(R.string.packing_chia_hang, R.drawable.ic_menu_item_21, Arrays.asList(MANAGER, DILIVERYMAN, BIGC_USER, TRANSPORTATION, LOWER_USER, NO_POSITION, TECHNICAL, FORKLIFT_DRIVER, PRODUCT_CHECKER, DOCUMENT, SUPERVISOR), CustomerActivity.class));

//        menuItems.add(new MenuItem(R.string.QL_KhayRo, R.drawable.ic_menu_item_24, Arrays.asList(MANAGER, DILIVERYMAN, TRANSPORTATION, PRODUCT_CHECKER, SUPERVISOR), BasketRouteActivity.class));
        //duong//menuItems.add(new MenuItem(R.string.QL_KhayRo, R.drawable.ic_baseline_shopping_basket_24, Arrays.asList(MANAGER, DILIVERYMAN, TRANSPORTATION, PRODUCT_CHECKER, SUPERVISOR), BasketRouteActivity.class));

        return menuItems;
    }
    private static List<MenuItem> filter(List<MenuItem> items, int group) {
        List<MenuItem> result = new ArrayList<>();
        for (MenuItem menuItem : items) {
            if (menuItem.getGroups().contains(group)) {
                menuItem.setPosition(result.size());
                result.add(menuItem);
            }
        }
        return result;
    }
}
