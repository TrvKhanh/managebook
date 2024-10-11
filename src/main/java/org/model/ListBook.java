package org.model;

import java.util.ArrayList;
import java.util.Iterator;
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
    public Book findBookByIsbn(String isbn) {
        for (Book book : books) {
            if (Objects.equals(book.getISBN(), isbn)) {
                return book;
            }
        }
        return null; // Trả về null nếu không tìm thấy sách
    }

    // Hàm xóa sách theo mã ISBN
    public Book removeBook(String isbn) {
        Book book = findBookByIsbn(isbn); // Tìm sách theo ISBN
        if (book != null) {
            books.remove(book); // Xóa sách khỏi danh sách nếu tìm thấy
        }
        return book; // Trả về sách đã xóa (có thể là null nếu không tìm thấy)
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
        return null; // Trả về null nếu chỉ số không hợp lệ
    }


    // Phương thức trả về Iterator của danh sách sách để sử dụng trong vòng lặp
    @Override
    public Iterator<Book> iterator() {
        return books.iterator();
    }
}
