package com.sanshy.farmmanagement.extras;

import java.util.Date;

public class SingleExpOrInc {
    String Id;
    String Remark;
    double Amount;
    Date date;

    public SingleExpOrInc(String Id, String Remark, double Amount, Date date) {
        this.Id = Id;
        this.Remark = Remark;
        this.Amount = Amount;
        this.date = date;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double Amount) {
        this.Amount = Amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
