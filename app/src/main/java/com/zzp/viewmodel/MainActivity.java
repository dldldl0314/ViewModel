package com.zzp.viewmodel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;


import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.tencent.mmkv.MMKV;
import com.zzp.viewmodel.adapter.WallPaperAdapter;
import com.zzp.viewmodel.databinding.ActivityMainBinding;

import com.zzp.viewmodel.model.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding dataBinding;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //数据绑定视图
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        //网络请求
        mainViewModel.getBiying();
        //返回数据时更新ViewModel，ViewModel更新则xml更新
        mainViewModel.biying.observe(this, biYingImgResponse -> dataBinding.setViewModel(mainViewModel));
        //MMKV初始化
        MMKV.initialize(this);
        initView();
        //热门壁纸 网络请求
        mainViewModel.getWallPaper();
        mainViewModel.wallPaper.observe(this, wallPaperResponse -> dataBinding.ry.setAdapter(new WallPaperAdapter(wallPaperResponse.getRes().getVertical())));

    }
    /**
     * 初始化
     */
    private void initView() {
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        dataBinding.ry.setLayoutManager(manager);
        //伸缩偏移量监听
        dataBinding.appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {//收缩时
                    dataBinding.toolbarLayout.setTitle("MVVM-Demo");
                    isShow = true;
                } else if (isShow) {//展开时
                    dataBinding.toolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        });


    }

}
