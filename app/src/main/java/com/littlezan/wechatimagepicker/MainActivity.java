package com.littlezan.wechatimagepicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.littlezan.imagepicker.ImagePicker;
import com.littlezan.imagepicker.bean.ImageItem;
import com.littlezan.imagepicker.decoration.GridSpacingItemDecoration;
import com.littlezan.wechatimagepicker.imageloader.GlideImageLoader;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_IMAGE_PICKER = 100;
    public static final int REQUEST_CODE_IMAGE_PREVIEW = 101;

    private android.widget.EditText etContent;
    private android.widget.ImageView ivImg;
    private android.widget.ImageView ivVideo;
    private android.support.v7.widget.RecyclerView recyclerView;
    private AddImageAdapter addImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.recyclerView = findViewById(R.id.recycler_view);
        this.ivVideo = findViewById(R.id.iv_video);
        this.ivImg = findViewById(R.id.iv_img);
        this.etContent = findViewById(R.id.et_content);

        ivImg.setOnClickListener(this);

        initImagePicker();
        initView();
    }

    private void initImagePicker() {
        ImagePicker.getInstance().setImageLoader(new GlideImageLoader());
    }

    private void initView() {
        initRecyclerView();


    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.addItemDecoration(GridSpacingItemDecoration.newBuilder().spacing(10).build());
        recyclerView.setHasFixedSize(true);
        addImageAdapter = new AddImageAdapter();
        recyclerView.setAdapter(addImageAdapter);
        addImageAdapter.setOnItemClickListener(new AddImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object item, int position) {
                if (item instanceof ImageAddMode) {
                    startPickerImage();
                } else if (item instanceof ImageItem) {
                    startPreviewImage();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.iv_img) {
            startPickerImage();
        } else if (viewId == R.id.iv_video) {

        }
    }

    private void startPickerImage() {
        ImagePicker.getInstance().startPicker(this, REQUEST_CODE_IMAGE_PICKER, ImagePicker.ModeMediaType.MEDIA_TYPE_IMAGE, 9);
    }

    private void startPreviewImage() {
        ImagePicker.getInstance().startPreview(this, REQUEST_CODE_IMAGE_PREVIEW);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_IMAGE_PICKER:
                case REQUEST_CODE_IMAGE_PREVIEW:
                    ArrayList<ImageItem> selectedImages = ImagePicker.getInstance().getSelectedImages();
                    if (selectedImages != null && selectedImages.size() > 0) {
                            addImageAdapter.refreshData(selectedImages);
                            recyclerView.setVisibility(View.VISIBLE);
                            ivImg.setVisibility(View.GONE);
                            ivVideo.setVisibility(View.GONE);
                    } else {
                            recyclerView.setVisibility(View.GONE);
                            ivImg.setVisibility(View.VISIBLE);
                            ivVideo.setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
