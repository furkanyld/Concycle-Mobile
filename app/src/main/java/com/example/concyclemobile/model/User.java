package com.example.concyclemobile.model;

public class User {
    private String id;
    private String name;
    private String email;
    private String passwordHash;
    private int score;

    public User(){}

    public User(String name, String email, String passwordHash){
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public String getId(){
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
