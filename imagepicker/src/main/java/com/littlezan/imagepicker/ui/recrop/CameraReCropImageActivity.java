package com.littlezan.imagepicker.ui.recrop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.littlezan.imagepicker.ImagePicker;
import com.littlezan.imagepicker.bean.ImageItem;

/**
 * ClassName: CameraReCropImageActivity
 * Description: 拍照界面进来的重新裁剪 ，需要特需处理
 *
 * @author 彭赞
 * @version 1.0
 * @since 2018-01-24  19:13
 */
public class CameraReCropImageActivity extends BaseReCropImageActivity implements View.OnClickListener {


    public static void startForResult(AppCompatActivity appCompatActivity, int requestCode, Uri sourceUri, Uri resultUri) {
        Intent intent = new Intent(appCompatActivity, CameraReCropImageActivity.class);
        intent.putExtra(KEY_URI_SOURCE, sourceUri);
        intent.putExtra(KEY_URI_RESULT, resultUri);
        appCompatActivity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvNavRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageItem imageItem = new ImageItem();
                if (sourceUri != null) {
                    imageItem.path = sourceUri.getPath();
                    imageItem.cropUri = resultUri;
                    ImagePicker.getInstance().addSelectedImageItem(imageItem, true);
                }
                //发送广播通知图片增加了
                ImagePicker.galleryAddPic(CameraReCropImageActivity.this, ImagePicker.getInstance().getTakeImageFile());
                setResult(RESULT_CODE_FINISH_SELECT);
                finish();
            }
        });
    }
}
