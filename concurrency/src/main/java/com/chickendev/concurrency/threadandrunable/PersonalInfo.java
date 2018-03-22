package com.chickendev.concurrency.threadandrunable;


import java.io.Serializable;

public class PersonalInfo implements Serializable {
    public static final long serialVersionUID = 1L;

    private String fullName;
    private String email;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
