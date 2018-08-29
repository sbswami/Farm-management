package com.sanshy.farmmanagement.crops;

public class SingleFourColumnsList {
    String Date;
    String Remark;
    String Amount;
    String ServiceOrBuyer;

    public SingleFourColumnsList(String date, String remark, String amount, String serviceOrBuyer) {
        Date = date;
        Remark = remark;
        Amount = amount;
        ServiceOrBuyer = serviceOrBuyer;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getServiceOrBuyer() {
        return ServiceOrBuyer;
    }

    public void setServiceOrBuyer(String serviceOrBuyer) {
        ServiceOrBuyer = serviceOrBuyer;
    }
}
