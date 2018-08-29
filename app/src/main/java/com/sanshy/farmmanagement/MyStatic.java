package com.sanshy.farmmanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sanshy.farmmanagement.crops.SingleIncomeCropData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyStatic {

    public static final String CROP_FIRESTORE_KEY = "Crop";
    public static final String ROLES_FIRESOTORE_KEY = "Roles";
    public static final String BUYERS_FIRESTORE_KEY = "Buyers";
    public static final String PARTNERS_FIRESTORE = "Partners";
    public static final String SERVICE_PROVIDERS_FIRESTORE = "ServiceProviders";
    public static final String SERVICE_TYPE = "ServiceType";
    public static final String CROPS_FIRESTORE = "Crops";
    public static final String CROP_TYPE = "CropType";
    public static final String PARTNER_LIST = "PartnerList";
    public static final String BUYER_LIST = "BuyerList";
    public static final String SERVICE_PROVIDER_LIST = "ServiceProviderList";
    public static final String SIDE_INCOME_REMARK = "SideIncomeRemark";
    public static final String OTHER_EXP_REMARK = "OtherExpRemark";
    public static final String HOME_EXP_REMARK = "HomeExpRemark";
    public static final String SIDE_EXP_REMARK = "SideExpRemark";
    public static final String SOWING_CROP_REMARK = "SowingCropRemark";
    public static final String INCOME_CROP_REMARK = "IncomeCropRemark";
    public static final String REAPING_CROP_REMARK = "ReapingCropRemark";
    public static final String ON_A_STANDING_CROP_REMARK = "OnAStandingCropRemark";
    public static final String BACK_UP_CROP = "BackUpCrop";



    public static final String LOAN_ID = "LoanId";
    public static final String LOAN_TYPE = "LoanType";
    public static final String SERVICE_PROVIDER_REMAINING_AMOUNT = "ServiceProviderRemainingAmount";
    public static final String BUYER_REMAIN_AMOUNT = "BuyerRemainAmount";
    public static final String SERVICE_PROVIDER_NAME = "ServiceProviderName";
    public static final String SERVICE_PROVIDER_ID = "ServiceProviderId";
    public static final String PARTNER_NAME = "PartnerName";
    public static final String PARTNER_ID = "PartnerId";
    public static final String BUYER_NAME = "BuyerName";
    public static final String BUYER_ID = "BuyerId";
    public static final String YEAR_OF_REAPING = "YearOfReaping";
    public static final String YEAR_OF_SOWING = "YearOfSowing";
    public static final String CURRENT_CROP_ID = "CurrentCropId";
    public static final String CURRENT_CROP_NAME = "CurrentCropName";
    public static final String HOME_EXP = "HomeExp";
    public static final String OTHER_EXP = "OtherExp";
    public static final String HOME_EXPENDITURE = "HomeExpenditure";
    public static final String OTHER_EXPENDITURE = "OtherExpenditure";
    public static final String SIDE_INCOME = "SideIncome";
    public static final String REPORT_SIDE_BUSINESS = "ReportSideBusiness";
    public static final String EXTRAS_FIRESTORE_KEY = "Extras";
    public static final String SIDE_BUSINESS = "SideBusiness";
    public static final String SIDE_EXPENDITURE = "SideExpenditure";

    public static final int RC_SIGN_IN = 123;
    public static final String DATE_OF_CROP_ADDING = "DateOfCropAdding";
    public static final String EXPENDITURE_FIRESTORE_FIELD_KEY = "Expenditure";
    public static final String INCOME_FIRESTORE_FIELD_KEY = "Income";
    public static final String DATE_SIDE_BUSINESS_KEY = "date";
    public static final String REMARK_SIDE_FOR_ALL = "Remark";
    public static final String AMOUNT_SIDE_FOR_ALL = "Amount";
    public static final String ID_SIDE_FOR_ALL = "Id";
    public static final String DATE_SIDE_FOR_ALL = "date";
    public static final String ID = "Id";
    public static final String NAME = "Name";
    public static final String PHONE = "Phone";
    public static final String ADDRESS = "Address";
    public static final String REMAINING_AMOUNT = "RemainingAmount";
    public static final String DATE = "Date";
    public static final String PARTNER_ID_4 = "PartnerId4";
    public static final String PARTNER_NAME_4 = "PartnerName4";
    public static final String PARTNER_SHARE_4 = "PartnerShare4";
    public static final String PARTNER_ID_3 = "PartnerId3";
    public static final String PARTNER_NAME_3 = "PartnerName3";
    public static final String PARTNER_SHARE_3 = "PartnerShare3";
    public static final String PARTNER_ID_2 = "PartnerId2";
    public static final String PARTNER_NAME_2 = "PartnerName2";
    public static final String PARTNER_SHARE_2 = "PartnerShare2";
    public static final String PARTNER_ID_1 = "PartnerId1";
    public static final String PARTNER_NAME_1 = "PartnerName1";
    public static final String PARTNER_SHARE_1 = "PartnerShare1";
    public static final String LAND_OF_AREA = "LandOfArea";
    public static final String PARTNER = "Partner";
    public static final String PARTNER_COUNT = "PartnerCount";
    public static final String PARTNER_ID_5 = "PartnerId5";
    public static final String PARTNER_NAME_5 = "PartnerName5";
    public static final String PARTNER_SHARE_5 = "PartnerShare5";
    public static final String SOWING = "Sowing";
    public static final String SOWING_CROP = "SowingCrop";
    public static final String SINGLE_CROP = "SingleCrop";
    public static final String ON_A_STANDING_CROP = "OnAStandingCrop";
    public static final String REAPING_CROP = "ReapingCrop";
    public static final String ON_A_STANDING = "OnAStanding";
    public static final String REAPING = "Reaping";
    public static final String REMARK = "Remark";
    public static final String AMOUNT = "Amount";
    public static final String AREA_OF_LAND = "AreaOfLand";
    public static final String CASH_MODE = "Mode";
    public static final String BY_PARTNER = "ByPartner";
    public static final String INCOME_CROP = "IncomeCrop";
    public static final String INCOME = "Income";
    public static final String RATE = "Rate";
    public static final String WEIGHT = "Weight";
    public static final String SELF_INCOME = "SelfIncome";
    public static final String PARTNER_AMOUNT_5 = "PartnerAmount5";
    public static final String PARTNER_AMOUNT_4 = "PartnerAmount4";
    public static final String PARTNER_AMOUNT_3 = "PartnerAmount3";
    public static final String PARTNER_AMOUNT_2 = "PartnerAmount2";
    public static final String PARTNER_AMOUNT_1 = "PartnerAmount1";
    public static final String REPORT_CROP = "ReportCrop";
    public static final String ALL = "All";
    public static final String CASH_AMOUNT = "CashAmount";
    public static final String BORROW_AMOUNT = "BorrowAmount";
    public static final String SELF = "Self";
    public static final String CROP_WAY = "CropWay";
    public static final String SERVICE_PROVIDER = "ServiceProvider";
    public static final String BUYER = "Buyer";
    public static final String PAY = "Pay";
    public static final String HISTORY = "History";
    public static final String PURCHASE = "Purchase";
    public static final String CROP_ID = "CropID";


    public static FirebaseAuth auth = FirebaseAuth.getInstance();
    public static FirebaseUser currentUser = auth.getCurrentUser();
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static DatabaseReference RealtimeDatabase = FirebaseDatabase.getInstance().getReference();

    public static boolean temp  = GetUserId();

    public static String CurrentUserId;
    public static boolean GetUserId(){
        try{
            CurrentUserId = currentUser.getUid();
        }catch (Exception ex){
            CurrentUserId = "000";
        }
        return true;
    }

    public static String getCurrentUserId() {
        return CurrentUserId;
    }

    public static void setCurrentUserId(String currentUserId) {
        CurrentUserId = currentUserId;
    }

    public static final String USERS_DATA_FIRESTORE_KEY = "UsersData";
    public static DatabaseReference mainData = RealtimeDatabase.child(USERS_DATA_FIRESTORE_KEY).child(CurrentUserId);
    public static DatabaseReference serviceType = RealtimeDatabase.child(USERS_DATA_FIRESTORE_KEY).child(CurrentUserId).child(CROP_FIRESTORE_KEY).child(SERVICE_TYPE);
    public static DatabaseReference cropType = RealtimeDatabase.child(USERS_DATA_FIRESTORE_KEY).child(CurrentUserId).child(CROP_FIRESTORE_KEY).child(CROP_TYPE);
    public static DatabaseReference partnerList = RealtimeDatabase.child(USERS_DATA_FIRESTORE_KEY).child(CurrentUserId).child(CROP_FIRESTORE_KEY).child(PARTNER_LIST);
    public static DatabaseReference buyerList = RealtimeDatabase.child(USERS_DATA_FIRESTORE_KEY).child(CurrentUserId).child(CROP_FIRESTORE_KEY).child(BUYER_LIST);
    public static DatabaseReference serviceProviderList = RealtimeDatabase.child(USERS_DATA_FIRESTORE_KEY).child(CurrentUserId).child(CROP_FIRESTORE_KEY).child(SERVICE_PROVIDER_LIST);
    public static DatabaseReference sideExpRemark = RealtimeDatabase.child(USERS_DATA_FIRESTORE_KEY).child(CurrentUserId).child(CROP_FIRESTORE_KEY).child(SIDE_EXP_REMARK);
    public static DatabaseReference sideIncomeRemark = RealtimeDatabase.child(USERS_DATA_FIRESTORE_KEY).child(CurrentUserId).child(CROP_FIRESTORE_KEY).child(SIDE_INCOME_REMARK);
    public static DatabaseReference HomeExpRemark = RealtimeDatabase.child(USERS_DATA_FIRESTORE_KEY).child(CurrentUserId).child(CROP_FIRESTORE_KEY).child(HOME_EXP_REMARK);
    public static DatabaseReference OtherExpRemark = RealtimeDatabase.child(USERS_DATA_FIRESTORE_KEY).child(CurrentUserId).child(CROP_FIRESTORE_KEY).child(OTHER_EXP_REMARK);
    public static DatabaseReference sowingCropRemark = RealtimeDatabase.child(USERS_DATA_FIRESTORE_KEY).child(CurrentUserId).child(CROP_FIRESTORE_KEY).child(SOWING_CROP_REMARK);
    public static DatabaseReference onAStandingCropRemark = RealtimeDatabase.child(USERS_DATA_FIRESTORE_KEY).child(CurrentUserId).child(CROP_FIRESTORE_KEY).child(ON_A_STANDING_CROP_REMARK);
    public static DatabaseReference reapingCropRemark = RealtimeDatabase.child(USERS_DATA_FIRESTORE_KEY).child(CurrentUserId).child(CROP_FIRESTORE_KEY).child(REAPING_CROP_REMARK);
    public static DatabaseReference incomeCropRemark = RealtimeDatabase.child(USERS_DATA_FIRESTORE_KEY).child(CurrentUserId).child(CROP_FIRESTORE_KEY).child(INCOME_CROP_REMARK);




    public static DocumentReference mainDoc = db.collection(USERS_DATA_FIRESTORE_KEY).document(CurrentUserId);
    public static CollectionReference partnerLocation = db.collection(USERS_DATA_FIRESTORE_KEY).document(CurrentUserId).collection(CROP_FIRESTORE_KEY).document(ROLES_FIRESOTORE_KEY).collection(PARTNERS_FIRESTORE);
    public static CollectionReference buyerLocation = db.collection(USERS_DATA_FIRESTORE_KEY).document(CurrentUserId).collection(CROP_FIRESTORE_KEY).document(ROLES_FIRESOTORE_KEY).collection(BUYERS_FIRESTORE_KEY);
    public static CollectionReference serviceProviderLocation = db.collection(USERS_DATA_FIRESTORE_KEY).document(CurrentUserId).collection(CROP_FIRESTORE_KEY).document(ROLES_FIRESOTORE_KEY).collection(SERVICE_PROVIDERS_FIRESTORE);
    public static CollectionReference singleCropLocation = db.collection(USERS_DATA_FIRESTORE_KEY).document(CurrentUserId).collection(CROP_FIRESTORE_KEY).document(ROLES_FIRESOTORE_KEY).collection(CROPS_FIRESTORE);
    public static CollectionReference singleCropBackUpLocation = db.collection(USERS_DATA_FIRESTORE_KEY).document(CurrentUserId).collection(CROP_FIRESTORE_KEY).document(ROLES_FIRESOTORE_KEY).collection(BACK_UP_CROP);
    public static CollectionReference sideExpLocation = db.collection(USERS_DATA_FIRESTORE_KEY).document(CurrentUserId).collection(EXTRAS_FIRESTORE_KEY).document(SIDE_BUSINESS).collection(SIDE_EXPENDITURE);
    public static CollectionReference sideIncomeLocation = db.collection(USERS_DATA_FIRESTORE_KEY).document(CurrentUserId).collection(EXTRAS_FIRESTORE_KEY).document(SIDE_BUSINESS).collection(SIDE_INCOME);
    public static CollectionReference sideReportLocation = db.collection(USERS_DATA_FIRESTORE_KEY).document(CurrentUserId).collection(EXTRAS_FIRESTORE_KEY).document(SIDE_BUSINESS).collection(REPORT_SIDE_BUSINESS);
    public static CollectionReference homeExpLocation = db.collection(USERS_DATA_FIRESTORE_KEY).document(CurrentUserId).collection(EXTRAS_FIRESTORE_KEY).document(HOME_EXPENDITURE).collection(HOME_EXP);
    public static CollectionReference otherExpLocation = db.collection(USERS_DATA_FIRESTORE_KEY).document(CurrentUserId).collection(EXTRAS_FIRESTORE_KEY).document(OTHER_EXPENDITURE).collection(OTHER_EXP);
    public static CollectionReference sowingCropLocation(String CropId){
        return db.collection(USERS_DATA_FIRESTORE_KEY).document(CurrentUserId).collection(CROP_FIRESTORE_KEY).document(SINGLE_CROP).collection(CropId).document(SOWING_CROP).collection(SOWING);
    }
    public static CollectionReference onAStandingCropLocation(String CropId){
        return db.collection(USERS_DATA_FIRESTORE_KEY).document(CurrentUserId).collection(CROP_FIRESTORE_KEY).document(SINGLE_CROP).collection(CropId).document(ON_A_STANDING_CROP).collection(ON_A_STANDING);
    }
    public static CollectionReference reapingCropLocation(String CropId){
        return db.collection(USERS_DATA_FIRESTORE_KEY).document(CurrentUserId).collection(CROP_FIRESTORE_KEY).document(SINGLE_CROP).collection(CropId).document(REAPING_CROP).collection(REAPING);
    }
    public static CollectionReference incomeCropLocation(String CropId){
        return db.collection(USERS_DATA_FIRESTORE_KEY).document(CurrentUserId).collection(CROP_FIRESTORE_KEY).document(SINGLE_CROP).collection(CropId).document(INCOME_CROP).collection(INCOME);
    }
    public static CollectionReference singleCropReportLocation(String CropId,String FilterType){
        return db.collection(USERS_DATA_FIRESTORE_KEY).document(CurrentUserId).collection(CROP_FIRESTORE_KEY).document(SINGLE_CROP).collection(CropId).document(REPORT_CROP).collection(FilterType);
    }
    public static CollectionReference cropReportLocation(String FilterType){
        return db.collection(USERS_DATA_FIRESTORE_KEY).document(CurrentUserId).collection(CROP_FIRESTORE_KEY).document(REPORT_CROP).collection(FilterType);
    }
    public static CollectionReference payBuyerLocation(String BuyerId){
        return db.collection(USERS_DATA_FIRESTORE_KEY).document(CurrentUserId).collection(CROP_FIRESTORE_KEY).document(BUYER).collection(HISTORY).document(BuyerId).collection(PAY);
    }
    public static CollectionReference purchaseBuyerLocation(String BuyerId){
        return db.collection(USERS_DATA_FIRESTORE_KEY).document(CurrentUserId).collection(CROP_FIRESTORE_KEY).document(BUYER).collection(HISTORY).document(BuyerId).collection(PURCHASE);
    }
    public static CollectionReference payServiceProviderLocation(String ServiceProviderId){
        return db.collection(USERS_DATA_FIRESTORE_KEY).document(CurrentUserId).collection(CROP_FIRESTORE_KEY).document(SERVICE_PROVIDER).collection(HISTORY).document(ServiceProviderId).collection(PAY);
    }
    public static CollectionReference purchaseServiceProviderLocation(String ServiceProviderId){
        return db.collection(USERS_DATA_FIRESTORE_KEY).document(CurrentUserId).collection(CROP_FIRESTORE_KEY).document(SERVICE_PROVIDER).collection(HISTORY).document(ServiceProviderId).collection(PURCHASE);
    }
    public static void postSingleReport(final String FilterType, final String CropId, final boolean CashMode, final String AmountSt, final String CropWay){
        singleCropReportLocation(CropId,FilterType).document(CropWay).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> myMap = new HashMap<>();
                double cashAmount = 0;
                double borrowAmount = 0;
                if (documentSnapshot.exists()){

                    Map<String, Object> CloudMap = documentSnapshot.getData();
                    cashAmount = (double) CloudMap.get(MyStatic.CASH_AMOUNT);
                    borrowAmount = (double) CloudMap.get(MyStatic.BORROW_AMOUNT);

                    if (CashMode){
                        cashAmount+= Double.parseDouble(AmountSt);
                    }else{
                        borrowAmount+= Double.parseDouble(AmountSt);
                    }

                }else{

                    if (CashMode){
                        cashAmount+= Double.parseDouble(AmountSt);
                    }else{
                        borrowAmount+= Double.parseDouble(AmountSt);
                    }

                }
                myMap.put(MyStatic.CROP_WAY,CropWay);
                myMap.put(MyStatic.CASH_AMOUNT,cashAmount);
                myMap.put(MyStatic.BORROW_AMOUNT,borrowAmount);

                singleCropReportLocation(CropId,FilterType).document(CropWay).set(myMap);

            }
        });
    }

    public static void lessSingleReport(final boolean CashMode,final String CropId,final String FilterType, final double AmountR,final String CropWay) {
        singleCropReportLocation(CropId, FilterType).document(CropWay).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> myMap = new HashMap<>();
                double cashAmount = 0;
                double borrowAmount = 0;
                Map<String, Object> CloudMap = documentSnapshot.getData();
                cashAmount = (double) CloudMap.get(MyStatic.CASH_AMOUNT);
                borrowAmount = (double) CloudMap.get(MyStatic.BORROW_AMOUNT);

                if (CashMode) {
                    cashAmount -= AmountR;
                } else {
                    borrowAmount -= AmountR;
                }

                myMap.put(MyStatic.CASH_AMOUNT, cashAmount);
                myMap.put(MyStatic.BORROW_AMOUNT, borrowAmount);

                singleCropReportLocation(CropId, FilterType).document(CropWay).update(myMap);

            }
        });
    }
    public static void postAllRemainingReport(final String FilterType, final String AmountSt, final String CropWay){
        cropReportLocation(FilterType).document(CropWay).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> myMap = new HashMap<>();
                double remainingAmount = 0;
                if (documentSnapshot.exists()){

                    Map<String, Object> CloudMap = documentSnapshot.getData();
                    remainingAmount = (double) CloudMap.get(REMAINING_AMOUNT);
                    remainingAmount+= Double.parseDouble(AmountSt);

                }else{
                    remainingAmount+= Double.parseDouble(AmountSt);

                }
                myMap.put(MyStatic.CROP_WAY,CropWay);
                myMap.put(MyStatic.REMAINING_AMOUNT,remainingAmount);

                cropReportLocation(FilterType).document(CropWay).set(myMap);
            }
        });
    }
    public static void lessAllRemainingReport(final String FilterType, final double AmountR, final String CropWay){
        cropReportLocation(FilterType).document(CropWay).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> myMap = new HashMap<>();
                double remainingAmount = 0;
                Map<String, Object> CloudMap = documentSnapshot.getData();
                remainingAmount = (double) CloudMap.get(REMAINING_AMOUNT);
                remainingAmount-= AmountR;

                myMap.put(MyStatic.CROP_WAY,CropWay);
                myMap.put(MyStatic.REMAINING_AMOUNT,remainingAmount);

                cropReportLocation(FilterType).document(CropWay).set(myMap);
            }
        });
    }

    public static void postAllReport(final String FilterType, final boolean CashMode, final String AmountSt, final String CropWay){
        cropReportLocation(FilterType).document(CropWay).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> myMap = new HashMap<>();
                double cashAmount = 0;
                double borrowAmount = 0;
                if (documentSnapshot.exists()){

                    Map<String, Object> CloudMap = documentSnapshot.getData();
                    cashAmount = (double) CloudMap.get(MyStatic.CASH_AMOUNT);
                    borrowAmount = (double) CloudMap.get(MyStatic.BORROW_AMOUNT);

                    if (CashMode){
                        cashAmount+= Double.parseDouble(AmountSt);
                    }else{
                        borrowAmount+= Double.parseDouble(AmountSt);
                    }

                }else{

                    if (CashMode){
                        cashAmount+= Double.parseDouble(AmountSt);
                    }else{
                        borrowAmount+= Double.parseDouble(AmountSt);
                    }

                }
                myMap.put(MyStatic.CROP_WAY,CropWay);
                myMap.put(MyStatic.CASH_AMOUNT,cashAmount);
                myMap.put(MyStatic.BORROW_AMOUNT,borrowAmount);

                cropReportLocation(FilterType).document(CropWay).set(myMap);
            }
        });
    }
    public static void lessAllReport(final boolean CashMode,final String FilterType, final double AmountR,final String CropWay) {
        cropReportLocation(FilterType).document(CropWay).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> myMap = new HashMap<>();
                double cashAmount = 0;
                double borrowAmount = 0;
                Map<String, Object> CloudMap = documentSnapshot.getData();
                cashAmount = (double) CloudMap.get(MyStatic.CASH_AMOUNT);
                borrowAmount = (double) CloudMap.get(MyStatic.BORROW_AMOUNT);

                if (CashMode) {
                    cashAmount -= AmountR;
                } else {
                    borrowAmount -= AmountR;
                }

                myMap.put(MyStatic.CASH_AMOUNT, cashAmount);
                myMap.put(MyStatic.BORROW_AMOUNT, borrowAmount);

                cropReportLocation(FilterType).document(CropWay).update(myMap);

            }
        });
    }

    public static void addRemainingAmountServiceProvider(final String ServiceProviderId, final double newAmount){
        serviceProviderLocation.document(ServiceProviderId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    double oldAmount = (Double) documentSnapshot.get(REMAINING_AMOUNT);
                    double totalAmount = oldAmount + newAmount;

                    Map<String, Object> saveCloud = new HashMap<>();
                    saveCloud.put(REMAINING_AMOUNT,totalAmount);

                    serviceProviderLocation.document(ServiceProviderId).update(saveCloud);
                }
            }
        });
    }
    public static void removeRemainingAmountServiceProvider(final String ServiceProviderId, final double newAmount){
        serviceProviderLocation.document(ServiceProviderId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    double oldAmount = (Double) documentSnapshot.get(REMAINING_AMOUNT);
                    double totalAmount = oldAmount - newAmount;

                    Map<String, Object> saveCloud = new HashMap<>();
                    saveCloud.put(REMAINING_AMOUNT,totalAmount);

                    serviceProviderLocation.document(ServiceProviderId).update(saveCloud);
                }
            }
        });
    }
    public static void addRemainingAmountBuyer(final String BuyerId, final double newAmount){
        buyerLocation.document(BuyerId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    double oldAmount = (Double) documentSnapshot.get(REMAINING_AMOUNT);
                    double totalAmount = oldAmount + newAmount;

                    Map<String, Object> saveCloud = new HashMap<>();
                    saveCloud.put(REMAINING_AMOUNT,totalAmount);

                    buyerLocation.document(BuyerId).update(saveCloud);
                }
            }
        });
    }
    public static void removeRemainingAmountBuyer(final String BuyerId, final double newAmount){
        buyerLocation.document(BuyerId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    double oldAmount = (Double) documentSnapshot.get(REMAINING_AMOUNT);
                    double totalAmount = oldAmount - newAmount;

                    Map<String, Object> saveCloud = new HashMap<>();
                    saveCloud.put(REMAINING_AMOUNT,totalAmount);

                    buyerLocation.document(BuyerId).update(saveCloud);
                }
            }
        });
    }

    public static void setSnipper(Activity activity,AutoCompleteTextView ACTV, ArrayList<String> List){
        ArrayAdapter<String> Adapter = new ArrayAdapter<>(activity,android.R.layout.simple_spinner_dropdown_item,List);
        ACTV.setAdapter(Adapter);
    }

    static ProgressDialog mProgress;
    static String progressText;
    static Context myContext;

    public static void MyProgress(String progressText, Context myContext) {
        mProgress = new ProgressDialog(myContext);
        mProgress.setMessage(progressText);
    }
    public static void MyProgress(Context myContext){
        mProgress = new ProgressDialog(myContext);
        mProgress.setMessage(myContext.getString(R.string.loading));
    }
    public static void ShowProgress(){mProgress.show();}
    public static void HideProgress(){mProgress.hide();}

    public static void ShowProgress(Context myContext){
        mProgress = new ProgressDialog(myContext);
        mProgress.setMessage(myContext.getString(R.string.loading));

        mProgress.show();
    }



    public static void ShowDialog(Context context, String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(text)
                .setPositiveButton(context.getString(R.string.ok),null);

        builder.create().show();
    }
    public static void DateRequestDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.choose_date)
                .setMessage(context.getString(R.string.please_choose_any_date))
                .setPositiveButton(context.getString(R.string.ok),null)
                .create()
                .show();
    }
    public static void ErrorFeedbackDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.error)
                .setMessage(context.getString(R.string.something_is_wrong)+"\n" +
                        context.getString(R.string.try_again_later_or_send_feedback))
                .setPositiveButton(context.getString(R.string.ok),null)
                .create().show();
    }

    public static String DateToString(Date date){
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        return myFormat.format(date);
    }
    public static String TimeStampKey(Date date){
        return  new SimpleDateFormat("ddMMyyyyHHmmss").format(date);
    }
    public static String DateToString2(Date date){
        SimpleDateFormat myFormat = new SimpleDateFormat("ddMMyyyy");
        return myFormat.format(date);
    }

    public static boolean incomeUndo = false;
    public static boolean ShowDetails(Context context,String[] List){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = inflater.inflate(R.layout.custom_show_detail_crops,null,true);

        TextView Remark = myView.findViewById(R.id.remark_dialog);
        TextView Amount = myView.findViewById(R.id.amount_dialog);
        TextView Date = myView.findViewById(R.id.date_dialog);
        TextView ServiceProvider = myView.findViewById(R.id.service_provider_dialog);
        TextView AreaOfLand = myView.findViewById(R.id.area_of_land_dialog);
        TextView PaymentMode = myView.findViewById(R.id.payment_mode_dialog);
        TextView PaidBy = myView.findViewById(R.id.paid_by_dialog);

        Remark.setText(List[0]);
        Amount.setText(List[1]);
        Date.setText(List[2]);
        ServiceProvider.setText(List[3]);
        AreaOfLand.setText(List[4]);
        PaymentMode.setText(List[5]);
        PaidBy.setText(List[6]);

        final ArrayList<Integer> temp = new ArrayList<>();

        builder.setView(myView)
                .setPositiveButton(context.getString(R.string.ok), null);

        builder.setNegativeButton(context.getString(R.string.undo), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                temp.add(1);
            }
        });
        builder.create().show();

        return temp.size() != 0;
    }

    public static boolean ShowDetailsIncome(Context context, SingleIncomeCropData List){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = inflater.inflate(R.layout.custom_show_details_for_income,null,true);

        TextView Remark = myView.findViewById(R.id.remark_dialog);
        TextView RatePerWeight = myView.findViewById(R.id.rate_dialog);
        TextView Weight = myView.findViewById(R.id.weight_dialog);
        TextView Amount = myView.findViewById(R.id.amount_dialog);
        TextView Date = myView.findViewById(R.id.date_dialog);
        TextView BuyerName = myView.findViewById(R.id.buyer_dialog);
        TextView AreaOfLand = myView.findViewById(R.id.area_of_land_dialog);
        TextView PaymentMode = myView.findViewById(R.id.payment_mode_dialog);
        TextView MyIncome = myView.findViewById(R.id.my_income_dialog);

        TableRow Partner1 = myView.findViewById(R.id.partner_1_container);
        TableRow Partner2 = myView.findViewById(R.id.partner_2_container);
        TableRow Partner3 = myView.findViewById(R.id.partner_3_container);
        TableRow Partner4 = myView.findViewById(R.id.partner_4_container);
        TableRow Partner5 = myView.findViewById(R.id.partner_5_container);

        TextView PName1 = myView.findViewById(R.id.partner_name1);
        TextView PName2 = myView.findViewById(R.id.partner_name2);
        TextView PName3 = myView.findViewById(R.id.partner_name3);
        TextView PName4 = myView.findViewById(R.id.partner_name4);
        TextView PName5 = myView.findViewById(R.id.partner_name5);

        TextView PShare1 = myView.findViewById(R.id.partner_share1);
        TextView PShare2 = myView.findViewById(R.id.partner_share2);
        TextView PShare3 = myView.findViewById(R.id.partner_share3);
        TextView PShare4 = myView.findViewById(R.id.partner_share4);
        TextView PShare5 = myView.findViewById(R.id.partner_share5);

        long PartnerCount = List.getPartnerCount();
        int pC = (int) PartnerCount;
        
        switch (pC){
            case 5 :
                Partner5.setVisibility(View.VISIBLE);
                PName5.setText(List.getPartnerName5());
                PShare5.setText(String.valueOf(List.getPartnerShareAmount5()));
            case 4 :
                Partner4.setVisibility(View.VISIBLE);
                PName4.setText(List.getPartnerName4());
                PShare4.setText(String.valueOf(List.getPartnerShareAmount4()));
            case 3 :
                Partner3.setVisibility(View.VISIBLE);
                PName3.setText(List.getPartnerName3());
                PShare3.setText(String.valueOf(List.getPartnerShareAmount3()));
            case 2 :
                Partner2.setVisibility(View.VISIBLE);
                PName2.setText(List.getPartnerName2());
                PShare2.setText(String.valueOf(List.getPartnerShareAmount2()));
            case 1 :
                Partner1.setVisibility(View.VISIBLE);
                PName1.setText(List.getPartnerName1());
                PShare1.setText(String.valueOf(List.getPartnerShareAmount1()));
        }
        
        Remark.setText(List.getIncomeRemark());
        Amount.setText(String.valueOf(List.getIncomeAmount()));
        Date.setText(DateToString(List.getIncomeDate()));
        BuyerName.setText(List.getBuyerCropsName());
        AreaOfLand.setText(String.valueOf(List.getIncomeLandArea()));
        PaymentMode.setText(List.isCashBuyer() ?context.getString(R.string.cash):context.getString(R.string.borrow));
        RatePerWeight.setText(String.valueOf(List.getIncomeCropRate()));
        Weight.setText(String.valueOf(List.getIncomeCropWeight()));
        MyIncome.setText(String.valueOf(List.getSelfShareAmount()));

        final ArrayList<Integer> temp = new ArrayList<>();

        builder.setView(myView)
                .setPositiveButton(context.getString(R.string.ok), null);

        builder.setNegativeButton(context.getString(R.string.undo), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                temp.add(1);
            }
        });
        builder.create().show();

        return temp.size() != 0;
    }

    public static int fday = 0;
    public static int fmonth = 0;
    public static int fYear = 0;
    public static Date staticDate = new Date();
    public static Date singleDateStatic = new Date();
    public static Date fromDateStatic = new Date();
    public static Date toDateStatic = new Date();

    public static boolean ChooseDateDialog(final Context context, final TextView dateShow){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        fday = dayOfMonth;
                        fmonth = ++monthOfYear;
                        fYear = year;

                        Date currentDate = new Date();
                        SimpleDateFormat cHour = new SimpleDateFormat("hh");
                        int hour = Integer.parseInt(cHour.format(currentDate));

                        SimpleDateFormat cMin = new SimpleDateFormat("mm");
                        int min = Integer.parseInt(cMin.format(currentDate));

                        SimpleDateFormat cSecond = new SimpleDateFormat("ss");
                        int sec = Integer.parseInt(cSecond.format(currentDate));

                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(0);
                        cal.set(fYear,fmonth, fday,hour,min,sec);
                        Date chosenDate = cal.getTime();

                        dateShow.setText(fday+"/"+fmonth+"/"+fYear);
                        staticDate = chosenDate;

                    }
                }, mYear, mMonth, mDay);


        datePickerDialog.show();

        return true;
    }

    public static boolean ChooseDateDialogSingle(final Context context, final TextView dateShow){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        fday = dayOfMonth;
                        fmonth = ++monthOfYear;
                        fYear = year;

                        Date currentDate = new Date();
                        SimpleDateFormat cHour = new SimpleDateFormat("hh");
                        int hour = Integer.parseInt(cHour.format(currentDate));

                        SimpleDateFormat cMin = new SimpleDateFormat("mm");
                        int min = Integer.parseInt(cMin.format(currentDate));

                        SimpleDateFormat cSecond = new SimpleDateFormat("ss");
                        int sec = Integer.parseInt(cSecond.format(currentDate));

                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(0);
                        cal.set(fYear,fmonth, fday,hour,min,sec);
                        Date chosenDate = cal.getTime();

                        dateShow.setText(fday+"/"+fmonth+"/"+fYear);
                        singleDateStatic = chosenDate;

                    }
                }, mYear, mMonth, mDay);


        datePickerDialog.show();

        return true;
    }
    public static boolean ChooseDateDialogFrom(final Context context, final TextView dateShow){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        fday = dayOfMonth;
                        fmonth = ++monthOfYear;
                        fYear = year;

                        Date currentDate = new Date();
                        SimpleDateFormat cHour = new SimpleDateFormat("hh");
                        int hour = Integer.parseInt(cHour.format(currentDate));

                        SimpleDateFormat cMin = new SimpleDateFormat("mm");
                        int min = Integer.parseInt(cMin.format(currentDate));

                        SimpleDateFormat cSecond = new SimpleDateFormat("ss");
                        int sec = Integer.parseInt(cSecond.format(currentDate));

                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(0);
                        cal.set(fYear,fmonth, fday,hour,min,sec);
                        Date chosenDate = cal.getTime();

                        dateShow.setText(fday+"/"+fmonth+"/"+fYear);
                        fromDateStatic = chosenDate;

                    }
                }, mYear, mMonth, mDay);


        datePickerDialog.show();

        return true;
    }
    public static boolean ChooseDateDialogTo(final Context context, final TextView dateShow){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        fday = dayOfMonth;
                        fmonth = ++monthOfYear;
                        fYear = year;

                        Date currentDate = new Date();
                        SimpleDateFormat cHour = new SimpleDateFormat("hh");
                        int hour = Integer.parseInt(cHour.format(currentDate));

                        SimpleDateFormat cMin = new SimpleDateFormat("mm");
                        int min = Integer.parseInt(cMin.format(currentDate));

                        SimpleDateFormat cSecond = new SimpleDateFormat("ss");
                        int sec = Integer.parseInt(cSecond.format(currentDate));

                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(0);
                        cal.set(fYear,fmonth, fday,hour,min,sec);
                        Date chosenDate = cal.getTime();

                        dateShow.setText(fday+"/"+fmonth+"/"+fYear);
                        toDateStatic = chosenDate;

                    }
                }, mYear, mMonth, mDay);


        datePickerDialog.show();

        return true;
    }

    public static ArrayList<Date> dateInterval(Date initial, Date last) {
        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(initial);

        while (calendar.getTime().before(last)) {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }

        return dates;
    }

}
