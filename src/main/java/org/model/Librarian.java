package org.model;

import org.view.ManagerBook;

public class Librarian extends Person{

    private int managerId;
    private String name_account;
    private String password;
    public Librarian(String fullName, String address, String phoneNumber, String email, ManagerBook managerBook, int managerId,
                     String name_account, String password) {
        super(fullName, address, phoneNumber, email, managerBook);
        this.managerId = managerId;
        this.name_account = name_account;
        this.password = password;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {

    }

    public String getName_account() {
        return name_account;
    }

    public void setName_account(String name_account) {
        this.name_account = name_account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

