package com.example.chala.group12_hw09_part_a;

import java.io.Serializable;

/**
 * Created by chala on 4/21/2017.
 */

public class message implements Serializable{
    String message,sender,image,key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "message{" +
                "message='" + message + '\'' +
                ", sender='" + sender + '\'' +
                ", image='" + image + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
