package com.sanshy.farmmanagement.crops;

import java.util.Date;

public class SingleSowingCropData {

    String SowingId;
    String SowingRemark;
    String CropId;
    double SowingAmount;
    double SowingLandArea;
    Date SowingDate;
    boolean CashService;
    String ServiceProviderName;
    String ServiceProviderId;

    boolean CheckPartner;
    String PartnerName;
    String PartnerId;

    public SingleSowingCropData(String sowingId,  String sowingRemark,String cropId, double sowingAmount, double sowingLandArea, Date sowingDate, boolean cashService, String serviceProviderName, String serviceProviderId, boolean checkPartner) {
        SowingId = sowingId;
        SowingRemark = sowingRemark;
        CropId = cropId;
        SowingAmount = sowingAmount;
        SowingLandArea = sowingLandArea;
        SowingDate = sowingDate;
        CashService = cashService;
        ServiceProviderName = serviceProviderName;
        ServiceProviderId = serviceProviderId;
        CheckPartner = checkPartner;
    }

    public SingleSowingCropData(String sowingId, String sowingRemark, String cropId, double sowingAmount, double sowingLandArea, Date sowingDate, boolean cashService, String serviceProviderName, String serviceProviderId, boolean checkPartner, String partnerName, String partnerId) {
        SowingId = sowingId;
        SowingRemark = sowingRemark;
        CropId = cropId;
        SowingAmount = sowingAmount;
        SowingLandArea = sowingLandArea;
        SowingDate = sowingDate;
        CashService = cashService;
        ServiceProviderName = serviceProviderName;
        ServiceProviderId = serviceProviderId;
        CheckPartner = checkPartner;
        PartnerName = partnerName;
        PartnerId = partnerId;
    }

    public String getCropId() {
        return CropId;
    }

    public void setCropId(String cropId) {
        CropId = cropId;
    }

    public String getSowingId() {
        return SowingId;
    }

    public void setSowingId(String sowingId) {
        SowingId = sowingId;
    }

    public String getSowingRemark() {
        return SowingRemark;
    }

    public void setSowingRemark(String sowingRemark) {
        SowingRemark = sowingRemark;
    }

    public double getSowingAmount() {
        return SowingAmount;
    }

    public void setSowingAmount(double sowingAmount) {
        SowingAmount = sowingAmount;
    }

    public double getSowingLandArea() {
        return SowingLandArea;
    }

    public void setSowingLandArea(double sowingLandArea) {
        SowingLandArea = sowingLandArea;
    }

    public Date getSowingDate() {
        return SowingDate;
    }

    public void setSowingDate(Date sowingDate) {
        SowingDate = sowingDate;
    }

    public boolean isCashService() {
        return CashService;
    }

    public void setCashService(boolean cashService) {
        CashService = cashService;
    }

    public String getServiceProviderName() {
        return ServiceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        ServiceProviderName = serviceProviderName;
    }

    public String getServiceProviderId() {
        return ServiceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        ServiceProviderId = serviceProviderId;
    }

    public boolean isCheckPartner() {
        return CheckPartner;
    }

    public void setCheckPartner(boolean checkPartner) {
        CheckPartner = checkPartner;
    }

    public String getPartnerName() {
        return PartnerName;
    }

    public void setPartnerName(String partnerName) {
        PartnerName = partnerName;
    }

    public String getPartnerId() {
        return PartnerId;
    }

    public void setPartnerId(String partnerId) {
        PartnerId = partnerId;
    }
}
