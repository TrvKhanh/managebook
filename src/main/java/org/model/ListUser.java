package org.model;

import java.util.ArrayList;
import java.util.List;

public class ListUser {
    private List<LibraryUser> users;

    public ListUser(){
        users = new ArrayList<>();
    }

    public void addUser(LibraryUser user){
        users.add(new LibraryUser(user));
    }

}
