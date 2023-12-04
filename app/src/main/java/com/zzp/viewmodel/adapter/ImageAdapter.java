package com.zzp.viewmodel.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.zzp.viewmodel.R;
import com.zzp.viewmodel.databinding.ItemImageBinding;
import com.zzp.viewmodel.db.bean.WallPaper;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ImageAdapter extends BaseQuickAdapter<WallPaper, BaseDataBindingHolder<ItemImageBinding>> {

    public ImageAdapter(@Nullable List<WallPaper> data) {
        super(R.layout.item_image, data);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemImageBinding> bindingHolder, WallPaper wallPaper) {
        if (wallPaper == null) {
            return;
        }
        ItemImageBinding binding = bindingHolder.getDataBinding();
        if (binding != null) {
            binding.setWallPaper(wallPaper);
            binding.executePendingBindings();
        }
    }
}
