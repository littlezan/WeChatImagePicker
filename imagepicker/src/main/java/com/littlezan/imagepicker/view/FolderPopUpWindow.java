package com.littlezan.imagepicker.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.littlezan.imagepicker.R;


/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy0216
 * 版    本：1.0
 * 创建日期：16/8/1
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class FolderPopUpWindow extends PopupWindow implements View.OnClickListener {

    private ListView listView;
    private OnPopUpWindowInteraction onPopUpWindowInteraction;
    private final View masker;
    //    private final View marginView;
    private int marginPx;
    private int listViewHeight;

    public FolderPopUpWindow(Context context, BaseAdapter adapter) {
        super(context);

        final View view = View.inflate(context, R.layout.pop_folder, null);
        masker = view.findViewById(R.id.masker);
        masker.setOnClickListener(this);
//        marginView = view.findViewById(R.id.margin);
//        marginView.setOnClickListener(this);
        listView = view.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        setContentView(view);
        //如果不设置，就是 AnchorView 的宽度
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(0));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (onPopUpWindowInteraction != null) {
                    onPopUpWindowInteraction.onItemClick(adapterView, view, position, l);
                }
            }
        });

        setAnimationStyle(0);


    }

    public void show(View anchor) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            showAsDropDown(anchor, 0, 0);
        } else {
            // 适配 android 7.0
            int[] location = new int[2];
            anchor.getLocationOnScreen(location);
            showAtLocation(anchor, Gravity.NO_GRAVITY, 0, location[1]+anchor.getHeight());
        }
        startEnterAnimatorBefore();
    }

    private void startEnterAnimatorBefore() {
        if (listViewHeight == 0) {
            listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    listView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    listViewHeight = listView.getHeight();
                    enterAnimator();
                }
            });
        } else {
            enterAnimator();
        }
    }

    private void enterAnimator() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(masker, "alpha", 0, 1);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(listView, "translationY", -listViewHeight, 0);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(400);
        set.playTogether(alpha, translationY);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
    }

    @Override
    public void dismiss() {
        if (onPopUpWindowInteraction != null) {
            onPopUpWindowInteraction.onDismissPopUp();
        }
        exitAnimator();
    }


    private void exitAnimator() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(masker, "alpha", 1, 0);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(listView, "translationY", 0, -listViewHeight);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(300);
        set.playTogether(alpha, translationY);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                listView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                FolderPopUpWindow.super.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                FolderPopUpWindow.super.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        set.start();
    }

    public void setOnPopUpWindowInteraction(OnPopUpWindowInteraction listener) {
        this.onPopUpWindowInteraction = listener;
    }

    public void setSelection(int selection) {
        listView.setSelection(selection);
    }

    public void setMargin(int marginPx) {
        this.marginPx = marginPx;
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    public interface OnPopUpWindowInteraction {
        void onItemClick(AdapterView<?> adapterView, View view, int position, long l);

        void onDismissPopUp();
    }
}
