package com.littlezan.wechatimagepicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.littlezan.imagepicker.ImagePicker;
import com.littlezan.wechatimagepicker.imageloader.GlideImageLoader;

public class MainActivity extends AppCompatActivity {

    private android.widget.Button btnToImagePicker;
    private android.widget.Button btnToVideoPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.btnToImagePicker = (Button) findViewById(R.id.btn_to_image_picker);
        this.btnToVideoPicker = (Button) findViewById(R.id.btn_to_video_picker);
        btnToImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.getInstance().start(MainActivity.this, ImagePicker.ModeMediaType.MEDIA_TYPE_IMAGE);
            }
        });

        btnToVideoPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.getInstance().start(MainActivity.this, ImagePicker.ModeMediaType.MEDIA_TYPE_VIDEO);
            }
        });

        initImagePicker();
    }

    private void initImagePicker() {
        ImagePicker.getInstance().setImageLoader(new GlideImageLoader());
    }
}
