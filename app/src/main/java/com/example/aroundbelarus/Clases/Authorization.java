package com.example.aroundbelarus.Clases;

import java.util.HashMap;
import java.util.Set;

public class Authorization extends Registration {
    boolean isExists;
    String Login;
    String Password;

    Authorization(String Login, String Password) {
        super();
        this.Login = Login;
        this.Password = Password;
    }

//    public boolean Check()
//    {
//        HashMap<String, User> temp;
//
//        temp = user.getDict();
//
//        Set<String> keys = temp.keySet();
//
//        for (String login: keys)
//        {
//            if(!(login.equals(Login)))
//            {
//                isExists = false;
//            }
//            else
//            {
//                isExists = true;
//                break;
//            }
//        }
//        if (!isExists)
//        {
//            return false;
//        }
//
//        User us = temp.get(Login);
//
//        String password  = us.getPassword();
//
//        if(password.equals(Password))
//        {
//            return true;
//        }
//
//        return false;
//    }
}
