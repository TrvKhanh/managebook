package org.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

import org.model.*;
import org.view.ManagerBook;

import javax.swing.*;

public class ManagerController implements ActionListener {

    private ListBook listBooks;
    private Book book;
    private ManagerBook managerBook;
    private LibraryUser user;
    private LoadModelAndPredict model_predict  = new LoadModelAndPredict();

    public ManagerController(ManagerBook managerBook, ListBook listBooks) {
        this.managerBook = managerBook;
        this.book = new Book(
                "", 0, "","","",0,"", this.managerBook);
        this.user = new LibraryUser(null, null, null, null,0,null,null, null,0, null, this.managerBook, "",0);
        this.listBooks = listBooks;

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
                searchBook();
                break;
            case "Return books":
                returnBook();
                break;
            case "Remove Book":
                removeBook();
                break;
            case "Edit User":
                editUser();
                break;
            case "Add Book":
                insertBook();
                break;
            case "Update":
                updateUserInfor();
                break;
            case "Search User":
                searchUser();
                break;
            case "Book Deleted":
                displayBookDeleted();
                break;
            case "User Deleted":
                displayUserDeleted();
                System.out.println("NHap vao nut book delete");
                break;
            case "Display user":
                displayUser();
                System.out.println("NHap vao nut book delete");
                break;
            case "Display books":
                displayBook();
                break;
            case "Arrange Title":
                ArrangeTilte();
                break;
            case "Arrange Page Number":
                ArrangePageNumber();
                break;
            case "Clear":
                clear();
                break;
            case "Exit":
                exit();
                break;
            default:
                break;
        }
    }
    public void ArrangeTilte(){
        String sql = "SELECT * FROM Books";
        ListBook listbook = getAllBook(sql);
        listbook.sortBooksAscending();
        if (listbook != null) {
            // Cập nhật thông tin lên JTable
            managerBook.updateTable(listbook);
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy sách!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    }
    public void ArrangePageNumber(){
        String sql = "SELECT * FROM Books";
        ListBook listbook = getAllBook(sql);
        listbook.sortBooksPageNumber();
        if (listbook != null) {
            // Cập nhật thông tin lên JTable
            managerBook.updateTable(listbook);
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy sách!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    }
    public void displayUser(){
        String sql = "SELECT * FROM User";
        disPlay(sql);
    }
    public void displayBookDeleted(){
        String sql = "SELECT * FROM Books_delete";
        ListBook listbook = getAllBook(sql);
        if (listbook != null) {
            // Cập nhật thông tin lên JTable
            managerBook.updateTable(listbook);
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy sách!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

    }

    private void displayUserDeleted() {
        String sql = "SELECT * FROM User_delete";
        disPlay(sql);
    }

    public void disPlay(String sql){
        ListUser listUser = getAllUser(sql);
        ListBook listbook = new ListBook();
        if (listUser.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy người dùng!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return; // Dừng phương thức nếu danh sách người dùng trống
        }

        // Duyệt qua danh sách người dùng
        for (LibraryUser user : listUser.getUsers()) {
            String isbn = user.getIsbn();
            System.out.println(isbn);

            Book book = getBookByIsbnDelete(isbn);
            listbook.addBook(book);

            if (book != null) {
                System.out.println(book.getAuthors());
            } else {
                System.out.println("Không tìm thấy sách với ISBN: " + isbn);
            }
        }
        managerBook.updateTableUser(listUser, listbook);
    }


    private ListUser getAllUser(String sql) {
        ListUser listUser = new ListUser(); // Tạo danh sách để lưu trữ các user tìm thấy

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery(); // Thực hiện truy vấn SQL

            // Duyệt qua tất cả kết quả truy vấn và tạo đối tượng LibraryUser từ kết quả
            while (rs.next()) {
                LibraryUser user = new LibraryUser(
                        rs.getString("fullname"),          // fullName
                        rs.getString("address"),           // address
                        rs.getString("phonenumber"),       // phoneNumber
                        rs.getString("email"),             // email
                        rs.getInt("id"),                   // borrowerId
                        rs.getDate("borrow_date"),       // borrowDay
                        rs.getDate("due_date"),          // dueDay
                        rs.getDate("return_date"),       // returnDay
                        rs.getInt("readingTime"),          // readingTime
                        rs.getString("type_user"),         // typeUser
                        this.managerBook,                  // managerBook (Thay đổi nếu cần)
                        rs.getString("isbn"),              // isbn
                        rs.getInt("find_money")            // fineMoney (Nếu có lỗi, sửa thành "fine_money")
                );
                listUser.addUser(user); // Thêm user vào danh sách
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi truy vấn user: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return listUser; // Trả về danh sách user tìm thấy (có thể rỗng nếu không tìm thấy user)
    }

    //"Borrow books" ===========================================================================>>>>>>>>>>>>>>>>>
    private void borrowBook() {
        Book currentBook = null;

        // Kiểm tra điều kiện nhập liệu
        boolean isTitleAndAuthorFilled = !this.book.managerBook.getTitle().isEmpty() && !this.book.managerBook.getAuthor().isEmpty();
        boolean isIsbnFilled = !this.book.managerBook.getISBN().isEmpty();

        if (!(isTitleAndAuthorFilled || isIsbnFilled)) {
            // Nếu không nhập đủ thông tin theo yêu cầu, hiển thị thông báo
            JOptionPane.showMessageDialog(null, "Vui lòng nhập tên sách và tên tác giả hoặc chỉ nhập mã ISBN!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return ; // Dừng hàm nếu thông tin không hợp lệ
        }
        LibraryUser currentUser = setUser();
        String sql = "SELECT * FROM Books WHERE ISBN = ?";
        // Tìm sách theo ISBN hoặc theo tên và tác giả
        if (isIsbnFilled) {
            currentBook = getBookByIsbn(this.book.managerBook.getISBN(), sql);
        } else if (isTitleAndAuthorFilled) {
            currentBook = getBookByTitleAndAuthor(this.book.managerBook.getTitle(), this.book.managerBook.getAuthor());
        }

        if (currentBook == null) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy sách với thông tin đã nhập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return; // Dừng hàm nếu không tìm thấy sách
        }

        // Kiểm tra số lượng sách có sẵn trước khi mượn
        if (currentBook.getTotalBooksInStock() <= 0) {
            JOptionPane.showMessageDialog(null, "Sách hiện tại đã hết, không thể cho mượn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return; // Không cho phép mượn nếu không còn sách
        }

        if (currentUser == null) {
            return; // Nếu có thông tin người dùng không hợp lệ, thoát khỏi phương thức
        }
        // Dự đoán giá trị
        double valuePredict = model_predict.predict(currentBook.getPageNumber(), currentUser.getReadingTime());
        int value = (int) (valuePredict * 100);
        JOptionPane.showMessageDialog(null, "Khả năng trả sách đúng hạn là "+value+"%", "Thông báo", JOptionPane.WARNING_MESSAGE);
        // Gọi phương thức để hiển thị giao diện
        brorwerBook(currentBook, currentUser);

        currentBook.updateNumberBook(); // Giảm số lượng sách đi 1
        updateTotalBook(currentBook, currentBook.getISBN());
        updateStatus(currentBook);
    }

    //"Search Book"======================================================================================
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
            return;
        } else {
            // Nếu đúng một trường được nhập
            if (count == 0) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập ít nhất một trường để tìm kiếm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
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
    //"Return books"========================================================================
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

        saveUser_delelted(users.getIsbn(), users);

        String deleteQuery = "DELETE FROM User WHERE id = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Trả sách thành công!", "Thông báo!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Không thể trả sách!", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi trả sách: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        String sql = "SELECT * FROM Books WHERE ISBN = ?";
        Book book = getBookByIsbn(users.getIsbn(), sql);
        updateTotalBook(book, book.getISBN());
        updateStatus(book);


    }
    //"Remove Book"=================================================================
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
            String sql = "SELECT * FROM Books WHERE ISBN = ?";
            listBooks.removeBook(this.book.managerBook.getISBN());
            Book book_find =  getBookByIsbn(this.book.managerBook.getISBN(), sql);

            saveBook_delelted(book_find);
            // Xóa sách từ cơ sở dữ liệu
            String deleteQuery = "DELETE FROM Books WHERE ISBN = ?";
            try (Connection conn = SQLiteConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
                pstmt.setString(1, this.book.managerBook.getISBN());
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Xóa sách thành công!", "Thông báo!", JOptionPane.INFORMATION_MESSAGE);
                    return;
                } else {
                    JOptionPane.showMessageDialog(null, "Không thể xóa sách!", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Lỗi khi xóa sách: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }
    // "Edit User"==============================================================>>>>>>>>>>>>>>>>>>>>>
    public void updateUserInfor(){
        editUser();

    }
    public void editUser() {
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



        // Gọi phương thức để tìm sách và hiển thị thông tin
        LibraryUser user = getUserById(id); // Giả sử getBookByISBN là phương thức đã tạo trước đó

        if (user != null) {
            // Hiển thị thông tin sách lên các JTextField
            this.book.managerBook.setEditUser(user);
            updateUserDatabase(user);
        } else {
            return;
        }
    }
    //ham them sach=======================================================================================
    private void insertBook() {
        Book books = setBook();

        // Kiểm tra xem books có phải là null không
        if (books == null) {
            return;
        }

        // Kiểm tra nếu mã ISBN đã tồn tại thì thông báo và thoát
        if (isIsbnExist(books.getISBN())) {
            JOptionPane.showMessageDialog(null, "Mã ISBN đã tồn tại!!", "Cảnh báo !!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        books.addNumberBook();
        String sql = "INSERT INTO Books(title, author, publisher, genre, available_copies, page_number, status, available_books, ISBN) VALUES(?,?,?,?,?,?,?,?,?)";

        listBooks.addBook(books);
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Sử dụng đối tượng books vừa tạo để thêm vào cơ sở dữ liệu
            pstmt.setString(1, books.getTitle());
            pstmt.setString(2, books.getAuthors());
            pstmt.setString(3, books.getPublisher());
            pstmt.setString(4, books.getGenre());
            pstmt.setInt(5, books.getNumberBook());
            pstmt.setInt(6, books.getPageNumber());
            pstmt.setString(7, books.getStatus());
            pstmt.setInt(8, books.getTotalBooksInStock());
            pstmt.setString(9, books.getISBN());
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Thêm sách thành công!!", "Thông báo!!", JOptionPane.INFORMATION_MESSAGE);
            return;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    //"Search User"====================================================================================
    public void searchUser(){
        searchUserId();
    }
    //"Book Deleted"===================================================================================
    //"displayBookDeleted "


    public void displayBook(){
        String sql = "SELECT * FROM Books";
        ListBook listbook = getAllBook(sql);
        if (listbook != null) {
            // Cập nhật thông tin lên JTable
            managerBook.updateTable(listbook);
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy sách!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    }

    private ListBook getAllBook(String sql) {
        // Câu lệnh SQL để lấy tất cả các sách
        ListBook books = new ListBook(); // Danh sách để lưu các cuốn sách tìm thấy

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery(); // Thực hiện truy vấn

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
                        this.managerBook); // Thay `this.managerBook` nếu cần thiết
                books.addBook(book); // Thêm sách vào danh sách
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi truy vấn sách: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return books; // Trả về danh sách sách tìm thấy (có thể rỗng nếu không tìm thấy sách)
    }


    public void updateStatus(Book book) {
        String sqlUpdate = "UPDATE Books SET status = ? WHERE ISBN = ?";

        if (book == null) {
            JOptionPane.showMessageDialog(null, "Book không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return; // Thoát phương thức nếu book null
        }

        if (book.getTotalBooksInStock() == 0) {
            book.setStatus();
        }

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {

            // Cập nhật trạng thái vào cơ sở dữ liệu
            pstmtUpdate.setString(1, book.getStatus()); // Cập nhật trạng thái
            pstmtUpdate.setString(2, book.getISBN()); // Cập nhật theo ISBN

            int rowsAffected = pstmtUpdate.executeUpdate();

            if (rowsAffected > 0) {
                // Hiển thị thông báo khi cập nhật thành công
                System.out.println("Cap nhat thanh cong!!");
            } else {
                // Không tìm thấy sách với ISBN trong cơ sở dữ liệu
                JOptionPane.showMessageDialog(null, "Không tìm thấy sách với ISBN: " + book.getISBN(), "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            // Hiển thị thông báo lỗi khi xảy ra ngoại lệ
            e.printStackTrace(); // Hoặc bạn có thể sử dụng JOptionPane để thông báo lỗi cho người dùng
        }
    }

    private void saveUser_delelted(String isbn, LibraryUser user){
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
            pstmt.setDate(7, user.setBorrowDay());
            pstmt.setDate(8,  user.setDueDay());
            pstmt.setInt(9, user.getReadingTime());
            pstmt.setString(10, user.getTypeUser());
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void saveBook_delelted(Book book){
        String sql = "INSERT INTO Books_delete(title, author, publisher, genre, available_copies, page_number, status, available_books, ISBN) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, book.getTitle());
                pstmt.setString(2, book.getAuthors());
                pstmt.setString(3, book.getPublisher());
                pstmt.setString(4, book.getGenre());
                pstmt.setInt(5, book.getNumberBook());
                pstmt.setInt(6, book.getPageNumber());
                pstmt.setString(7, book.getStatus());
                pstmt.setInt(8, book.getTotalBooksInStock());
                pstmt.setString(9, book.getISBN());
                pstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void brorwerBook(Book books, LibraryUser user){
        String sql = "INSERT INTO User(id, fullname, address, phonenumber, email, isbn, borrow_date, due_date, readingTime, type_user) VALUES(?,?,?,?,?,?,?,?,?,?)";

        user.setIsbn(books.getISBN());
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user.getBorrowerId());
            pstmt.setString(2, user.getFullName());
            pstmt.setString(3, user.getAddress());
            pstmt.setString(4, user.getPhoneNumber());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getIsbn());
            pstmt.setDate(7, user.setBorrowDay());
            pstmt.setDate(8,  user.setDueDay());
            pstmt.setInt(9, user.getReadingTime());
            pstmt.setString(10, user.getTypeUser());
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    private void searchISBN(){
        String sql = "SELECT * FROM Books WHERE ISBN = ?";
        String ISBN = this.book.managerBook.getISBN();
        Book book = getBookByIsbn(ISBN, sql);
        if (book != null) {
            // Cập nhật thông tin lên JTable
            managerBook.updateTable_ISBN(book);
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy sách!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    }

    private void searchTitle() {
        String title = this.book.managerBook.getTitle();
        ListBook books = getBooksByTitle(title);  // Giả sử getBooksByTitle trả về một danh sách các sách

        if (books != null && !books.isEmpty()) {
            // Cập nhật thông tin lên JTable
            managerBook.updateTable(books);
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy sách!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
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
            return;
        }
    }


    private void searchPublisher() {
        String publisher = this.book.managerBook.getPublisher();
        ListBook books = getBooksByPublisher(publisher);

        if (books != null && !books.isEmpty()) {
            // Cập nhật thông tin lên JTable
            managerBook.updateTable(books);
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy sách!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    }


    private void searchPageNumber() {
        int pageNumber;

        try {
            pageNumber = Integer.parseInt(this.book.managerBook.getPageNumber());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Số trang không hợp lệ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ListBook books = getBooksByPageNumber(pageNumber);

        if (books != null && !books.isEmpty()) {
            // Cập nhật thông tin lên JTable
            managerBook.updateTable(books);
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy sách!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    }

    private void searchGenre() {
        String genre = this.book.managerBook.getGenre();
        ListBook books = getBooksByGenre(genre);  // Sửa lại thành getBooksByGenre

        if (books != null && !books.isEmpty()) {
            // Cập nhật thông tin lên JTable
            managerBook.updateTable(books);
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy sách!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
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




    //lay thong tin sach tu nguoi dung
    private Book setBook() {
        // Kiểm tra các trường thông tin khi người dùng nhập sách không được để trống
        if (this.book.managerBook.getTitle().isEmpty() ||
                this.book.managerBook.getAuthor().isEmpty() ||
                this.book.managerBook.getPublisher().isEmpty() ||
                this.book.managerBook.getISBN().isEmpty() ||
                this.book.managerBook.getGenre().isEmpty() ||
                this.book.managerBook.getNumberBook().isEmpty() ||
                this.book.managerBook.getPageNumber().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Không được để trống thông tin sách.", "Alert", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        // Xử lý ngoại lệ khi người dùng nhập số sách không phải là số
        try {
            this.book.setNumberBook(); // Giả sử setNumberBook() thiết lập giá trị
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập số sách hợp lệ.", "Alert", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (Integer.parseInt(this.book.managerBook.getNumberBook()) <= 0) {
            JOptionPane.showMessageDialog(null, "Số sách phải lớn hơn 0.", "Alert", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        try {
            this.book.setPageNumber(); // Giả sử setPageNumber() thiết lập giá trị
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập số trang sách hợp lệ.", "Alert", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (Integer.parseInt(this.book.managerBook.getPageNumber()) <= 0) {
            JOptionPane.showMessageDialog(null, "Số trang sách phải lớn hơn 0.", "Alert", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        // Thiết lập các thuộc tính khác của sách
        this.book.setTitle(); // Đảm bảo rằng các phương thức này thực hiện việc thiết lập giá trị cho thuộc tính
        this.book.setAuthors();
        this.book.setPublisher();
        this.book.setISBN();
        this.book.setGenre();
        return this.book; // Trả về đối tượng book đã được thiết lập
    }


    private LibraryUser setUser() {

        if (this.user.managerBook.getFullName().isEmpty() || this.user.managerBook.getEmail().isEmpty() ||
                this.user.managerBook.getAddress().isEmpty() || this.user.managerBook.getPhoneNumber().isEmpty() ||
                this.user.managerBook.getBorrowerId().isEmpty() ||
                this.user.managerBook.getTyprUse().isEmpty()|| (this.user.managerBook.getReadingTime() == 0)) {
            JOptionPane.showMessageDialog(null, "Không được để trống thông tin người dùng.", "Alert", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        String borrowerId = this.user.managerBook.getBorrowerId();
        try {
            Integer.parseInt(borrowerId); // Chỉ cần kiểm tra nếu ID là số hợp lệ
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID phải là một số hợp lệ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        this.user.setFullName();
        this.user.setEmail();
        this.user.setBorrowerId();
        this.user.setAddress();
        this.user.setReadingTime();
        this.user.setDueDay();
        this.user.setBorrowDay();
        this.user.setPhoneNumber();
        this.user.setTypeUser();

        return this.user;
    }
    private void searchUserId() {
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
                !this.user.managerBook.getTyprUse().isEmpty()) {

            JOptionPane.showMessageDialog(null, "Chỉ được nhập Borrower ID, không được nhập thêm thông tin khác!", "Thông báo", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LibraryUser users = getUserById(id);
        if (users == null) {
            return;
        }
        String sql = "SELECT * FROM Books WHERE ISBN = ?";
        Book book = getBookByIsbn(users.getIsbn(), sql);
        if (book != null) {
            // Cập nhật thông tin lên JTable
            managerBook.displayTable_user(book, users);
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy user với ISBN này!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
        System.out.println("Searching book...");
    }




    public void updateTotalBook(Book book, String isbn) {
        String sqlUpdate = "UPDATE Books SET available_books = ? WHERE ISBN = ?";

        if (book == null) {
            JOptionPane.showMessageDialog(null, "Book không hợp lệ hoặc không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {
            // Cập nhật số lượng vào cơ sở dữ liệu
            pstmtUpdate.setInt(1, book.getTotalBooksInStock());
            pstmtUpdate.setString(2, isbn);
            int rowsAffected = pstmtUpdate.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cap nhat thanh cong!!");
            } else {
                // Không tìm thấy sách với ISBN trong cơ sở dữ liệu
                JOptionPane.showMessageDialog(null, "Không tìm thấy sách với ISBN: " + isbn, "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            // Hiển thị thông báo lỗi khi xảy ra ngoại lệ
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật số lượng sách: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    private LibraryUser getUserById(int id) {
        String sql = "SELECT * FROM User WHERE id = ?";
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
                        rs.getDate("borrow_date"),
                        rs.getDate("due_date"),
                        rs.getDate("return_date"),
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

    public Book getBookByIsbnDelete(String isbn) {
        Book book = null;
        String sql1 = "SELECT * FROM Books WHERE ISBN = ?"; // Cập nhật câu lệnh SQL

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql1)) {
            pstmt.setString(1, isbn); // Thiết lập tham số ISBN
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
            } else {
                System.out.println("Không tìm thấy sách với ISBN: " + isbn);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi truy vấn sách: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return book; // Trả về book (có thể là null nếu không tìm thấy)
    }

    public Book getBookByIsbn(String isbn, String sql) {
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

    private void updateUserDatabase(LibraryUser user) {
        // Check if the user object is not null
        if (user == null) {
            JOptionPane.showMessageDialog(null, "User cannot be null!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "UPDATE User SET fullname = ?, address = ?, phonenumber = ?, email = ?, isbn = ?, borrow_date = ?, due_date = ?, readingTime = ?, type_user = ? WHERE id = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set values for parameters from the user object
            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getAddress());
            pstmt.setString(3, user.getPhoneNumber());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getIsbn());
            pstmt.setDate(6, user.getBorrowDay());
            pstmt.setDate(7, user.getDueDay());
            pstmt.setInt(8, user.getReadingTime());
            pstmt.setInt(9, user.getBorrowerId()); // Use user ID for the WHERE clause

            // Execute the update
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Cập nhật thông tin user thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the error
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

        updatedBook.addNumberBook();
        updateTotalBook(updatedBook, updatedBook.getISBN());


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