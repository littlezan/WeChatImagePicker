package com.littlezan.imagepicker.util.thirdlib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.littlezan.imagepicker.ui.ucrop.UCropCustomActivity;
import com.littlezan.imagepicker.util.NougatTools;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.util.UUID;


/**
 * ClassName: UCropUtil
 * Description: UCrop 工具类
 *
 * @author 彭赞
 * @version 1.0
 * @since 2017-09-28  13:38
 */
public class UCropUtil {

    private static final int MAX_WIDTH = 1080;
    private static final int MAX_HEIGHT = 1920;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "TelepathyCropImage_";
    private static final String SUFFIX = ".jpg";


    public static void start(@NonNull Activity activity, @NonNull Uri uri) {
        UCrop uCrop = getUCrop(activity, uri);
        Intent intent = uCrop.getIntent(activity);
        intent.setClass(activity, UCropCustomActivity.class);
        activity.startActivityForResult(intent, UCrop.REQUEST_CROP);
    }

    private static UCrop getUCrop(Context context, @NonNull Uri uri) {
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME + UUID.randomUUID().toString() + SUFFIX;
        Uri destination = NougatTools.formatFileProviderUri(context, new File(context.getCacheDir(), destinationFileName));
        UCrop uCrop = UCrop.of(uri, destination);
        uCrop = basisConfig(uCrop);
        uCrop = advancedConfig(context, uCrop);
        return uCrop;
    }


    private static UCrop basisConfig(UCrop uCrop) {
        uCrop = uCrop.useSourceImageAspectRatio();
        uCrop = uCrop.withAspectRatio(1, 1);
        uCrop = uCrop.withMaxResultSize(MAX_WIDTH, MAX_HEIGHT);
        return uCrop;
    }

    private static UCrop advancedConfig(Context context, UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(50);
        options.setHideBottomControls(true);
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.NONE, UCropActivity.NONE);
        //设置toolbar颜色
//        options.setToolbarColor(Color.parseColor("#7F3E3E3E"));
//        options.setToolbarWidgetColor(ContextCompat.getColor(context, R.color.colorAccent));
        //设置状态栏颜色
        options.setStatusBarColor(Color.parseColor("#7F3E3E3E"));
        //是否能调整裁剪框
        options.setFreeStyleCropEnabled(false);


        /*
        This sets max size for bitmap that will be decoded from source Uri.
        More size - more memory allocation, default implementation uses screen diagonal.

        options.setMaxBitmapSize(640);
        * */


       /*

        Tune everything (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧

        options.setMaxScaleMultiplier(5);
        options.setImageToCropBoundsAnimDuration(666);
        options.setDimmedLayerColor(Color.CYAN);
        options.setCircleDimmedLayer(true);
        options.setShowCropFrame(false);
        options.setCropGridStrokeWidth(20);
        options.setCropGridColor(Color.GREEN);
        options.setCropGridColumnCount(2);
        options.setCropGridRowCount(1);
        options.setToolbarCropDrawable(R.drawable.your_crop_icon);
        options.setToolbarCancelDrawable(R.drawable.your_cancel_icon);

        // Color palette
        options.setToolbarColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setRootViewBackgroundColor(ContextCompat.getColor(this, R.color.your_color_res));

        // Aspect ratio options
        options.setAspectRatioOptions(1,
            new AspectRatio("WOW", 1, 2),
            new AspectRatio("MUCH", 3, 4),
            new AspectRatio("RATIO", CropImageView.DEFAULT_ASPECT_RATIO, CropImageView.DEFAULT_ASPECT_RATIO),
            new AspectRatio("SO", 16, 9),
            new AspectRatio("ASPECT", 1, 1));

       */
        return uCrop.withOptions(options);
    }

}
