package org.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.event.AncestorListener;

import org.controller.LoginController;
import org.model.ListBook;

import javax.swing.JPasswordField;

public class LogIn extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField id_login_text;
    private JPasswordField passwordUser;
    private JButton btnLogin;
    private JButton btnReset;
    public LogIn(ListBook listBook) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(800, 400, 300, 200);

        // Tạo controller cho Login
        LoginController action = new LoginController(this, listBook);

        // Tạo pane chính
        contentPane = new JPanel();
        contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        Component verticalGlue = Box.createVerticalGlue();
        verticalGlue.setBounds(186, 306, 1, 1);
        contentPane.add(verticalGlue);

        id_login_text = new JTextField();
        id_login_text.setBounds(130, 11, 148, 22);
        contentPane.add(id_login_text);
        id_login_text.setColumns(10);

        btnLogin = new JButton("Log In");
        btnLogin.setBackground(Color.CYAN);
        btnLogin.setBounds(53, 85, 88, 22);
        btnLogin.addActionListener(action); // Kết nối với action listener
        contentPane.add(btnLogin);

        JLabel lblNewLabel = new JLabel("ID USER:");
        lblNewLabel.setBounds(33, 12, 65, 19);
        contentPane.add(lblNewLabel);

        JLabel lblPassworld = new JLabel("Password:");
        lblPassworld.setBounds(33, 54, 75, 19);
        contentPane.add(lblPassworld);

        btnReset = new JButton("Cancel");
        btnReset.setBackground(Color.CYAN);
        btnReset.setBounds(178, 85, 88, 22);
        btnReset.addActionListener(action); // Kết nối với action listener
        contentPane.add(btnReset);

        passwordUser = new JPasswordField();
        passwordUser.setBounds(130, 54, 148, 19);
        contentPane.add(passwordUser);

        JButton btnSignIn = new JButton("Sign In");
        btnSignIn.setBackground(Color.GREEN);
        btnSignIn.setBounds(53, 119, 88, 22);
        btnSignIn.addActionListener(action);
        contentPane.add(btnSignIn);
    }

    // Phương thức để lấy thông tin đăng nhập
    public String getIdLoginText() {
        return id_login_text.getText();
    }

    public String getPasswordText() {
        return new String(passwordUser.getPassword());
    }

    public void resetFields() {
        id_login_text.setText("");
        passwordUser.setText("");
    }
}
