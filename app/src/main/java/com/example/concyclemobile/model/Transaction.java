package com.example.concyclemobile.model;

public class Transaction {
    private String description;
    private int score;

    public Transaction() {}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
