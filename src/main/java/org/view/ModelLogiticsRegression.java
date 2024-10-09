package org.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.*;
import javax.swing.border.BevelBorder;

@SuppressWarnings("unused")
public class ModelLogiticsRegression extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField predect_text;

    public ModelLogiticsRegression() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(250, 130);
        setLocationRelativeTo(null);
        // Create the content pane
        contentPane = new JPanel();
        contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        Component verticalGlue = Box.createVerticalGlue();
        verticalGlue.setBounds(186, 306, 1, 1);
        contentPane.add(verticalGlue);

        predect_text = new JTextField();
        predect_text.setBackground(Color.LIGHT_GRAY);
        predect_text.setBounds(186, 11, 42, 22);
        contentPane.add(predect_text);
        predect_text.setEditable(false);
        predect_text.setColumns(10);

        JLabel lblNewLabel = new JLabel("Return books over limit:");
        lblNewLabel.setBounds(12, 12, 177, 19);
        contentPane.add(lblNewLabel);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBackground(Color.CYAN);
        btnCancel.setBounds(110, 58, 105, 22);
        contentPane.add(btnCancel);

        btnCancel.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Cho mượn sách!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);// Đóng giao diện
            dispose();
        });

    }
    public void setValuePredict(int value) {
        predect_text.setText(value + "%");
    }
}


