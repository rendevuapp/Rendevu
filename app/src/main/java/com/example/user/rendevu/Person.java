/*
* This class is not used!!!
*
*
* */

package com.example.user.rendevu;
/*
    Ricardo Cantu
    This class creates a 'Person' Object that is used by the card view.
 */
public class Person {
     String name;
     String age;
     int photoID;
     int phoneNumber;

    public Person(String name, String age, int photoID){
        this.name = name;
        this.age = age;
        this.photoID = photoID;
        this.phoneNumber = phoneNumber;
    }


    /*
    * Josh
    * functions for get/set contact data from firebase
    * */
    public String getFullName() {
        return name;
    }

    public void setFullName(String name) {
        this.name = name;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}



