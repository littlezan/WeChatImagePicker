package com.littlezan.imagepicker.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import com.littlezan.imagepicker.util.thirdlib.UCropUtil;
import com.yalantis.ucrop.UCrop;

/**
 * ClassName: BaseImageCropActivity
 * Description:
 *
 * @author 彭赞
 * @version 1.0
 * @since 2018-01-24  17:44
 */
public abstract class BaseImageCropActivity extends AppCompatActivity {

    public static final int REQUEST_RE_COPE = UCrop.REQUEST_CROP + 10;
    private Uri sourceUri;

    public void startCrop(Uri uri) {
        this.sourceUri = uri;
        UCropUtil.start(this, this.sourceUri);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case UCrop.REQUEST_CROP:
                    //图片裁剪
                    Uri resultUri = UCrop.getOutput(data);
//                    if (imageView != null) {
//                        ImageLoader.newBuilder()
//                                .isSkipMemoryCache(true)
//                                .scaleType(ImageView.ScaleType.CENTER_CROP)
//                                .build()
//                                .show(imageView, resultUri);
//                    }
                    handleCropResult(resultUri);
                    if (!(this instanceof ReCropImageActivity)) {
                        ReCropImageActivity.startForResult(this, REQUEST_RE_COPE, sourceUri, resultUri);
                    }
                    break;
                case REQUEST_RE_COPE:

//                    handleReCropResult(resultUri);
                    break;
                default:
                    break;
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }

    private void handleReCropResult() {

    }

    protected void handleCropResult(Uri resultUri){

    }

    private void handleCropError(Intent data) {

    }

}
