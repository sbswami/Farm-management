package com.sanshy.farmmanagement;

import java.util.Date;

public class PayHistoryDataList {
    String Id;
    Date date;
    double amount;


    public PayHistoryDataList(String id, Date date, double amount) {
        Id = id;
        this.date = date;
        this.amount = amount;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
