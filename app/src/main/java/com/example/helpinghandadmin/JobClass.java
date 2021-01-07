package com.example.helpinghandadmin;

import java.io.Serializable;

public class JobClass implements Serializable {
    String id;
    String name;
    String address;
    String preferredAddress;
    String phone;
    String gender;
    String workType;
    String profileImage;
    String frontImage;
    String backImage;
    String expectedSalary;

    public JobClass() {
    }

    public JobClass(String id) {
        this.id = id;
    }

    public JobClass(String name, String address, String preferredAddress, String phone, String gender, String workType, String profileImage, String frontImage, String backImage, String expectedSalary) {
        this.name = name;
        this.address = address;
        this.preferredAddress = preferredAddress;
        this.phone = phone;
        this.gender = gender;
        this.workType = workType;
        this.profileImage = profileImage;
        this.frontImage = frontImage;
        this.backImage = backImage;
        this.expectedSalary = expectedSalary;
    }

    public JobClass(String id, String name, String address, String preferredAddress, String phone, String gender, String workType, String profileImage, String frontImage, String backImage, String expectedSalary) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.preferredAddress = preferredAddress;
        this.phone = phone;
        this.gender = gender;
        this.workType = workType;
        this.profileImage = profileImage;
        this.frontImage = frontImage;
        this.backImage = backImage;
        this.expectedSalary = expectedSalary;
    }

    public String getExpectedSalary() {
        return expectedSalary;
    }

    public void setExpectedSalary(String expectedSalary) {
        this.expectedSalary = expectedSalary;
    }

    public String getId() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPreferredAddress() {
        return preferredAddress;
    }

    public void setPreferredAddress(String preferredAddress) {
        this.preferredAddress = preferredAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(String frontImage) {
        this.frontImage = frontImage;
    }

    public String getBackImage() {
        return backImage;
    }

    public void setBackImage(String backImage) {
        this.backImage = backImage;
    }
}
