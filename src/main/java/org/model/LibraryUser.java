package org.model;

import org.view.ManagerBook;

import java.sql.Date;

public class LibraryUser extends Person {

    private int borrowerId;
    private Date borrowDay;
    private Date dueDay;
    private Date returnDay;
    private int readingTime;
    private String typeUser;
    private int fineMoney;
    private String isbn = "";

    public LibraryUser(String fullName, String address, String phoneNumber, String email, int borrowerId,
                       Date borrowDay, Date dueDay, Date returnDay, int readingTime, String typeUser, ManagerBook managerBook, String isbn, int fineMoney) {
        super(fullName, address, phoneNumber, email, managerBook);
        this.borrowerId = borrowerId;
        this.borrowDay = borrowDay;
        this.dueDay = dueDay;
        this.returnDay = returnDay;
        this.readingTime = readingTime;
        this.typeUser = typeUser;
        this.isbn = isbn;
        this.fineMoney = fineMoney;
    }

    public LibraryUser(LibraryUser user) {
        super(user.getFullName(), user.getAddress(), user.getPhoneNumber(), user.getEmail(), user.managerBook);
        this.borrowerId = user.borrowerId;
        this.borrowDay = user.borrowDay;
        this.dueDay = user.dueDay;
        this.returnDay = user.returnDay;
        this.readingTime = user.readingTime;
        this.typeUser = user.typeUser;
        this.isbn = user.isbn;
        this.fineMoney = user.fineMoney;
    }

    public int getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId() {
        this.borrowerId = Integer.parseInt(managerBook.getBorrowerId());
    }

    public Date getBorrowDay() {
        return borrowDay;
    }
    public Date setBorrowDay() {
        this.dueDay = managerBook.getDueDate();
        return dueDay;
    }
    public Date getDueDay() {
        return dueDay;
    }

    public Date setDueDay() {
        this.dueDay = managerBook.getDueDate();
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

    public Date getReturnDay() {
        return returnDay;
    }

    public void setReturnDay(Date returnDay) {
        this.returnDay = returnDay;
    }
}
