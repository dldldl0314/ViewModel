package com.zzp.viewmodel.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zzp.viewmodel.User;

/**
 * 登录页面ViewModel
 * @author llw
 */
public class LoginViewModel extends ViewModel {

    public MutableLiveData<User> user;

    public MutableLiveData<User> getUser(){
        if(user == null){
            user = new MutableLiveData<>();
        }
        return user;
    }
}
