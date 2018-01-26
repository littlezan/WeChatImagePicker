package com.littlezan.imagepicker.ui;

import android.app.Activity;

import com.yalantis.ucrop.UCrop;

/**
 * ClassName: RequestCode
 * Description:
 *
 * @author 彭赞
 * @version 1.0
 * @since 2018-01-26  11:51
 */
public interface RequestCode {


    /**
     * 系统拍照
     */
    int REQUEST_CODE_TAKE = 1001;
    /**
     * UCrop 裁剪
     */
    int REQUEST_U_CROP = UCrop.REQUEST_CROP;
    /**
     * 重新裁剪
     */
    int REQUEST_RE_COPE = REQUEST_U_CROP + 1;


    /**
     * 完成图片选择
     */
    int RESULT_CODE_FINISH_SELECT = Activity.RESULT_OK + 1;
}
