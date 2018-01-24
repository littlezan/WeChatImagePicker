package com.littlezan.imagepicker.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.littlezan.imagepicker.ImagePicker;
import com.littlezan.imagepicker.R;

/**
 * ClassName: ReCropImageActivity
 * Description: 重新裁剪
 *
 * @author 彭赞
 * @version 1.0
 * @since 2018-01-24  19:13
 */
public class ReCropImageActivity extends BaseImageCropActivity implements View.OnClickListener {


    public static final String KEY_URI_SOURCE = "key_uri_source";
    public static final String KEY_URI_RESULT = "KEY_URI_RESULT";

    private com.github.chrisbanes.photoview.PhotoView photoView;
    private android.widget.ImageView ivNavLeft;
    private android.widget.TextView tvNavRight;
    private android.support.v7.widget.Toolbar toolbar;
    private android.widget.TextView tvReCrop;


    private Uri sourceUri;
    private Uri resultUri;


    public static void startForResult(AppCompatActivity appCompatActivity, int requestCode, Uri sourceUri, Uri resultUri) {
        Intent intent = new Intent(appCompatActivity, ReCropImageActivity.class);
        intent.putExtra(KEY_URI_SOURCE, sourceUri);
        intent.putExtra(KEY_URI_RESULT, resultUri);
        appCompatActivity.startActivityForResult(intent, requestCode);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recrop);
        this.tvReCrop = findViewById(R.id.tv_re_crop);
        this.toolbar = findViewById(R.id.toolbar);
        this.tvNavRight = findViewById(R.id.tv_nav_right);
        this.ivNavLeft = findViewById(R.id.iv_nav_left);
        this.photoView = findViewById(R.id.photo_view);

        tvReCrop.setOnClickListener(this);
        tvNavRight.setOnClickListener(this);


        parseIntent();
        initToolbar();
        initView();
    }

    private void parseIntent() {
        sourceUri = getIntent().getParcelableExtra(KEY_URI_SOURCE);
        resultUri = getIntent().getParcelableExtra(KEY_URI_RESULT);
    }

    private void initToolbar() {
        ivNavLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        ImagePicker.getInstance().getImageLoader().displayImage(this, sourceUri.getPath(), photoView);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.tv_nav_right) {
            setResult(Activity.RESULT_OK);
            finish();
        } else if (viewId == R.id.tv_re_crop) {
            startCrop(sourceUri);
        }
    }
}
