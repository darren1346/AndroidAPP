package com.example.loginregister;

public class Users {
    private String name;
    private String email;
    private String userid;
    private String fullname;
    private String userlevel;

    public String getUserlevel() { return userlevel; }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUserid() {
        return userid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setUserlevel(String userlevel) { this.userlevel = userlevel; }


}
