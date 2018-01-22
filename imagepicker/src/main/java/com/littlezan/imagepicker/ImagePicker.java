package com.littlezan.imagepicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.littlezan.imagepicker.bean.ImageItem;
import com.littlezan.imagepicker.loader.ImageLoader;
import com.littlezan.imagepicker.ui.ImagePickerActivity;
import com.littlezan.imagepicker.util.ImagePickerUtils;
import com.littlezan.imagepicker.util.ProviderUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * ClassName: ImagePicker
 * Description:
 *
 * @author 彭赞
 * @version 1.0
 * @since 2018-01-22  10:58
 */
public class ImagePicker {



    public enum ModeMediaType {
        /**
         * 图片
         */
        MEDIA_TYPE_IMAGE,
        /**
         * 视频
         */
        MEDIA_TYPE_VIDEO;
    }


    private ImagePicker() {
    }

    public static ImagePicker getInstance() {
        return SingletonHolder.INSTANCE;
    }


    private static class SingletonHolder {
        private static final ImagePicker INSTANCE = new ImagePicker();
    }

    public static final int REQUEST_CODE_TAKE = 1001;

    private File takeImageFile;

    /**
     * 图片加载器
     */
    private ImageLoader imageLoader;

    /**
     * 选中的图片集合
     */
    private ArrayList<ImageItem> selectedImages = new ArrayList<>();

    /**
     * 最大选择图片数量
     */
    private int selectLimit = 9;

    /**
     * 图片选中的监听回调
     */
    private List<OnImageSelectedListener> mImageSelectedListeners;

    private ModeMediaType modeMediaType;

    /**
     * 当前选中的文件夹位置 0表示所有图片
     */
    private int mCurrentImageFolderPosition = 0;


    public void start(Context context, ModeMediaType modeMediaType) {
        ImagePickerActivity.start(context);
        this.modeMediaType = modeMediaType;
    }


    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public ArrayList<ImageItem> getSelectedImages() {
        return selectedImages;
    }

    public void setSelectedImages(ArrayList<ImageItem> selectedImages) {
        this.selectedImages = selectedImages;
    }

    public int getSelectLimit() {
        return selectLimit;
    }

    public void setSelectLimit(int selectLimit) {
        this.selectLimit = selectLimit;
    }

    public ModeMediaType getModeMediaType() {
        return modeMediaType;
    }

    public int getCurrentImageFolderPosition() {
        return mCurrentImageFolderPosition;
    }

    public void setCurrentImageFolderPosition(int mCurrentSelectedImageSetPosition) {
        mCurrentImageFolderPosition = mCurrentSelectedImageSetPosition;
    }


    public void addSelectedImageItem(int position, ImageItem item, boolean isAdd) {
        if (isAdd) {
            selectedImages.add(item);
        } else {
            selectedImages.remove(item);
        }
        notifyImageSelectedChanged(position, item, isAdd);
    }

    private void notifyImageSelectedChanged(int position, ImageItem item, boolean isAdd) {
        if (mImageSelectedListeners == null) {
            return;
        }
        for (OnImageSelectedListener l : mImageSelectedListeners) {
            l.onImageSelected(position, item, isAdd);
        }
    }

    /**
     * 拍照的方法
     */
    public void takePicture(Activity activity, int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            if (ImagePickerUtils.existSDCard()) {
                takeImageFile = new File(Environment.getExternalStorageDirectory(), "/DCIM/camera/");
            } else {
                takeImageFile = Environment.getDataDirectory();
            }
            takeImageFile = createFile(takeImageFile, "IMG_", ".jpg");
            if (takeImageFile != null) {
                // 默认情况下，即不需要指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                // 照相机有自己默认的存储路径，拍摄的照片将返回一个缩略图。如果想访问原始图片，
                // 可以通过dat extra能够得到原始图片位置。即，如果指定了目标uri，data就没有数据，
                // 如果没有指定uri，则data就返回有数据！

                Uri uri;
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                    uri = Uri.fromFile(takeImageFile);
                } else {

                    /**
                     * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
                     * 并且这样可以解决MIUI系统上拍照返回size为0的情况
                     */
                    uri = FileProvider.getUriForFile(activity, ProviderUtil.getFileProviderName(activity), takeImageFile);
                    //加入uri权限 要不三星手机不能拍照
                    List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        activity.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                }

                Log.e("nanchen", ProviderUtil.getFileProviderName(activity));
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
        }
        activity.startActivityForResult(takePictureIntent, requestCode);
    }


    /**
     * 根据系统时间、前缀、后缀产生一个文件
     */
    public static File createFile(File folder, String prefix, String suffix) {
        if (!folder.exists() || !folder.isDirectory()) {
            folder.mkdirs();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        String filename = prefix + dateFormat.format(new Date(System.currentTimeMillis())) + suffix;
        return new File(folder, filename);
    }


    public void clear() {
        if (mImageSelectedListeners != null) {
            mImageSelectedListeners.clear();
            mImageSelectedListeners = null;
        }
//        if (mImageFolders != null) {
//            mImageFolders.clear();
//            mImageFolders = null;
//        }
        if (selectedImages != null) {
            selectedImages.clear();
        }
//        mCurrentImageFolderPosition = 0;
    }


    public void addOnImageSelectedListener(OnImageSelectedListener l) {
        if (mImageSelectedListeners == null) {
            mImageSelectedListeners = new ArrayList<>();
        }
        mImageSelectedListeners.add(l);
    }

    public void removeOnImageSelectedListener(OnImageSelectedListener l) {
        if (mImageSelectedListeners == null) {
            return;
        }
        mImageSelectedListeners.remove(l);
    }

    /**
     * 图片选中的监听
     */
    public interface OnImageSelectedListener {
        /**
         * 选中了图片
         *
         * @param position position
         * @param item     item
         * @param isAdd    isAdd
         */
        void onImageSelected(int position, ImageItem item, boolean isAdd);
    }

}
