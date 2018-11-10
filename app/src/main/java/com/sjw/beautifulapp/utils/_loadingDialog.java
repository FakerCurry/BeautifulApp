package com.sjw.beautifulapp.utils;

/**
 * 进度条
 */

//http://AY131122092539Z:81/svn/jxcivilization/
import android.app.ProgressDialog;
import android.content.Context;


public class _loadingDialog {
	public static ProgressDialog _loadingDialog = null;

	/**
	 * 无序进度条 显示于等待服务器响应
	 *
	 * @param context
	 *            当前Aactivity
	 * @param text
	 *            进度条提示信息
	 */
	public static void submitLoading(Context context, String text) {

		_loadingDialog = new ProgressDialog(context);
		_loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		_loadingDialog.setCancelable(true);
		_loadingDialog.setCanceledOnTouchOutside(false);
		_loadingDialog.setMessage(text);
		_loadingDialog.show();
	}

	public static void updateLoading(Context context, String text,boolean isCancel) {

		_loadingDialog = new ProgressDialog(context);
		_loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		_loadingDialog.setCancelable(isCancel);
		_loadingDialog.setCanceledOnTouchOutside(false);
		_loadingDialog.setMessage(text);
		_loadingDialog.show();
	}
	public static void generalLoading(Context context, String text,boolean isCancel) {

		_loadingDialog = new ProgressDialog(context);
		_loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		_loadingDialog.setCancelable(true);
		_loadingDialog.setCanceledOnTouchOutside(false);
		_loadingDialog.setMessage(text);
		_loadingDialog.show();
	}
	public static void updateText(String text) {


		_loadingDialog.setMessage(text);

	}


	/**
	 * 关闭进度条
	 */
	public static void dismissLoading() {
		_loadingDialog.dismiss();
	}
}
