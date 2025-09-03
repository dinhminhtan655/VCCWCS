package com.wcs.vcc.api;

import com.google.gson.JsonObject;
import com.squareup.okhttp.RequestBody;
import com.wcs.vcc.api.checkouttrip.request.ScanCheckOutRequest;
import com.wcs.vcc.api.checkouttrip.response.CheckOutTripResponse;
import com.wcs.vcc.api.production_order.ProductionOrderResponse;
import com.wcs.vcc.api.xdoc.BookingInsertPictureParameter;
import com.wcs.vcc.api.xdoc.DeliveryTypeResponse;
import com.wcs.vcc.api.xdoc.request.AssignCartonToPalletRequest;
import com.wcs.vcc.api.xdoc.response.CheckStatusPickPackResponse;
import com.wcs.vcc.api.xdoc.response.PackingListParent;
import com.wcs.vcc.api.xdoc.response.PaletInfor;
import com.wcs.vcc.api.xdoc.response.XdocDispatchingOrderResponse;
import com.wcs.vcc.login.LoginInfo;
import com.wcs.vcc.main.QLKhayRo.models.BasketCCDCRequest;
import com.wcs.vcc.main.QLKhayRo.models.BasketCCDCResponse;
import com.wcs.vcc.main.QLKhayRo.models.BasketConfirmRequest;
import com.wcs.vcc.main.QLKhayRo.models.BasketCustomer;
import com.wcs.vcc.main.QLKhayRo.models.BasketCustomerRequest;
import com.wcs.vcc.main.QLKhayRo.models.BasketDefaultRequest;
import com.wcs.vcc.main.QLKhayRo.models.BasketDefaultResponse;
import com.wcs.vcc.main.QLKhayRo.models.BasketPackRequest;
import com.wcs.vcc.main.QLKhayRo.models.BasketPackResponse;
import com.wcs.vcc.main.QLKhayRo.models.BasketRouteDetailRequest;
import com.wcs.vcc.main.QLKhayRo.models.BasketRouteDetailResponse;
import com.wcs.vcc.main.QLKhayRo.models.BasketRouteRequest;
import com.wcs.vcc.main.QLKhayRo.models.BasketRouteResponse;
import com.wcs.vcc.main.QLKhayRo.models.KhayRoRequest;
import com.wcs.vcc.main.QLKhayRo.models.KhayRoResponse;
import com.wcs.vcc.main.QLKhayRo.models.PickPutAwaySync;
import com.wcs.vcc.main.QLKhayRo.models.PickPutSyncBarcodeScan;
import com.wcs.vcc.main.QLKhayRo.models.UpdateBasketPackRequest;
import com.wcs.vcc.main.QLKhayRo.models.UpdateKhayRoRequest;
import com.wcs.vcc.main.QLKhayRo.models.UpdateKhayRoResponse;
import com.wcs.vcc.main.TimeServer;
import com.wcs.vcc.main.UpdateStoreResult;
import com.wcs.vcc.main.VersionInfo;
import com.wcs.vcc.main.bigc.Basket;
import com.wcs.vcc.main.bigc.BasketMovement;
import com.wcs.vcc.main.bigc.Product;
import com.wcs.vcc.main.bigc.ProductUpdate;
import com.wcs.vcc.main.bigc.Store;
import com.wcs.vcc.main.bigc.Supplier;
import com.wcs.vcc.main.bigcqa.bigccheckout.BigDock_TripDetails;
import com.wcs.vcc.main.booking.Booking;
import com.wcs.vcc.main.capdau.DistributionOrderView;
import com.wcs.vcc.main.capdau.Driver;
import com.wcs.vcc.main.capdau.TruckDriver;
import com.wcs.vcc.main.chuyenhang.ListLocationInfo;
import com.wcs.vcc.main.chuyenhang.LocationInfo;
import com.wcs.vcc.main.chuyenhang.lichsu.StockMovementHistoriesInfo;
import com.wcs.vcc.main.containerandtruckinfor.model.CarNumber;
import com.wcs.vcc.main.containerandtruckinfor.model.ContainerAndTruckInfo;
import com.wcs.vcc.main.containerandtruckinfor.model.Door;
import com.wcs.vcc.main.containerandtruckinfor.model.UserCheckOut;
import com.wcs.vcc.main.crm.Event;
import com.wcs.vcc.main.crm.Guest;
import com.wcs.vcc.main.crm.detail.MeetingDetail;
import com.wcs.vcc.main.detailphieu.OrderDetail;
import com.wcs.vcc.main.detailphieu.RequirementInfo;
import com.wcs.vcc.main.detailphieu.chuphinh.AttachmentInfo;
import com.wcs.vcc.main.detailphieu.chuphinh.OrderInfo;
import com.wcs.vcc.main.detailphieu.chuphinh.OrderNumber;
import com.wcs.vcc.main.detailphieu.so_do_day.LoadingReportInfo;
import com.wcs.vcc.main.detailphieu.worker.EmployeeWorkingTonPerHourInfo;
import com.wcs.vcc.main.detailphieu.worker.ProductView;
import com.wcs.vcc.main.detailphieu.worker.WorkerInfo;
import com.wcs.vcc.main.doichieu.difference_check.DifferenceCheck;
import com.wcs.vcc.main.doichieu.difference_check.current.DifferenceCheckCurrent;
import com.wcs.vcc.main.doichieu.model.ShiftConfirm;
import com.wcs.vcc.main.doichieu.model.ShiftConfirmDetail;
import com.wcs.vcc.main.equipment.EquipmentInventoryInfo;
import com.wcs.vcc.main.giaonhanhoso.DSCartonCategoriesInfo;
import com.wcs.vcc.main.giaonhanhoso.DSDispatchingOrderDetailsInfo;
import com.wcs.vcc.main.giaonhanhoso.DSDispatchingOrdersInfo;
import com.wcs.vcc.main.giaonhanhoso.cartonreturn.DSReceivingCartonReturnListInfo;
import com.wcs.vcc.main.gps.GPSUserInfo;
import com.wcs.vcc.main.gps.GPSViewByUserParameter;
import com.wcs.vcc.main.gps.listuser.UserInfo;
import com.wcs.vcc.main.gskscan.OrderResultsSupervisorScan;
import com.wcs.vcc.main.kiemcontainer.ContainerInfo;
import com.wcs.vcc.main.kiemcontainer.HistoryCheckingInfo;
import com.wcs.vcc.main.kiemcontainer.detail.ContainerDetailInfo;
import com.wcs.vcc.main.kiemhoso.KiemHoSoInfo;
import com.wcs.vcc.main.kiemqa.metroqacheckingcarton.MetroCartonInfo;
import com.wcs.vcc.main.kiemqa.metroqacheckinglistproducts.QACheckingListProductsInfo;
import com.wcs.vcc.main.kiemqa.metroqacheckingproduct.MetroCheckingProductInfo;
import com.wcs.vcc.main.kiemqa.metroqacheckingsuppliers.MetroQAInfo;
import com.wcs.vcc.main.kiemvesinh.HouseKeepingCheck;
import com.wcs.vcc.main.kiemvitri.LocationCheckingInfo;
import com.wcs.vcc.main.len_ha_hang.model.Aisle;
import com.wcs.vcc.main.len_ha_hang.model.Ticket;
import com.wcs.vcc.main.lichlamviec.MyCalendarInfo;
import com.wcs.vcc.main.lichlamviec.WorkingSchedulesEmployeePlanInfo;
import com.wcs.vcc.main.lichlamviec.WorkingSchedulesInfo;
import com.wcs.vcc.main.lichsuravao.EmployeeInOutInfo;
import com.wcs.vcc.main.logrecords.LogRecord;
import com.wcs.vcc.main.mms.MaintenanceJob;
import com.wcs.vcc.main.mms.detail.MaintenanceJobDetail;
import com.wcs.vcc.main.mms.employee.MaintenanceEmployee;
import com.wcs.vcc.main.mms.equipment.Equipment;
import com.wcs.vcc.main.mms.job.JobDaily;
import com.wcs.vcc.main.mms.job.JobDefinition;
import com.wcs.vcc.main.mms.part.PartRemain;
import com.wcs.vcc.main.mms.part.WriteOff;
import com.wcs.vcc.main.nangsuat.EmployeePerformanceInfo;
import com.wcs.vcc.main.nangsuatnhanvien.ComboDepartmentID;
import com.wcs.vcc.main.nangsuatnhanvien.ComboPositionID;
import com.wcs.vcc.main.nangsuatnhanvien.ComboShiftID;
import com.wcs.vcc.main.nangsuatnhanvien.EmployeeWorkingByDate;
import com.wcs.vcc.main.newscanmasan.APIGetNewLablePalletResponse;
import com.wcs.vcc.main.newscanmasan.itemmasan.ParentItemPickingList;
import com.wcs.vcc.main.newscanmasan.itemmasan.XdocPackingPickingListOrderResponse;
import com.wcs.vcc.main.newscanmasan.pickpackshippickinglist.CheckTotalInfo;
import com.wcs.vcc.main.newscanmasan.pickpackshippickinglist.PickingListParent;
import com.wcs.vcc.main.nhaphoso.ReceivingOrderDetailsInfo;
import com.wcs.vcc.main.nhapngoaigio.EmployeeIDFindInfo;
import com.wcs.vcc.main.nhapngoaigio.OverTimeOrderDetailsInfo;
import com.wcs.vcc.main.nhapngoaigio.detail.OverTimeViewInfo;
import com.wcs.vcc.main.nhapngoaigio.detail.PayRollMonthIDInfo;
import com.wcs.vcc.main.nhapphi.ChiPhi;
import com.wcs.vcc.main.opportunity.Customer;
import com.wcs.vcc.main.opportunity.Opportunity;
import com.wcs.vcc.main.opportunity.OpportunityCustomerCategory;
import com.wcs.vcc.main.packingscan.PickPackShipOrder;
import com.wcs.vcc.main.packingscan.carton.Carton;
import com.wcs.vcc.main.packingscan.carton.PackageType;
import com.wcs.vcc.main.packingscan.pack.Pack;
import com.wcs.vcc.main.packingscan.packdetails.PackDetail;
import com.wcs.vcc.main.packingscan.save_package.SavePackageHN;
import com.wcs.vcc.main.palletcartonchecking.MovementHistoryInfo;
import com.wcs.vcc.main.palletcartonchecking.PalletFind;
import com.wcs.vcc.main.palletcartonweighting.PalletCartonWeighting;
import com.wcs.vcc.main.phieucuatoi.PhieuCuaToiInfo;
import com.wcs.vcc.main.phieuhomnay.HomNayInfo;
import com.wcs.vcc.main.phieuhomnay.InOutToDayUnfinishedInfo;
import com.wcs.vcc.main.phieuhomnay.giaoviec.EmployeeInfo;
import com.wcs.vcc.main.phieuhomnay.giaoviec.GiaoViecInfo;
import com.wcs.vcc.main.pickship.PickShip;
import com.wcs.vcc.main.pickship.carton.PickShipCarton;
import com.wcs.vcc.main.pickship.cartonscan.PickShipCartonScan;
import com.wcs.vcc.main.pickship.detail.PickShipDetail;
import com.wcs.vcc.main.scanbarcode.STAndroid_CartonScannedByDO;
import com.wcs.vcc.main.scanbhx.model.ABATimeRaiking;
import com.wcs.vcc.main.scanbhx.model.SealGroup;
import com.wcs.vcc.main.scanhang.model.CustomerScan;
import com.wcs.vcc.main.scanhang.model.ItemScan;
import com.wcs.vcc.main.scanhang.model.MinMaxPalletID;
import com.wcs.vcc.main.scanhang.model.PrinterDevices;
import com.wcs.vcc.main.scanhang.model.ScanPalletCode;
import com.wcs.vcc.main.scannewzealand.model.XDockOutboundPackingViewSupplierProductsNewZealandABA;
import com.wcs.vcc.main.scannewzealand.model.XDockVinOutboundPackingViewNewZealandABA;
import com.wcs.vcc.main.scannewzealand.model.XDockVinOutboundPackingViewSupplierNewZealandABA;
import com.wcs.vcc.main.scanvinmart.CartonListResponse;
import com.wcs.vcc.main.scanvinmart.LoadPackInCartonResponse;
import com.wcs.vcc.main.scanvinmart.XDockDeleteCartonDispatchingParam;
import com.wcs.vcc.main.scanvinmart.XdocScanCartonParam;
import com.wcs.vcc.main.scanvinmart.Xdoc_Vin_POListResponse;
import com.wcs.vcc.main.services.NotificationInfo;
import com.wcs.vcc.main.soki.model.ProductId;
import com.wcs.vcc.main.soki.model.ProductKg;
import com.wcs.vcc.main.soki.model.RO;
import com.wcs.vcc.main.technical.assign.AssignWorkInfo;
import com.wcs.vcc.main.technical.schedulejobplan.ScheduleJobPlanInfo;
import com.wcs.vcc.main.tonkho.detailkhachhang.StockOnHandByCustomerInfo;
import com.wcs.vcc.main.tonkho.detailproduct.StockOnHandDetailsInfo;
import com.wcs.vcc.main.tonkho.khachhang.StockOnHandInfo;
import com.wcs.vcc.main.tripdelivery.TripDelivery;
import com.wcs.vcc.main.tripdelivery.deliverydetail.TripDeliveryOrderDetail;
import com.wcs.vcc.main.tripdelivery.orderlist.TripDeliveryDetail;
import com.wcs.vcc.main.tripdelivery.productdetails.TripDeliveryProductDetails;
import com.wcs.vcc.main.tripdelivery.statuslist.TripDeliveryStatusList;
import com.wcs.vcc.main.vesinhantoan.CommentInfo;
import com.wcs.vcc.main.vesinhantoan.QHSEInfo;
import com.wcs.vcc.main.vitritrong.FreeLocationDetailsInfo;
import com.wcs.vcc.main.vitritrong.FreeLocationInfo;
import com.wcs.vcc.main.vo.PalletPickPut;
import com.wcs.vcc.main.vo.PickPut;
import com.wcs.vcc.main.vo.QaPoProduct;
import com.wcs.vcc.roomdb.models.PickPutDetailOffline;
import com.wcs.vcc.roomdb.models.PickPutOrderDetailRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;


public interface MyRequests {

    @POST("/api/login")
    Call<LoginInfo> signIn(@Body LoginParam loginParam);

    @POST("/api/RegisterUser")
    Call<String> register(@Body RegisterParameter parameter);

    @POST("/api/LogOut")
    Call<String> signOut(@Body String userName);

    @POST("/api/InOutToday")
    Call<List<HomNayInfo>> getPhieuHomNay(@Body InOutToDayInfo toDayInfo);

    @POST("/api/InOutTodayUnFinish")
    Call<List<InOutToDayUnfinishedInfo>> getPhieuCustomer(@Body InOutToDayUnfinishedParameter unFinishInfo);

    @POST("/api/MyOrders")
    Call<List<PhieuCuaToiInfo>> getPhieuCuaToi(@Body MyOrderInfo myOrderInfo);

    @POST("/api/InOutAvailableForSupervisor")
    Call<List<PhieuCuaToiInfo>> getInOutAvailableForSupervisor(@Body InOutAvailableForSupervisorParameter myOrderInfo);

    @POST("/api/OrdersResult")
    Call<List<OrderDetail>> getDetailPhieu(@Body OrdersInfo orderNumber);

    @POST("/api/LoadingReport")
    Call<List<LoadingReportInfo>> getLoadingReport(@Body LoadingReportParameter parameter);

    @POST("/api/LoadingReportInsert")
    Call<String> executeLoadingReportInsert(@Body LoadingReportInsertParameter parameter);

    @POST("/api/LoadingReportUpdate")
    Call<String> executeLoadingReportUpdate(@Body LoadingReportUpdateParameter parameter);

    @POST("/api/CustomerRequirement")
    Call<List<RequirementInfo>> getRequestPhieu(@Body CustomerRequirementParam params);

    @POST("/api/EmployeeWorkingAssign")
    Call<List<GiaoViecInfo>> getGiaoViec(@Body GiaoViecParameter parameter);

    @POST("/api/EmployeePresent")
    Call<List<EmployeeInfo>> getEmployeeID(@Body EmployeePresentParameter parameter);

    @POST("/api/LocationList")
    Call<List<ListLocationInfo>> getLocation(@Body ListLocationParameter parameter);

    @POST("/api/EmployeeWorkingDelete")
    Call<String> deleteEmployeeID(@Body DeleteEmployeeIDGiaoViecParameter parameter);

    @POST("/api/EmployeeWorking")
    Call<List<WorkerInfo>> getEmployeeWorking(@Body EmployeeWorkingParameter parameter);

    @POST("/api/EmployeeWorkingInsert")
    Call<String> insertEmployeeWorking(@Body InsertWorkerParameter parameter);

    @POST("/api/EmployeeWorkingISOUpdate")
    Call<String> employeeWorkingISOUpdate(@Body EmployeeWorkingISOUpdateParams parameter);

    @POST("/api/EmployeeWorkingISOCheckInput")
    Call<String> employeeWorkingISOCheckInput(@Body EmployeeWorkingISOCheckInputParams params);

    @POST("/api/EmployeeWorkingTonPerHour")
    Call<List<EmployeeWorkingTonPerHourInfo>> getEmployeeWorkingTonPerHour(@Body String parameter);

    @POST("/api/DispatchingOrderDetailUpdate")
    Call<String> updateDispatchingOrderDetail(@Body UpdateDispatchingOrderDetailParameter parameter);

    @POST("/api/DispatchingOrderScannedDelete")
    Call<String> executeDispatchingOrderScannedDelete(@Body DispatchingOrderScannedDeleteParameter parameter);

    @Multipart
    @POST("/api/PostFileFromAndroid")
    Call<String> uploadFile(@Part("file") RequestBody photo, @Part("filename") RequestBody fileName, @Part("description") RequestBody description);

    @POST("/api/OrderInfoPhoto")
    Call<List<OrderInfo>> getOrderInfo(@Body String parameter);

    @POST("/api/Attachment")
    Call<String> setAttachment(@Body AttachmentParameter parameter);

    @POST("/api/AttachmentView")
    Call<List<AttachmentInfo>> getAttachmentInfo(@Body String orderNumber);

    @POST("/api/ContainerChecking")
    Call<List<ContainerInfo>> getContainerChecking(@Body ContainerCheckingParameter param);

    @POST("/api/ContainerCheckingHistories")
    Call<List<HistoryCheckingInfo>> getHistoryChecking(@Body UUID ContInOutID);

    @POST("/api/ContainerCheckingDetails")
    Call<List<ContainerDetailInfo>> getContainerInfo(@Body ContainerCheckingDetailParameter parameter);

    @POST("/api/ContainerCheckingUpdate")
    Call<String> updateContainerChecking(@Body UpdateContainerCheckingParameter parameter);

    @POST("/api/ContainerCheckingCompleted")
    Call<String> completedChecking(@Body CompletedCheckingParameter parameter);

    @POST("/api/QHSE")
    Call<List<QHSEInfo>> getQHSE(@Body QHSEParameter parameter);

    @POST("/api/QSHEComment")
    Call<List<CommentInfo>> getQHSEComment(@Body CommentParameter parameter);

    @POST("/api/QHSEInsert")
    Call<String> insertQHSE(@Body InsertQHSEParameter parameter);

    @POST("/api/QHSE")
    Call<List<AssignWorkInfo>> getAssignWork(@Body AssignWorkParameter parameter);

    @POST("/api/MMSScheduledJobWeekPlan")
    Call<List<ScheduleJobPlanInfo>> getScheduleJobPlanInfo(@Body ScheduleJobPlanParameter parameter);

    @POST("/api/MMSScheduledJobUpdate")
    Call<String> updateScheduleJobPlan(@Body UpdateScheduleJobPlanParameter parameter);

    @POST("/api/QSHEComment")
    Call<List<com.wcs.vcc.main.technical.assign.CommentInfo>> getAssignWorkComment(@Body CommentParameter parameter);

    @POST("/api/QHSEInsert")
    Call<String> insertAssignWork(@Body InsertAssignWorkParameter parameter);

    @POST("/api/QHSEAssignmentInsert")
    Call<String> executeQHSEAssignmentInsert(@Body QHSEAssignmentInsertParameter parameter);

    @POST("/api/ChangePassword")
    Call<String> changePassword(@Body ChangePasswordParameter parameter);

    @POST("/api/ReceivingOrderDetails")
    Call<List<ReceivingOrderDetailsInfo>> getReceivingOrderDetails(@Body ReceivingOrderDetailParameter parameter);

    @POST("/api/ReceivingLocationUpdate")
    Call<String> updateLocationReceivingOrderDetails(@Body UpdateLocationReceivingOrder parameter);

    @POST("/api/NotificationMainScreen")
    Call<List<NotificationInfo>> getNotification(@Body NotificationParameter parameter);

    @POST("/api/DSDispatchingOrders")
    Call<List<DSDispatchingOrdersInfo>> getDSDispatchingOrders(@Body NotificationParameter parameter);

    @POST("/api/DSReceivingCartonReturnList")
    Call<List<DSReceivingCartonReturnListInfo>> getDSReceivingCartonReturnList(@Body DSCartonCategoriesParameter parameter);

    @POST("/api/DSROCartonReturnAddNew")
    Call<String> executeDSROCartonReturnAddNew(@Body DSROCartonReturnAddNewParameter parameter);

    @POST("/api/DSDispatchingOrderDetails")
    Call<List<DSDispatchingOrderDetailsInfo>> getDSDispatchingOrderDetails(@Body DSOrderDetailParameter parameter);

    @POST("/api/DSRODOCartonUpdate")
    Call<String> executeDSRODOCartonUpdate(@Body DSRODOCartonUpdateParameter parameter);

    @POST("/api/SendMailDSNote")
    Call<String> sendMail(@Body SendMailParameter parameter);

    @POST("/api/DSCartonCategories")
    Call<List<DSCartonCategoriesInfo>> getDSCartonCategories(@Body DSCartonCategoriesParameter parameter);

    @POST("/api/DSCreateNewCarton")
    Call<String> executeDSCreateNewCarton(@Body DSCreateNewCartonParameter parameter);

    @POST("/api/DSROCartonDelete")
    Call<String> executeDSROCartonDelete(@Body DSROCartonDeleteParameter parameter);

    @POST("/api/whcversion")
    Call<List<VersionInfo>> getWHCVersion();

    @POST("/api/FreeLocation")
    Call<List<FreeLocationInfo>> getFreeLocation(@Body int storeId);

    @POST("/api/FreeLocationDetails")
    Call<List<FreeLocationDetailsInfo>> getFreeLocationDetails(@Body FreeLocationDetailsParameter parameter);

    @POST("/api/FreeLocationUpdate")
    Call<String> executeFreeLocationUpdate(@Body int storeId);

    @POST("/api/PalletFind")
    Call<List<PalletFind>> getPalletFind(@Body PalletFindParameter parameter);

    @POST("/api/PalletActualQuantityUpdate")
    Call<String> updateActualPalletQuantity(@Body PalletActualQuantityUpdateParam parameter);

    @POST("/api/MovementsHistories")
    Call<List<MovementHistoryInfo>> getMovementHistory(@Body String parameter);

    @POST("/api/EmployeePerformance")
    Call<List<EmployeePerformanceInfo>> getEmployeePerformance(@Body EmployeePerformanceParameter parameter);

    @POST("/api/EmployeeInOut")
    Call<List<EmployeeInOutInfo>> getEmployeeInOut(@Body EmployeeInOutParameter parameter);

    @POST("/api/GPSInsert")
    Call<String> executeGPSInsert(@Body GPSInsertParameter parameter);

    @POST("/api/InventoryChecking")
    Call<List<KiemHoSoInfo>> getDSInventoryChecking(@Body DSInventoryCheckingParameter parameter);

    @POST("/api/InventoryCheckingDelete")
    Call<String> executeInventoryCheckingDelete(@Body int idInventoryCheckingDelete);

    @POST("/api/WorkingSchedules")
    Call<List<WorkingSchedulesInfo>> getWorkingSchedules(@Body WorkingSchedulesParameter parameter);

    @POST("/api/MyCalendar")
    Call<List<MyCalendarInfo>> getMyCalendar(@Body MyCalendarParameter parameter);

    @POST("/api/MyCalendarDetails")
    Call<List<Event>> getEventsOfDay(@Body WorkingSchedulesParameter parameter);

    @POST("/api/WorkingSchedulesEmployeePlan")
    Call<List<WorkingSchedulesEmployeePlanInfo>> getWorkingSchedulesEmployeePlan(@Body WorkingSchedulesParameter parameter);

    @POST("/api/LocationChecking")
    Call<List<LocationCheckingInfo>> getLocationChecking(@Body LocationCheckingParameter parameter);

    @POST("/api/StockMovement")
    Call<List<LocationInfo>> getStockMovement(@Body StockMovementParameter parameter);

    @POST("/api/StockMovementInsert")
    Call<String> executeStockMovementInsert(@Body StockMovementInsertParameter parameter);

    @POST("/api/StockMovementReversed")
    Call<String> executeStockMovementReversed(@Body StockMovementReversedParameter parameter);

    @POST("/api/StockMovementHistories")
    Call<List<StockMovementHistoriesInfo>> getStockMovementHistories(@Body StockMovementHistoriesParameter parameter);

    @POST("/api/StockOnHand")
    Call<List<StockOnHandInfo>> getStockOnHand(@Body int storeId);

    @POST("/api/StockOnHandByCustomer")
    Call<List<StockOnHandByCustomerInfo>> getStockOnHandByCustomer(@Body StockOnHandByCustomerParameter parameter);

    @POST("/api/StockOnHandDetails")
    Call<List<StockOnHandDetailsInfo>> getStockOnHandDetails(@Body StockOnHandDetailsParameter parameter);

    @POST("/api/EmployeeIDFind")
    Call<List<EmployeeIDFindInfo>> getEmployeeID(@Body EmployeeIDFindParameter parameter);

    @POST("/api/OverTimeEntry")
    Call<String> executeOverTimeEntry(@Body OverTimeEntryParameter parameter);

    @POST("/api/OverTimeDelUpdate")
    Call<String> executeOverTimeDelUpdate(@Body OverTimeDelUpdateParameter parameter);

    @POST("/api/PayRollMonthIDList")
    Call<List<PayRollMonthIDInfo>> getPayRollMonthIDList();

    @POST("/api/OverTimeView")
    Call<List<OverTimeViewInfo>> getOverTimeView(@Body OverTimeViewParameter parameter);

    @POST("/api/OverTimeOrderDetails")
    Call<List<OverTimeOrderDetailsInfo>> getOverTimeOrderDetails(@Body OverTimeOrderDetailsParameter parameter);

    @POST("/api/MetroQACheckingSuppliers")
    Call<List<MetroQAInfo>> getMetroQACheckingSuppliers(@Body String date);

    @POST("/api/MetroQACheckingProductList")
    Call<List<QACheckingListProductsInfo>> getMetroQACheckingListProducts(@Body MetroQACheckingListProductsParameter parameter);

    @POST("/api/MetroQACheckingProduct")
    Call<List<MetroCheckingProductInfo>> getMetroQACheckingProducts(@Body MetroQACheckingProductsParameter parameter);

    @POST("/api/MetroQACheckingCarton")
    Call<List<MetroCartonInfo>> getMetroQACheckingCarton(@Body MetroQACheckingCartonParameter parameter);

    @POST("/api/MetroQACheckingCartonInsert")
    Call<String> executeMetroQACheckingCartonInsert(@Body MetroQACheckingCartonInsertParameter parameter);

    @POST("/api/MetroQACheckingCartonDelUpdate")
    Call<String> executeMetroQACheckingCartonDelUpdate(@Body MetroQACheckingCartonDelUpdateParameter parameter);

    @POST("/api/UserManagement")
    Call<List<UserInfo>> getListUser(@Body short Flag);

    @POST("/api/GPSViewByUser")
    Call<List<GPSUserInfo>> getGPSViewByUser(@Body GPSViewByUserParameter parameter);

    @POST("/api/EquipmentInventory")
    Call<List<EquipmentInventoryInfo>> getEquipmentInventory(@Body EquipmentInventoryParameter parameter);

    @POST("/api/ContainerAndTruckInfor")
    Call<List<ContainerAndTruckInfo>> getContainerAndTruckInfo(@Body ContainerAndTruckInfoParameter param);

    @POST("/api/CRMOpportunities")
    Call<List<Opportunity>> getOpportunities();

    @POST("/api/CRMOpportunitiesCustomerCategory")
    Call<List<OpportunityCustomerCategory>> getOpportunityCustomerCategory();

    @POST("/api/CRMOpportunitiesDetail")
    Call<List<Opportunity>> getOpportunity(@Body UUID opportunityId);

    @POST("/api/CRMOpportunitiesCustomer")
    Call<List<Customer>> getListCustomer(@Body Byte flag);

    @POST("/api/CRMMeetingUsersInsert")
    Call<String> addMeetingUsers(@Body MeetingUserParameter parameter);

    @POST("/api/CRMMeetingUsersUpdate")
    Call<String> updateMeetingUsers(@Body MeetingUserParameter parameter);

    @POST("/api/CRMOpportunitiesUpdate")
    Call<String> updateOpportunity(@Body OpportunityParameter parameter);

    @POST("/api/CRMOpportunitiesDelete")
    Call<String> deleteOpportunity(@Body OpportunityDeleteParameter parameter);

    @POST("/api/CRMOpportunitiesInsert")
    Call<String> addOpportunity(@Body OpportunityParameter parameter);

    @POST("/api/CRMMeetingsInsert")
    Call<String> addMeeting(@Body MeetingParameter parameter);

    @POST("/api/CRMMeetingUpdate")
    Call<String> updateMeeting(@Body MeetingParameter parameter);

    @POST("/api/CRMMeetingDetail")
    Call<List<MeetingDetail>> getMeetingDetail(@Body int id);

    @POST("/api/CRMMeetingDelete")
    Call<String> deleteMeeting(@Body int id);

    @POST("/api/CRMMeetingUserList")
    Call<List<Guest>> getMeetingGuest(@Body MeetingUserParameter parameter);

    @POST("/api/MMSListEquipment")
    Call<List<Equipment>> getListEquipment(@Body EquipmentParameter parameter);

    @POST("/api/MMSListJobDefinition")
    Call<List<JobDefinition>> getListJobDefinition(@Body JobDefinitionParameter param);

    @POST("/api/MMSPartRemain")
    Call<List<PartRemain>> getPartRemain(@Body PartRemainParameter param);

    @POST("/api/MMSViewMaintenanceJob")
    Call<List<MaintenanceJob>> getMaintenanceJob();

    @POST("/api/MMSMaintenanceJobDetails")
    Call<List<MaintenanceJobDetail>> getMaintenanceJobDetail(@Body MaintenanceJobDetailParameter parameter);

    @POST("/api/MMSMaintenanceJobDetailUpdate")
    Call<String> updateMaintenanceJobDetail(@Body MaintenanceJobDetailUpdateParameter parameter);

    @POST("/api/MMSMaintenanceJobDaily")
    Call<List<JobDaily>> getMaintenanceJobDaily(@Body int id);

    @POST("/api/MMSMaintenanceJobWriteOffs")
    Call<List<WriteOff>> getMaintenanceJobWriteOffs(@Body int id);

    @POST("/api/MMSMaintenanceEmployeeView")
    Call<List<MaintenanceEmployee>> getMaintenanceEmployee(@Body MaintenanceJobEmployeeParameter param);

    @POST("/api/MMSScheduledJobMJInsert")
    Call<String> insertMMSSJ(@Body MMSSJParameter param);

    @POST("/api/MMSInsertMaintenanceJob")
    Call<Integer> insertMaintenanceJob(@Body MaintenanceJobParameter param);

    @POST("/api/MMSMaintenanceJobDelete")
    Call<String> deleteMaintenanceJob(@Body int MaintenanceJobID);

    @POST("/api/MMSMaintenanceJobDailyInsert")
    Call<String> insertMaintenanceJobDaily(@Body JobDailyParameter param);

    @POST("/api/MMSMaintenanceJobDailyUpdate")
    Call<String> updateMaintenanceJobDaily(@Body JobDailyParameter param);

    @POST("/api/MMSMaintenanceJobDailyDelete")
    Call<String> deleteMaintenanceJobDaily(@Body int jobDailyId);

    @POST("/api/MMSMaintenanceJobWriteOffsInsert")
    Call<String> insertMaintenanceJobWriteOffs(@Body WriteOffParameter param);

    @POST("/api/MMSMaintenanceJobWriteOffsUpdate")
    Call<String> updateMaintenanceJobWriteOffs(@Body WriteOffParameter param);

    @POST("/api/MMSMaintenanceJobWriteOffsDelete")
    Call<String> deleteMaintenanceJobWriteOffs(@Body int writeOffId);

    @POST("/api/MMSMaintenanceEmployeeInsert")
    Call<String> insertMaintenanceEmployee(@Body MMSEmployeeParameter param);

    @POST("/api/MMSMaintenanceEmployeeUpdate")
    Call<String> updateMaintenanceEmployee(@Body MMSEmployeeParameter param);

    @POST("/api/MMSMaintenanceEmployeeDelete")
    Call<String> deleteMaintenanceEmployee(@Body MMSEmployeeParameter param);

    @POST("/api/ComboDepartmentID")
    Call<List<ComboDepartmentID>> getComboDepartmentID();

    @POST("/api/ComboShiftID")
    Call<List<ComboShiftID>> getComboShiftID();

    @POST("/api/ComboPositionID")
    Call<List<ComboPositionID>> getComboPositionID();

    @POST("/api/EmployeeWorkingByDate")
    Call<List<EmployeeWorkingByDate>> getEmployeeWorkingByDate(@Body EmployeeWorkingByDateParameter param);

    @POST("/api/GetTime")
    Call<List<TimeServer>> getTimeServer();

    @POST("/api/CustomerBookingByTimeSlot")
    Call<List<Booking>> getBookings(@Body CustomerBookingByTimeSlotParameter param);

    @POST("/api/HouseKeepingCheck")
    Call<List<HouseKeepingCheck>> getHouseKeepingCheck(@Body HouseKeepingCheckParameter param);

    @POST("/api/HouseKeepingCheckInsert")
    Call<String> insertHouseKeepingCheck(@Body HouseKeepingCheckInsertParameter param);

    @POST("/api/SCMPurchasingOrderToday")
    Call<List<Supplier>> getSuppliersToday(@Body SuppliersTodayParameter param);

    @POST("/api/SCMSalesOrderBySupplier")
    Call<List<Store>> getStoresOfSupplier(@Body StoresOfSupplierParameter param);

    @POST("/api/SCMSalesOrderProductByStore")
    Call<List<Product>> getProductsOfStore(@Body ProductsOfStoreParameter param);

    @POST("/api/SCMBasketList")
    Call<List<Basket>> getBasketList();

    @POST("/api/SCMSalesOrderProducts")
    Call<List<ProductUpdate>> getSCMSalesOrderProducts(@Body SCMSalesOrderProductsParams params);

    @POST("/api/SCMSalesOrderProductUpdate")
    Call<String> updateProductBigC(@Body ProductUpdateBigCParameter param);

    @POST("/api/SCMBasketMovement")
    Call<List<BasketMovement>> getBasketMovement(@Body BasketMovementParameter param);

    @POST("/api/SCMBasketMovementSupplierInsert")
    Call<String> insertBasketMovement(@Body InsertBasketMovementParameter param);

    @POST("/api/SCMBasketMovementReturnInsert")
    Call<String> insertBasketMovementReturn(@Body InsertBasketMovementReturnParameter param);

    @POST("/api/ChangeStore")
    Call<List<UpdateStoreResult>> updateStore(@Body ChangeStoreParameter param);

    @POST("/api/SCMSalesOrderProductDelete")
    Call<String> deleteSalesOrderProduct(@Body SCMSalesOrderProductParameter param);

    @POST("/api/SCMSalesOrderProductGrossUpdate")
    Call<String> updateSalesOrderProduct(@Body SCMSalesOrderProductParameter param);

    @POST("/api/SCMSalesOrderProductInsert")
    Call<String> insertSalesOrderProduct(@Body SCMSalesOrderProductParameter param);

    @POST("/api/SCMQAPurchasingOrderProducts")
    Call<List<QaPoProduct>> loadQaPurchasingOrderProduct(@Body QaPoProductParam param);

    @POST("/api/SCMQAPurchasingOrderProductUpdate")
    Call<String> updateQaPoProduct(@Body QaPoProductUpdateParam param);

    @POST("/api/ProductView")
    Call<List<ProductView>> loadProductView(@Body ProductViewParam param);

    @POST("/api/PickAndPutAwayView")
    Call<List<PickPut>> loadPickAndPutAwayView(@Body JsonObject body);

    @POST("/api/PickAndPutAwayPalletScan")
    Call<List<PalletPickPut>> loadPickAndPutAwayPalletScan(@Body RequestBody body);

    @POST("/api/PickAndPutAwayLocationScan")
    Call<List<PalletPickPut>> loadPickAndPutAwayLocationScan(@Body PickAndPutAwayLocationScanParam body);

    @POST("/api/PickAndPutAwayPickingPalletScan")
    Call<List<PalletPickPut>> loadPickAndPutAwayPickingPalletScan(@Body PickAndPutAwayLocationScanParam body);

    @POST("/api/PickAndPutAwayLocationScanConfirm")
    Call<List<PalletPickPut>> confirPickAndPutAwayLocationScanConfirm(@Body PickAndPutAwayLocationScanParam body);

    @POST("/api/PickAndPutAwayPickingPalletUpdate")
    Call<String> updatePickAndPutAwayPickingPallet(@Body PickAndPutAwayLocationScanParam body);

    @POST("/api/PalletMovementCheckCode")
    Call<String> PalletMovementCheckCode(@Body PalletMovementCheckCodeParams params);

    @POST("/api/OrderResultsSupervisorScan")
    Call<String> orderResultsSupervisorScan(@Body OrderResultsSupervisorScanParam body);

    @POST("/api/OrderResultsSupervisorScanView")
    Call<List<OrderResultsSupervisorScan>> orderResultsSupervisorScanView(@Body OrderResultsSupervisorScanViewParam body);

    @POST("/api/PalletCartonWeightingView")
    Call<List<PalletCartonWeighting>> getPalletCartonWeightingList(@Body PalletCartonWeightingViewParam body);

    @POST("/api/STAndroid_PalletCartonStock")
    Call<List<PalletCartonWeighting>> getSTAndroid_PalletCartonStock(@Body PalletCartonWeightingViewParam body);

    @POST("/api/PalletCartonStockSearch")
    Call<List<PalletCartonWeighting>> getSTAndroid_PalletCartonStockSearch(@Body PalletCartonWeightingViewParam body);

    @POST("/api/PalletCartonWeightInsert")
    Call<String> insertPalletCartonWeight(@Body PalletCartonWeightInsertParam body);

    @POST("/api/DispatchingCartonInsert")
    Call<String> DispatchingCartonInser(@Body PalletCartonWeightInsertParam body);

    @POST("/api/PalletCartonWeightManualInsert")
    Call<String> manualInsertPalletCartonWeight(@Body PalletCartonWeightManualInsertParam body);

    @POST("/api/PalletCartonWeightDelete")
    Call<String> deletePalletCartonWeight(@Body PalletCartonWeightDeleteParam body);

    @POST("/api/PalletCartonWeightDeleteAll")
    Call<String> deleteAllPalletCartonWeight(@Body PalletCartonWeightDeleteAlParam body);

    @POST("/api/PalletCartonWeightUpdate")
    Call<List<PalletCartonWeighting>> updatePalletCartonWeight(@Body PalletCartonWeightingParam body);

    @POST("/api/ResetPassword")
    Call<String> resetPassword(@Body ResetPasswordParameter parameter);

    @POST("/api/TripDelivery")
    Call<List<TripDelivery>> tripDelivery(@Body TripDeliveryParameter parameter);

    @POST("/api/TripDeliveryProductDetails")
    Call<List<TripDeliveryProductDetails>> getTripDeliveryProductDetails(@Body TripDeliveryProductDetailsParams parameter);

    @POST("/api/TripDeliveryDetails")
    Call<List<TripDeliveryDetail>> tripDeliveryDetail(@Body RequestBody requestBody);

    @POST("/api/TripDeliveryOrderDetails")
    Call<List<TripDeliveryOrderDetail>> tripDeliveryOrderDetail(@Body RequestBody requestBody);

    @POST("/api/TripDeliveryStatusList")
    Call<List<TripDeliveryStatusList>> tripDeliveryStatusList();

    @POST("/api/TripDeliveryDetailStatusUpdate")
    Call<List<TripDeliveryOrderDetail>> tripDeliveryDetailStatusUpdate(@Body TripDeliveryDetailStatusUpdateParameter parameter);

    @POST("/api/DistributionOrderView")
    Call<List<DistributionOrderView>> getDistributionOrderView(@Body DistributionOrderViewParameter parameter);

    @POST("/api/DistributionOrderUpdate")
    Call<String> updateDistributionOrder(@Body DistributionOrderUpdateParameter parameter);

    @POST("/api/comboTruckDriverID")
    Call<List<TruckDriver>> getTruckDrivers(@Body BaseParameter parameter);

    @POST("/api/comboDriverID")
    Call<List<Driver>> getDrivers(@Body BaseParameter parameter);

    @POST("/api/DistributionDriverChange")
    Call<String> changeDriver(@Body DriverChangeParameter parameter);

    @POST("/api/DistributionOrderInsert")
    Call<String> distributionOrderInsert(@Body DistributionOrderInsertParameter parameter);

    @POST("/api/DistributionOrderCheckOut")
    Call<String> checkoutDistributionOrder(@Body DistributionOrderCheckOutParameter parameter);

    @POST("/api/TripPickShip")
    Call<List<PickShip>> pickShip(@Body TripDeliveryParameter parameter);

    @POST("/api/TripPickShipDetails")
    Call<List<PickShipDetail>> pickShipDetail(@Body PickShipDetailParameter parameter);

    @POST("/api/TripPickShipCartons")
    Call<List<PickShipCarton>> pickShipCarton(@Body PickShipDetailParameter parameter);

    @POST("/api/TripPickShipCartonScan")
    Call<List<PickShipCartonScan>> pickShipCartonScan(@Body PickShipCartonScanParameter parameter);

    @POST("/api/PickPackShipOrders")
    Call<List<PickPackShipOrder>> pickPackShipOrders(@Body PickPackShipOrdersParameter parameter);

    @POST("/api/PickPackShipOrderByCustomer")
    Call<List<PickPackShipOrder>> pickPackShipOrderByCustomer(@Body PickPackShipOrdersParameter parameter);

    @POST("/api/PickPackShipCartons")
    Call<List<Carton>> pickPackShipCartons(@Body PickPackShipCartonsParameter parameter);

    @POST("/api/PickPackShipCartonInsert")
    Call<String> insertPickPackShipCarton(@Body PickPackShipCartonInsertParameter parameter);

    @POST("/api/PickPackShipComboPackageType")
    Call<List<PackageType>> pickPackShipComboPackageType();

    @POST("/api/PickPackShipPackageTypeUpdate")
    Call<String> updatePickPackShipPackageType(@Body PickPackShipPackageTypeUpdateParameter parameter);

    @POST("/api/PickPackShipPacks")
    Call<List<Pack>> pickPackShipPacks(@Body PickPackShipPacksParameter parameter);

    @POST("/api/PickPackShipFinishItem")
    Call<String> finishPickPackShipItem(@Body PickPackShipFinishItemParameter parameter);

    @POST("/api/PickPackShipFinishDO")
    Call<String> finishPickPackShipDO(@Body PickPackShipFinishDOParameter parameter);

    @POST("/api/UpdateStatusPacking")
    Call<String> UpdateStatusPacking(@Body RefeshStatusPackingParam parameter);

    @POST("/api/PickPackShipPackScanCarton")
    Call<String> scanCartonPickPackShipPack(@Body PickPackShipPackScanCartonParameter parameter);


    @POST("/api/PickPackShipCartonsComplete")
    Call<String> completePickPackShipCartons(@Body PickPackShipCartonsCompleteParameter parameter);

    @POST("/api/PickPackShipCartonDelete")
    Call<String> deletePickPackShipCarton(@Body PickPackShipCartonDeleteParameter parameter);

    @POST("/api/PickPackShipPackDelete")
    Call<String> deletePickPackShipPack(@Body PickPackShipPackDeleteParameter parameter);

    @POST("/api/PickPackShipPackDetails/Post")
    Call<List<PackDetail>> getPackDetails(@Body PickPackShipPackDetailsParameter pickPackShipPackDetailsParameter);

    @POST("/api/DifferenceCheck2")
    Call<List<DifferenceCheck>> getDifferenceCheck(@Body int storeId);

    @POST("/api/DifferenceCheck_CurrentQtyByPalletID")
    Call<List<DifferenceCheckCurrent>> getDifferenceCheckCurrent(@Body DifferenceCheckCurrentParams params);

    @POST("/api/DifferenceCheck_PalletUpdate")
    Call<String> updateDifferenceCheckPallet(@Body DifferenceCheckPalletUpdateParams params);

    @POST("/api/PickPackShipPackScan")
    Call<String> scanPickPackShipPack(@Body PickPackShipPackScanParameter parameter);

    @POST("/api/PickPackShipPackScanFixWeight")
    Call<String> scanPickPackShipPackFixWeight(@Body PickPackShipPackScanParameter parameter);

    @POST("/api/XdocPackingScan")
    Call<String> XdocPackingScan(@Body PickPackShipPackScanParameter parameter);

    @POST("/api/XdocPickingListOrder/Scan")
    Call<XdocPackingPickingListOrderResponse> XdocPickingListScan(@Body PickPackShipPackScanParameter parameter);

    @POST("/api/PickinglistScanInsert")
    Call<String> savePickPackShipPack(@Body SavePackShipPackScanParameter params);

    @POST("/api/PickinglistScanView")
    Call<List<SavePackageHN>> getListSavePackageHN(@Body SaveListPackShipPackScanParameter params);

    @POST("/api/PickinglistScanDelete")
    Call<String> deleteSavePackageHN(@Body DeleteSavePackageParameter params);

    @POST("/api/DispatchingCartonDelete")
    Call<String> DeleteDO_byID(@Body DeleteDOParameter params);

    @POST("/api/PickinglistCreateEDI")
    Call<String> PickinglistCreateEDISavePackageHN(@Body PickinglistCreateEDISavePackageHNParameter params);

    @POST("/api/CostDefinition")
    Call<List<ChiPhi>> getListNameChiPhi();

    @POST("/api/STAndroid_BarcodeScan_Dispatch_ByDO/Post")
    Call<String> scanMassan(@Body JsonObject jsonObject);

    @POST("/api/STAndroid_BarcodeScan_Dispatch_ByDO_Location/Post")
    Call<String> scanMassanByPallet(@Body JsonObject jsonObject);

    @POST("/api/STAndroid_CartonScannedByDO")
    Call<List<STAndroid_CartonScannedByDO>> loadScanMassan(@Body JsonObject jsonObject);

    @POST("/api/ComboCustomer/Post")
    Call<List<ComboCustomerResult>> loadListCustomer(@Body JsonObject jsonObject);

    @GET("/api/APIDeliveryType/Get")
    Call<List<DeliveryTypeResponse>> APIDeliveryType(@Query("CustomerID") String CustomerID, @Query("DispatchingOrderDate") String DispatchingOrderDate);

    @POST("/api/PalletToLocation/Post")
    Call<String> PalletToLocation(@Body PalletToLocationParam param);

    @POST("/api/XDockVin_Inbound_SupplierSummary")
    Call<List<XDockVinInboundSupplierSummary>> loadXDockVinInboundSupplierSummary(@Body JsonObject jsonObject);

    @POST("/api/XDockVin_Inbound_ItemScan")
    Call<List<XDockVinInboundItemScan>> loadXDockVinInboundItemScan(@Body JsonObject jsonObject);

    @POST("/api/XDockVin_Inbound_WeightReceiveInsert")
    Call<String> insertXDockVinInboundWeightReceive(@Body JsonObject jsonObject);

    @POST("/api/XDockVin_Inbound_WeightReceiveView")
    Call<List<XDockVinInboundWeightReceiveView>> loadXDockVinInboundWeightReceiveView(@Body JsonObject jsonObject);

    @POST("/api/XDockVin_Inbound_WeightReceiveEdit")
    Call<String> editXDockVinInboundWeightReceive(@Body JsonObject jsonObject);

    @POST("/api/XDockVin_Inbound_WeightReceiveDelete")
    Call<String> deleteXDockVinInboundWeightReceive(@Body JsonObject jsonObject);

    @POST("/api/XDockVin_Inbound_SupplierDetails")
    Call<List<XDockVinInboundWeightReceiveViewDetail>> loadXDockVinInboundSupplierDetails(@Body JsonObject jsonObject);


    @POST("/api/XDockVin_Outbound_PackingView")
    Call<List<XDockVinOutboundPackingView>> loadXDockVinOutboundPackingView(@Body JsonObject jsonObject);

    @POST("/api/XDockVin_Outbound_PackingScanUpdate")
    Call<String> updateXDockVinOutboundPackingScan(@Body JsonObject jsonObject);

    @POST("/api/XDockVin_Outbound_PackingDetailMove")
    Call<String> updateXDockVinOutboundPackingDetailMove(@Body JsonObject jsonObject);

    @POST("/api/XDockVin_Outbound_PackingSummaryView")
    Call<List<XDockVinOutboundPackingSummaryView>> loadXDockVinOutboundPackingSummaryView(@Body JsonObject jsonObject);

    @POST("/api/XDockVin_Outbound_PackingByStoreView")
    Call<List<XDockVinOutboundPackingByStoreView>> loadXDockVinOutboundPackingByStoreView(@Body JsonObject jsonObject);

    //////////////////////////////////////////////////////////////////////////////////////////////////


    @POST("/api/ABA_XDockVin_Inbound_SupplierSummary")
    Call<List<XDockVinInboundSupplierSummary>> loadXDockVinInboundSupplierSummary_ABA(@Body JsonObject jsonObject);

    @POST("/api/ABA_XDockVin_Inbound_ItemScan")
    Call<List<XDockVinInboundItemScan>> loadXDockVinInboundItemScan_ABA(@Body JsonObject jsonObject);

    @POST("/api/ABA_XDockVin_Inbound_WeightReceiveInsert")
    Call<String> insertXDockVinInboundWeightReceive_ABA(@Body JsonObject jsonObject);

    @POST("/api/ABA_XDockVin_Inbound_WeightReceiveView")
    Call<List<XDockVinInboundWeightReceiveView>> loadXDockVinInboundWeightReceiveView_ABA(@Body JsonObject jsonObject);

    @POST("/api/ABA_XDockVin_Inbound_WeightReceiveEdit")
    Call<String> editXDockVinInboundWeightReceive_ABA(@Body JsonObject jsonObject);

    @POST("/api/ABA_XDockVin_Inbound_WeightReceiveDelete")
    Call<String> deleteXDockVinInboundWeightReceive_ABA(@Body JsonObject jsonObject);

    @POST("/api/ABA_XDockVin_Inbound_SupplierDetails")
    Call<List<XDockVinInboundWeightReceiveViewDetail>> loadXDockVinInboundSupplierDetails_ABA(@Body JsonObject jsonObject);

    @POST("/api/ABA_XDockVin_Outbound_PackingView")
    Call<List<XDockVinOutboundPackingViewABA>> loadXDockVinOutboundPackingView_ABA(@Body JsonObject jsonObject);

    @POST("/api/ABA_XDockVin_Outbound_PackingDetailMove")
    Call<String> updateXDockVinOutboundPackingDetailMove_ABA(@Body JsonObject jsonObject);

    @POST("/api/ABA_XDockVin_Outbound_PackingSummaryView")
    Call<List<XDockVinOutboundPackingSummaryView>> loadXDockVinOutboundPackingSummaryView_ABA(@Body JsonObject jsonObject);

    @POST("/api/ABA_XDockVin_Outbound_PackingByStoreView")
    Call<List<XDockVinOutboundPackingByStoreView>> loadXDockVinOutboundPackingByStoreView_ABA(@Body JsonObject jsonObject);

    ///////////////////////////////////////////////////BHX////////////////////////////////////////////////////////////////////

    @POST("/api/ABA_XDockBHX_Outbound_PackingView")
    Call<List<XDockVinOutboundPackingViewABA>> loadXDockBHXOutboundPackingView(@Body JsonObject jsonObject);

    @POST("/api/ABA_XDockBHX_Outbound_PackingDetailMove")
    Call<String> updateXDockBHXOutboundPackingDetailMove(@Body JsonObject jsonObject);


    //Xdoc
    @GET("/api/XDoc/DispatchingOrderByDate")
    Call<XdocDispatchingOrderResponse> loadDispatchingOrderByDate(@Query("StoreID") Integer StoreID, @Query("OrderDate") String OrderDate);

    @GET("/api/XDoc/DispatchingCartonByDO")
    Call<XdocDispatchingOrderResponse> loadDispatchingCartonByDO(@Query("StoreNumber") Integer CustomerClientID, @Query("OrderDate") String OrderDate, @Query("DispatchingOrderNumber") String DispatchingOrderNumber);

    @POST("/api/XdocAssignCarton")
    Call<XdocDispatchingOrderResponse> assginCartonToPallet(@Body AssignCartonToPalletRequest request);

    @GET("api/XDoc/LoadPackingList")
    Call<PackingListParent> loadPackingList(@Query("ProductNumber") String ProductNumber, @Query("OrderDate") String OrderDate);

    @GET("api/PickPackShipLoadTotalCarton/getDataCartonByPallet")
    Call<CheckTotalInfo> pickPackShipLoadTotalCarton(@Query("CustomerID") String CustomerID, @Query("DispatchingOrderDate") String DispatchingOrderDate, @Query("Pallet") Integer Pallet,@Query("PackagedItem") String PackagedItem, @Query("DeliveryType") String DeliveryType);

    @POST("api/PickPackShipLoadTotalCarton/saveCarton")
    Call<String> saveCarton(@Query("DispatchingOrderID") String DispatchingOrderID, @Body JsonObject jsonObject);

    @POST("api/XdocScanning")
    Call<String> scanPackingList(@Body JsonObject jsonObject);

    //CheckOutScanning
    @GET("/api/CheckOutTrip/FindAllTripByDate")
    Call<CheckOutTripResponse> loadTripInforCheckOut(@Query("StoreID") Integer StoreID, @Query("TripDate") String TripDate);

    @GET("/api/CheckOutTrip/FindTripTripNumber")
    Call<CheckOutTripResponse> loadCustomerByTrip(@Query("TripNumber") String TripNumber);

    @POST("/api/CheckOutTrip/Scan")
    Call<String> scanCheckOut(@Body ScanCheckOutRequest request);

    @POST("/api/CheckOutTrip/ShipConfirm")
    Call<String> scanShipConfirm(@Query("TripNumber") String TripNumber,@Body ScanCheckOutRequest request);

    @GET("/api/CheckOutTrip/CartonStatusByDO")
    Call<CheckOutTripResponse> loadCartonByCustomerClient(@Query("DispatchingOrderNumber") String DispatchingOrderNumber);

    @POST("/api/XdocCompleteCartonByDate")
    Call<String> updateDongThung(@Body JsonObject jsonObject);

    @POST("/api/PackingStatus")
    Call<ArrayList<CheckStatusPickPackResponse>> packingStatus(@Body JsonObject jsonObject);

    @GET("/api/PickPackShipPickingList/LoadPickingListDetails")
    Call<PickingListParent> loadPickingList(@Query("CustomerID") String CustomerID, @Query("DispatchingOrderDate") String DispatchingOrderDate,
                                            @Query("BarcodeString") String BarcodeString,   @Query("PalletFrom") Integer PalletFrom, @Query("PalletTo") Integer PalletTo);
    @GET("/api/XdocPickingListOrder/LoadPickingList")
    Call<PickingListParent> pickingListLoadPickingListDetails(@Query("CustomerID") String CustomerID, @Query("DispatchingOrderDate") String DispatchingOrderDate,
                                                              @Query("PickingListOrder") String PickingListOrder,    @Query("BarcodeString") String BarcodeString,@Query("PalletFrom") Integer PalletFrom, @Query("PalletTo") Integer PalletTo,@Query("Lot") String lot);

    @POST("/api/API_Outbound_PackingViewSupplier")
    Call<List<XDockVinOutboundPackingViewSupplier>> loadSupplier(@Body JsonObject jsonObject);

    @POST("/api/XDockVin_Outbound_PackingViewSupplierProducts")
    Call<List<XDockVinOutboundPackingViewSupplierProducts>> loadSupplierProducts(@Body JsonObject jsonObject);

    @GET("/api/PickingListTotal/LoadPickingList")
    Call<ParentItemPickingList> loadItemPickingList(@Query("CustomerID") String CustomerID, @Query("DispatchingOrderDate") String DispatchingOrderDate, @Query("UserName") String UserName);

    @GET("/api/XdocPickingListOrder/LoadPickingListOrder")
    Call<XdocPackingPickingListOrderResponse> loadItemPickingListOrder(@Query("CustomerID") String CustomerID, @Query("DispatchingOrderDate") String DispatchingOrderDate, @Query("PalletID") Integer PalletID,@Query("BarcodeScan") String BarcodeScan );

    @POST("/api/BigDock_TripDetails")
    Call<List<BigDock_TripDetails>> loadBigDockTripDetail(@Body JsonObject jsonObject);

    @POST("/api/BigDock_TripSalesOrderProductsScan")
    Call<String> updateScanCheckOutBigC(@Body JsonObject jsonObject);

    @POST("/api/BigDock_SalesOrderProductBreakTrip")
    Call<String> updateScanCheckOutBreakSOBigC(@Body JsonObject jsonObject);


    @POST("/api/ABA_XDockVin_Outbound_PackingViewSupplier")
    Call<List<XDockVinOutboundPackingViewSupplierABA>> loadSupplierABA(@Body JsonObject jsonObject);

    @POST("/api/ABA_XDockVin_Outbound_PackingViewSupplierProducts")
    Call<List<XDockVinOutboundPackingViewSupplierProductsABA>> loadSupplierProductsABA(@Body JsonObject jsonObject);


    @POST("/api/ABA_XDockBHX_Outbound_PackingViewSupplier")
    Call<List<XDockVinOutboundPackingViewSupplierABA>> loadSupplierABABHX(@Body JsonObject jsonObject);

    @POST("/api/ABA_XDockBHX_Outbound_PackingViewSupplierProducts")
    Call<List<XDockVinOutboundPackingViewSupplierProductsABA>> loadSupplierProductsABABHX(@Body JsonObject jsonObject);

    //Production Order
    @GET("/api/ProductionListIsRunning/ProductionOrderList")
    Call<ProductionOrderResponse> loadProductionOrder(@Query("CustomerID") UUID CustomerID);

    @GET("/api/ABA_Xdoc_Vin_POList")
    Call<Xdoc_Vin_POListResponse> POListByDate(@Query("OrderDate") String OrderDate);

    @GET("/api/CartonListByPO")
    Call<CartonListResponse> CartonListByPO(@Query("OrderDate") String OrderDate, @Query("PalletID") Integer PalletID);

    @GET("/api/LoadPackInCarton")
    Call<LoadPackInCartonResponse> LoadPackInCarton(@Query("DispatchingCartonID") Integer DispatchingCartonID);

    @POST("/api/XdocScanCartonDispatching")
    Call<String> XdocScanCartonDispatching(@Body XdocScanCartonParam parameter);

    @POST("/api/XDockDeleteCartonDispatching")
    Call<String> deleteXdocPickPackShipCarton(@Body XDockDeleteCartonDispatchingParam parameter);


    //-----------------------------------------BHX SEAL---------------------------------------
    @FormUrlEncoded
    @PUT("api/seal/addnewseal")
    retrofit2.Call<String> AddNewSeal(@Field("PalletID") String PalletID,
                                      @Field("SealName") String SealName,
                                      @Field("DeliveryDate") String DeliveryDate,
                                      @Field("UsernameScan") String UsernameScan
    );

    @retrofit2.http.GET("api/seal/getminmaxpallet")
    retrofit2.Call<List<Integer>> GetMinMaxPallet(@retrofit2.http.Query("deliveryDate") String deliveryDate, @retrofit2.http.Query("groupSorting") String groupSorting);

    @retrofit2.http.GET("api/seal/getsealgrouppallet")
    retrofit2.Call<List<SealGroup>> GetSealGroupPallet(@retrofit2.http.Query("deliveryDate") String deliveryDate, @retrofit2.http.Query("groupSorting") String groupSorting, @retrofit2.http.Query("palletID") String palletID);

    @FormUrlEncoded
    @retrofit2.http.POST("api/seal/removeseal")
    retrofit2.Call<String> RemoveSeal(@Field("id") int id);



    @retrofit2.http.POST("api/ABA_XDockNEWZEALAND_Outbound_PackingView")
    retrofit2.Call<List<XDockVinOutboundPackingViewNewZealandABA>> loadXDockNEWZEALANDOutboundPackingView(@retrofit2.http.Body JsonObject jsonObject);

    @retrofit2.http.POST("api/ABA_XDockNEWZEALAND_Outbound_PackingDetailMove")
    retrofit2.Call<String> updateXDockNEWZEALANDOutboundPackingDetailMove(@retrofit2.http.Body JsonObject jsonObject);



    @retrofit2.http.POST("api/ABA_XDockNEWZEALAND_Outbound_PackingViewSupplier")
    retrofit2.Call<List<XDockVinOutboundPackingViewSupplierNewZealandABA>> loadSupplierABANEWZEALAND(@retrofit2.http.Body JsonObject jsonObject);


    @retrofit2.http.POST("api/ABA_XDockNEWZEALAND_Outbound_PackingViewSupplierProducts")
    retrofit2.Call<List<XDockOutboundPackingViewSupplierProductsNewZealandABA>> loadSupplierProductsABANEWZEALAND(@retrofit2.http.Body JsonObject jsonObject);


    @retrofit2.http.POST("api/ABA_XDock3F_Outbound_PackingView")
    retrofit2.Call<List<XDockVinOutboundPackingViewNewZealandABA>> loadXDock3FOutboundPackingView(@retrofit2.http.Body JsonObject jsonObject);

    @retrofit2.http.POST("api/ABA_XDock3F_Outbound_PackingDetailMove")
    retrofit2.Call<String> updateXDock3FOutboundPackingDetailMove(@retrofit2.http.Body JsonObject jsonObject);

    @retrofit2.http.POST("api/ABA_XDock3F_Outbound_PackingViewSupplier")
    retrofit2.Call<List<XDockVinOutboundPackingViewSupplierNewZealandABA>> loadSupplierABA3F(@retrofit2.http.Body JsonObject jsonObject);

    @retrofit2.http.POST("api/ABA_XDock3F_Outbound_PackingViewSupplierProducts")
    retrofit2.Call<List<XDockOutboundPackingViewSupplierProductsNewZealandABA>> loadSupplierProductsABA3F(@retrofit2.http.Body JsonObject jsonObject);


    @POST("/api/LoadOrderByCustomer")
    Call<List<PickPackShipOrder>> loadOrderByCustomer(@Body PickPackShipOrdersParameter parameter);


    //---------------------------Scan Raiking V2--------------------------------

    @retrofit2.http.GET("api/CustomerScan")
    retrofit2.Call<List<CustomerScan>> LoadCustomerScan();

    @retrofit2.http.POST("api/ABA_XDockAll_Outbound_PackingView")
    retrofit2.Call<List<XDockVinOutboundPackingViewNewZealandABA>> loadXDockAllOutboundPackingView(@retrofit2.http.Body JsonObject jsonObject);

    @retrofit2.http.POST("api/ABA_XDockAll_Outbound_PackingDetailMove")
    retrofit2.Call<String> updateXDockAllOutboundPackingDetailMove(@retrofit2.http.Body JsonObject jsonObject);

    @retrofit2.http.POST("api/ABA_XDockAll_Outbound_PackingViewSupplier")
    retrofit2.Call<List<XDockVinOutboundPackingViewSupplierNewZealandABA>> loadSupplierABAAll(@retrofit2.http.Body JsonObject jsonObject);

    @retrofit2.http.POST("api/ABA_XDockAll_Outbound_PackingViewSupplierProducts")
    retrofit2.Call<List<XDockOutboundPackingViewSupplierProductsNewZealandABA>> loadSupplierProductsABAAll(@retrofit2.http.Body JsonObject jsonObject);

    @retrofit2.http.POST("api/ABA_XDockAll_Outbound_CurrentBoxNumber")
    retrofit2.Call<Integer> loadCurrentBoxNumberABAAll(@retrofit2.http.Body JsonObject jsonObject);


    //---------------------------Scan Raiking V2.1--------------------------------

    @retrofit2.http.GET("api/CustomerScan")
    retrofit2.Call<List<CustomerScan>> LoadCustomerScan2();

    @retrofit2.http.POST("api/ABA_XDockAll_Outbound_PackingView")
    retrofit2.Call<List<ItemScan>> loadXDockAllOutboundPackingView2(@retrofit2.http.Body JsonObject jsonObject);

    @retrofit2.http.POST("api/ABA_XDockAll_Outbound_PackingDetailMove")
    retrofit2.Call<String> updateXDockAllOutboundPackingDetailMove2(@retrofit2.http.Body JsonObject jsonObject);

    @retrofit2.http.POST("api/ABA_XDockAll_Outbound_PackingViewSupplier")
    retrofit2.Call<List<XDockVinOutboundPackingViewSupplierNewZealandABA>> loadSupplierABAAll2(@retrofit2.http.Body JsonObject jsonObject);

    @retrofit2.http.POST("api/ABA_XDockAll_Outbound_PackingViewSupplierProducts")
    retrofit2.Call<List<XDockOutboundPackingViewSupplierProductsNewZealandABA>> loadSupplierProductsABAAll2(@retrofit2.http.Body JsonObject jsonObject);

    @retrofit2.http.POST("api/ABA_XDockAll_Outbound_CurrentBoxNumber")
    retrofit2.Call<Integer> loadCurrentBoxNumberABAAll2(@retrofit2.http.Body JsonObject jsonObject);


    @PUT("api/pallet/addnewpalletcode")
    retrofit2.Call<String> AddNewPalletCode(@retrofit2.http.Body List<ScanPalletCode> item2);
    @GET("api/APIGetNewLablePallet/Get")
    Call<List<APIGetNewLablePalletResponse>>AddNewPalletCode(@Query("PalletID") Integer PalletID, @Query("CustomerID") String CustomerID, @Query("ProductGroup") String ProductGroup ) ;

    @retrofit2.http.GET("api/pallet/getitempalletcode/{deliveryDate}/{region}/{customerCode}/{groupSorting}/{flag}")
    retrofit2.Call<List<ScanPalletCode>> GetItemPalletCode(@retrofit2.http.Query("deliveryDate") String deliveryDate, @retrofit2.http.Query("region") String region, @retrofit2.http.Query("customerCode") String customerCode, @retrofit2.http.Query("groupSorting") String groupSorting, @retrofit2.http.Query("flag") String flag);

    @retrofit2.http.POST("api/ABA_XDockAll_Outbound_GetMinMaxPallet")
    retrofit2.Call<MinMaxPalletID> loadMinMaxPalletId(@retrofit2.http.Body JsonObject jsonObject);

    @PUT("api/pallet/updateinactivepallet")
    retrofit2.Call<String> UpdateInActivePallet(@retrofit2.http.Body ScanPalletCode item2);

    @retrofit2.http.POST("api/ABA_TimedRaiking")
    retrofit2.Call<ABATimeRaiking> GetTimeRaiking(@retrofit2.http.Body JsonObject jsonObject);

    //----------------------- Get Printer Devices------------------------------------
    @retrofit2.http.POST("api/GetPrinterDevices")
    retrofit2.Call<List<PrinterDevices>> getPrinterDevices(@retrofit2.http.Body JsonObject jsonObject);

    //---------------------- Ghi số kí -------------------------------------------

    @POST("/api/ProductByCustomerView")
    Call<List<ProductId>> loadProductByCustomer(@Body JsonObject jsonObject);

    @POST("/api/PickinglistManualInsert")
    Call<String> addPickinglistManual(@Body JsonObject jsonObject);

    @POST("/api/PickinglistScanView")
    Call<List<ProductKg>> loadProductKg(@Body JsonObject jsonObject);

    @POST("/api/PickinglistScanUpdate")
    Call<String> updatePickinglistScan(@Body JsonObject jsonObject);

    @POST("/api/ReceivingOrderList")
    Call<List<RO>> loadRO(@Body JsonObject jsonObject);

    @GET("api/APIGetPalletInfor/Get")
    Call<PaletInfor> APIGetPalletInfor(@Query("PalletNumber") Integer PalletNumber);

    //--------------------Lên hạ hàng-----------------------------------

    @POST("/api/PickAndPutAwayByAisleView")
    Call<List<Aisle>> loadPickAndPutAwayByAisleView(@Body JsonObject jsonObject);

    @POST("/api/PickAndPutAwayByOrderView")
    Call<List<Ticket>> loadPickAndPutAwayByOrderView(@Body JsonObject jsonObject);

    //--------------------Shift Confirm--------------------------------

    @retrofit2.http.POST("api/DoiChieu")
    retrofit2.Call<List<ShiftConfirm>> getDoiChieu(@retrofit2.http.Body JsonObject jsonObject);

    @retrofit2.http.POST("api/DoiChieuDetail")
    retrofit2.Call<List<ShiftConfirmDetail>> getDoiChieuDetail(@retrofit2.http.Body JsonObject jsonObject);


    @PUT("api/shiftconfirm/PutShiftConfirm")
    retrofit2.Call<String> UpdateBoxConfirmDriver(@retrofit2.http.Body List<ShiftConfirm> item2);

    @PUT("api/shiftconfirm/PutShiftConfirmDetail")
    retrofit2.Call<String> UpdateQtyConfirmDriver(@retrofit2.http.Body List<ShiftConfirmDetail> item2);

    //--------------------QL Khay rổ--------------------------------
    @POST("/api/BasketCustomer")
    Call<List<BasketCustomer>> getCustomerList(@Body BasketCustomerRequest request);

    @POST("/api/BasketRoute")
    Call<List<BasketRouteResponse>> getRouteList(@Body BasketRouteRequest basketRouteRequest);

    @POST("/api/basketpallet")
    Call<List<BasketRouteDetailResponse>> getRoutePallet(@Body BasketRouteDetailRequest basketRouteDetailRequest);

    @POST("/api/basketsku")
    Call<List<KhayRoResponse>> getKhayRo(@Body KhayRoRequest khayRoRequest);

    @POST("/api/baskettransaction")
    Call<UpdateKhayRoResponse> updateKhayRo(@Body UpdateKhayRoRequest updateKhayRoRequest);

    @POST("/api/basketpack")
    Call<List<BasketPackResponse>> loadDataBasketPack(@Body BasketPackRequest basketPackRequest);

    @POST("api/BasketConfirmPack")
    Call<UpdateKhayRoResponse> updateDataBasketPack(@Body UpdateBasketPackRequest updateBasketPackRequest);

    @POST("api/basketCCDC")
    Call<List<BasketCCDCResponse>> getBasketCCDC(@Body BasketCCDCRequest basketCCDCRequest);

    @POST("api/BasketConfirmSend")
    Call<UpdateKhayRoResponse> basketConfirm(@Body BasketConfirmRequest basketConfirmRequest);

    @POST("api/BasketConfirmReceived")
    Call<UpdateKhayRoResponse> basketConfirmReceived(@Body BasketConfirmRequest basketConfirmRequest);

    @POST("api/basketskudefault")
    Call<List<BasketDefaultResponse>> getBasketName(@Body BasketDefaultRequest request);
    //--------------------QL Khay rổ--------------------------------

    @POST("api/PickAndPutAwayOrderDetailOffline")
    Call<List<PickPutDetailOffline>> getPickPutOrderDetail(@Body PickPutOrderDetailRequest request);

    @FormUrlEncoded
    @POST("api/PickAndPutAwaySynBarcodeScan")
    Call<String> syncDataBarcodeScan(@Body PickPutSyncBarcodeScan request);

    @FormUrlEncoded
    @POST("api/PickAndPutAwaySyn")
    Call<String> syncDataPickPut(@Body PickPutAwaySync request);

    @POST("/api/BookingInsertPicture")
    Call<String> bookingInsertPicture(@Body BookingInsertPictureParameter param);

    @POST("/api/BookingInsertPicture")
    Call<String> bookingInsertPicture2(@Body JsonObject jsonObject);

    @POST("/api/AttachmentViewImage")
    Call<List<AttachmentInfo>> getAttachmentInfoV2(@Body OrderNumber orderNumber);

    @POST("/api/AttachmentViewImage")
    Call<List<AttachmentInfo>> getAttachmentInfoV2(@Body String orderNumber);

    @POST("/api/GateContInOutInsert")
    Call<String> gateContInOutInsert(@Body JsonObject jsonObject);

    @POST("/api/ComboDockDoorID")
    Call<List<Door>> getComboDockDoorID();

    @POST("/api/GateContInOutCheckOut")
    Call<List<CarNumber>> gateContInOutCheckOut(@Body JsonObject jsonObject);

    @POST("/api/ContInOutUpdateRemark")
    Call<String> updateContInOutRemark(@Body JsonObject jsonObject);

    @POST("/api/GetUserOut")
    Call<UserCheckOut> getUserCheckOut(@Body JsonObject jsonObject);


    @POST("/api/ViewLogRecords")
    Call<List<LogRecord>> getViewLogRecords(@Body JsonObject jsonObject);

    @POST("/api/AcceptAll")
    Call<String> updateSTTransactionInsertAll(@Body JsonObject jsonObject);

    @POST("/api/ContainerAndTruckDelete")
    Call<String> deleteContainerAndTruckDelete(@Body JsonObject jsonObject);


    @POST("/api/ContainerAndTruckUpdateTime")
    Call<String> containerAndTruckUpdateTime(@Body JsonObject jsonObject);


    @POST("/api/ContainerAndTruckInforDetails")
    Call<List<ContainerAndTruckInfo>> containerAndTruckInforDetails(@Body JsonObject jsonObject);

}