package com.example.aroundbelarus.Clases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User
{
    private String name;
    private String password;
    private String login;
    private String email;
    private String number;
    private boolean admin;
    private boolean moderator;
    private ArrayList<Mark> favorits;

    public  User(){}
    public User(String name,String password, String Login, String email, String number)
    {
        this.name = name;
        this.password = password;
        this.login = Login;
        this.email = email;
        this.number = number;
        admin = false;
        moderator = false;
        favorits = new ArrayList<>();

    }
    public ArrayList<Mark> getFavorits() {
        return favorits;
    }

    public void setFavorits(ArrayList<Mark> favorits) {
        this.favorits = favorits;
    }

    public String getName()
    {
        return this.name;
    }
    public String getLogin()
    {
        return this.login;
    }
    public String getPassword()
    {
        return this.password;
    }
    public String getEmail()
    {
        return email;
    }
    public String getNumber()
    {
        return number;
    }

    public boolean getAdmin(){return this.admin;}
    public boolean getModerator(){return this.moderator;}
    public void SetAdmin(boolean a){this.admin = a;}
    public void SetModerator(boolean a){this.moderator = a;}


    public void SetName(String name)
    {
        this.name = name;
    }
    public void SetLogin(String Login)
    {
        this.login = Login;
    }
    public void SetPassword(String password)
    {
        this.password = password;
    }
    public void SetEmail(String email)
    {
        this.email = email;
    }
    public void SetNumber(String number)
    {
        this.number = number;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof User)) {
            return false;
        }

        User us  = (User) obj;
        return this.login.equals(us.login);
    }
}
