package com.zzp.viewmodel.repository;

import androidx.lifecycle.MutableLiveData;

import com.zzp.viewmodel.BaseApplication;
import com.zzp.viewmodel.db.bean.WallPaper;

import java.util.List;

import io.reactivex.Flowable;

public class PictureRepository {

    private final MutableLiveData<List<WallPaper>> wallPaper = new MutableLiveData<>();

    public MutableLiveData<List<WallPaper>> getWallPaper() {
        Flowable<List<WallPaper>> listFlowable = BaseApplication.getDb().wallPaperDao().getAll();
        CustomDisposable.addDisposable(listFlowable, wallPaper::postValue);
        return wallPaper;
    }
}
