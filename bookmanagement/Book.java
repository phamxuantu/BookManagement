package com.example.sev_user.bookmanagement;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Book implements Serializable {
    private int id;
    private String name;
    private String author;
    private String content;
    private String publisher;
    private String image;
private String alarm;
    public Book() {
    }

    public Book(int id, String name, String author, String content, String publisher, String image,String alarm) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.content = content;
        this.publisher = publisher;
        this.image = image;
this.alarm = alarm;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDateFormat(Date d) {
        SimpleDateFormat sdf = new
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(d);
    }

    public String getHourFormat(Date d) {
        SimpleDateFormat sdf = new
                SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(d);
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }
}
