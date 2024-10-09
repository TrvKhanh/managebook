package org.view;

import org.controller.LoginController;
import org.model.Book;
import org.model.ListBook;

import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        ListBook books = new ListBook();

        try {
            LogIn frame = new LogIn(books);
            frame.setVisible(true);
        } catch (Exception e) {
            // Thông báo cho người dùng nếu có lỗi
            JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi khi khởi tạo giao diện đăng nhập.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }


    }
}
