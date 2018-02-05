package com.littlezan.imagepicker.adapter;

import android.app.Activity;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.littlezan.imagepicker.ImagePicker;
import com.littlezan.imagepicker.R;
import com.littlezan.imagepicker.bean.ImageCamera;
import com.littlezan.imagepicker.bean.ImageItem;
import com.littlezan.imagepicker.bean.ImageVideo;
import com.littlezan.imagepicker.ui.ImagePickerActivity;
import com.littlezan.imagepicker.ui.preview.ImageFolderPreviewActivity;

import java.util.ArrayList;

/**
 * ClassName: ImageCellAdapter
 * Description: 图片浏览网格
 *
 * @author 彭赞
 * @version 1.0
 * @since 2018-01-22  10:36
 */
public class ImageCellAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * 相机
     */
    private static final int ITEM_TYPE_CAMERA = 0;
    /**
     * 视频
     */
    private static final int ITEM_TYPE_VIDEO = 1;
    /**
     * 照片
     */
    private static final int ITEM_TYPE_NORMAL = 2;

    /**
     * 当前需要显示的所有的图片数据
     */
    private ArrayList<Object> items = new ArrayList<>();

    private OnItemClickListener onItemClickListener;

    private Activity activity;

    public ImageCellAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CAMERA) {
            return new CameraViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_camera, parent, false));
        } else if (viewType == ITEM_TYPE_VIDEO) {
            return new VideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_video, parent, false));
        } else {
            return new NormalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_normal, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Object o = items.get(position);
        if (holder instanceof CameraViewHolder) {
            CameraViewHolder cameraViewHolder = (CameraViewHolder) holder;
            if (o instanceof ImageCamera) {
                final ImageCamera imageCamera = (ImageCamera) o;
                cameraViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(v, position, imageCamera);
                        }
                    }
                });
            }
        } else if (holder instanceof VideoViewHolder) {
            VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
            if (o instanceof ImageVideo) {
                ImageVideo imageVideo = (ImageVideo) o;
                videoViewHolder.bind(imageVideo);
            }
        } else if (holder instanceof NormalViewHolder) {
            NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
            if (o instanceof ImageItem) {
                ImageItem imageItem = (ImageItem) o;
                normalViewHolder.bind(imageItem);
            }
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object o = items.get(position);
        if (o instanceof ImageCamera) {
            return ITEM_TYPE_CAMERA;
        } else if (o instanceof ImageVideo) {
            return ITEM_TYPE_VIDEO;
        } else if (o instanceof ImageItem) {
            return ITEM_TYPE_NORMAL;
        } else {
            return 0;
        }
    }

    public void refreshData(int folderPosition, ArrayList<ImageItem> images) {
        if (images == null || images.size() == 0) {
            return;
        }
        items.clear();
        if (ImagePicker.getInstance().getModeMediaType() == ImagePicker.ModeMediaType.MEDIA_TYPE_IMAGE) {
            if (folderPosition == 0) {
                items.add(new ImageCamera());
            }
        } else if (ImagePicker.getInstance().getModeMediaType() == ImagePicker.ModeMediaType.MEDIA_TYPE_VIDEO) {
            if (folderPosition == 0) {
                items.add(new ImageVideo());
            }
        }
        items.addAll(images);
        notifyDataSetChanged();
    }

    public ArrayList<Object> getItems() {
        return items;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {

        /**
         * 点击item
         *
         * @param view     view
         * @param position position
         * @param item     item
         */
        void onItemClick(View view, int position, Object item);
    }

    public static class CameraViewHolder extends RecyclerView.ViewHolder {

        View rootView;

        CameraViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
        }

    }


    public static class VideoViewHolder extends RecyclerView.ViewHolder {

        View rootView;

        VideoViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
        }

        void bind(ImageVideo imageVideo) {
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }


    public class NormalViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImg;
        ImageView ivCheck;
        View mask;

        NormalViewHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
            ivCheck = itemView.findViewById(R.id.iv_check);
            mask = itemView.findViewById(R.id.mask);
        }

        void bind(final ImageItem imageItem) {
            String path = imageItem.cropUri == null ? imageItem.path : imageItem.cropUri.getPath();
            ImagePicker.getInstance().getImageLoader().displayImage(ivImg.getContext(), path, ivImg);
            boolean containsCurrent = ImagePicker.getInstance().getSelectedImages().contains(imageItem);
            ivCheck.setSelected(containsCurrent);
            mask.setVisibility(ivCheck.isSelected() ? View.VISIBLE : View.GONE);
            ivCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectSize = ImagePicker.getInstance().getSelectedImages().size();
                    int selectLimit = ImagePicker.getInstance().getSelectLimit();
                    if (!ivCheck.isSelected() && selectSize >= selectLimit) {
                        Toast.makeText(ivCheck.getContext(), ivCheck.getContext().getString(R.string.ip_select_limit, selectLimit), Toast.LENGTH_SHORT).show();
                    } else {
                        ImagePicker.getInstance().addSelectedImageItem(imageItem, !ivCheck.isSelected());
                    }
                }
            });

            ViewCompat.setTransitionName(ivImg , activity.getString(R.string.transition_view_image));
            ivImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentPositionInFolder = ImagePicker.getInstance().getCurrentImageFolderItems().indexOf(imageItem);
                    ImageFolderPreviewActivity.start(activity,ivImg,  ImagePickerActivity.REQUEST_CODE_FOLDER_PREVIEW, currentPositionInFolder);
                }
            });

        }

    }
}
