package com.sjw.beautifulapp.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sjw.beautifulapp.R;
import com.sjw.beautifulapp.application.AppApplication;


/**
 * Created by pc on 2018/8/28.
 */

public class CustomToast {

    private static Context mContext = AppApplication.mContext;

    public static void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    /**
     * 带图片的吐司提示
     *
     * @param text
     */
    public static void showCustomImgToast(String text) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.toast_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.toast_image);
        imageView.setBackgroundResource(R.drawable.save);
        TextView t = (TextView) view.findViewById(R.id.toast_text);
        t.setText(text);
        Toast toast = null;
        if (toast != null) {
            toast.cancel();
        }
        toast = new Toast(mContext);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    /**
     * 带图片的吐司提示
     * 通过参数传递，可是设置吐司的图片和文字内容
     *
     * @param text
     */
    public static void showCustomImgToast(String text, int imgResId) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.toast_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.toast_image);
        imageView.setBackgroundResource(imgResId);
        TextView t = (TextView) view.findViewById(R.id.toast_text);
        t.setText(text);
        Toast toast = null;
        if (toast != null) {
            toast.cancel();
        }
        toast = new Toast(mContext);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    /**
     * 不带图片的吐司提示
     *
     * @param text
     */
    public static void showCustomToast(String text) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.toast_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.toast_image);
        imageView.setVisibility(View.GONE);
        TextView t = (TextView) view.findViewById(R.id.toast_text);
        t.setText(text);
        Toast toast = null;
        if (toast != null) {
            toast.cancel();
        }
        toast = new Toast(mContext);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    /**
     * 带图片的吐司，设置吐司弹出的位置为屏幕中心
     *
     * @param text
     */
    public static void showCustomToastCenter(String text) {
        showCustomToastCenter(text, R.drawable.save);
    }

    /**
     * 带图片的吐司，设置吐司弹出的位置为屏幕中心
     * 通过参数传递，可是设置吐司的图片和文字内容
     *
     * @param text
     */
    public static void showCustomToastCenter(String text, int imgResId) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.toast_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.toast_image);
        imageView.setBackgroundResource(imgResId);
        TextView t = (TextView) view.findViewById(R.id.toast_text);
        t.setText(text);
        Toast toast = null;
        if (toast != null) {
            toast.cancel();
        }
        toast = new Toast(mContext);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
