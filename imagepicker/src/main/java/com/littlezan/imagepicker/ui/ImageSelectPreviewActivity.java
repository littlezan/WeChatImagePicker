package com.littlezan.imagepicker.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.littlezan.imagepicker.ImagePicker;
import com.littlezan.imagepicker.bean.ImageItem;

import java.util.ArrayList;

/**
 * ClassName: ImageFolderPreviewActivity
 * Description: 图片选中预览界面
 *
 * @author 彭赞
 * @version 1.0
 * @since 2018-01-23  15:36
 */
public class ImageSelectPreviewActivity extends BaseImagePreviewActivity {


    public static void start(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, ImageSelectPreviewActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvDelete.setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<ImageItem> initImagePageAdapterData() {
        return ImagePicker.getInstance().getSelectedImages();
    }

    @Override
    public void finish() {
        setResult(Activity.RESULT_OK);
        super.finish();
    }
}
