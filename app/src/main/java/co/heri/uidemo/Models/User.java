package co.heri.uidemo.Models;

import java.util.Date;

public class User {

    private static int instanceCounter = 0;

    private  Integer id;
    private String name;
    private Integer age;
    private String email;

    private String dob;
    private String gender;
    private String phone;

    protected Date createdAt;

    public User(String name, Integer age, String email, String dob, String gender, String phone) {
        instanceCounter++;

        this.name = name;
        this.age = age;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
        this.phone = phone;

        this.createdAt = new Date();
        this.id = instanceCounter;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
