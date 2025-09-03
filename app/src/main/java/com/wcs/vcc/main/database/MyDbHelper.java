package com.wcs.vcc.main.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.wcs.vcc.main.giaonhanhoso.DSDispatchingOrderDetailsInfo;
import com.wcs.vcc.main.giaonhanhoso.DSDispatchingOrdersInfo;

import java.util.ArrayList;
import java.util.List;


public class MyDbHelper extends SQLiteOpenHelper {
    public static final String TAG = MyDbHelper.class.getSimpleName();

    private static final String DB_NAME = "wcs.db";
    private static final int VERSION = 3;
    private static final String SQL_CREATE_DSDISPATCHINGORDERS = "CREATE TABLE " +
            DSDispatchingOrdersInfo.TABLE_NAME + " (" +
            DSDispatchingOrdersInfo._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DSDispatchingOrdersInfo.DISPATCHING_ORDER_NUMBER + " TEXT," +
            DSDispatchingOrdersInfo.DISPATCHING_ORDER_DATE + " TEXT," +
            DSDispatchingOrdersInfo.REMARK + " TEXT," +
            DSDispatchingOrdersInfo.CUSTOMER_NUMBER + " TEXT," +
            DSDispatchingOrdersInfo.CUSTOMER_NAME + " TEXT," +
            DSDispatchingOrdersInfo.TOTAL_CARTON + " INTEGER," +
            DSDispatchingOrdersInfo.TOTAL_VOLUME + " REAL," +
            DSDispatchingOrdersInfo.ORDER_STATUS + " INTEGER," +
            DSDispatchingOrdersInfo.ORDER_TYPE + " TEXT," +
            DSDispatchingOrdersInfo.STATUS + " INTEGER)";

    private static final String SQL_CREATE_DSDISPATCHINGORDERS_DETAIL = "CREATE TABLE " +
            DSDispatchingOrderDetailsInfo.TABLE_NAME + " (" +
            DSDispatchingOrderDetailsInfo._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DSDispatchingOrderDetailsInfo.CARTON_NEW_ID + " INTEGER," +
            DSDispatchingOrderDetailsInfo.CARTON_DESCRIPTION + " TEXT," +
            DSDispatchingOrderDetailsInfo.CUSTOMER_REF + " TEXT," +
            DSDispatchingOrderDetailsInfo.CARTON_SIZE + " REAL," +
            DSDispatchingOrderDetailsInfo.ORDER_NUMBER + " TEXT," +
            DSDispatchingOrderDetailsInfo.ATTACHMENT_FILE + " TEXT," +
            DSDispatchingOrderDetailsInfo.RESULT + " TEXT," +
            DSDispatchingOrderDetailsInfo.REMARK + " TEXT," +
            DSDispatchingOrderDetailsInfo.SCANNED_TYPE + " INTEGER," +
            DSDispatchingOrderDetailsInfo.IS_RECORD_NEW + " INTEGER," +
            DSDispatchingOrderDetailsInfo.DS_RO_CARTON_ID + " INTEGER," +
            DSDispatchingOrderDetailsInfo.BARCODE + " TEXT," +
            DSDispatchingOrderDetailsInfo.USER_NAME + " TEXT," +
            DSDispatchingOrderDetailsInfo.DONE + " INTEGER)";

    private static final String SQL_DELETE_DSDISPATCHINGORDERS =
            "DROP TABLE IF EXISTS " + DSDispatchingOrdersInfo.TABLE_NAME;
    private static final String SQL_DELETE_DSDISPATCHINGORDERS_DETAIL =
            "DROP TABLE IF EXISTS " + DSDispatchingOrderDetailsInfo.TABLE_NAME;
    public static final int DONE = 0;
    public static final int UNFINISHED = -1;
    public static final int ONCE_SCANNED = 1;

    private MyDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);

    }

    private static MyDbHelper instance;

    public static MyDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new MyDbHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DSDISPATCHINGORDERS);
        db.execSQL(SQL_CREATE_DSDISPATCHINGORDERS_DETAIL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_DSDISPATCHINGORDERS);
        db.execSQL(SQL_DELETE_DSDISPATCHINGORDERS_DETAIL);
        onCreate(db);
    }

    public void saveDispatchingOrders(List<DSDispatchingOrdersInfo> dispatchingOrders) {
        try {
            SQLiteDatabase database = getWritableDatabase();

            for (DSDispatchingOrdersInfo order : dispatchingOrders) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DSDispatchingOrdersInfo.DISPATCHING_ORDER_NUMBER, order.getDispatchingOrderNumber());
                contentValues.put(DSDispatchingOrdersInfo.DISPATCHING_ORDER_DATE, order.getDispatchingOrderDate());
                contentValues.put(DSDispatchingOrdersInfo.REMARK, order.getRemark());
                contentValues.put(DSDispatchingOrdersInfo.CUSTOMER_NUMBER, order.getCustomerNumber());
                contentValues.put(DSDispatchingOrdersInfo.CUSTOMER_NAME, order.getCustomerName());
                contentValues.put(DSDispatchingOrdersInfo.TOTAL_CARTON, order.getTotalCarton());
                contentValues.put(DSDispatchingOrdersInfo.TOTAL_VOLUME, order.getTotalVolume());
                contentValues.put(DSDispatchingOrdersInfo.ORDER_STATUS, order.isOrderStatus() ? 1 : 0);
                contentValues.put(DSDispatchingOrdersInfo.ORDER_TYPE, order.getOrderType());
                contentValues.put(DSDispatchingOrdersInfo.STATUS, order.getStatus());

                long insert = database.insert(DSDispatchingOrdersInfo.TABLE_NAME, null, contentValues);

                Log.d(TAG, "saveDispatchingOrders: " + insert);
            }
        } catch (SQLiteException ex) {
            Log.d(TAG, "saveDispatchingOrders: " + ex.getMessage());
        }

    }

    public List<DSDispatchingOrdersInfo> getDispatchingOrders() {
        SQLiteDatabase database = getReadableDatabase();

        List<DSDispatchingOrdersInfo> dispatchingOrders = new ArrayList<>();
        Cursor cursor = database.query(DSDispatchingOrdersInfo.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            DSDispatchingOrdersInfo order = new DSDispatchingOrdersInfo();
            order.setDispatchingOrderNumber(cursor.getString(cursor.getColumnIndex(DSDispatchingOrdersInfo.DISPATCHING_ORDER_NUMBER)));
            order.setDispatchingOrderDate(cursor.getString(cursor.getColumnIndex(DSDispatchingOrdersInfo.DISPATCHING_ORDER_DATE)));
            order.setRemark(cursor.getString(cursor.getColumnIndex(DSDispatchingOrdersInfo.REMARK)));
            order.setCustomerNumber(cursor.getString(cursor.getColumnIndex(DSDispatchingOrdersInfo.CUSTOMER_NUMBER)));
            order.setCustomerName(cursor.getString(cursor.getColumnIndex(DSDispatchingOrdersInfo.CUSTOMER_NAME)));
            order.setTotalCarton(cursor.getInt(cursor.getColumnIndex(DSDispatchingOrdersInfo.TOTAL_CARTON)));
            order.setTotalVolume(cursor.getFloat(cursor.getColumnIndex(DSDispatchingOrdersInfo.TOTAL_VOLUME)));
            order.setOrderStatus(cursor.getInt(cursor.getColumnIndex(DSDispatchingOrdersInfo.ORDER_STATUS)) == 1);
            order.setOrderType(cursor.getString(cursor.getColumnIndex(DSDispatchingOrdersInfo.ORDER_TYPE)));
            order.setStatus(cursor.getInt(cursor.getColumnIndex(DSDispatchingOrdersInfo.STATUS)));

            dispatchingOrders.add(order);
        }
        cursor.close();

        return dispatchingOrders;
    }

    public void saveDispatchingOrdersDetail(List<DSDispatchingOrderDetailsInfo> odersDetail) {
        try {
            SQLiteDatabase database = getWritableDatabase();
            for (DSDispatchingOrderDetailsInfo order : odersDetail) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DSDispatchingOrderDetailsInfo.CARTON_NEW_ID, order.getCartonNewID());
                contentValues.put(DSDispatchingOrderDetailsInfo.CARTON_DESCRIPTION, order.getCartonDescription());
                contentValues.put(DSDispatchingOrderDetailsInfo.CUSTOMER_REF, order.getCustomerRef());
                contentValues.put(DSDispatchingOrderDetailsInfo.CARTON_SIZE, order.getCartonSize());
                contentValues.put(DSDispatchingOrderDetailsInfo.ORDER_NUMBER, order.getOrderNumber());
                contentValues.put(DSDispatchingOrderDetailsInfo.ATTACHMENT_FILE, order.getAttachmentFile());
                contentValues.put(DSDispatchingOrderDetailsInfo.RESULT, order.getResult());
                contentValues.put(DSDispatchingOrderDetailsInfo.REMARK, order.getRemark());
                contentValues.put(DSDispatchingOrderDetailsInfo.SCANNED_TYPE, order.getScannedType());
                contentValues.put(DSDispatchingOrderDetailsInfo.IS_RECORD_NEW, order.isRecordNew());
                contentValues.put(DSDispatchingOrderDetailsInfo.DS_RO_CARTON_ID, order.getDSROCartonID());
                contentValues.put(DSDispatchingOrderDetailsInfo.BARCODE, "");
                contentValues.put(DSDispatchingOrderDetailsInfo.USER_NAME, "");
                contentValues.put(DSDispatchingOrderDetailsInfo.DONE, (order.getAttachmentFile().length() > 0) ? DONE : UNFINISHED);

                long insert = database.insert(DSDispatchingOrderDetailsInfo.TABLE_NAME, null, contentValues);

                Log.d(TAG, "saveDispatchingOrdersDetail: " + insert);
            }
        } catch (SQLiteException ex) {
            Log.d(TAG, "saveDispatchingOrders: " + ex.getMessage());
        }
    }

    public List<DSDispatchingOrderDetailsInfo> getDispatchingOrderDetails(String orderNumber) {
        SQLiteDatabase database = getReadableDatabase();

        List<DSDispatchingOrderDetailsInfo> orderDetails = new ArrayList<>();

        String selection = DSDispatchingOrderDetailsInfo.ORDER_NUMBER + " =?";
        String[] selectionArgs = {orderNumber};

        Cursor cursor = database.query(DSDispatchingOrderDetailsInfo.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            DSDispatchingOrderDetailsInfo orderDetail = new DSDispatchingOrderDetailsInfo();
            orderDetail.setCartonNewID(cursor.getInt(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.CARTON_NEW_ID)));
            orderDetail.setCartonDescription(cursor.getString(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.CARTON_DESCRIPTION)));
            orderDetail.setCustomerRef(cursor.getString(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.CUSTOMER_REF)));
            orderDetail.setCartonSize(cursor.getFloat(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.CARTON_SIZE)));
            orderDetail.setOrderNumber(cursor.getString(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.ORDER_NUMBER)));
            orderDetail.setAttachmentFile(cursor.getString(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.ATTACHMENT_FILE)));
            orderDetail.setResult(cursor.getString(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.RESULT)));
            orderDetail.setRemark(cursor.getString(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.REMARK)));
            orderDetail.setScannedType(cursor.getInt(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.SCANNED_TYPE)));
            orderDetail.setIsRecordNew(cursor.getInt(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.IS_RECORD_NEW)));
            orderDetail.setDSROCartonID(cursor.getInt(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.DS_RO_CARTON_ID)));

            orderDetails.add(orderDetail);
        }
        cursor.close();

        return orderDetails;
    }

    public List<DSDispatchingOrderDetailsInfo> getDispatchingOrderDetailsHaveChange() {
        SQLiteDatabase database = getReadableDatabase();

        List<DSDispatchingOrderDetailsInfo> orderDetails = new ArrayList<>();

        String selection = DSDispatchingOrderDetailsInfo.DONE + " > 0";
        String[] selectionArgs = {};

        Cursor cursor = database.query(DSDispatchingOrderDetailsInfo.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            DSDispatchingOrderDetailsInfo orderDetail = new DSDispatchingOrderDetailsInfo();
            orderDetail.setCartonNewID(cursor.getInt(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.CARTON_NEW_ID)));
            orderDetail.setCartonDescription(cursor.getString(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.CARTON_DESCRIPTION)));
            orderDetail.setCustomerRef(cursor.getString(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.CUSTOMER_REF)));
            orderDetail.setCartonSize(cursor.getFloat(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.CARTON_SIZE)));
            orderDetail.setOrderNumber(cursor.getString(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.ORDER_NUMBER)));
            orderDetail.setAttachmentFile(cursor.getString(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.ATTACHMENT_FILE)));
            orderDetail.setResult(cursor.getString(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.RESULT)));
            orderDetail.setRemark(cursor.getString(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.REMARK)));
            orderDetail.setScannedType(cursor.getInt(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.SCANNED_TYPE)));
            orderDetail.setIsRecordNew(cursor.getInt(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.IS_RECORD_NEW)));
            orderDetail.setDSROCartonID(cursor.getInt(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.DS_RO_CARTON_ID)));
            orderDetail.setUsername(cursor.getString(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.USER_NAME)));
            orderDetail.setBarcode(cursor.getString(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.BARCODE)));

            orderDetails.add(orderDetail);
        }
        cursor.close();

        return orderDetails;
    }

    public void updateOrderDone(int cartonId) {
        SQLiteDatabase db = getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DSDispatchingOrderDetailsInfo.DONE, DONE);

        String whereClause = DSDispatchingOrderDetailsInfo.CARTON_NEW_ID + "=?";
        String[] whereArgs = {String.valueOf(cartonId)};

        int update = db.update(DSDispatchingOrderDetailsInfo.TABLE_NAME, contentValues, whereClause, whereArgs);
        Log.d(TAG, "updateOrderDone: " + update);
    }

    public void updateSignaturePath(String orderNumber, String pathLocal) {
        SQLiteDatabase db = getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DSDispatchingOrderDetailsInfo.ATTACHMENT_FILE, pathLocal);

        String whereClause = DSDispatchingOrderDetailsInfo.ORDER_NUMBER + "=?";
        String[] whereArgs = {orderNumber};

        int update = db.update(DSDispatchingOrderDetailsInfo.TABLE_NAME, contentValues, whereClause, whereArgs);
        Log.d(TAG, "updateSignature: " + update);
    }

    public void updateOrderDetail(String orderNumber, int cartonId, String username, String barcode, int numberScanned) {
        SQLiteDatabase db = getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DSDispatchingOrderDetailsInfo.USER_NAME, username);
        contentValues.put(DSDispatchingOrderDetailsInfo.BARCODE, barcode);
        contentValues.put(DSDispatchingOrderDetailsInfo.RESULT, "OK");
        contentValues.put(DSDispatchingOrderDetailsInfo.IS_RECORD_NEW, numberScanned);
        contentValues.put(DSDispatchingOrderDetailsInfo.DONE, numberScanned + 1);

        String whereClause = DSDispatchingOrderDetailsInfo.ORDER_NUMBER + "=? AND " + DSDispatchingOrderDetailsInfo.CARTON_NEW_ID + "=?";
        String[] whereArgs = {orderNumber, String.valueOf(cartonId)};

        int update = db.update(DSDispatchingOrderDetailsInfo.TABLE_NAME, contentValues, whereClause, whereArgs);
        Log.d(TAG, "updateSignature: " + update);
    }

    public List<DSDispatchingOrderDetailsInfo> getOrderDetail(String orderNumber, int cartonId) {
        SQLiteDatabase database = getReadableDatabase();

        List<DSDispatchingOrderDetailsInfo> orderDetails = new ArrayList<>();

        String selection = DSDispatchingOrderDetailsInfo.ORDER_NUMBER + " =? AND " + DSDispatchingOrderDetailsInfo.CARTON_NEW_ID + "=?";
        String[] selectionArgs = {orderNumber, String.valueOf(cartonId)};

        Cursor cursor = database.query(DSDispatchingOrderDetailsInfo.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            DSDispatchingOrderDetailsInfo orderDetail = new DSDispatchingOrderDetailsInfo();
            orderDetail.setCartonNewID(cursor.getInt(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.CARTON_NEW_ID)));
            orderDetail.setCartonDescription(cursor.getString(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.CARTON_DESCRIPTION)));
            orderDetail.setCustomerRef(cursor.getString(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.CUSTOMER_REF)));
            orderDetail.setCartonSize(cursor.getFloat(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.CARTON_SIZE)));
            orderDetail.setOrderNumber(cursor.getString(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.ORDER_NUMBER)));
            orderDetail.setAttachmentFile(cursor.getString(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.ATTACHMENT_FILE)));
            orderDetail.setResult(cursor.getString(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.RESULT)));
            orderDetail.setRemark(cursor.getString(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.REMARK)));
            orderDetail.setScannedType(cursor.getInt(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.SCANNED_TYPE)));
            orderDetail.setIsRecordNew(cursor.getInt(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.IS_RECORD_NEW)));
            orderDetail.setDSROCartonID(cursor.getInt(cursor.getColumnIndex(DSDispatchingOrderDetailsInfo.DS_RO_CARTON_ID)));

            orderDetails.add(orderDetail);

            Log.d(TAG, "getOrderDetail: " + new Gson().toJson(orderDetail));
        }
        cursor.close();

        return orderDetails;
    }

    public void insertOrderError(String orderNumber, String username, String barcode, int cartonId) {
        try {
            SQLiteDatabase database = getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(DSDispatchingOrderDetailsInfo.ORDER_NUMBER, orderNumber);
            contentValues.put(DSDispatchingOrderDetailsInfo.USER_NAME, username);
            contentValues.put(DSDispatchingOrderDetailsInfo.BARCODE, barcode);
            contentValues.put(DSDispatchingOrderDetailsInfo.RESULT, "NO");
            contentValues.put(DSDispatchingOrderDetailsInfo.CARTON_NEW_ID, cartonId);

            long insert = database.insert(DSDispatchingOrderDetailsInfo.TABLE_NAME, null, contentValues);

            Log.d(TAG, "insertOrderError: " + insert);
        } catch (SQLiteException ex) {
            Log.d(TAG, "insertOrderError: " + ex.getMessage());
        }

    }

    public void clearAllData() {
        SQLiteDatabase db = getReadableDatabase();

        int delete = db.delete(DSDispatchingOrdersInfo.TABLE_NAME, null, null);
        Log.d(TAG, "clearAllData: DSDispatchingOrdersInfo = " + delete);

        delete = db.delete(DSDispatchingOrderDetailsInfo.TABLE_NAME, null, null);
        Log.d(TAG, "clearAllData: DSDispatchingOrderDetailsInfo = " + delete);
    }
}
