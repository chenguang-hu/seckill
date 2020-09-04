package com.example.seckill.entity;

import java.io.Serializable;

public class Goods implements Serializable {
    private Long id;
    private String title;
    private String images;
    private Long price;
    private String indexes;
    private String own_spec;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String image) {
        this.images = image;
    }

    public String getIndexes() {
        return indexes;
    }

    public void setIndexes(String indexes) {
        this.indexes = indexes;
    }

    public String getOwn_spec() {
        return own_spec;
    }

    public void setOwn_spec(String own_spec) {
        this.own_spec = own_spec;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
