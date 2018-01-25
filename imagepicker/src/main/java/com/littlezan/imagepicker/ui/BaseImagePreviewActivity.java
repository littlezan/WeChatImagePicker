package com.littlezan.imagepicker.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.littlezan.imagepicker.ImagePicker;
import com.littlezan.imagepicker.R;
import com.littlezan.imagepicker.adapter.ImagePageAdapter;
import com.littlezan.imagepicker.adapter.ImagePreviewHorizontalCellAdapter;
import com.littlezan.imagepicker.bean.ImageItem;

import java.io.File;
import java.util.ArrayList;

/**
 * ClassName: ImageFolderPreviewActivity
 * Description: 图片预览界面
 *
 * @author 彭赞
 * @version 1.0
 * @since 2018-01-23  15:36
 */
public abstract class BaseImagePreviewActivity extends BaseImageCropActivity implements View.OnClickListener {

    public static final String EXTRA_SELECTED_IMAGE_POSITION = "extra_selected_image_position";


    private android.widget.ImageView ivNavLeft;
    private android.widget.TextView tvNavCenter;
    private android.widget.TextView tvNavRight;
    private android.support.v7.widget.Toolbar toolbar;
    private ViewPager viewPager;
    private android.support.v7.widget.RecyclerView recyclerView;
    private android.widget.TextView tvSelect;
    private android.widget.RelativeLayout llBottom;
    protected TextView tvDelete;
    protected RelativeLayout rlCrop;


    /**
     * 跳转进ImagePreviewFragment时的序号，第几个图片
     */
    protected int currentPosition = 0;
    private ImagePreviewHorizontalCellAdapter horizontalCellAdapter;
    private ImagePageAdapter imagePageAdapter;
    private ArrayList<ImageItem> imageListSource;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        this.llBottom = findViewById(R.id.ll_bottom);
        this.tvSelect = findViewById(R.id.tv_select);
        android.widget.TextView tvCrop = findViewById(R.id.tv_crop);
        this.recyclerView = findViewById(R.id.recycler_view);
        this.viewPager = findViewById(R.id.view_pager);
        this.toolbar = findViewById(R.id.toolbar);
        this.tvNavRight = findViewById(R.id.tv_nav_right);
        this.tvNavCenter = findViewById(R.id.tv_nav_center);
        this.ivNavLeft = findViewById(R.id.iv_nav_left);
        this.tvDelete = findViewById(R.id.tv_delete);
        this.rlCrop = findViewById(R.id.rl_crop);

        tvSelect.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        tvCrop.setOnClickListener(this);
        parseIntent();
        initToolbar();
        initData();
        initView();
    }

    private void parseIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            currentPosition = intent.getIntExtra(EXTRA_SELECTED_IMAGE_POSITION, 0);
        }
    }

    private void initToolbar() {
        tvNavCenter.setText(getString(R.string.ip_preview_image_count, currentPosition + 1, ImagePicker.getInstance().getCurrentImageFolderItems().size()));
        rendNavRight();
        ivNavLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvNavRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void rendNavRight() {
        int selectImageCount = ImagePicker.getInstance().getSelectedImages().size();
        if (selectImageCount > 0) {
            tvNavRight.setText(getString(R.string.ip_select_complete, selectImageCount, ImagePicker.getInstance().getSelectLimit()));
        } else {
            tvNavRight.setText("完成");
        }
    }

    private void initData() {
        imageListSource = initImagePageAdapterData();
    }

    private void initView() {
        ImageItem currentItem = imageListSource.get(currentPosition);
        tvSelect.setSelected(ImagePicker.getInstance().getSelectedImages().contains(currentItem));
        initViewPager();
        initRecyclerView();
    }

    private void initViewPager() {

        imagePageAdapter = new ImagePageAdapter(imageListSource);
        viewPager.setAdapter(imagePageAdapter);
        imagePageAdapter.setPhotoViewClickListener(new ImagePageAdapter.PhotoViewClickListener() {
            @Override
            public void onPhotoTapListener(View view, float v, float v1) {
                onImageSingleTap();
            }
        });
        viewPager.setCurrentItem(currentPosition, false);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                ImageItem imageItem = imageListSource.get(position);
                boolean isSelected = ImagePicker.getInstance().isSelect(imageItem);
                tvSelect.setSelected(isSelected);
                tvNavCenter.setText(getString(R.string.ip_preview_image_count, currentPosition + 1, imageListSource.size()));
                horizontalCellAdapter.setCurrentImageItem(imageItem);
                horizontalCellAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 初始化 数据源
     * @return
     */
    protected abstract ArrayList<ImageItem> initImagePageAdapterData();

    private void initRecyclerView() {
        setRecyclerViewVisible();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        horizontalCellAdapter = new ImagePreviewHorizontalCellAdapter();
        recyclerView.setAdapter(horizontalCellAdapter);
        horizontalCellAdapter.setCurrentImageItem(imageListSource.get(currentPosition));
        horizontalCellAdapter.refreshData(ImagePicker.getInstance().getSelectedImages());
        horizontalCellAdapter.setOnItemClickListener(new ImagePreviewHorizontalCellAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ImageItem imageItem) {
                int position = imageListSource.indexOf(imageItem);
                viewPager.setCurrentItem(position, false);
            }
        });
    }

    private void setRecyclerViewVisible() {
        if (ImagePicker.getInstance().getSelectedImages().size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }

    /**
     * 单击时，隐藏头和尾
     */
    public void onImageSingleTap() {
        if (toolbar.getVisibility() == View.VISIBLE) {
            toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateDecelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    toolbar.setVisibility(View.GONE);
                }
            }).start();
            llBottom.animate().translationY(llBottom.getTop() + llBottom.getHeight()).setInterpolator(new AccelerateDecelerateInterpolator()).start();
//            tintManager.setStatusBarTintResource(Color.TRANSPARENT);//通知栏所需颜色
            //给最外层布局加上这个属性表示，Activity全屏显示，且状态栏被隐藏覆盖掉。
//            if (Build.VERSION.SDK_INT >= 16) content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            toolbar.animate().translationY(0).setInterpolator(new AccelerateDecelerateInterpolator()).setListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    toolbar.setVisibility(View.VISIBLE);
                }
            }).start();
            llBottom.animate().translationY(0).setInterpolator(new AccelerateDecelerateInterpolator()).start();
//            tintManager.setStatusBarTintResource(R.color.ip_color_primary_dark);//通知栏所需颜色
            //Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住
//            if (Build.VERSION.SDK_INT >= 16) content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_select) {
            //选中
            int selectSize = ImagePicker.getInstance().getSelectedImages().size();
            int selectLimit = ImagePicker.getInstance().getSelectLimit();
            if (!tvSelect.isSelected() && selectSize >= selectLimit) {
                Toast.makeText(tvSelect.getContext(), tvSelect.getContext().getString(R.string.ip_select_limit, selectLimit), Toast.LENGTH_SHORT).show();
            } else {
                tvSelect.setSelected(!tvSelect.isSelected());
                ImageItem currentItem = imageListSource.get(currentPosition);
                ImagePicker.getInstance().addSelectedImageItem(currentItem, tvSelect.isSelected());
                if (tvSelect.isSelected()) {
                    horizontalCellAdapter.addData(currentItem);
                } else {
                    horizontalCellAdapter.removeData(currentItem);
                }
                setRecyclerViewVisible();
                rendNavRight();
            }
        } else if (id == R.id.tv_delete) {
            ImageItem currentItem = imageListSource.get(currentPosition);
            ImagePicker.getInstance().addSelectedImageItem(currentItem, false);
            horizontalCellAdapter.removeData(currentItem);
            imagePageAdapter.notifyDataSetChanged();
            setRecyclerViewVisible();
            rendNavRight();
            if (ImagePicker.getInstance().getSelectedImages().size() == 0) {
                finish();
            }
        } else {
            if (id == R.id.tv_crop) {
                ImageItem currentItem = imageListSource.get(currentPosition);
                startCrop(Uri.fromFile(new File(currentItem.path)));
            }
        }
    }


    @Override
    protected void handleReCropResult(Uri resultUri) {
        ImageItem currentItem = imageListSource.get(currentPosition);
        currentItem.cropUri = resultUri;
        imagePageAdapter.notifyDataSetChanged();
        ImagePicker.getInstance().notifyImageItemChanged(currentItem);
    }
}
