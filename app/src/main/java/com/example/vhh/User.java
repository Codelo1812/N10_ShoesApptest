package com.example.vhh;

import java.io.Serializable;
import com.google.firebase.auth.FirebaseUser;

public class User implements Serializable {
    private String userName;
    private String passWord;
    private String email;
    private String phoneNumber;
    private String role;
    private String uid;
    private int sex;
    public User(){};
    public User(String userName, String passWord, String email, String phoneNumber, int sex){
        this.userName = userName;
        this.passWord = passWord;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.sex = sex;
    }
    public User(String userName, String passWord, String email,String role,String uid){
        this.userName = userName;
        this.passWord = passWord;
        this.email = email;
        this.role=role;
        this.uid=uid;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassWord() {
        return passWord;
    }
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public int getSex() {
        return sex;
    }
    public void setSex(int sex) {
        this.sex = sex;
    }
    public void setRole(String role){
        this.role=role;
    }
    public String getRole(){
        return role;
    }
    public void setUid(String uid){
        this.uid=uid;
    }
    public String getUid(){
        return uid;
    }
}
