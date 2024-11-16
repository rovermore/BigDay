package com.smallworldfs.moneytransferapp.utils;

import android.content.Context;
import android.os.Build;

/**
 * Created by ccasanova on 09/04/2018
 */
public class SamsungMemLeak {
	private static final String TAG = SamsungMemLeak.class.getSimpleName();
	public static void onDestroy(Context context) {
		try {
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.MANUFACTURER.equals("samsung")) {
				Object systemService = context.getSystemService(Class.forName("com.samsung.android.content.clipboard.SemClipboardManager"));
				if(systemService != null) {
					java.lang.reflect.Field mContext = systemService.getClass().getDeclaredField("mContext");
					mContext.setAccessible(true);
					mContext.set(systemService, context);
				}
			}
		}
		catch(ClassNotFoundException ignore) { }
		catch(Exception e) {
			Log.e(TAG, "onDestroy:e:----------------------------------------------------------",e);
		}
	}
}
