package com.zzp.viewmodel.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zzp.viewmodel.User;
import com.zzp.viewmodel.repository.MainRepository;

/**
 * 主页面ViewModel
 *
 * @author llw
 * @description MainViewModel
 */
public class MainViewModel extends ViewModel {

    public LiveData<BiYingResponse> biying;
    public LiveData<WallPaperResponse> wallPaper;
    public void getBiying(){
        biying = new MainRepository().getBiYing();
    }
    public void getWallPaper(){
        wallPaper = new MainRepository().getWallPaper();
    }
}


