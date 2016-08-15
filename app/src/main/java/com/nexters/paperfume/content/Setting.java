package com.nexters.paperfume.content;

/**
 * Created by user on 2016-08-06.
 */

public class Setting {
    private String color;
    private String gender;
    private String blood;
    private int age;

    public Setting() {
        super();
    }

    public Setting(String color, String gender, String blood, int age) {
        this.color = color;
        this.gender = gender;
        this.blood = blood;
        this.age = age;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Setting{" +
                "color='" + color + '\'' +
                ", gender='" + gender + '\'' +
                ", blood='" + blood + '\'' +
                ", age=" + age +
                '}';
    }
}
