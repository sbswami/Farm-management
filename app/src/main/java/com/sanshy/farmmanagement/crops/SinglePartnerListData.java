package com.sanshy.farmmanagement.crops;

public class SinglePartnerListData {

    String PartnerId;
    String PartnerName;
    String PartnerPhone;

    public SinglePartnerListData(String partnerId, String partnerName, String partnerPhone) {
        PartnerId = partnerId;
        PartnerName = partnerName;
        PartnerPhone = partnerPhone;
    }

    public String getPartnerId() {
        return PartnerId;
    }

    public void setPartnerId(String partnerId) {
        PartnerId = partnerId;
    }

    public String getPartnerName() {
        return PartnerName;
    }

    public void setPartnerName(String partnerName) {
        PartnerName = partnerName;
    }

    public String getPartnerPhone() {
        return PartnerPhone;
    }

    public void setPartnerPhone(String partnerPhone) {
        PartnerPhone = partnerPhone;
    }
}
