package com.sanshy.farmmanagement.crops;

import java.util.Date;

public class SingleSowingCropData {

    String SowingId;
    String SowingRemark;
    double SowingAmount;
    double SowingLandArea;
    Date SowingDate;
    boolean CashService;
    String ServiceProviderName;
    String ServiceProviderPhoneNumber;
    String ServiceProviderId;

    boolean CheckPartner;
    String PartnerName;
    String PartnerPhoneNumber;
    String PartnerId;

    public SingleSowingCropData(String sowingId, String sowingRemark, double sowingAmount, double sowingLandArea, Date sowingDate, boolean cashService, String serviceProviderName, String serviceProviderPhoneNumber, String serviceProviderId, boolean checkPartner) {
        SowingId = sowingId;
        SowingRemark = sowingRemark;
        SowingAmount = sowingAmount;
        SowingLandArea = sowingLandArea;
        SowingDate = sowingDate;
        CashService = cashService;
        ServiceProviderName = serviceProviderName;
        ServiceProviderPhoneNumber = serviceProviderPhoneNumber;
        ServiceProviderId = serviceProviderId;
        CheckPartner = checkPartner;
    }

    public SingleSowingCropData(String sowingId, String sowingRemark, double sowingAmount, double sowingLandArea, Date sowingDate, boolean cashService, String serviceProviderName, String serviceProviderPhoneNumber, String serviceProviderId, boolean checkPartner, String partnerName, String partnerPhoneNumber, String partnerId) {
        SowingId = sowingId;
        SowingRemark = sowingRemark;
        SowingAmount = sowingAmount;
        SowingLandArea = sowingLandArea;
        SowingDate = sowingDate;
        CashService = cashService;
        ServiceProviderName = serviceProviderName;
        ServiceProviderPhoneNumber = serviceProviderPhoneNumber;
        ServiceProviderId = serviceProviderId;
        CheckPartner = checkPartner;
        PartnerName = partnerName;
        PartnerPhoneNumber = partnerPhoneNumber;
        PartnerId = partnerId;
    }

    public String getServiceProviderPhoneNumber() {
        return ServiceProviderPhoneNumber;
    }

    public void setServiceProviderPhoneNumber(String serviceProviderPhoneNumber) {
        ServiceProviderPhoneNumber = serviceProviderPhoneNumber;
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

    public String getPartnerPhoneNumber() {
        return PartnerPhoneNumber;
    }

    public void setPartnerPhoneNumber(String partnerPhoneNumber) {
        PartnerPhoneNumber = partnerPhoneNumber;
    }

    public String getPartnerId() {
        return PartnerId;
    }

    public void setPartnerId(String partnerId) {
        PartnerId = partnerId;
    }
}
