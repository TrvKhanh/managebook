package org.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ListBook implements Iterable<Book> {
    private ArrayList<Book> books;

    // Constructor khởi tạo danh sách sách
    public ListBook() {
        this.books = new ArrayList<>();
    }

    // Kiểm tra xem danh sách sách có rỗng hay không
    public boolean isEmpty() {
        return books.isEmpty();
    }

    // Thêm một cuốn sách vào danh sách
    public void addBook(Book book) {
        books.add(new Book(book)); // Thêm bản sao của sách để tránh thay đổi không mong muốn
    }

    // Hàm tìm kiếm sách theo mã ISBN
    public List<Book> findBooksByIsbn(String isbn) {
        List<Book> foundBooks = new ArrayList<>();
        for (Book book : books) {
            if (Objects.equals(book.getISBN(), isbn)) {
                foundBooks.add(book);
            }
        }
        return foundBooks; // Trả về danh sách sách tìm được
    }

    // Hàm xóa sách theo mã ISBN
    public Book removeBook(String isbn) {
        Book book = findBooksByIsbn(isbn).isEmpty() ? null : findBooksByIsbn(isbn).get(0); // Tìm sách theo ISBN
        if (book != null) {
            books.remove(book); // Xóa sách khỏi danh sách nếu tìm thấy
            return book; // Trả về sách đã xóa
        }
        throw new IllegalArgumentException("Không tìm thấy sách với mã ISBN: " + isbn);
    }
    // Trả về danh sách tất cả các sách
    public ArrayList<Book> getBooks() {
        return books;
    }

    // Lấy số lượng sách trong danh sách
    public int size() {
        return books.size();
    }

    // Lấy sách tại một chỉ số cụ thể
    public Book getBook(int index) {
        // Kiểm tra nếu chỉ số nằm trong khoảng hợp lệ
        if (index >= 0 && index < books.size()) {
            return books.get(index); // Trả về sách tại vị trí index
        }
        throw new IndexOutOfBoundsException("Chỉ số không hợp lệ: " + index);
    }

    // Lấy sách theo chỉ số
    public Book getBookByIndex(int index) {
        if (index >= 0 && index < books.size()) {
            return books.get(index); // Trả về sách tại vị trí index
        }
        throw new IndexOutOfBoundsException("Chỉ số không hợp lệ: " + index);
    }

    // Phương thức sắp xếp sách theo tên tăng dần
    // Phương thức sắp xếp sách theo tên tăng dần và trả về danh sách đã sắp xếp
    // Phương thức sắp xếp sách theo tên tăng dần và trả về đối tượng ListBook
    public ListBook sortBooksAscending() {
        Collections.sort(books, new Comparator<Book>() {
            @Override
            public int compare(Book b1, Book b2) {
                return b1.getTitle().compareTo(b2.getTitle()); // Sắp xếp theo tên sách
            }
        });
        return this; // Trả về đối tượng ListBook đã sắp xếp
    }

    public ListBook sortBooksPageNumber() {
        Collections.sort(books, new Comparator<Book>() {
            @Override
            public int compare(Book b1, Book b2) {
                return Integer.compare(b1.getPageNumber(), b2.getPageNumber()); // Sắp xếp theo số trang
            }
        });
        return this; // Trả về đối tượng ListBook đã sắp xếp
    }



    // Phương thức trả về Iterator của danh sách sách để sử dụng trong vòng lặp
    @Override
    public Iterator<Book> iterator() {
        return books.iterator();
    }
}
