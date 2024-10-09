package org.model;

import org.view.ManagerBook;

public class Person {
    private String fullName;
    private String address;
    private String phoneNumber;
    private String email;
    public ManagerBook managerBook;

    Person(String fullName, String address, String phoneNumber, String email, ManagerBook managerBook) {
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.managerBook = managerBook;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName() {
        this.fullName = managerBook.getFullName() ;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress() {
        this.address = managerBook.getAddress();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber() {
        this.phoneNumber = managerBook.getPhoneNumber();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail() {
        this.email = managerBook.getEmail();
    }


}
