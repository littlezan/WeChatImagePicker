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
public abstract class BaseImageCropActivity extends AppCompatActivity implements RequestCode {

    private Uri resultUri;
    private Uri sourceUri;

    public void startCrop(Uri uri) {
        sourceUri = uri;
        UCropUtil.start(this, sourceUri);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_U_CROP:
                //uCrop 裁剪结果
                if (resultCode == Activity.RESULT_OK) {
                    //图片裁剪
                    resultUri = UCrop.getOutput(data);
                    handleCropResult(sourceUri, resultUri);
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    handleCropError(data);
                }
                break;
            case REQUEST_RE_COPE:
                if (resultCode == Activity.RESULT_OK) {
                    handleReCropResult(resultUri);
                } else if (resultCode == RESULT_CODE_FINISH_SELECT) {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    protected void handleCropResult(Uri sourceUri, Uri resultUri) {

    }

    protected void handleCropError(Intent data) {

    }

    protected void handleReCropResult(Uri resultUri) {

    }

}
