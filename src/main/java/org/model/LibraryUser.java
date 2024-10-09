package org.model;

import org.view.ManagerBook;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class LibraryUser extends Person{

    private int borrowerId;
    private Date borrowDay;
    private Date dueDay;
    private Date returnDay;
    private int readingTime;
    private String typeUser;
    private int fineMoney;
    public LibraryUser(String fullName, String address, String phoneNumber, String email, int borrowerId,
                       Date borrowDay, Date dueDay, Date returnDay, int readingTime, String typeUser, ManagerBook managerBook) {
        super(fullName, address, phoneNumber, email, managerBook);
        this.borrowerId = borrowerId;
        this.borrowDay = borrowDay;
        this.dueDay = dueDay;
        this.readingTime = readingTime;
        this.typeUser = typeUser;
        fineMoney = 0;
        this.returnDay = returnDay;
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

    public Date getBorrowDay() {
        return borrowDay;
    }


    public Date getDueDay() {
        return dueDay;
    }

    public String setDueDay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false); // Không cho phép định dạng sai

        String dueDate = null;
        try {
            this.dueDay = dateFormat.parse(managerBook.getDueDate());
            dueDate = dateFormat.format(this.dueDay);
        } catch (ParseException ex) {
            System.out.println("Invalid Date Format");
        }
        return dueDate;
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


}