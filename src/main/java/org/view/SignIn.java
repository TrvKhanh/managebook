package org.view;

import org.controller.SignInController;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class SignIn extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField id_signIn_text;
    private JPasswordField passwordSignIn;
    private JPasswordField passwordReEnter;
    private JTextField fullName_signIn;
    private JTextField address_SignIn;
    private JTextField email_signIN;
    private JTextField phoneNumber_signIn;
    private JButton bntSignIn;
    private JButton btnReset_SignIn;

    /**
     * Launch the application.
     */

    /**
     * Create the frame.
     */
    public SignIn() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(800, 400, 368, 447);

        SignInController signInController = new SignInController(this);
        // Create the content pane
        contentPane = new JPanel();
        contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        Component verticalGlue = Box.createVerticalGlue();
        verticalGlue.setBounds(186, 306, 1, 1);
        contentPane.add(verticalGlue);

        id_signIn_text = new JTextField();
        id_signIn_text.setBounds(201, 11, 148, 22);
        contentPane.add(id_signIn_text);
        id_signIn_text.setColumns(10);

        JButton bntSignIn = new JButton("Sign In");
        bntSignIn.addActionListener(signInController);
        bntSignIn.setBackground(Color.CYAN);
        bntSignIn.setBounds(33, 349, 88, 22);

        contentPane.add(bntSignIn);

        JLabel lblNewLabel = new JLabel("Name ACCOUNT:");
        lblNewLabel.setBounds(33, 12, 150, 19);
        contentPane.add(lblNewLabel);

        JLabel lblPassworld = new JLabel("Password:");
        lblPassworld.setBounds(33, 54, 75, 19);
        contentPane.add(lblPassworld);

        JButton btnReset_SignIn = new JButton("Cancel");
        btnReset_SignIn.setBackground(Color.CYAN);
        btnReset_SignIn.setBounds(200, 349, 88, 22);
        btnReset_SignIn.addActionListener(signInController);
        contentPane.add(btnReset_SignIn);

        JLabel lblReenterPassword = new JLabel("Re-enter password:");
        lblReenterPassword.setBounds(33, 85, 176, 19);
        contentPane.add(lblReenterPassword);

        passwordSignIn = new JPasswordField();
        passwordSignIn.setBounds(201, 54, 148, 19);
        contentPane.add(passwordSignIn);

        passwordReEnter = new JPasswordField();
        passwordReEnter.setBounds(201, 85, 148, 19);
        contentPane.add(passwordReEnter);

        fullName_signIn = new JTextField();
        fullName_signIn.setColumns(10);
        fullName_signIn.setBounds(201, 123, 148, 22);
        contentPane.add(fullName_signIn);

        address_SignIn = new JTextField();
        address_SignIn.setColumns(10);
        address_SignIn.setBounds(201, 162, 148, 22);
        contentPane.add(address_SignIn);

        email_signIN = new JTextField();
        email_signIN.setColumns(10);
        email_signIN.setBounds(201, 205, 148, 22);
        contentPane.add(email_signIN);

        phoneNumber_signIn = new JTextField();
        phoneNumber_signIn.setColumns(10);
        phoneNumber_signIn.setBounds(201, 246, 148, 22);
        contentPane.add(phoneNumber_signIn);

        JLabel lblNewLabel_1 = new JLabel("Full Name:");
        lblNewLabel_1.setBounds(33, 126, 111, 15);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_1_1 = new JLabel("Address:");
        lblNewLabel_1_1.setBounds(33, 165, 111, 15);
        contentPane.add(lblNewLabel_1_1);

        JLabel lblNewLabel_1_2 = new JLabel("Email:");
        lblNewLabel_1_2.setBounds(33, 208, 111, 15);
        contentPane.add(lblNewLabel_1_2);

        JLabel lblNewLabel_1_3 = new JLabel("Phone NUmber:");
        lblNewLabel_1_3.setBounds(33, 249, 111, 15);
        contentPane.add(lblNewLabel_1_3);

    }

    public JButton getBntSignIn() {
        return bntSignIn;
    }

    public JButton getBntCancel() {
        return btnReset_SignIn;
    }
    // Getter methods to retrieve values from text fields
    public String getIdSignInText() {
        return id_signIn_text.getText();
    }

    public char[] getPasswordSignIn() {
        return passwordSignIn.getPassword();
    }

    public char[] getPasswordReEnter() {
        return passwordReEnter.getPassword();
    }

    public String getFullNameSignIn() {
        return fullName_signIn.getText();
    }

    public String getAddressSignIn() {
        String text = address_SignIn.getText();
        return text;
    }

    public String getEmailSignIn() {
        return email_signIN.getText();
    }

    public String getPhoneNumberSignIn() {
        return phoneNumber_signIn.getText();
    }

}



