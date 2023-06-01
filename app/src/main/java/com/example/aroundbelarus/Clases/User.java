package com.example.aroundbelarus.Clases;

import java.util.HashMap;

public class User
{
    private String name;
    private String  password;
    private String Login;
    private String email;
    private String number;
    private static User current_user;

    public  User(){}
    public User(String name,String password, String Login, String email, String number)
    {
        this.name = name;
        this.password = password;
        this.Login = Login;
        this.email = email;
        this.number = number;
    }

    public User get_CurrentUser()
    {
        return current_user;
    }


    public String getName()
    {
        return this.name;
    }
    public String getLogin()
    {
        return this.Login;
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
        this.Login = Login;
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
        return this.Login.equals(us.Login);
    }
}
