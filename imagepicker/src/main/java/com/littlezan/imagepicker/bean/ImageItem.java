package com.littlezan.imagepicker.bean;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧 Github地址：https://github.com/jeasonlzy0216
 * 版    本：1.0
 * 创建日期：2016/5/19
 * 描    述：图片信息
 * 修订历史：
 * ================================================
 */
public class ImageItem implements Serializable, Parcelable {

    /**
     * 图片的名字
     */
    public String name;
    /**
     * 图片的路径
     */
    public String path;
    /**
     * 图片的大小
     */
    public long size;
    /**
     * 图片的宽度
     */
    public int width;
    /**
     * 图片的高度
     */
    public int height;
    /**
     * 图片的类型
     */
    public String mimeType;
    /**
     * 图片的创建时间
     */
    public long addTime;
    /**
     * Ucrop裁剪后的Uri
     */
    public Uri cropUri;

    /**
     * 图片的路径 相同 认为是同一张图片
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImageItem imageItem = (ImageItem) o;
        return path.equals(imageItem.path);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeLong(this.size);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeString(this.mimeType);
        dest.writeLong(this.addTime);
        dest.writeParcelable(this.cropUri, flags);
    }

    public ImageItem() {
    }

    protected ImageItem(Parcel in) {
        this.name = in.readString();
        this.path = in.readString();
        this.size = in.readLong();
        this.width = in.readInt();
        this.height = in.readInt();
        this.mimeType = in.readString();
        this.addTime = in.readLong();
        this.cropUri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {
        @Override
        public ImageItem createFromParcel(Parcel source) {
            return new ImageItem(source);
        }

        @Override
        public ImageItem[] newArray(int size) {
            return new ImageItem[size];
        }
    };
}
