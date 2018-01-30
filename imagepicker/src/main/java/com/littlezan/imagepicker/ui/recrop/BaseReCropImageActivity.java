package com.littlezan.imagepicker.ui.recrop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.littlezan.imagepicker.ImagePicker;
import com.littlezan.imagepicker.R;
import com.littlezan.imagepicker.ui.RequestCode;
import com.littlezan.imagepicker.ui.crop.ImageCropActivity;

/**
 * ClassName: BaseReCropImageActivity
 * Description: 重新裁剪
 *
 * @author 彭赞
 * @version 1.0
 * @since 2018-01-24  19:13
 */
public abstract class BaseReCropImageActivity extends AppCompatActivity implements RequestCode, View.OnClickListener {


    public static final String KEY_URI_SOURCE = "key_uri_source";
    public static final String KEY_URI_RESULT = "KEY_URI_RESULT";

    private com.github.chrisbanes.photoview.PhotoView photoView;
    private android.widget.ImageView ivNavLeft;
    protected android.widget.TextView tvNavRight;


    protected Uri sourceUri;
    protected Uri resultUri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recrop);
        android.widget.TextView tvReCrop = findViewById(R.id.tv_re_crop);
        this.tvNavRight = findViewById(R.id.tv_nav_right);
        this.ivNavLeft = findViewById(R.id.iv_nav_left);
        this.photoView = findViewById(R.id.photo_view);

        tvReCrop.setOnClickListener(this);

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
        ImagePicker.getInstance().getImageLoader().displayImage(this, resultUri.getPath(), photoView);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.tv_re_crop) {
            ImageCropActivity.start(this, sourceUri);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_COPE:
                if (resultCode == Activity.RESULT_OK) {
                    //图片裁剪
                    if (data != null) {
                        Uri resultUri = data.getParcelableExtra(ImageCropActivity.KEY_INTENT_IMAGE_CROP_RESULT_URI);
                        ImagePicker.getInstance().getImageLoader().displayImage(this, resultUri.getPath(), photoView);
                    }
                }
                break;
            default:
                break;
        }
    }
}
