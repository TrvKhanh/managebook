package org.view;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Component;
import java.awt.Desktop.Action;

import java.awt.Font;
import java.awt.Label;
import javax.swing.border.BevelBorder;
import java.awt.Color;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import org.controller.ManagerController;
import org.model.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.sql.Date ;
import java.util.Calendar;

@SuppressWarnings("unused")
public class ManagerBook extends JFrame {

    private static final long serialVersionUID = 1L;
    private final JButton btnDisplayBooks;
    private final JButton btnDisplayUser;
    private JPanel contentPane;
    private JTextField titelBook_text;
    private JTextField author_text;
    private JTextField nameBrrower_text;
    private JTextField address_text;
    private JTextField phoneName_text;
    private JTextField email_text;
    private JTextField brrowerId_text;
    private JTable table;
    private JTextField ISBN_text;
    private JTextField manager_text;
    private JTextField publisher_text;
    private JTextField genre_text;
    private JTextField numberBook_text;
    private JTextField page_number_text;
    private JComboBox comboBox_type_user;
    private JSpinner spinner_readingTime;
    private DefaultTableModel model ;
    private ManagerController action;
    private JSpinner spinner_readingTime_dueDate;
    private JSpinner spinner_readingTime_borrowDate;
    private JButton btnArrange_title;
    private JButton btnArrangePageNum;

    /**
     * Launch the application.
     */


    /**
     * Create the frame.
     */
    public ManagerBook(ListBook listBook) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1920, 1080); // Reduced window size to make it more manageable
        action = new ManagerController(this, listBook);



        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        JMenuItem mntmOpen = new JMenuItem("OPen");
        mnFile.add(mntmOpen);

        JMenuItem mntmClose = new JMenuItem("Close");
        mnFile.add(mntmClose);

        JSeparator separator_2 = new JSeparator();
        mnFile.add(separator_2);

        JMenuItem mntmExit = new JMenuItem("Exit");
        mnFile.add(mntmExit);

        JMenu mnAbout = new JMenu("About");
        menuBar.add(mnAbout);

        JMenuItem mntmAboutMe = new JMenuItem("About me");
        mnAbout.add(mntmAboutMe);

        // Create the content pane
        contentPane = new JPanel();
        contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        Label label = new Label("Information");
        label.setFont(new Font("Dialog", Font.PLAIN, 23));
        label.setBounds(36, 155, 151, 21);
        contentPane.add(label);

        JLabel lblName = new JLabel("Full Name:");
        lblName.setBounds(60, 493, 80, 15);
        contentPane.add(lblName);

        titelBook_text = new JTextField();
        titelBook_text.setBounds(217, 224, 183, 19);
        contentPane.add(titelBook_text);
        titelBook_text.setColumns(10);

        author_text = new JTextField();
        author_text.setBounds(217, 251, 183, 19);
        contentPane.add(author_text);
        author_text.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("Address:");
        lblNewLabel_1.setBounds(60, 520, 70, 15);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("Phone number:");
        lblNewLabel_2.setBounds(60, 547, 111, 15);
        contentPane.add(lblNewLabel_2);

        JLabel lblNewLabel_3 = new JLabel("Title: ");
        lblNewLabel_3.setBounds(60, 226, 70, 15);
        contentPane.add(lblNewLabel_3);

        JLabel lblNewLabel_4 = new JLabel("Author:");
        lblNewLabel_4.setBounds(60, 253, 70, 15);
        contentPane.add(lblNewLabel_4);

        JLabel lblNewLabel_5 = new JLabel("  ");
        lblNewLabel_5.setBounds(60, 580, 94, 15);
        contentPane.add(lblNewLabel_5);

        JLabel lblBorrower = new JLabel("Borrower");
        lblBorrower.setBounds(36, 466, 70, 15);
        contentPane.add(lblBorrower);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(60, 580, 70, 15);
        contentPane.add(lblEmail);

        JButton bntBorrow = new JButton("Borrow books");
        bntBorrow.setBackground(Color.GREEN);
        bntBorrow.setBounds(60, 846, 139, 25);
        bntBorrow.addActionListener(action);
        contentPane.add(bntBorrow);

        JButton btnSearchBook = new JButton("Search Book");
        btnSearchBook.addActionListener(action);
        btnSearchBook.setBackground(Color.CYAN);
        btnSearchBook.setBounds(261, 895, 139, 25);
        contentPane.add(btnSearchBook);

        JButton btnReturn = new JButton("Return books");
        btnReturn.setBackground(Color.GREEN);
        btnReturn.setBounds(60, 895, 139, 25);
        btnReturn.addActionListener(action);
        contentPane.add(btnReturn);

        JButton btnRemoveBook = new JButton("Remove Book");
        btnRemoveBook.setBackground(Color.RED);
        btnRemoveBook.setBounds(1066, 895, 139, 25);
        btnRemoveBook.addActionListener(action);
        contentPane.add(btnRemoveBook);

        nameBrrower_text = new JTextField();
        nameBrrower_text.setColumns(10);
        nameBrrower_text.setBounds(217, 491, 183, 19);
        contentPane.add(nameBrrower_text);

        address_text = new JTextField();
        address_text.setColumns(10);
        address_text.setBounds(217, 520, 183, 19);
        contentPane.add(address_text);

        phoneName_text = new JTextField();
        phoneName_text.setColumns(10);
        phoneName_text.setBounds(217, 551, 183, 19);
        contentPane.add(phoneName_text);

        email_text = new JTextField();
        email_text.setColumns(10);
        email_text.setBounds(217, 578, 183, 19);
        contentPane.add(email_text);

        JLabel lblDueDate = new JLabel("Due Date:");
        lblDueDate.setBounds(60, 608, 80, 21);
        contentPane.add(lblDueDate);

        brrowerId_text = new JTextField();
        brrowerId_text.setColumns(10);
        brrowerId_text.setBounds(217, 657, 183, 19);
        contentPane.add(brrowerId_text);

        JLabel lblBorrowerId = new JLabel("Borrower id:");
        lblBorrowerId.setBounds(60, 659, 94, 15);
        contentPane.add(lblBorrowerId);

        JLabel lblLybrari = new JLabel("Library Book Manager ");
        lblLybrari.setBounds(337, 37, 428, 49);
        contentPane.add(lblLybrari);
        lblLybrari.setFont(new Font("Fira Sans Book", Font.BOLD, 40));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(412, 223, 1466, 365);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); // Hiển thị thanh cuộn ngang
        contentPane.add(scrollPane);

        JLabel lblIsbn_3_1 = new JLabel("Page Number:");
        lblIsbn_3_1.setBounds(60, 388, 111, 15);
        contentPane.add(lblIsbn_3_1);

        page_number_text = new JTextField();
        page_number_text.setColumns(10);
        page_number_text.setBounds(217, 386, 183, 19);
        contentPane.add(page_number_text);
        table = new JTable();

        table.setFont(new Font("Dialog", Font.BOLD, 14));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Tắt tự động điều chỉnh kích thước cột
        table.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        "ISBN", "Title", "Author", "Publisher", "Genre", "Page number", "Available Copies", "Available Books", "Status", "Brrower ID", "Full Name", "Address", "Email", "Phone Number", "Borrow Date", "Due Date", "Return Date", "Type User", "Fined money"
                }
        ));
        model = (DefaultTableModel) table.getModel();
        scrollPane.setViewportView(table);

        JLabel lblReadingTime = new JLabel("Reading Time:");
        lblReadingTime.setBounds(60, 730, 127, 15);
        contentPane.add(lblReadingTime);

        spinner_readingTime = new JSpinner();
        spinner_readingTime.setBounds(217, 725, 40, 20);
        contentPane.add(spinner_readingTime);

        JButton btnAddBook = new JButton("Add Book");
        btnAddBook.setBackground(Color.CYAN);
        btnAddBook.setBounds(261, 846, 139, 25);
        btnAddBook.addActionListener(action);
        contentPane.add(btnAddBook);

        JLabel lblNewLabel = new JLabel("Function");
        lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 23));
        lblNewLabel.setBounds(19, 774, 135, 33);
        contentPane.add(lblNewLabel);

        JLabel lblIsbn = new JLabel("ISBN:");
        lblIsbn.setBounds(60, 279, 70, 15);
        contentPane.add(lblIsbn);

        ISBN_text = new JTextField();
        ISBN_text.setColumns(10);
        ISBN_text.setBounds(217, 277, 183, 19);
        contentPane.add(ISBN_text);

        manager_text = new JTextField();
        manager_text.setBackground(Color.LIGHT_GRAY);
        manager_text.setBounds(1494, 37, 221, 19);
        manager_text.setEditable(false);
        contentPane.add(manager_text);
        manager_text.setColumns(10);
        JLabel lblNewLabel_10 = new JLabel("Admin:");
        lblNewLabel_10.setBounds(1431, 39, 57, 15);
        contentPane.add(lblNewLabel_10);

        Box horizontalBox_2 = Box.createHorizontalBox();
        horizontalBox_2.setBorder(new LineBorder(new Color(0, 0, 0)));
        horizontalBox_2.setBounds(36, 12, 1842, 124);
        contentPane.add(horizontalBox_2);

        publisher_text = new JTextField();
        publisher_text.setColumns(10);
        publisher_text.setBounds(217, 304, 183, 19);
        contentPane.add(publisher_text);

        genre_text = new JTextField();
        genre_text.setColumns(10);
        genre_text.setBounds(217, 329, 183, 19);
        contentPane.add(genre_text);

        JLabel lblPublisher = new JLabel("Publisher:");
        lblPublisher.setBounds(60, 306, 94, 15);
        contentPane.add(lblPublisher);

        JLabel lblGenre = new JLabel("Genre:");
        lblGenre.setBounds(60, 331, 70, 15);
        contentPane.add(lblGenre);

        JLabel lblIsbn_3 = new JLabel("Number:");
        lblIsbn_3.setBounds(60, 361, 70, 15);
        contentPane.add(lblIsbn_3);

        numberBook_text = new JTextField();
        numberBook_text.setColumns(10);
        numberBook_text.setBounds(217, 359, 183, 19);
        contentPane.add(numberBook_text);

        JLabel lblNewLabel_8 = new JLabel("Manager Book");
        lblNewLabel_8.setBounds(38, 819, 102, 15);
        contentPane.add(lblNewLabel_8);

        JLabel lblBorrower_1 = new JLabel("Book");
        lblBorrower_1.setBounds(36, 199, 70, 15);
        contentPane.add(lblBorrower_1);

        Box horizontalBox_1_1_1 = Box.createHorizontalBox();
        horizontalBox_1_1_1.setBorder(new LineBorder(new Color(0, 0, 0)));
        horizontalBox_1_1_1.setBounds(36, 811, 1242, 146);
        contentPane.add(horizontalBox_1_1_1);


        JLabel lblTypeUser = new JLabel("Type User:");
        lblTypeUser.setBounds(60, 691, 127, 15);
        contentPane.add(lblTypeUser);


        String[] typeUser = new String[] {"","Lecturer", "Student"};
        comboBox_type_user = new JComboBox<String>(typeUser);
        comboBox_type_user.setBounds(217, 689, 183, 21);
        contentPane.add(comboBox_type_user);


        JButton button_exit = new JButton("Exit");
        button_exit.setBackground(Color.PINK);
        button_exit.setBounds(1066, 846, 139, 25);
        contentPane.add(button_exit);
        button_exit.addActionListener(action);

        JButton btnClear = new JButton("Clear");
        btnClear.setBackground(Color.CYAN);
        btnClear.setBounds(886, 846, 151, 25);
        btnClear.addActionListener(action);
        contentPane.add(btnClear);

        String[] status = new String[] {"","Available", "Check Out"};

        JButton btnBookDeleted = new JButton("Book Deleted");
        btnBookDeleted.setBackground(Color.MAGENTA);
        btnBookDeleted.setBounds(886, 895, 151, 25);
        contentPane.add(btnBookDeleted);
        btnBookDeleted.addActionListener(action);

        btnDisplayUser = new JButton("Display user");
        btnDisplayUser.setBackground(Color.CYAN);
        btnDisplayUser.setBounds(462, 846, 139, 25);
        contentPane.add(btnDisplayUser);
        btnDisplayUser.addActionListener(action);

        btnDisplayBooks = new JButton("Display books");
        btnDisplayBooks.setBackground(Color.CYAN);
        btnDisplayBooks.setBounds(462, 895, 139, 25);
        contentPane.add(btnDisplayBooks);

        btnArrange_title = new JButton("Arrange Title");
        btnArrange_title.setBackground(Color.CYAN);
        btnArrange_title.setBounds(636, 846, 193, 25);
        contentPane.add(btnArrange_title);
        btnArrange_title.addActionListener(action);

        spinner_readingTime_borrowDate = new JSpinner();
        spinner_readingTime_borrowDate.setModel(new SpinnerDateModel(new Date(1728579600000L), new Date(1728579600000L), null, Calendar.DAY_OF_YEAR));
        spinner_readingTime_borrowDate.setBounds(217, 633, 183, 20);
        contentPane.add(spinner_readingTime_borrowDate);

        JLabel lblBorrowerDate = new JLabel("Borrower Date:");
        lblBorrowerDate.setBounds(60, 632, 127, 15);
        contentPane.add(lblBorrowerDate);

        spinner_readingTime_dueDate = new JSpinner();
        spinner_readingTime_dueDate.setModel(new SpinnerDateModel(new Date(1728579600000L), new Date(1728579600000L), null, Calendar.DAY_OF_YEAR));
        spinner_readingTime_dueDate.setBounds(217, 608, 183, 20);
        contentPane.add(spinner_readingTime_dueDate);

        btnArrangePageNum = new JButton("Arrange Page Number");
        btnArrangePageNum.setBackground(Color.CYAN);
        btnArrangePageNum.setBounds(636, 895, 193, 25);
        contentPane.add(btnArrangePageNum);
        btnArrangePageNum.addActionListener(action);
        btnDisplayBooks.addActionListener(action);

        // Điều chỉnh độ rộng cho các cột
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(170); // Đặt độ rộng của mỗi cột là 150 (có thể điều
            // chỉnh)
        }}

    //lay ten dang nhap
    public void setNameAdmin(String name){
        manager_text.setText(name);
    }
    //lay ten sach
    public String getTitle() {
        return titelBook_text.getText();
    }

    public String getAuthor() {
        return author_text.getText();
    }

    public String getISBN() {
        return ISBN_text.getText();
    }

    public String getFullName() {
        return nameBrrower_text.getText();
    }

    public String getAddress() {
        return address_text.getText();
    }

    public String getPhoneNumber() {
        return phoneName_text.getText();
    }

    public String getEmail() {
        return email_text.getText();
    }

    public Date getDueDate() {
        java.util.Date utilDate = (java.util.Date) spinner_readingTime_dueDate.getValue();
        return new Date(utilDate.getTime());
    }

    public Date getBorrowDate() {
        java.util.Date utilDate = (java.util.Date) spinner_readingTime_borrowDate.getValue();
        return new Date(utilDate.getTime());
    }

    public String getNumberBook() {
        return numberBook_text.getText();
    }
    public String getPublisher(){
        return publisher_text.getText();
    }
    public int getAvailableCopies(){
        return Integer.parseInt(numberBook_text.getText());
    }
    public String getGenre() {
        return genre_text.getText();
    }

    public int getReadingTime(){
        return (int)spinner_readingTime.getValue();
    }

    public String getBorrowerId(){
        return brrowerId_text.getText();
    }

    public String getTyprUse(){
        return (String) comboBox_type_user.getSelectedItem();
    }

    public String getPageNumber(){
        return page_number_text.getText();
    }
    public void clear(){
        titelBook_text.setText("");
        author_text.setText("");
        ISBN_text.setText("");
        publisher_text.setText("");
        genre_text.setText("");
        numberBook_text.setText("");
        nameBrrower_text.setText("");
        address_text.setText("");
        phoneName_text.setText("");
        email_text.setText("");
        brrowerId_text.setText("");
        page_number_text.setText("");
        spinner_readingTime.setValue(0);
        comboBox_type_user.setSelectedIndex(0);
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);



    }
    public void updateTable_ISBN(Book book) {
        // Giả sử bạn đã tạo một DefaultTableModel trước đó
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        // Xóa tất cả các hàng hiện có
        model.setRowCount(0);

        // Lặp qua danh sách sách và thêm vào model

        Object[] rowData = {book.getISBN(), book.getTitle(), book.getAuthors(), book.getPublisher(), book.getGenre(), book.getPageNumber(), book.getNumberBook(), book.getTotalBooksInStock() , book.getStatus()};
        model.addRow(rowData);  // Thêm hàng vào DefaultTableModel

    }

    public void displayTable_user(Book book, LibraryUser user) {
        // Giả sử bạn đã tạo một DefaultTableModel trước đó
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        // Xóa tất cả các hàng hiện có
        model.setRowCount(0);

        // Lặp qua danh sách sách và thêm vào model

        Object[] rowData = {book.getISBN(), book.getTitle(), book.getAuthors(), book.getPublisher(), book.getGenre(), book.getPageNumber(), book.getNumberBook(), book.getTotalBooksInStock() , book.getStatus(),
                user.getBorrowerId(),user.getFullName(), user.getAddress(), user.getEmail(), user.getPhoneNumber(), user.getBorrowDay(), user.getDueDay(), user.getReturnDay(), user.getTypeUser(), user.getFineMoney()};
        model.addRow(rowData);  // Thêm hàng vào DefaultTableModel

    }

    public void displayTables_user(LibraryUser user) {
        // Giả sử bạn đã tạo một DefaultTableModel trước đó
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        // Không cần xóa tất cả các hàng hiện có trong vòng lặp
        Object[] rowData = {
                "", "", "", "", "", "", "", "", "",
                user.getBorrowerId(),
                user.getFullName(),
                user.getAddress(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getBorrowDay(),
                user.getDueDay(),
                user.getReturnDay(),
                user.getTypeUser(),
                user.getFineMoney()
        };
        model.addRow(rowData);  // Thêm hàng vào DefaultTableModel
    }



    public void updateTable(ListBook books) {
        // Giả sử bạn đã tạo một DefaultTableModel trước đó
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        // Xóa tất cả các hàng hiện có
        model.setRowCount(0);

        // Lặp qua danh sách sách và thêm vào model
        for (Book book : books) { // Duyệt qua từng cuốn sách trong ListBook
            Object[] rowData = {
                    book.getISBN(),
                    book.getTitle(),
                    book.getAuthors(), // Sửa tên phương thức từ getAuthors thành getAuthor (nếu cần)
                    book.getPublisher(),
                    book.getGenre(),
                    book.getPageNumber(),
                    book.getNumberBook(), // Hoặc getNumberBook() nếu đó là phương thức đúng
                    book.getTotalBooksInStock(), // Cần đảm bảo phương thức này tồn tại
                    book.getStatus() // Cần đảm bảo phương thức này tồn tại
            };
            model.addRow(rowData); // Thêm hàng vào DefaultTableModel
        }
    }

    public void updateTableUser(ListUser listUser, ListBook listBook) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        int num = listUser.getSize();
        ;// Xóa tất cả các hàng hiện có
        for(int i = 0; i<num; i++){
            LibraryUser user = listUser.getUserByIndex(i);
            Book book = listBook.getBookByIndex(i);
            Object[] rowData = {
                    book.getISBN(),
                    book.getTitle(),
                    book.getAuthors(), // Sửa tên phương thức từ getAuthors thành getAuthor (nếu cần)
                    book.getPublisher(),
                    book.getGenre(),
                    book.getPageNumber(),
                    book.getNumberBook(), // Hoặc getNumberBook() nếu đó là phương thức đúng
                    book.getTotalBooksInStock(), // Cần đảm bảo phương thức này tồn tại
                    book.getStatus(),
                    user.getBorrowerId(),user.getFullName(), user.getAddress(), user.getEmail(), user.getPhoneNumber(),
                    user.getBorrowDay(), user.getDueDay(), user.getReturnDay(), user.getTypeUser(), user.getFineMoney()
            };
            model.addRow(rowData);
        }
    }


    public void setEditUser(LibraryUser user){
        brrowerId_text.setText(String.valueOf(user.getBorrowerId()));
        nameBrrower_text.setText(user.getFullName());
        address_text.setText(user.getAddress());
        phoneName_text.setText(user.getPhoneNumber());
        email_text.setText(user.getEmail());
        spinner_readingTime_dueDate.setValue(user.getDueDay());
        spinner_readingTime_borrowDate.setValue(user.getBorrowDay());
        comboBox_type_user.setSelectedIndex(0);
        spinner_readingTime.setValue(0);
    }
}
