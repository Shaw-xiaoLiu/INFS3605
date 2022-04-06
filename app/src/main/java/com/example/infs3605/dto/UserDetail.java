package com.example.infs3605.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class UserDetail implements Parcelable {
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private Gender gender;
    private int age;

    public UserDetail() {
    }

    protected UserDetail(Parcel in) {
        name = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        address = in.readString();
        gender = Gender.valueOf(in.readString());
        age = in.readInt();
    }

    public static final Creator<UserDetail> CREATOR = new Creator<UserDetail>() {
        @Override
        public UserDetail createFromParcel(Parcel in) {
            return new UserDetail(in);
        }

        @Override
        public UserDetail[] newArray(int size) {
            return new UserDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phoneNumber);
        dest.writeString(address);
        dest.writeString(gender.name());
        dest.writeInt(age);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static Creator<UserDetail> getCREATOR() {
        return CREATOR;
    }
}
