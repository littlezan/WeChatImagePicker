package com.littlezan.imagepicker.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.littlezan.imagepicker.ImagePicker;
import com.littlezan.imagepicker.bean.ImageItem;

import java.util.ArrayList;


/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧 Github地址：https://github.com/jeasonlzy0216
 * 版    本：1.0
 * 创建日期：2016/5/19
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class ImagePageAdapter extends PagerAdapter {

    private ArrayList<ImageItem> images = new ArrayList<>();
    public PhotoViewClickListener listener;

    public ImagePageAdapter(ArrayList<ImageItem> images) {
        this.images = images;
    }

    public void setData(ArrayList<ImageItem> images) {
        this.images = images;
    }

    public void setPhotoViewClickListener(PhotoViewClickListener listener) {
        this.listener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Context context = container.getContext();
        PhotoView photoView = new PhotoView(context);
        ImageItem imageItem = images.get(position);
        String path = imageItem.cropUri == null ? imageItem.path : imageItem.cropUri.getPath();
        ImagePicker.getInstance().getImageLoader().displayImage(context, path, photoView);
        photoView.setOnPhotoTapListener(new OnPhotoTapListener() {

            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                if (listener != null) {
                    listener.onPhotoTapListener(view, x, y);
                }
            }
        });
        container.addView(photoView);
        return photoView;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public interface PhotoViewClickListener {
        void onPhotoTapListener(View view, float v, float v1);
    }
}
