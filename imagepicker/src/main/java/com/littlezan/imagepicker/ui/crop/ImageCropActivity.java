package com.littlezan.imagepicker.ui.crop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.littlezan.imagepicker.R;
import com.littlezan.imagepicker.ui.RequestCode;
import com.steelkiwi.cropiwa.AspectRatio;
import com.steelkiwi.cropiwa.CropIwaView;
import com.steelkiwi.cropiwa.config.CropIwaSaveConfig;
import com.steelkiwi.cropiwa.config.InitialPosition;

import java.io.File;
import java.util.UUID;

/**
 * ClassName: ImageCropActivity
 * Description:
 *
 * @author 彭赞
 * @version 1.0
 * @since 2018-01-30  14:16
 */
public class ImageCropActivity extends AppCompatActivity implements View.OnClickListener, RequestCode {

    private static final int MAX_WIDTH = 1080;
    private static final int MAX_HEIGHT = 1920;

    private static final String KEY_INTENT_IMAGE_SOURCE_URI = "key_intent_image_uri";
    public static final String KEY_INTENT_IMAGE_CROP_RESULT_URI = "key_intent_image_crop_result_uri";

    private com.steelkiwi.cropiwa.CropIwaView cropLwaView;
    private android.widget.TextView tvCommit;
    private android.widget.ImageView ivCropAnimator;
    private Uri imageUri;


    public static void start(Activity activity, Uri imageUri) {
        Intent intent = new Intent(activity, ImageCropActivity.class);
        intent.putExtra(KEY_INTENT_IMAGE_SOURCE_URI, imageUri);
        activity.startActivityForResult(intent, REQUEST_COPE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);
        this.ivCropAnimator = (ImageView) findViewById(R.id.iv_crop_animator);
        this.tvCommit = (TextView) findViewById(R.id.tv_commit);
        TextView tvCancel = (TextView) findViewById(R.id.tv_cancel);
        this.cropLwaView = (CropIwaView) findViewById(R.id.crop_lwa_view);

        tvCommit.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        ivCropAnimator.setVisibility(View.GONE);

        parseIntent();
        initCropLwaView();
    }

    private void parseIntent() {
        imageUri = getIntent().getParcelableExtra(KEY_INTENT_IMAGE_SOURCE_URI);
    }

    private void initCropLwaView() {

        cropLwaView.setImageUri(imageUri);
        cropLwaView.configureOverlay()
                .setDynamicCrop(false)
                .setShouldDrawGrid(false)
                .setAspectRatio(new AspectRatio(1, 1))
                .apply();


        cropLwaView.configureImage()
                .setImageInitialPosition(InitialPosition.CENTER_INSIDE)
                .setImageScaleEnabled(true)
                .setImageTranslationEnabled(true)
                .setMinScale(0.5f)
                .setScale(0.035f)
                .apply();

        cropLwaView.setCropSaveCompleteListener(new CropIwaView.CropSaveCompleteListener() {

            @Override
            public void onCroppedRegionSaved(Uri bitmapUri) {
                Intent intent = new Intent();
                intent.putExtra(KEY_INTENT_IMAGE_CROP_RESULT_URI, bitmapUri);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        cropLwaView.setErrorListener(new CropIwaView.ErrorListener() {
            @Override
            public void onError(Throwable e) {
                setResult(RESULT_CODE_CROP_ERROR);
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.tv_cancel) {
            //取消
            onBackPressed();
        } else if (viewId == R.id.tv_commit) {
            CropIwaSaveConfig.Builder cropIwaSaveConfig = new CropIwaSaveConfig.Builder(getSaveFileUri())
                    .setQuality(50)
                    .setSize(MAX_WIDTH, MAX_HEIGHT)
                    .setCompressFormat(Bitmap.CompressFormat.PNG);
            cropLwaView.crop(cropIwaSaveConfig.build());
            ivCropAnimator.setVisibility(View.VISIBLE);
            tvCommit.setVisibility(View.GONE);
            if (ivCropAnimator.getBackground() instanceof Animatable) {
                Animatable animatable = (Animatable) ivCropAnimator.getBackground();
                animatable.start();
            }
        }
    }

    private static final String SAMPLE_CROPPED_IMAGE_NAME = "CropImage_";
    private static final String SUFFIX = ".png";

    public Uri getSaveFileUri() {
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME + UUID.randomUUID().toString() + SUFFIX;
        return Uri.fromFile(new File(getCacheDir(), destinationFileName));
    }

}
