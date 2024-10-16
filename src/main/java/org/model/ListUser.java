package org.model;

import java.util.ArrayList;
import java.util.Iterator;

public class ListUser implements Iterable<LibraryUser> {
    private ArrayList<LibraryUser> users;

    // Khởi tạo danh sách người dùng
    public ListUser() {
        this.users = new ArrayList<>();
    }

    // Kiểm tra xem danh sách người dùng có rỗng hay không
    public boolean isEmpty() {
        return users.isEmpty();
    }

    public int getSize() {
        return users.size();
    }

    // Thêm một người dùng vào danh sách
    public void addUser(LibraryUser user) {
        users.add(new LibraryUser(user)); // Thêm bản sao của người dùng
    }

    // Trả về danh sách tất cả các người dùng
    public ArrayList<LibraryUser> getUsers() {
        return users; // Trả về danh sách gốc, không tạo bản sao
    }

    // Lấy một người dùng theo chỉ số
    public LibraryUser getUserByIndex(int index) {
        if (index >= 0 && index < users.size()) {
            return users.get(index);
        } else {
            throw new IndexOutOfBoundsException("Chỉ số không hợp lệ: " + index);
        }
    }

    // Triển khai phương thức iterator để sử dụng for-each
    @Override
    public Iterator<LibraryUser> iterator() {
        return users.iterator();
    }

    // Phương thức để duyệt và hiển thị thông tin của tất cả người dùng
    public void forUser() {
        if (users.isEmpty()) {
            System.out.println("Danh sách người dùng rỗng.");
            return;
        }

        System.out.println("Danh sách người dùng:");
        for (LibraryUser user : users) {
            System.out.println(user); // In thông tin của từng người dùng
        }
    }
}
