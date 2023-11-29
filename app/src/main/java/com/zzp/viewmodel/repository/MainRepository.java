package com.zzp.viewmodel.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.zzp.viewmodel.Api.ApiService;
import com.zzp.viewmodel.BaseApplication;
import com.zzp.viewmodel.db.bean.Image;
import com.zzp.viewmodel.db.bean.WallPaper;
import com.zzp.viewmodel.model.BiYingResponse;

import com.zzp.viewmodel.model.WallPaperResponse;
import com.zzp.viewmodel.network.BaseObserver;
import com.zzp.viewmodel.network.NetworkApi;
import com.zzp.viewmodel.network.utils.DateUtil;
import com.zzp.viewmodel.network.utils.KLog;
import com.zzp.viewmodel.utils.Constant;
import com.zzp.viewmodel.utils.MVUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * Main存储库 用于对数据进行处理
 * @author llw
 */
public class MainRepository {

    private static final String TAG = MainRepository.class.getSimpleName();

    final MutableLiveData<BiYingResponse> biyingImage = new MutableLiveData<>();
    /**
     * 热门壁纸数据
     */
    final MutableLiveData<WallPaperResponse> wallPaper = new MutableLiveData<>();
    /**
     * 保存数据
     */
    private void saveImageData(BiYingResponse biYingImgResponse) {
        //记录今日已请求
        MVUtils.put(Constant.IS_TODAY_REQUEST,true);
        //记录此次请求的时最晚有效时间戳
        MVUtils.put(Constant.REQUEST_TIMESTAMP, DateUtil.getMillisNextEarlyMorning());
        BiYingResponse.ImagesBean bean = biYingImgResponse.getImages().get(0);
        //保存到数据库
        Completable insert = BaseApplication.getDb().imageDao().insertAll(
                new Image(1,bean.getUrl(),bean.getUrlbase(),bean.getCopyright(),
                        bean.getCopyrightlink(),bean.getTitle())
        );
        //RcJava处理Room数据存储
        CustomDisposable.addDisposable(insert,()->Log.d(TAG,"saveImageData: 插入数据成功"));

    }

    /**
     * 从网络上请求数据
     */
    @SuppressLint("CheckResult")
    private void requestNetworkApi() {
        Log.d(TAG, "requestNetworkApi: 从网络获取");
        ApiService apiService = NetworkApi.createService(ApiService.class,0);
        apiService.biying().compose(NetworkApi.applySchedulers(new BaseObserver<BiYingResponse>() {
            @Override
            public void onSuccess(BiYingResponse biYingImgResponse) {
                //存储到本地数据库中，并记录今日已请求了数据
                saveImageData(biYingImgResponse);
                biyingImage.setValue(biYingImgResponse);
            }

            @Override
            public void onFailure(Throwable e) {
                KLog.e("BiYing Error: " + e.toString());
            }
        }));
    }
    /**
     * 从本地数据库获取
     */
    private void getLocalDB() {
        Log.d(TAG, "getLocalDB: 从本地数据库获取");
        BiYingResponse biYingImgResponse = new BiYingResponse();
        //从数据库获取
        Flowable<Image> imageFlowable = BaseApplication.getDb().imageDao().queryById(1);
        //RxJava处理Room数据获取
        CustomDisposable.addDisposable(imageFlowable,image -> {
            BiYingResponse.ImagesBean imagesBean = new BiYingResponse.ImagesBean();
            imagesBean.setUrl(image.getUrl());
            imagesBean.setUrlbase(image.getUrlbase());
            imagesBean.setCopyright(image.getCopyright());
            imagesBean.setCopyrightlink(image.getCopyrightlink());
            imagesBean.setTitle(image.getTitle());
            List<BiYingResponse.ImagesBean> imagesBeanList = new ArrayList<>();
            imagesBeanList.add(imagesBean);
            biYingImgResponse.setImages(imagesBeanList);
            biyingImage.postValue(biYingImgResponse);
        });
    }


    public MutableLiveData<BiYingResponse> getBiYing() {
        //今日此接口是否已请求
        if (MVUtils.getBoolean(Constant.IS_TODAY_REQUEST)) {
            if(DateUtil.getTimestamp() <= MVUtils.getLong(Constant.REQUEST_TIMESTAMP)){
                //当前时间未超过次日0点，从本地获取
                getLocalDB();
            } else {
                //大于则数据需要更新，从网络获取
                requestNetworkApi();
            }
        } else {
            //没有请求过接口 或 当前时间，从网络获取
            requestNetworkApi();
        }
        return biyingImage;
    }

    /**
     * 获取壁纸数据
     * @return wallPaper
     */
    public MutableLiveData<WallPaperResponse> getWallPaper() {
        //今日此接口是否已经请求
        if (MVUtils.getBoolean(Constant.IS_TODAY_REQUEST_WALLPAPER)) {
            if (DateUtil.getTimestamp() <= MVUtils.getLong(Constant.REQUEST_TIMESTAMP_WALLPAPER)) {
                getLocalDBForWallPaper();
            } else {
                requestNetWorkApiForWallPaper();
            }
        } else {
            requestNetWorkApiForWallPaper();
        }
        return wallPaper;
    }

    /**
     * 从网络中获取壁纸数据
     * @return wallPaper
     */
    private void requestNetWorkApiForWallPaper() {
        Log.d(TAG,"requestNetWorkApiForWallPaper : 从网络获取 热门壁纸");
        NetworkApi.createService(ApiService.class,1).
                wallPaper().compose(NetworkApi.applySchedulers(new BaseObserver<WallPaperResponse>() {
                    @Override
                    public void onSuccess(WallPaperResponse wallPaperResponse) {
                        if (wallPaperResponse.getCode() == 0){
                            saveWallPaper(wallPaperResponse);
                            wallPaper.setValue(wallPaperResponse);
                        }else{
//                            failed.postValue(wallPaperResponse.getMsg());
                        }


                    }

                    @Override
                    public void onFailure(Throwable e) {
//                        failed.postValue("WallPaper error : "+e.toString());
                        KLog.e("WallPaper Error: " + e.toString());
                    }
                }));
    }
    /**
     * 从本地数据库获取热门壁纸
     */
    private void getLocalDBForWallPaper() {
        Log.d(TAG, "getLocalDBForWallPaper: 从本地数据库获取 热门壁纸");
        WallPaperResponse wallPaperResponse = new WallPaperResponse();
        WallPaperResponse.ResBean resBean = new WallPaperResponse.ResBean();
        List<WallPaperResponse.ResBean.VerticalBean> verticalBeanList = new ArrayList<>();
        Flowable<List<WallPaper>> listFlowable = BaseApplication.getDb().wallPaperDao().getAll();
        CustomDisposable.addDisposable(listFlowable, wallPapers -> {
            for (WallPaper paper : wallPapers) {
                WallPaperResponse.ResBean.VerticalBean verticalBean = new WallPaperResponse.ResBean.VerticalBean();
                verticalBean.setImg(paper.getImg());
                verticalBeanList.add(verticalBean);
            }
            resBean.setVertical(verticalBeanList);
            wallPaperResponse.setRes(resBean);
            wallPaper.postValue(wallPaperResponse);
        });
    }

    /**
     * 保存热门壁纸数据
     */
    private void saveWallPaper(WallPaperResponse wallPaperResponse) {
        MVUtils.put(Constant.IS_TODAY_REQUEST_WALLPAPER, true);
        MVUtils.put(Constant.REQUEST_TIMESTAMP_WALLPAPER, DateUtil.getMillisNextEarlyMorning());

        Completable deleteAll = BaseApplication.getDb().wallPaperDao().deleteAll();
        CustomDisposable.addDisposable(deleteAll, () -> {
            Log.d(TAG, "saveWallPaper: 删除数据成功");
            List<WallPaper> wallPaperList = new ArrayList<>();
            for (WallPaperResponse.ResBean.VerticalBean verticalBean : wallPaperResponse.getRes().getVertical()) {
                wallPaperList.add(new WallPaper(verticalBean.getImg()));
            }
            //保存到数据库
            Completable insertAll = BaseApplication.getDb().wallPaperDao().insertAll(wallPaperList);
            Log.d(TAG, "saveWallPaper: 插入数据：" + wallPaperList.size() + "条");
            //RxJava处理Room数据存储
            CustomDisposable.addDisposable(insertAll, () -> Log.d(TAG, "saveWallPaper: 热门天气数据保存成功"));
        });
    }


}

