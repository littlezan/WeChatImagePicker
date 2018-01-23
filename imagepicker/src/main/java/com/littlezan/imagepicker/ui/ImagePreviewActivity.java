package com.littlezan.imagepicker.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.littlezan.imagepicker.R;

/**
 * ClassName: ImagePreviewActivity
 * Description: 图片预览界面
 *
 * @author 彭赞
 * @version 1.0
 * @since 2018-01-23  15:36
 */
public class ImagePreviewActivity extends AppCompatActivity {


    private android.widget.ImageView ivNavLeft;
    private android.widget.TextView tvNavCenter;
    private android.widget.TextView tvNavRight;
    private android.support.v7.widget.Toolbar toolbar;
    private android.support.design.widget.AppBarLayout appBarLayout;
    private android.support.v4.view.ViewPager viewPager;
    private android.support.v7.widget.RecyclerView recyclerView;
    private android.widget.TextView tvCrop;
    private android.widget.TextView tvSelect;
    private android.widget.RelativeLayout llBottom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        this.llBottom = findViewById(R.id.ll_bottom);
        this.tvSelect = findViewById(R.id.tv_select);
        this.tvCrop = findViewById(R.id.tv_crop);
        this.recyclerView = findViewById(R.id.recycler_view);
        this.viewPager = findViewById(R.id.view_pager);
        this.appBarLayout = findViewById(R.id.app_bar_layout);
        this.toolbar = findViewById(R.id.toolbar);
        this.tvNavRight = findViewById(R.id.tv_nav_right);
        this.tvNavCenter = findViewById(R.id.tv_nav_center);
        this.ivNavLeft = findViewById(R.id.iv_nav_left);

        initView();
    }

    private void initView() {

    }
}
