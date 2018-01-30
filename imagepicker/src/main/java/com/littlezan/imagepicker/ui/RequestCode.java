package com.littlezan.imagepicker.ui;

import android.app.Activity;


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
     * 新的裁剪方案
     */
    int REQUEST_COPE = 27;
    /**
     * 重新裁剪
     */
    int REQUEST_RE_COPE = REQUEST_COPE + 1;


    /**
     * 完成图片选择
     */
    int RESULT_CODE_FINISH_SELECT = Activity.RESULT_OK + 100;
    /**
     * 裁剪失败
     */
    int RESULT_CODE_CROP_ERROR = RESULT_CODE_FINISH_SELECT + 100;
}
