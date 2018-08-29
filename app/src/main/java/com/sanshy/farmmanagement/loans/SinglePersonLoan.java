package com.sanshy.farmmanagement.loans;

import java.util.Date;

public class SinglePersonLoan {
    String Id;
    String Name;
    long Phone;
    String Address;
    Date DateOfCreation;

    public SinglePersonLoan(String id, String name, long phone, String address, Date dateOfCreation) {
        Id = id;
        Name = name;
        Phone = phone;
        Address = address;
        DateOfCreation = dateOfCreation;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public long getPhone() {
        return Phone;
    }

    public void setPhone(long phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public Date getDateOfCreation() {
        return DateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        DateOfCreation = dateOfCreation;
    }
}
