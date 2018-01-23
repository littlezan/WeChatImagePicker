package com.littlezan.imagepicker.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.littlezan.imagepicker.ImagePicker;
import com.littlezan.imagepicker.R;
import com.littlezan.imagepicker.bean.ImageItem;

import java.util.ArrayList;

/**
 * ClassName: ImagePreviewHorizontalCellAdapter
 * Description: 图片预览页 底部小图选中
 *
 * @author 彭赞
 * @version 1.0
 * @since 2018-01-23  17:44
 */
public class ImagePreviewHorizontalCellAdapter extends RecyclerView.Adapter<ImagePreviewHorizontalCellAdapter.ViewHolder> {

    private ArrayList<ImageItem> items = new ArrayList<>();
    private OnItemClickListener onItemClickListener;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preview_horizontal_cell, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void refreshData(ArrayList<ImageItem> images) {
        items.clear();
        items.addAll(images);
        notifyDataSetChanged();
    }

    public void addData(ImageItem imageItem) {
        items.add(imageItem);
        notifyItemInserted(items.size());
    }

    public void removeData(ImageItem imageItem) {
        if (!items.contains(imageItem)) {
            return;
        }
        int position = items.indexOf(imageItem);
        items.remove(imageItem);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(View view, ImageItem imageItem);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPreview;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPreview = itemView.findViewById(R.id.iv_preview);
        }

        public void bind(final ImageItem imageItem) {
            ImagePicker.getInstance().getImageLoader().displayImage(ivPreview.getContext(), imageItem.path, ivPreview);
            ivPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, imageItem);
                    }
                }
            });
        }
    }
}
