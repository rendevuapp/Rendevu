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

    public User(){}

    public User(String fullname, String username,
                String email, String dob, int userPhone) {
        try {
            this.fullname = fullname;
            this.userPhone = userPhone;
            this.username = username;
            this.email = email;
            this.dob = dob;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * Setters and getters for user data
    * */
    public String getFullName() {
            return fullname;
    }

    public void setFullName(String fullname) {
        try {
            this.fullname = fullname;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
            return username;
    }

    public void setUsername(String username) {
        try {
            this.username = username;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getEmail() {
            return email;
    }

    public void setEmail(String email) {
        try {
            this.email = email;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDOB() {
            return dob;
    }

    public void setDOB(String dob) {
        try {
            this.dob = dob;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getPhoneNumber() {
            return userPhone;
    }

    public void setPhoneNumber(int userPhone) {
        try {
            this.userPhone = userPhone;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
