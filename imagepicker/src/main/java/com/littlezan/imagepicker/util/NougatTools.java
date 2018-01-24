package com.littlezan.imagepicker.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.util.List;

/**
 * ClassName: NougatTools
 * Description: Android N 适配工具类
 *
 * @author 彭赞
 * @version 1.0
 * @since 2017-02-28  14:59
 */

public class NougatTools {

    private static final String NOUGAT_FILE_PROVIDER = ".fileprovider";

    /**
     * 将普通uri转化成适应7.0的content://形式  针对文件格式
     *
     * @param context    上下文
     * @param file       文件路径
     * @param intent     intent
     * @param intentType intent.setDataAndType
     * @return Intent
     */
    public static Intent formatFileProviderIntent(Context context, File file, Intent intent, String intentType) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, intentType);
        } else {
            Uri uri = FileProvider.getUriForFile(context, context.getPackageName()+NOUGAT_FILE_PROVIDER, file);
            // 表示文件类型
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setDataAndType(uri, intentType);
        }
        return intent;
    }

    /**
     * 将普通uri转化成适应7.0的content://形式  针对图片格式
     *
     * @param context 上下文
     * @param file    文件路径
     * @param intent  intent
     * @return Intent
     */
    public static Intent formatFileProviderPicIntent(Context context, File file, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri uri = FileProvider.getUriForFile(context, NOUGAT_FILE_PROVIDER, file);
            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(
                    intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, uri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            // 表示图片类型
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        return intent;
    }

    /**
     * 将普通uri转化成适应7.0的content://形式
     *
     * @return Uri
     */
    public static Uri formatFileProviderUri(Context context, File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(file);
        } else {
            uri = FileProvider.getUriForFile(context, NOUGAT_FILE_PROVIDER, file);
        }
        return uri;
    }
}
