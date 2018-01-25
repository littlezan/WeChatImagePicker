package com.littlezan.imagepicker.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.littlezan.imagepicker.ImageDataSource;
import com.littlezan.imagepicker.ImagePicker;
import com.littlezan.imagepicker.R;
import com.littlezan.imagepicker.VideoDataSource;
import com.littlezan.imagepicker.adapter.ImageCellAdapter;
import com.littlezan.imagepicker.adapter.ImageFolderAdapter;
import com.littlezan.imagepicker.bean.ImageFolder;
import com.littlezan.imagepicker.bean.ImageItem;
import com.littlezan.imagepicker.decoration.GridSpacingItemDecoration;
import com.littlezan.imagepicker.util.ImagePickerUtils;
import com.littlezan.imagepicker.view.FolderPopUpWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: ImagePickerActivity
 * Description: 图库浏览
 *
 * @author 彭赞
 * @version 1.0
 * @since 2018-01-19  15:04
 */
public class ImagePickerActivity extends AppCompatActivity implements View.OnClickListener, ImagePicker.OnImageSelectedListener {

    public static final int REQUEST_PERMISSION_STORAGE = 0x01;
    public static final int REQUEST_PERMISSION_CAMERA = 0x02;

    private android.widget.ImageView ivNavLeft;
    private android.widget.TextView tvNavCenter;
    private android.widget.TextView tvNavRight;
    private android.support.v7.widget.Toolbar toolbar;
    private android.support.design.widget.AppBarLayout appBarLayout;
    private android.support.v7.widget.RecyclerView recyclerView;
    private ImageCellAdapter imageCellAdapter;
    /**
     * 所有的图片文件夹
     */
    private List<ImageFolder> mImageFolders = new ArrayList<>();

    /**
     * ImageSet的PopupWindow
     */
    private FolderPopUpWindow mFolderPopupWindow;

    /**
     * 图片文件夹的适配器
     */
    private ImageFolderAdapter imageFolderAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, ImagePickerActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);
        this.recyclerView = findViewById(R.id.recycler_view);
        this.appBarLayout = findViewById(R.id.app_bar_layout);
        this.toolbar = findViewById(R.id.toolbar);
        this.tvNavRight = findViewById(R.id.tv_nav_right);
        this.tvNavCenter = findViewById(R.id.tv_nav_center);
        this.ivNavLeft = findViewById(R.id.iv_nav_left);

        initImagePicker();
        initToolbar();
        initView();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                initDataSourceLoader();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
            }
        } else {
            initDataSourceLoader();
        }

    }

    private void initDataSourceLoader() {
        if (ImagePicker.getInstance().getModeMediaType() == ImagePicker.ModeMediaType.MEDIA_TYPE_IMAGE) {
            new ImageDataSource(this, null, new ImageDataSource.OnImagesLoadedListener() {
                @Override
                public void onImagesLoaded(List<ImageFolder> imageFolders) {
                    mImageFolders = imageFolders;
                    if (imageFolders.size() > 0) {
                        ArrayList<ImageItem> images = imageFolders.get(0).images;
                        imageCellAdapter.refreshData(images);
                    }
                    imageFolderAdapter.refreshData(imageFolders);
                }
            });
        } else if (ImagePicker.getInstance().getModeMediaType() == ImagePicker.ModeMediaType.MEDIA_TYPE_VIDEO) {
            new VideoDataSource(this, null, new VideoDataSource.OnVideosLoadedListener() {
                @Override
                public void onVideosLoaded(List<ImageFolder> videoFolders) {
                    mImageFolders = videoFolders;
                    if (videoFolders.size() > 0) {
                        imageCellAdapter.refreshData(videoFolders.get(0).images);
                    }
                }
            });
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initDataSourceLoader();
            } else {
                Toast.makeText(this, "权限被禁止，无法选择本地图片", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ImagePicker.getInstance().takePicture(this, ImagePicker.REQUEST_CODE_TAKE);
            } else {
                Toast.makeText(this, "权限被禁止，无法打开相机", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initImagePicker() {
        ImagePicker.getInstance().clear();
        ImagePicker.getInstance().addOnImageSelectedListener(this);

    }

    private void initToolbar() {
        tvNavCenter.setText("相机胶卷");
        tvNavRight.setText("完成");
        ivNavLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(GridSpacingItemDecoration.newBuilder().spacing(ImagePickerUtils.dp2px(this, 4)).build());
        imageCellAdapter = new ImageCellAdapter();
        recyclerView.setAdapter(imageCellAdapter);
        imageCellAdapter.setOnItemClickListener(new ImageCellAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object item) {
                if (position == 0) {
                    if (ImagePicker.getInstance().getModeMediaType() == ImagePicker.ModeMediaType.MEDIA_TYPE_IMAGE) {
                        if (!(checkPermission(Manifest.permission.CAMERA))) {
                            ActivityCompat.requestPermissions(ImagePickerActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
                        } else {
                            ImagePicker.getInstance().takePicture(ImagePickerActivity.this, ImagePicker.REQUEST_CODE_TAKE);
                        }
                    }
                }
            }
        });

        tvNavCenter.setOnClickListener(this);
        createPopupFolderList();
    }

    public boolean checkPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImagePicker.getInstance().removeOnImageSelectedListener(this);
    }

    @Override
    public void onImageSelected(ImageItem imageItem, boolean isAdd) {
        int selectImageCount = ImagePicker.getInstance().getSelectedImages().size();
        if (selectImageCount > 0) {
            tvNavRight.setText(getString(R.string.ip_select_complete, selectImageCount, ImagePicker.getInstance().getSelectLimit()));
        } else {
            tvNavRight.setText("完成");
        }
        int notifyItemPosition = imageCellAdapter.getItems().indexOf(imageItem);
        imageCellAdapter.notifyItemChanged(notifyItemPosition);
    }

    @Override
    public void onImageItemChange(ImageItem imageItem) {
        int notifyItemPosition = imageCellAdapter.getItems().indexOf(imageItem);
        imageCellAdapter.notifyItemChanged(notifyItemPosition);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_nav_center) {
            //标题
            if (mImageFolders == null) {
                Log.i("ImageGridActivity", "您的手机没有图片");
                return;
            }
            //刷新数据
            imageFolderAdapter.refreshData(mImageFolders);
            mFolderPopupWindow.showAsDropDown(tvNavCenter, 0, 0);
            //默认选择当前选择的上一个，当目录很多时，直接定位到已选中的条目
            int index = imageFolderAdapter.getSelectIndex();
            index = index == 0 ? index : index - 1;
            mFolderPopupWindow.setSelection(index);
        } else if (i == R.id.tv_nav_right) {

        } else {
        }
    }

    /**
     * 创建弹出的ListView
     */
    private void createPopupFolderList() {
        imageFolderAdapter = new ImageFolderAdapter();
        mFolderPopupWindow = new FolderPopUpWindow(this, imageFolderAdapter);
        mFolderPopupWindow.setOnItemClickListener(new FolderPopUpWindow.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                imageFolderAdapter.setSelectIndex(position);
                ImagePicker.getInstance().setCurrentImageFolderPosition(position);
                mFolderPopupWindow.dismiss();
                ImageFolder imageFolder = (ImageFolder) adapterView.getAdapter().getItem(position);
                if (null != imageFolder) {
                    imageCellAdapter.refreshData(imageFolder.images);
                    tvNavCenter.setText(imageFolder.name);
                }
            }
        });
        mFolderPopupWindow.setMargin(tvNavCenter.getHeight());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getExtras() != null) {
//            if (resultCode == ImagePicker.RESULT_CODE_BACK) {
//                isOrigin = data.getBooleanExtra(ImagePreviewActivity.ISORIGIN, false);
//            } else {
//                //从拍照界面返回
//                //点击 X , 没有选择照片
//                if (data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) == null) {
//                    //什么都不做 直接调起相机
//                } else {
//                    //说明是从裁剪页面过来的数据，直接返回就可以
//                    setResult(ImagePicker.RESULT_CODE_ITEMS, data);
//                }
//                finish();
//            }
        } else {
            //如果是裁剪，因为裁剪指定了存储的Uri，所以返回的data一定为null
            if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE_TAKE) {
                //发送广播通知图片增加了
                ImagePicker.galleryAddPic(this, ImagePicker.getInstance().getTakeImageFile());

                /**
                 * 2017-03-21 对机型做旋转处理
                 */
                String path = ImagePicker.getInstance().getTakeImageFile().getAbsolutePath();
//                int degree = BitmapUtil.getBitmapDegree(path);
//                if (degree != 0){
//                    Bitmap bitmap = BitmapUtil.rotateBitmapByDegree(path,degree);
//                    if (bitmap != null){
//                        File file = new File(path);
//                        try {
//                            FileOutputStream bos = new FileOutputStream(file);
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//                            bos.flush();
//                            bos.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }

                ImageItem imageItem = new ImageItem();
                imageItem.path = path;
                ImagePicker.getInstance().addSelectedImageItem(imageItem, true);
                finish();
//                if (imagePicker.isCrop()) {
//                    Intent intent = new Intent(ImageGridActivity.this, ImageCropActivity.class);
//                    startActivityForResult(intent, ImagePicker.REQUEST_CODE_CROP);  //单选需要裁剪，进入裁剪界面
//                } else {
//                    Intent intent = new Intent();
//                    intent.putExtra(ImagePicker.EXTRA_RESULT_ITEMS, imagePicker.getSelectedImages());
//                    setResult(ImagePicker.RESULT_CODE_ITEMS, intent);   //单选不需要裁剪，返回数据
//                    finish();
//                }
            }
//            else if (directPhoto) {
//                finish();
//            }
        }
    }

}
