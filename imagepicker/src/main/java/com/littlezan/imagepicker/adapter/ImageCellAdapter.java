package com.littlezan.imagepicker.adapter;

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
import com.littlezan.imagepicker.ui.ImagePreviewActivity;

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


    public ImageCellAdapter() {
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object o = items.get(position);
        if (holder instanceof CameraViewHolder) {
            CameraViewHolder cameraViewHolder = (CameraViewHolder) holder;
            if (o instanceof ImageCamera) {
                ImageCamera imageCamera = (ImageCamera) o;
                cameraViewHolder.bind(imageCamera);
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
                normalViewHolder.bind(imageItem, position);
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

    public void refreshData(ArrayList<ImageItem> images) {
        items.clear();
        if (ImagePicker.getInstance().getModeMediaType() == ImagePicker.ModeMediaType.MEDIA_TYPE_IMAGE) {
            items.add(new ImageCamera());
        } else if (ImagePicker.getInstance().getModeMediaType() == ImagePicker.ModeMediaType.MEDIA_TYPE_VIDEO) {
            items.add(new ImageVideo());
        }
        items.addAll(images);
        notifyDataSetChanged();
    }


    public static class CameraViewHolder extends RecyclerView.ViewHolder {

        View rootView;

        CameraViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
        }

        void bind(ImageCamera imageCamera) {
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
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


    public static class NormalViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImg;
        ImageView ivCheck;
        View mask;

        NormalViewHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
            ivCheck = itemView.findViewById(R.id.iv_check);
            mask = itemView.findViewById(R.id.mask);
        }

        void bind(final ImageItem imageItem, final int position) {
            ImagePicker.getInstance().getImageLoader().displayImage(ivImg.getContext(), imageItem.path, ivImg);
            boolean containsCurrent = ImagePicker.getInstance().getSelectedImages().contains(imageItem);
            ivCheck.setSelected(containsCurrent);
            ivCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectSize = ImagePicker.getInstance().getSelectedImages().size();
                    int selectLimit = ImagePicker.getInstance().getSelectLimit();
                    if (!ivCheck.isSelected() && selectSize >= selectLimit) {
                        Toast.makeText(ivCheck.getContext(), ivCheck.getContext().getString(R.string.ip_select_limit, selectLimit), Toast.LENGTH_SHORT).show();
                    } else {
                        ivCheck.setSelected(!ivCheck.isSelected());
                        ImagePicker.getInstance().addSelectedImageItem(position, imageItem, ivCheck.isSelected());
                        mask.setVisibility(ivCheck.isSelected() ? View.VISIBLE : View.GONE);
                    }
                }
            });

            ivImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentPositionInFolder = position - 1;
                    ImagePreviewActivity.start(ivImg.getContext(), currentPositionInFolder);
                }
            });

        }

    }
}
