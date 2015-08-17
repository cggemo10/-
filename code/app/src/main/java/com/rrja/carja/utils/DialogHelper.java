package com.rrja.carja.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.rrja.carja.R;

public class DialogHelper {

	private static final String tag = DialogHelper.class.getName();

	private Context mContext;

	private Dialog loadingDialog;

	private void createLoadingDialog() {
		loadingDialog = new Dialog(mContext, mContext.getResources().getIdentifier("quick_style_loading", "style", mContext.getPackageName()));
		loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		LayoutInflater inflater = LayoutInflater.from(mContext);
		View loadRootView = inflater.inflate(R.layout.quicksdk_view_loading, null);
		final ImageView imgLoading = (ImageView) loadRootView.findViewById(R.id.quicksdk_img_loading);

		loadingDialog.setContentView(loadRootView);

		loadingDialog.setCancelable(true);

		loadingDialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				AnimationDrawable background = (AnimationDrawable) imgLoading.getDrawable();
				background.start();
			}
		});

	}

	public void init(Context context) {
		mContext = context;
		createLoadingDialog();
	}
	
	public void distroy() {
		mContext = null;
		try {
			if (loadingDialog != null || loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
			loadingDialog = null;
		} catch (Exception e) {
		}
	}

	public void showWaitting() {

		if (loadingDialog != null && !loadingDialog.isShowing()) {
			try {
				loadingDialog.show();
			} catch (Exception e) {
				Log.w(tag, "pop not show");
			}

		}
	}

	public void dismissWatting() {
		if (loadingDialog != null && loadingDialog.isShowing()) {
			loadingDialog.dismiss();
		}
	}

	private static class HelperHolder {
		private static DialogHelper helper = new DialogHelper();
	}

	public static DialogHelper getHelper() {

		return HelperHolder.helper;
	}



}
