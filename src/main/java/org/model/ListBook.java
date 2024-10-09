package org.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

// Lớp ListBook quản lý danh sách các cuốn sách và hỗ trợ các thao tác trên danh sách sách
public class ListBook implements Iterable<Book> {
    private ArrayList<Book> books;

    // Kiểm tra xem danh sách sách có rỗng hay không
    public boolean isEmpty() {
        return books.isEmpty();
    }

    // Constructor khởi tạo danh sách sách
    public ListBook() {
        books = new ArrayList<>();
    }

    // Thêm một cuốn sách vào danh sách
    public void addBook(Book book) {
        books.add(new Book(book));
    }

    // Hàm tìm kiếm sách theo mã ISBN
    public Book findBookByIsbn(String isbn) {
        for (Book book : books) {
            // Kiểm tra xem ISBN của sách có khớp với mã tìm kiếm không
            if (Objects.equals(book.getISBN(), isbn)) {
                return book;
            }
        }
        return null; // Trả về null nếu không tìm thấy sách
    }

    // Hàm xóa sách theo mã ISBN
    public Book removeBook(String ISBN) {
        Book book = findBookByIsbn(ISBN); // Tìm sách theo ISBN
        books.remove(book); // Xóa sách khỏi danh sách
        return book; // Trả về sách đã xóa
    }

    // Trả về danh sách tất cả các sách
    public ArrayList<Book> getBooks() {
        return books;
    }

    // Phương thức trả về Iterator của danh sách sách để sử dụng trong vòng lặp
    @Override
    public Iterator<Book> iterator() {
        return books.iterator(); // Trả về Iterator của `ArrayList`
    }
}
