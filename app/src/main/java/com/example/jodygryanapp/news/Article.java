package com.example.jodygryanapp.news;

import android.graphics.Bitmap;
import android.text.Spanned;

public class Article {
    private String author, title, url, urlToImage;
    private Spanned description;
    private Bitmap image;
    private long id;

    public Article(String title, String author, Spanned description, String url, String urlToImage, Bitmap image){
        this.title = title;
        this.author = author;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.image = image;
    }

    public Article(String title, String author, Spanned description, String url){
        this.title = title;
        this.author = author;
        this.description = description;
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Spanned getDescription() {
        return description;
    }

    public void setDescription(Spanned description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public Bitmap getImage(){
        return image;
    }

    public void setImage(Bitmap image){
        this.image = image;
    }

    public void setId(Long id){this.id = id;}

    public long getId(){return this.id;}
}
