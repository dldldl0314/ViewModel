package com.zzp.viewmodel.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.zzp.viewmodel.db.bean.WallPaper;
import com.zzp.viewmodel.repository.PictureRepository;

import java.util.List;

public class PictureViewModel extends ViewModel {

    public LiveData<List<WallPaper>> wallPaper;

    public void getWallPaper() {
        wallPaper = new PictureRepository().getWallPaper();
    }
}
