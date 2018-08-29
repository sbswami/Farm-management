package com.sanshy.farmmanagement.crops;

import java.util.Date;

public class SinglePartnerData {

    String PartnerCropId;
    String PartnerCropName;
    long PartnerCropPhone;
    String PartnerCropAddress;
    Date PartnerCropAddingDate;

    public SinglePartnerData(String partnerCropId, String partnerCropName, long partnerCropPhone, String partnerCropAddress, Date partnerCropAddingDate) {
        PartnerCropId = partnerCropId;
        PartnerCropName = partnerCropName;
        PartnerCropPhone = partnerCropPhone;
        PartnerCropAddress = partnerCropAddress;
        PartnerCropAddingDate = partnerCropAddingDate;
    }

    public String getPartnerCropName() {
        return PartnerCropName;
    }

    public void setPartnerCropName(String partnerCropName) {
        PartnerCropName = partnerCropName;
    }

    public long getPartnerCropPhone() {
        return PartnerCropPhone;
    }

    public void setPartnerCropPhone(long partnerCropPhone) {
        PartnerCropPhone = partnerCropPhone;
    }

    public String getPartnerCropAddress() {
        return PartnerCropAddress;
    }

    public void setPartnerCropAddress(String partnerCropAddress) {
        PartnerCropAddress = partnerCropAddress;
    }

    public String getPartnerCropId() {
        return PartnerCropId;
    }

    public void setPartnerCropId(String partnerCropId) {
        PartnerCropId = partnerCropId;
    }

    public Date getPartnerCropAddingDate() {
        return PartnerCropAddingDate;
    }

    public void setPartnerCropAddingDate(Date partnerCropAddingDate) {
        PartnerCropAddingDate = partnerCropAddingDate;
    }
}
