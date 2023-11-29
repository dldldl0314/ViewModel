package com.zzp.viewmodel.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zzp.viewmodel.PictureViewActivity;
import com.zzp.viewmodel.R;
import com.zzp.viewmodel.databinding.ItemWallPaperBinding;
import com.zzp.viewmodel.model.WallPaperResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WallPaperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 传递过来的数据
     */
    private final List<WallPaperResponse.ResBean.VerticalBean> verticalBeans;


    public WallPaperAdapter(List<WallPaperResponse.ResBean.VerticalBean> verticalBeans) {
        this.verticalBeans = verticalBeans;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemWallPaperBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_wall_paper, parent, false);
        return new ViewHolderWallPaper(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        ItemWallPaperBinding binding = ((ViewHolderWallPaper) holder).getBinding();
        binding.setWallPaper(verticalBeans.get(position));
        binding.setOnClick(new ClickBinding());
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return verticalBeans.size();
    }

    private static class ViewHolderWallPaper extends RecyclerView.ViewHolder {

        private ItemWallPaperBinding binding;

        public ItemWallPaperBinding getBinding() {
            return binding;
        }

        public void setBinding(ItemWallPaperBinding binding) {
            this.binding = binding;
        }

        public ViewHolderWallPaper(ItemWallPaperBinding inflate) {
            super(inflate.getRoot());
            this.binding = inflate;
        }
    }
    public static class ClickBinding {
        public void itemClick(WallPaperResponse.ResBean.VerticalBean verticalBean, View view) {
            Intent intent = new Intent(view.getContext(), PictureViewActivity.class);
            intent.putExtra("img", verticalBean.getImg());
            view.getContext().startActivity(intent);
        }
    }

}
