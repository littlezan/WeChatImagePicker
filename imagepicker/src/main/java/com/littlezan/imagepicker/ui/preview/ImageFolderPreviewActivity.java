package com.littlezan.imagepicker.ui.preview;

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
 * Description: 图片相册预览界面
 *
 * @author 彭赞
 * @version 1.0
 * @since 2018-01-23  15:36
 */
public class ImageFolderPreviewActivity extends BaseImagePreviewActivity {


    public static void start(Activity activity, int requestCode, int currentPositionInFolder) {
        Intent intent = new Intent(activity, ImageFolderPreviewActivity.class);
        intent.putExtra(EXTRA_SELECTED_IMAGE_POSITION, currentPositionInFolder);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rlCrop.setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<ImageItem> getImagePreviewSourceData() {
        ArrayList<ImageItem> currentImageFolderItems = ImagePicker.getInstance().getCurrentImageFolderItems();
        if (currentImageFolderItems == null) {
            currentImageFolderItems = new ArrayList<>();
        }
        return currentImageFolderItems;
    }


}
