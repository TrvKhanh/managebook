package org.model;

import org.view.ManagerBook;

public class Book {
    private String ISBN;
    private int numberBook;
    private boolean status;
    private String authors;
    private String title;
    private String publisher;
    private int pageNumber;
    private int totalBooksInStock;
    private String genre;
    public ManagerBook managerBook;
    public Book(String ISBN, int numberBook, String authors, String title, String publisher, int pageNumber, String genre, ManagerBook managerBook ) {
        super();
        this.ISBN = ISBN;
        this.numberBook = numberBook;
        this.pageNumber = pageNumber;
        this.status = true;
        this.authors = authors;
        this.title = title;
        this.publisher = publisher;
        this.totalBooksInStock = numberBook;
        this.genre = genre;
        this.managerBook = managerBook;
    }

    public Book(Book book) {
        this.ISBN = book.getISBN();
        this.numberBook = book.getNumberBook();
        this.pageNumber = book.getPageNumber();
        this.status = true;
        this.authors = book.getAuthors();
        this.title = book.getTitle();
        this.publisher = book.getPublisher();


        this.totalBooksInStock = book.getTotalBooksInStock();
        this.genre = book.getGenre();
        this.managerBook = book.managerBook;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN() {
        this.ISBN = managerBook.getISBN();
    }

    public int getNumberBook() {
        return numberBook;
    }

    public void setNumberBook() {
        this.numberBook = Integer.parseInt(managerBook.getNumberBook());
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus() {
        if(totalBooksInStock == 0) {
            this.status = false;
        }
        else{
            this.status = true;
        }
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors() {
        this.authors = managerBook.getAuthor();
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle() {
        this.title = managerBook.getTitle();
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher() {
        this.publisher = managerBook.getPublisher();
    }


    public int getTotalBooksInStock() {
        return totalBooksInStock;
    }

    public void setTotalBooksInStock() {
        this.totalBooksInStock = totalBooksInStock;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre() {
        this.genre = managerBook.getGenre();
    }

    public int getPageNumber() {
        return pageNumber;
    }
    public void setPageNumber(){
        this.pageNumber = Integer.parseInt(managerBook.getPageNumber());
    }


}