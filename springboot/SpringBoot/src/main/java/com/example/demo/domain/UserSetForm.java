package com.example.demo.domain;

import java.util.Set;

/**
 * Created by hunter on 2018/4/2.
 */
public class UserSetForm {
    Set<User> users;

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "UserSetForm{" +
                "users=" + users +
                '}';
    }
}
