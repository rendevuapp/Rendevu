package com.rendevu.main;

/**
 * Josh Davenport
 *
 * User object is created after registering.  Profile data is then
 * stored in database under a unique reference id string.
 */

public class User {

    private String fullname, username,
                   email, dob;
    private int userPhone;

    public User(String fullname, String username,
                String email, String dob, int userPhone) {
        this.fullname = fullname;
        this.userPhone = userPhone;
        this.username = username;
        this.email = email;
        this.dob = dob;
    }

    public String getFullName() {
        return fullname;
    }

    public void setFullName(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDOB() {
        return dob;
    }

    public void setDOB(String dob) {
        this.dob = dob;
    }

    public int getPhoneNumber() {
        return userPhone;
    }

    public void setPhoneNumber(int userPhone) {
        this.userPhone = userPhone;
    }
}
