package com.example.buttongame.data;

import java.io.Serializable;

public class Combo implements Serializable {
    private int id;
    private int image1;

    private int image2;

    private int image3;

    private int image4;

    private int image5;
    private String title;

    public Combo(int id, int image1, int image2, int image3, int image4, int image5, String title) {
        this.id = id;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.image5 = image5;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage1() {
        return image1;
    }

    public void setImage1(int image1) {
        this.image1 = image1;
    }

    public int getImage2() {
        return image2;
    }

    public void setImage2(int image2) {
        this.image2 = image2;
    }

    public int getImage3() {
        return image3;
    }

    public void setImage3(int image3) {
        this.image3 = image3;
    }

    public int getImage4() {
        return image4;
    }

    public void setImage4(int image4) {
        this.image4 = image4;
    }

    public int getImage5() {
        return image5;
    }

    public void setImage5(int image5) {
        this.image5 = image5;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
