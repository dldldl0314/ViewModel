package com.zzp.viewmodel;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class PictureViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_view);
        String img = getIntent().getStringExtra("img");
        if (img != null) {
            ImageView imageView = findViewById(R.id.image);
            Glide.with(this).load(img).into(imageView);
        }

    }
}
