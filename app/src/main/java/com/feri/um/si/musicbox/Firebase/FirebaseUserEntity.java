package com.feri.um.si.musicbox.Firebase;

public class FirebaseUserEntity {

    private String uId;

    private String email;

    private String name;


    public FirebaseUserEntity(){
    }

    public FirebaseUserEntity(String uId, String email, String name) {
        this.uId = uId;
        this.email = email;
        this.name = name;

    }

    public String getuId() {
        return uId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }


}