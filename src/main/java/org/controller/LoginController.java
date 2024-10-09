package org.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.model.ListBook;
import org.view.LogIn;
import org.view.ManagerBook;
import org.view.SignIn;

import javax.swing.*;

public class LoginController implements ActionListener {
    private LogIn logIn;
    private boolean exitLogin = true;
    private String nameId;
    private ListBook listBook;

    public LoginController(LogIn logIn, ListBook listBook) {
        this.logIn = logIn;
        this.listBook = listBook;

    }

    // Phương thức kiểm tra thông tin đăng nhập
    private boolean checkLogin(String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = SQLiteConnection.getConnection(); // Sử dụng lớp kết nối
            String sql = "SELECT * FROM Libian WHERE name_account = ? AND password = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            nameId = username;
            return resultSet.next(); // Nếu có kết quả, nghĩa là đăng nhập thành công
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false; // Đăng nhập không thành công
    }

    // Xử lý khi nhấn nút đăng nhập
    public boolean Login() {
        String idName = logIn.getIdLoginText();
        String password = logIn.getPasswordText();

        // Kiểm tra thông tin đăng nhập
        if (checkLogin(idName, password)) {
            try {
                logIn.dispose(); // Đóng cửa sổ đăng nhập
                exitLogin = false;
                ManagerBook frame = new ManagerBook(listBook);
                frame.setVisible(true);
                frame.setNameAdmin(nameId);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Hiển thị alert khi đăng nhập không thành công
            JOptionPane.showMessageDialog(logIn, "Thông tin đăng nhập không hợp lệ.\n" +
                    "Vui lòng kiểm tra lại thông tin đăng nhâp.\n"+ "Nếu chưa có tài khoản vui lòng nhấp vào nút sign in."
                    , "Thông báo", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private void openSignInWindow() {
        SignIn signInView = new SignIn();  // Tạo đối tượng giao diện Sign In
        signInView.setVisible(true);  // Hiển thị cửa sổ Sign In
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        switch (actionCommand) {
            case "Log In":
                Login();
                break;
            case "Sign In":
                openSignInWindow();
                break;
            case "Cancel":
                logIn.dispose(); // Đóng cửa sổ đăng nhập
                exitLogin = false;
        }
    }
}
