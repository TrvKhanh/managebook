package org.model;

import org.view.ManagerBook;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class LibraryUser extends Person{

    private int borrowerId;
    private String borrowDay;
    private String dueDay;
    private String returnDay;
    private int readingTime;
    private String typeUser;
    private int fineMoney;
    private String isbn = "";
    public LibraryUser(String fullName, String address, String phoneNumber, String email, int borrowerId,
                       String borrowDay, String dueDay, String returnDay, int readingTime, String typeUser, ManagerBook managerBook, String isbn, int fineMoney) {
        super(fullName, address, phoneNumber, email, managerBook);
        this.borrowerId = borrowerId;
        this.borrowDay = borrowDay;
        this.dueDay = dueDay;
        this.readingTime = readingTime;
        this.typeUser = typeUser;
        this.returnDay = returnDay;
        this.isbn = isbn;
        this.fineMoney = fineMoney;

    }

    public LibraryUser(LibraryUser user) {
        super(user.getFullName(), user.getAddress(), user.getPhoneNumber(), user.getEmail(), user.managerBook);
        this.borrowerId = user.borrowerId;
        this.borrowDay = user.borrowDay;
        this.dueDay = user.dueDay;
        this.readingTime = user.readingTime;
        this.typeUser = user.typeUser;
    }

    public int getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId() {
        this.borrowerId =  Integer.parseInt(managerBook.getBorrowerId());
    }

    public String getBorrowDay() {
        return borrowDay;
    }


    public String getDueDay() {
        return dueDay;
    }

    public String setDueDay() {
            dueDay = managerBook.getDueDate();
        return dueDay;
    }

    public int getReadingTime() {
        return readingTime;

    }

    public void setReadingTime() {
        this.readingTime = managerBook.getReadingTime();
    }

    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser() {
        this.typeUser = managerBook.getTyprUse();
    }


    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getFineMoney() {
        return fineMoney;
    }

    public void setFineMoney(int fineMoney) {
        this.fineMoney = fineMoney;
    }

    public String getReturnDay() {
        return returnDay;
    }

    public void setReturnDay(String returnDay) {
        this.returnDay = returnDay;
    }


}