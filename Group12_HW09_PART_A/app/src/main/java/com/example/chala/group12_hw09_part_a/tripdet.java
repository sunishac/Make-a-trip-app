package com.example.chala.group12_hw09_part_a;

/**
 * Created by chala on 4/21/2017.
 */

public class tripdet {
    String title,location,img;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "tripdet{" +
                "title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
