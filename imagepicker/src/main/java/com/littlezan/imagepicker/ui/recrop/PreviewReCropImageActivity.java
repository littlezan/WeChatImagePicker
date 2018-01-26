package com.littlezan.imagepicker.ui.recrop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * ClassName: PreviewReCropImageActivity
 * Description: 预览界面进来的重新裁剪
 *
 * @author 彭赞
 * @version 1.0
 * @since 2018-01-26  11:01
 */
public class PreviewReCropImageActivity extends BaseReCropImageActivity {

    public static void startForResult(AppCompatActivity appCompatActivity, int requestCode, Uri sourceUri, Uri resultUri) {
        Intent intent = new Intent(appCompatActivity, PreviewReCropImageActivity.class);
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
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }
}
