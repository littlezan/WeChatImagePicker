package com.littlezan.imagepicker.ui.preview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
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
import com.littlezan.imagepicker.ui.BaseImageCropActivity;
import com.littlezan.imagepicker.ui.recrop.PreviewReCropImageActivity;

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


    protected android.widget.ImageView ivNavLeft;
    protected android.widget.TextView tvNavCenter;
    protected android.widget.TextView tvNavRight;
    protected android.support.v7.widget.Toolbar toolbar;
    protected ViewPager viewPager;
    protected android.support.v7.widget.RecyclerView recyclerView;
    protected android.widget.TextView tvSelect;
    protected android.widget.RelativeLayout llBottom;
    protected TextView tvDelete;
    protected RelativeLayout rlCrop;


    /**
     * 跳转进ImagePreviewFragment时的序号，第几个图片
     */
    protected int currentPosition = 0;
    private ImagePreviewHorizontalCellAdapter horizontalCellAdapter;
    private ImagePageAdapter imagePageAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        this.llBottom = findViewById(R.id.ll_bottom);
        this.tvSelect = findViewById(R.id.tv_select);
        TextView tvCrop = findViewById(R.id.tv_crop);
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
        tvNavRight.setOnClickListener(this);
        tvCrop.setOnClickListener(this);
        parseIntent();
        initToolbar();
        initView();
    }

    private void parseIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            currentPosition = intent.getIntExtra(EXTRA_SELECTED_IMAGE_POSITION, 0);
        }
        getImagePreviewSourceData();
    }

    private void initToolbar() {
        rendTitle();
        rendNavRight();
        ivNavLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionFinish();
            }
        });
    }

    private void rendTitle() {
        tvNavCenter.setText(getString(R.string.ip_preview_image_count, currentPosition + 1, getImagePreviewSourceData().size()));
    }

    private void rendNavRight() {
        int selectImageCount = ImagePicker.getInstance().getSelectedImages().size();
        if (selectImageCount > 0) {
            tvNavRight.setText(getString(R.string.ip_select_complete, selectImageCount, ImagePicker.getInstance().getSelectLimit()));
        } else {
            tvNavRight.setText("完成");
        }
        tvNavRight.setSelected(ImagePicker.getInstance().getSelectedImages().size() > 0);
    }


    private void initView() {
        ImageItem currentItem = getImagePreviewSourceData().get(currentPosition);
        tvSelect.setSelected(ImagePicker.getInstance().getSelectedImages().contains(currentItem));
        initViewPager();
        initRecyclerView();
    }

    private void initViewPager() {
        ViewCompat.setTransitionName(viewPager,getString(R.string.transition_view_image));
        imagePageAdapter = new ImagePageAdapter(getImagePreviewSourceData());
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
                ImageItem imageItem = getImagePreviewSourceData().get(position);
                boolean isSelected = ImagePicker.getInstance().isSelect(imageItem);
                tvSelect.setSelected(isSelected);
                rendTitle();
                horizontalCellAdapter.setCurrentImageItem(imageItem);
                horizontalCellAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 初始化 数据源
     *
     * @return ArrayList<ImageItem>
     */
    protected abstract ArrayList<ImageItem> getImagePreviewSourceData();

    private void initRecyclerView() {
        setRecyclerViewVisible();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        horizontalCellAdapter = new ImagePreviewHorizontalCellAdapter();
        recyclerView.setAdapter(horizontalCellAdapter);
        horizontalCellAdapter.setCurrentImageItem(getImagePreviewSourceData().get(currentPosition));
        horizontalCellAdapter.refreshData(ImagePicker.getInstance().getSelectedImages());
        horizontalCellAdapter.setOnItemClickListener(new ImagePreviewHorizontalCellAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ImageItem imageItem) {
                int position = getImagePreviewSourceData().indexOf(imageItem);
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
        if (id == R.id.tv_nav_right) {
            if (ImagePicker.getInstance().getSelectedImages().size() == 0) {
                Toast.makeText(this, "您还没有选择图片哦!", Toast.LENGTH_SHORT).show();
            } else {
                setResult(RESULT_CODE_FINISH_SELECT);
                transitionFinish();
            }
        } else if (id == R.id.tv_select) {
            //选中
            int selectSize = ImagePicker.getInstance().getSelectedImages().size();
            int selectLimit = ImagePicker.getInstance().getSelectLimit();
            if (!tvSelect.isSelected() && selectSize >= selectLimit) {
                Toast.makeText(tvSelect.getContext(), tvSelect.getContext().getString(R.string.ip_select_limit, selectLimit), Toast.LENGTH_SHORT).show();
            } else {
                tvSelect.setSelected(!tvSelect.isSelected());
                ImageItem currentItem = getImagePreviewSourceData().get(currentPosition);
                ImagePicker.getInstance().addSelectedImageItem(currentItem, tvSelect.isSelected());
                if (tvSelect.isSelected()) {
                    horizontalCellAdapter.addData(currentItem);
                } else {
                    horizontalCellAdapter.removeData(currentItem);
                }
                rendNavRight();
                setRecyclerViewVisible();
            }
        } else if (id == R.id.tv_delete) {
            //删除
            ImageItem imageItemDelete = getImagePreviewSourceData().get(currentPosition);
            ImagePicker.getInstance().addSelectedImageItem(imageItemDelete, false);
            horizontalCellAdapter.removeData(imageItemDelete);
            imagePageAdapter.notifyDataSetChanged();
            if (ImagePicker.getInstance().getSelectedImages().size() == 0) {
                //最后一个退出
                transitionFinish();
            } else {
                //否则前移
                int currentPositionAfterDelete = currentPosition - 1;
                currentPositionAfterDelete = currentPositionAfterDelete < 0 ? 0 : currentPositionAfterDelete;
                viewPager.setCurrentItem(currentPositionAfterDelete);
                ImageItem imageItemCurrent = getImagePreviewSourceData().get(currentPositionAfterDelete);
                horizontalCellAdapter.setCurrentImageItem(imageItemCurrent);
                horizontalCellAdapter.notifyDataSetChanged();
                rendNavRight();
                rendTitle();
                setRecyclerViewVisible();
            }
        } else if (id == R.id.tv_crop) {
            //裁剪
            ImageItem currentItem = getImagePreviewSourceData().get(currentPosition);
            startCrop(Uri.fromFile(new File(currentItem.path)));
        }
    }

    private void transitionFinish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }else{
            finish();
        }
    }

    @Override
    protected void handleCropResult(Uri sourceUri, Uri resultUri) {
        PreviewReCropImageActivity.startForResult(this, REQUEST_RE_COPE, sourceUri, resultUri);
    }

    @Override
    protected void handleReCropResult(Uri resultUri) {
        ImageItem currentItem = getImagePreviewSourceData().get(currentPosition);
        currentItem.cropUri = resultUri;
        imagePageAdapter.notifyDataSetChanged();
        ImagePicker.getInstance().notifyImageItemChanged(currentItem);
    }
}
