package com.example.aroundbelarus.Clases;

import java.util.HashMap;

public class User
{
    private String name;
    private String password;
    private String login;
    private String email;
    private String number;

    public  User(){}
    public User(String name,String password, String Login, String email, String number)
    {
        this.name = name;
        this.password = password;
        this.login = Login;
        this.email = email;
        this.number = number;
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
