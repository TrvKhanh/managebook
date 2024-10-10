package org.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Date;

import org.model.Book;
import org.model.LibraryUser;
import org.model.ListBook;
import org.model.LoadModelAndPredict;
import org.view.ManagerBook;
import org.view.ModelLogiticsRegression;

import javax.swing.*;

public class ManagerController implements ActionListener {

    private ListBook listBooks;
    private Book book;
    private ManagerBook managerBook;
    private LibraryUser user;
    private LoadModelAndPredict model_predict  = new LoadModelAndPredict();
    private ModelLogiticsRegression modelLogiticsRegression ;

    public ManagerController(ManagerBook managerBook, ListBook listBooks) {
        this.managerBook = managerBook;
        this.book = new Book(
                "", 0, "","","",0,"", this.managerBook);
        this.user = new LibraryUser(null, null, null, null,0,null,null, null,0, null, this.managerBook, "",0);
        this.listBooks = listBooks;
        this.modelLogiticsRegression = new ModelLogiticsRegression();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // Lấy tên của nút được nhấn
        String actionCommand = e.getActionCommand();

        // Xử lý các hành động khác nhau dựa trên nút được nhấn
        switch (actionCommand) {
            case "Borrow books":
                borrowBook();
                break;
            case "Search Book":
                System.out.println("Search Book button clicked");
                searchBook();
                break;
            case "Return books":
                System.out.println("Return books button clicked");
                returnBook();
                break;
            case "Remove Book":
                System.out.println("Remove Book button clicked");
                removeBook();
                break;
            case "Edit":
                System.out.println("Edit Book button clicked");
                editBook();
                break;
            case "Add Book":
                System.out.println("Add Book button clicked");

                insertBook();
                break;
            case "Clear":
                System.out.println("Clear Book button clicked");
                clear();
                break;
            case "Exit":
                exit();
                System.out.println("exit Book button clicked");
                break;
            case "Update":
                updateBookInfo();
                System.out.println("exit Book button clicked");
                break;
            case "Search User":
                searchUser();
                break;
            default:
                System.out.println("Unknown action: " + actionCommand);
                break;
        }
    }
    //Ham muon lay gia tri tu nguoi dung va xu ly muon sach
    private void borrowBook() {
        Book currentBook = null;

        // Kiểm tra điều kiện nhập liệu
        boolean isTitleAndAuthorFilled = !this.book.managerBook.getTitle().isEmpty() && !this.book.managerBook.getAuthor().isEmpty();
        boolean isIsbnFilled = !this.book.managerBook.getISBN().isEmpty();

        if (!(isTitleAndAuthorFilled || isIsbnFilled)) {
            // Nếu không nhập đủ thông tin theo yêu cầu, hiển thị thông báo
            JOptionPane.showMessageDialog(null, "Vui lòng nhập tên sách và tên tác giả hoặc chỉ nhập mã ISBN!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return; // Dừng hàm nếu thông tin không hợp lệ
        }

        // Tìm sách theo ISBN hoặc theo tên và tác giả
        if (isIsbnFilled) {
            currentBook = getBookByIsbn(this.book.managerBook.getISBN());
        } else if (isTitleAndAuthorFilled) {
            currentBook = getBookByTitleAndAuthor(this.book.managerBook.getTitle(), this.book.managerBook.getAuthor());
        }

        if (currentBook == null) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy sách với thông tin đã nhập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return; // Dừng hàm nếu không tìm thấy sách
        }

        // Lưu kết quả từ setBook()
        LibraryUser currentUser = setUser();

        if (currentUser == null) {
            return; // Nếu có thông tin người dùng không hợp lệ, thoát khỏi phương thức
        }

        // Dự đoán giá trị
        double valuePredict = model_predict.predict(currentBook.getPageNumber(), currentUser.getReadingTime());
        int value = (int) (valuePredict * 100);

        // Gọi phương thức để hiển thị giao diện
        modelLogiticsRegression.setValuePredict(value); // Cập nhật giá trị dự đoán
        modelLogiticsRegression.setVisible(true);
        brorwerBook(currentBook, currentUser);// Hiển thị giao diện
    }


    private void saveUser(String isbn, LibraryUser user){
        String sql = "INSERT INTO User_delete(id, fullname, address, phonenumber, email, isbn, borrow_date, due_date, readingTime, type_user) VALUES(?,?,?,?,?,?,?,?,?,?)";

        user.setIsbn(isbn);
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user.getBorrowerId());
            pstmt.setString(2, user.getFullName());
            pstmt.setString(3, user.getAddress());
            pstmt.setString(4, user.getPhoneNumber());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getIsbn());
            pstmt.setString(7, String.valueOf(user.getBorrowDay()));
            pstmt.setString(8,  user.setDueDay());
            pstmt.setInt(9, user.getReadingTime());
            pstmt.setString(10, user.getTypeUser());
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void brorwerBook(Book books, LibraryUser user){
        String sql = "INSERT INTO Users(id, fullname, address, phonenumber, email, isbn, borrow_date, due_date, readingTime, type_user) VALUES(?,?,?,?,?,?,?,?,?,?)";

        user.setIsbn(books.getISBN());
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user.getBorrowerId());
            pstmt.setString(2, user.getFullName());
            pstmt.setString(3, user.getAddress());
            pstmt.setString(4, user.getPhoneNumber());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getIsbn());
            pstmt.setString(7, String.valueOf(user.getBorrowDay()));
            pstmt.setString(8,  user.setDueDay());
            pstmt.setInt(9, user.getReadingTime());
            pstmt.setString(10, user.getTypeUser());
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void borrowerDatabsase(){

    }

    private void searchBook() {
        int count = 0;

        // Kiểm tra từng trường và đếm số trường không trống
        if (!this.book.managerBook.getTitle().isEmpty()) count++;
        if (!this.book.managerBook.getAuthor().isEmpty()) count++;
        if (!this.book.managerBook.getPublisher().isEmpty()) count++;
        if (!this.book.managerBook.getISBN().isEmpty()) count++;
        if (!this.book.managerBook.getGenre().isEmpty()) count++;
        if (!this.book.managerBook.getPageNumber().isEmpty()) count++;

        if (count > 1) {
            // Nếu có hơn một trường được nhập, hiển thị cảnh báo
            JOptionPane.showMessageDialog(null, "Vui lòng chỉ nhập một trường duy nhất!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        } else {
            // Nếu đúng một trường được nhập
            if (count == 0) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập ít nhất một trường để tìm kiếm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            } else {
                // Tìm kiếm dựa trên trường không rỗng
                if (!this.book.managerBook.getTitle().isEmpty()) {
                    searchTitle();
                } else if (!this.book.managerBook.getAuthor().isEmpty()) {
                    searchAuthor();
                } else if (!this.book.managerBook.getPublisher().isEmpty()) {
                    searchPublisher();
                } else if (!this.book.managerBook.getISBN().isEmpty()) {
                    searchISBN();
                } else if (!this.book.managerBook.getGenre().isEmpty()) {
                    searchGenre();
                } else if (!this.book.managerBook.getPageNumber().isEmpty()) {
                    searchPageNumber();
                }
            }
        }
    }

    private void searchISBN(){
        String ISBN = this.book.managerBook.getISBN();
        Book book = getBookByIsbn(ISBN);
        if (book != null) {
            // Cập nhật thông tin lên JTable
            managerBook.updateTable_ISBN(book);
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy sách!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
        System.out.println("Searching book...");
    }

    private void searchTitle() {
        String title = this.book.managerBook.getTitle();
        ListBook books = getBooksByTitle(title);  // Giả sử getBooksByTitle trả về một danh sách các sách

        if (books != null && !books.isEmpty()) {
            // Cập nhật thông tin lên JTable
            managerBook.updateTable(books);
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy sách!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private void searchAuthor() {
        String author = this.book.managerBook.getAuthor();
        ListBook books = getBooksByAuthor(author);  // Giả sử getBooksByAuthor trả về một danh sách các sách

        if (books != null && !books.isEmpty()) {
            // Cập nhật thông tin lên JTable
            managerBook.updateTable(books);
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy sách!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private void searchPublisher() {
        String publisher = this.book.managerBook.getPublisher();
        ListBook books = getBooksByPublisher(publisher);  // Giả sử getBooksByPublisher trả về một danh sách các sách

        if (books != null && !books.isEmpty()) {
            // Cập nhật thông tin lên JTable
            managerBook.updateTable(books);
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy sách!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }

        System.out.println("Searching book...");
    }


    private void searchPageNumber() {
        int pageNumber;

        try {
            pageNumber = Integer.parseInt(this.book.managerBook.getPageNumber());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Số trang không hợp lệ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return; // Ngừng thực hiện nếu không thể chuyển đổi
        }

        ListBook books = getBooksByPageNumber(pageNumber);

        if (books != null && !books.isEmpty()) {
            // Cập nhật thông tin lên JTable
            managerBook.updateTable(books);
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy sách!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }

        System.out.println("Searching book...");
    }

    private void searchGenre() {
        String genre = this.book.managerBook.getGenre();
        ListBook books = getBooksByGenre(genre);  // Sửa lại thành getBooksByGenre

        if (books != null && !books.isEmpty()) {
            // Cập nhật thông tin lên JTable
            managerBook.updateTable(books);
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy sách!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }

        System.out.println("Searching book...");
    }



    private void removeBook() {
        if (this.book.managerBook.getISBN().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Không được để trống ISBN!.", "Alert", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            this.book.setISBN();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập  mã ISBN là số!", "Alert", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (isIsbnExist(this.book.managerBook.getISBN()) == false){
            JOptionPane.showMessageDialog(null, "\"Sách chưa tồn tại trong hệ thống.!!", "Cảnh báo !!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        else {
            listBooks.removeBook(this.book.managerBook.getISBN());

        }
        Book book_find =  listBooks.findBookByIsbn(this.book.managerBook.getISBN());

        // Xóa sách từ cơ sở dữ liệu
        String deleteQuery = "DELETE FROM Books WHERE ISBN = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
            pstmt.setString(1, this.book.managerBook.getISBN());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Xóa sách thành công!", "Thông báo!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Không thể xóa sách!", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi xóa sách: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

    }

    //Kiem tra ma ISBN da ton tai trong co so du lieu chua
    public static boolean isIsbnExist(String ISBN) {
        String sql = "SELECT 1 FROM Books WHERE ISBN = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ISBN);
            ResultSet rs = pstmt.executeQuery();

            // Nếu có kết quả trả về, ISBN đã tồn tại
            return rs.next();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    //ham them sach
    private void insertBook(){
        Book books = setBook();
        //Kiem tra neu ma ISBN da ton tai thi thong bao va thoat
        if (isIsbnExist(books.getISBN())) {
            JOptionPane.showMessageDialog(null, "\"Mã ISBN đã tồn tại!!", "Cảnh báo !!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String sql = "INSERT INTO Books(title, author, publisher, genre, available_copies, page_number, status, available_books, ISBN) VALUES(?,?,?,?,?,?,?,?,?)";


        listBooks.addBook(books);
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Book book : this.listBooks) {
                pstmt.setString(1, book.getTitle());
                pstmt.setString(2, book.getAuthors());
                pstmt.setString(3, book.getPublisher());
                pstmt.setString(4, book.getGenre());
                pstmt.setInt(5, book.getNumberBook());
                pstmt.setInt(6, book.getPageNumber());
                pstmt.setBoolean(7, book.getStatus());
                pstmt.setInt(8, book.getTotalBooksInStock());
                pstmt.setString(9, book.getISBN());
                pstmt.executeUpdate();
            }
            JOptionPane.showMessageDialog(null, "Thêm sách thành công!!", "Thông báo!!", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void editBook() {
        String isbn = this.book.managerBook.getISBN(); // Lấy mã ISBN từ JTextField

        // Kiểm tra xem mã ISBN có hợp lệ không
        if (isbn == null || isbn.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập mã ISBN!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return; // Ngừng thực hiện nếu không có ISBN
        }

        // Gọi phương thức để tìm sách và hiển thị thông tin
        Book foundBook = getBookByIsbn(isbn); // Giả sử getBookByISBN là phương thức đã tạo trước đó

        if (foundBook != null) {
            // Hiển thị thông tin sách lên các JTextField
            this.book.managerBook.setEditBook(foundBook);
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy sách với mã ISBN: "+isbn, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    //lay thong tin sach tu nguoi dung
    private Book setBook() {
        //kiem tra cac truong thong tin khi nguoi dung nhap sach khong duoc trong
        if (this.book.managerBook.getTitle().isEmpty() || this.book.managerBook.getAuthor().isEmpty() ||
                this.book.managerBook.getPublisher().isEmpty() || this.book.managerBook.getISBN().isEmpty() ||
                this.book.managerBook.getGenre().isEmpty() || this.book.managerBook.getNumberBook().isEmpty() ||
                this.book.managerBook.getPageNumber().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Không được để trống thông tin sách.", "Alert", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        //xu ly ngoai le khi nguoi dung nhap so sach khong phai la so;
        try {
            this.book.setNumberBook();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập số sách hợp lệ.", "Alert", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (Integer.parseInt(this.book.managerBook.getNumberBook()) <= 0) {
            JOptionPane.showMessageDialog(null, "Số sách phải lớn hơn 0.", "Alert", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        try {
            this.book.setPageNumber();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập trang số sách hợp lệ.", "Alert", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (Integer.parseInt(this.book.managerBook.getPageNumber()) <= 0) {
            JOptionPane.showMessageDialog(null, "Số trang sách phải lớn hơn 0.", "Alert", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        this.book.setTitle();
        this.book.setAuthors();
        this.book.setPublisher();
        this.book.setISBN();
        this.book.setGenre();
        this.book.setTotalBooksInStock();


        return book;
    }

    private LibraryUser setUser() {

        if (this.user.managerBook.getFullName().isEmpty() || this.user.managerBook.getEmail().isEmpty() ||
                this.user.managerBook.getAddress().isEmpty() || this.user.managerBook.getPhoneNumber().isEmpty() ||
                this.user.managerBook.getBorrowerId().isEmpty() || this.user.managerBook.getDueDate().isEmpty() ||
                this.user.managerBook.getTyprUse().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Không được để trống thông tin người dùng.", "Alert", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        this.user.setFullName();
        this.user.setEmail();
        this.user.setBorrowerId();
        this.user.setAddress();
        this.user.setReadingTime();
        this.user.setDueDay();
        this.user.setPhoneNumber();
        this.user.setTypeUser();

        return this.user;
    }
    private void searchUserId(){
        int id = 0;
        if (this.user.managerBook.getBorrowerId().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập Borrower ID!", "Thông báo", JOptionPane.ERROR_MESSAGE);
            return; // Kết thúc phương thức nếu không có thông tin nào được nhập
        }
        try {
           id = Integer.parseInt(this.user.managerBook.getBorrowerId());

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID phải là một số hợp lệ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Nếu có giá trị trong ô Borrower ID, đảm bảo không có thông tin nào khác được nhập
        if (!this.user.managerBook.getFullName().isEmpty() ||
                !this.user.managerBook.getEmail().isEmpty() ||
                !this.user.managerBook.getAddress().isEmpty() ||
                !this.user.managerBook.getPhoneNumber().isEmpty() ||
                !this.user.managerBook.getDueDate().isEmpty() ||
                !this.user.managerBook.getTyprUse().isEmpty()) {

            JOptionPane.showMessageDialog(null, "Chỉ được nhập Borrower ID, không được nhập thêm thông tin khác!", "Thông báo", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LibraryUser users = getUserById(id);
        Book book = getBookByIsbn(users.getIsbn());
        if (book != null && users != null) {
            // Cập nhật thông tin lên JTable
            managerBook.displayTable_user(book, users);
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
        System.out.println("Searching book...");
    }


    public void searchUser(){
        searchUserId();
    }

    private void returnBook() {
        int id = 0;
        if (this.user.managerBook.getBorrowerId().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập Borrower ID!", "Thông báo", JOptionPane.ERROR_MESSAGE);
            return; // Kết thúc phương thức nếu không có thông tin nào được nhập
        }
        try {
            id = Integer.parseInt(this.user.managerBook.getBorrowerId());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID phải là một số hợp lệ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Nếu có giá trị trong ô Borrower ID, đảm bảo không có thông tin nào khác được nhập
        if (!this.user.managerBook.getFullName().isEmpty() ||
                !this.user.managerBook.getEmail().isEmpty() ||
                !this.user.managerBook.getAddress().isEmpty() ||
                !this.user.managerBook.getPhoneNumber().isEmpty() ||
                !this.user.managerBook.getDueDate().isEmpty() ||
                !this.user.managerBook.getTyprUse().isEmpty()) {

            JOptionPane.showMessageDialog(null, "Chỉ được nhập Borrower ID, không được nhập thêm thông tin khác!", "Thông báo", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LibraryUser users = getUserById(id);

        // Kiểm tra nếu users là null
        if (users == null) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy người dùng với ID: " + id, "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        saveUser(users.getIsbn(), users);

        String deleteQuery = "DELETE FROM Users WHERE id = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Xóa sách thành công!", "Thông báo!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Không thể xóa sách!", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi xóa sách: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }



    private LibraryUser getUserById(int id) {
        String sql = "SELECT * FROM Users WHERE id = ?";
        LibraryUser user = null;

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            // Kiểm tra xem có kết quả không
            if (rs.next()) {
                user = new LibraryUser(
                        rs.getString("fullname"),
                        rs.getString("address"),
                        rs.getString("phonenumber"),
                        rs.getString("email"),
                        rs.getInt("id"),
                        rs.getString("borrow_date"),
                        rs.getString("due_date"),
                        rs.getString("return_date"),
                        rs.getInt("readingTime"),
                        rs.getString("type_user"),
                        this.managerBook,
                        rs.getString("isbn"),
                        rs.getInt("find_money")
                );
            } else {
                JOptionPane.showMessageDialog(null, "Không tìm thấy người dùng với ID: " + id, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            // Có thể ghi log lỗi ở đây
            JOptionPane.showMessageDialog(null, "Lỗi khi truy vấn người dùng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return user;
    }

    private Book getBookByIsbn(String isbn) {
        String sql = "SELECT * FROM Books WHERE ISBN = ?";
        Book book = null;

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();

            // Nếu có kết quả, tạo đối tượng Book từ kết quả truy vấn
            if (rs.next()) {
                book = new Book(
                        rs.getString("ISBN"),
                        rs.getInt("available_copies"),
                        rs.getString("author"),
                        rs.getString("title"),
                        rs.getString("publisher"),
                        rs.getInt("page_number"),
                        rs.getString("genre"),
                        this.managerBook);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi truy vấn sách: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return book;
    }



    private Book getBookByTitleAndAuthor(String title, String author) {
        String sql = "SELECT * FROM Books WHERE title = ? AND author = ?";
        Book book = null;

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Đặt tham số cho câu truy vấn
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            ResultSet rs = pstmt.executeQuery();

            // Nếu có kết quả, tạo đối tượng Book từ kết quả truy vấn
            if (rs.next()) {
                book = new Book(
                        rs.getString("ISBN"),
                        rs.getInt("available_copies"),
                        rs.getString("author"),
                        rs.getString("title"),
                        rs.getString("publisher"),
                        rs.getInt("page_number"),
                        rs.getString("genre"),
                        this.managerBook);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi truy vấn sách: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return book;
    }


    private ListBook getBooksByTitle(String title) {
        String sql = "SELECT * FROM Books WHERE title = ?"; // Câu lệnh SQL tìm kiếm theo tiêu đề
        ListBook books = new ListBook(); // Danh sách để lưu các cuốn sách tìm thấy

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title); // Đặt giá trị cho tham số của câu lệnh SQL
            ResultSet rs = pstmt.executeQuery();

            // Duyệt qua tất cả kết quả truy vấn và tạo đối tượng Book từ kết quả
            while (rs.next()) {
                Book book = new Book(
                        rs.getString("ISBN"),
                        rs.getInt("available_copies"),
                        rs.getString("author"),
                        rs.getString("title"),
                        rs.getString("publisher"),
                        rs.getInt("page_number"),
                        rs.getString("genre"),
                        this.managerBook);
                books.addBook(book); // Thêm sách vào danh sách
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi truy vấn sách: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return books; // Trả về danh sách sách tìm thấy (có thể rỗng nếu không tìm thấy sách)
    }

    private ListBook getBooksByPublisher(String publisher) {
        String sql = "SELECT * FROM Books WHERE publisher = ?"; // Câu lệnh SQL tìm kiếm theo tiêu đề
        ListBook books = new ListBook(); // Danh sách để lưu các cuốn sách tìm thấy

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, publisher); // Đặt giá trị cho tham số của câu lệnh SQL
            ResultSet rs = pstmt.executeQuery();

            // Duyệt qua tất cả kết quả truy vấn và tạo đối tượng Book từ kết quả
            while (rs.next()) {
                Book book = new Book(
                        rs.getString("ISBN"),
                        rs.getInt("available_copies"),
                        rs.getString("author"),
                        rs.getString("title"),
                        rs.getString("publisher"),
                        rs.getInt("page_number"),
                        rs.getString("genre"),
                        this.managerBook);
                books.addBook(book); // Thêm sách vào danh sách
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi truy vấn sách: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return books; // Trả về danh sách sách tìm thấy (có thể rỗng nếu không tìm thấy sách)
    }

    private ListBook getBooksByPageNumber(int page_number) {
        String sql = "SELECT * FROM Books WHERE page_number = ?"; // Câu lệnh SQL tìm kiếm theo tiêu đề
        ListBook books = new ListBook(); // Danh sách để lưu các cuốn sách tìm thấy

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, page_number); // Đặt giá trị cho tham số của câu lệnh SQL
            ResultSet rs = pstmt.executeQuery();

            // Duyệt qua tất cả kết quả truy vấn và tạo đối tượng Book từ kết quả
            while (rs.next()) {
                Book book = new Book(
                        rs.getString("ISBN"),
                        rs.getInt("available_copies"),
                        rs.getString("author"),
                        rs.getString("title"),
                        rs.getString("publisher"),
                        rs.getInt("page_number"),
                        rs.getString("genre"),
                        this.managerBook);
                books.addBook(book); // Thêm sách vào danh sách
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi truy vấn sách: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return books; // Trả về danh sách sách tìm thấy (có thể rỗng nếu không tìm thấy sách)
    }
    private ListBook getBooksByGenre(String genre) {
        String sql = "SELECT * FROM Books WHERE genre = ?"; // Câu lệnh SQL tìm kiếm theo tiêu đề
        ListBook books = new ListBook(); // Danh sách để lưu các cuốn sách tìm thấy

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, genre); // Đặt giá trị cho tham số của câu lệnh SQL
            ResultSet rs = pstmt.executeQuery();

            // Duyệt qua tất cả kết quả truy vấn và tạo đối tượng Book từ kết quả
            while (rs.next()) {
                Book book = new Book(
                        rs.getString("ISBN"),
                        rs.getInt("available_copies"),
                        rs.getString("author"),
                        rs.getString("title"),
                        rs.getString("publisher"),
                        rs.getInt("page_number"),
                        rs.getString("genre"),
                        this.managerBook);
                books.addBook(book); // Thêm sách vào danh sách
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi truy vấn sách: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return books; // Trả về danh sách sách tìm thấy (có thể rỗng nếu không tìm thấy sách)
    }

    //Ham truy van databses theo author va tra ve doi tuong book
    private ListBook getBooksByAuthor(String author) {
        String sql = "SELECT * FROM Books WHERE author = ?"; // Câu lệnh SQL tìm kiếm theo tiêu đề
        ListBook books = new ListBook(); // Danh sách để lưu các cuốn sách tìm thấy

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, author); // Đặt giá trị cho tham số của câu lệnh SQL
            ResultSet rs = pstmt.executeQuery();

            // Duyệt qua tất cả kết quả truy vấn và tạo đối tượng Book từ kết quả
            while (rs.next()) {
                Book book = new Book(
                        rs.getString("ISBN"),
                        rs.getInt("available_copies"),
                        rs.getString("author"),
                        rs.getString("title"),
                        rs.getString("publisher"),
                        rs.getInt("page_number"),
                        rs.getString("genre"),
                        this.managerBook);
                books.addBook(book); // Thêm sách vào danh sách
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi truy vấn sách: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return books; // Trả về danh sách sách tìm thấy (có thể rỗng nếu không tìm thấy sách)
    }



    private void updateBook(Book book) {
        String sql = "UPDATE Book SET title = ?, author = ?, publisher = ?, genre = ?, available_copies = ?, page_number = ? WHERE isbn = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Gán giá trị cho các tham số
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthors());
            pstmt.setString(3, book.getPublisher());
            pstmt.setString(4, book.getGenre());
            pstmt.setInt(5, book.getNumberBook());
            pstmt.setInt(6, book.getPageNumber());
            pstmt.setString(7, book.getISBN());

            // Thực hiện cập nhật
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Cập nhật thông tin sách thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Không tìm thấy sách để cập nhật!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý lỗi
            JOptionPane.showMessageDialog(null, "Cập nhật thất bại: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateBookInfo() {
        String isbn = this.book.managerBook.getISBN();
        String title = this.book.managerBook.getTitle();
        String author = this.book.managerBook.getAuthor();
        String publisher = this.book.managerBook.getPublisher();
        String genre = this.book.managerBook.getGenre();
        String numberBook =  this.book.managerBook.getNumberBook();
        String pageNumberStr = this.book.managerBook.getPageNumber();
        int pageNumber = 0;
        int numberBooks = 0;
        // Kiểm tra tính hợp lệ
        if (isbn.isEmpty() || title.isEmpty() || author.isEmpty() || publisher.isEmpty() || genre.isEmpty() || pageNumberStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            pageNumber = Integer.parseInt(pageNumberStr);
            numberBooks = Integer.parseInt(numberBook);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Số trang phải là một số hợp lệ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }


        Book updatedBook = new Book(isbn, numberBooks, author, title, publisher, pageNumber, genre, managerBook );
        // Gọi phương thức để cập nhật thông tin
        updateBook(updatedBook);
    }


    //ham xu ly button clear
    private void clear() {
        managerBook.clear();
    }
    //ham xu ly button exit
    public void exit(){
        managerBook.dispose();
        System.exit(0);
    }

}