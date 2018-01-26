package com.littlezan.imagepicker.ui.recrop;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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


}
