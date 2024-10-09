package org.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.view.SignIn;

public class SignInController implements ActionListener {
    private SignIn signIn;

    public SignInController(SignIn signIn) {
        this.signIn = signIn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        switch (actionCommand) {
            case "Sign In":
                System.out.println("Sign In Button Clicked");
                handleSignIn();
                break;
            case "Cancel":
                signIn.dispose();  // Đóng cửa sổ đăng ký
                break;
            default:
                break;
        }
    }

    // Xử lý sự kiện đăng ký tài khoản
    private void handleSignIn() {
        String username = signIn.getIdSignInText();
        String password = String.valueOf(signIn.getPasswordSignIn());
        String rePassword = String.valueOf(signIn.getPasswordReEnter());
        String fullName = signIn.getFullNameSignIn();
        String address = signIn.getAddressSignIn();
        String email = signIn.getEmailSignIn();
        String phoneNumber = signIn.getPhoneNumberSignIn();

        // Kiểm tra các trường không được để trống
        if (username.isEmpty() || password.isEmpty() || rePassword.isEmpty() || fullName.isEmpty() ||
                address.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Không được để trống thông tin.", "Alert", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Kiểm tra tính hợp lệ của mật khẩu
        if (!password.equals(rePassword)) {
            JOptionPane.showMessageDialog(null, "Mật khẩu không khớp.", "Alert", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Đăng ký tài khoản người dùng
        if (registerUser(username, password, fullName, address, email, phoneNumber)) {
            JOptionPane.showMessageDialog(null, "Đăng ký thành công.", "Success", JOptionPane.INFORMATION_MESSAGE);
            signIn.dispose();  // Đóng cửa sổ đăng ký sau khi thành công
        } else {
            JOptionPane.showMessageDialog(null, "Đăng ký thất bại vui lòng thử lại.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Phương thức thêm người dùng vào cơ sở dữ liệu
    private boolean registerUser(String username, String password, String fullName, String address, String email, String phoneNumber) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = SQLiteConnection.getConnection();  // Kết nối tới cơ sở dữ liệu
            String sql = "INSERT INTO Libian (fullName, address, phoneNumber, email, name_account, password) VALUES (?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, phoneNumber);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, username);
            preparedStatement.setString(6, password);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;  // Trả về true nếu đã thêm người dùng thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Trả về false nếu có lỗi
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
