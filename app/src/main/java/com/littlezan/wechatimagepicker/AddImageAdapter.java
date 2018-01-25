package com.littlezan.wechatimagepicker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.littlezan.imagepicker.bean.ImageItem;

import java.util.ArrayList;

/**
 * ClassName: AddImageAdapter
 * Description:
 *
 * @author 彭赞
 * @version 1.0
 * @since 2018-01-25  14:28
 */
public class AddImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_NORMAL = 1;
    private static final int ITEM_ADD = 2;

    private ArrayList<Object> itemList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_NORMAL) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_image, parent, false));
        } else {
            return new AddViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_image, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Object item = itemList.get(position);
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            final ImageItem imageItem = (ImageItem) item;
            String path = imageItem.cropUri == null ? imageItem.path : imageItem.cropUri.getPath();
            ImageView sivIcon = viewHolder.sivIcon;
            Glide.with(sivIcon.getContext())
                    .load(path)
                    .into(sivIcon);
            viewHolder.sivIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, imageItem, position);
                    }
                }
            });
        } else if (holder instanceof AddViewHolder) {
            AddViewHolder addViewHolder = (AddViewHolder) holder;
            final ImageAddMode imageAddMode = (ImageAddMode) item;
            addViewHolder.sivIcon.setImageResource(R.drawable.btn_dongtai_fabu_addpic);
            addViewHolder.sivIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, imageAddMode, position);
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position) instanceof ImageAddMode) {
            return ITEM_ADD;
        } else {
            return ITEM_NORMAL;
        }
    }

    public void refreshData(ArrayList<ImageItem> imageItems) {
        if (imageItems == null || imageItems.size() == 0) {
            return;
        }
        itemList.clear();
        itemList.addAll(imageItems);
        if (imageItems.size() < 9) {
            itemList.add(new ImageAddMode());
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        /**
         * 点击某个条目
         *
         * @param view     view
         * @param item     item
         * @param position position
         */
        void onItemClick(View view, Object item, int position);
    }


    static class AddViewHolder extends RecyclerView.ViewHolder {

        ImageView sivIcon;

        AddViewHolder(View itemView) {
            super(itemView);
            sivIcon = itemView.findViewById(R.id.siv_icon);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView sivIcon;

        ViewHolder(View itemView) {
            super(itemView);
            sivIcon = itemView.findViewById(R.id.siv_icon);
        }
    }
}
