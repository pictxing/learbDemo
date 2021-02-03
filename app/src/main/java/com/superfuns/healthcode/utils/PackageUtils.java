package com.superfuns.healthcode.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.superfuns.healthcode.App;
import com.superfuns.healthcode.Constants.Config;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PackageUtils {
	/**
	 * 获取本地版本号
	 *
	 * @return
	 */
	public static float getLocalVersion() {
		try {
			PackageInfo packageInfo = App.getContext().getApplicationContext().getPackageManager().getPackageInfo(App.getContext().getPackageName(), 0);
			int verion = packageInfo.versionCode;
			return verion;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		return 0;
	}


	/**
	 * 将输入流写入文件
	 *
	 * @param file   文件
	 * @param is     输入流
	 * @param append 是否追加在文件末
	 * @return {@code true}: 写入成功<br>{@code false}: 写入失败
	 */
	public static boolean writeFileFromIS(File file, InputStream is, boolean append) {
		if (file == null || is == null) return false;
		if (!createOrExistsFile(file)) return false;
		OutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(file, append));
			byte data[] = new byte[1024];
			int len;
			while ((len = is.read(data, 0, 1024)) != -1) {
				os.write(data, 0, len);
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeIO(is, os);
		}
		return false;
	}

	/**
	 * 关闭IO
	 *
	 * @param closeables closeable
	 */
	public static void closeIO(Closeable... closeables) {
		if (closeables == null) return;
		for (Closeable closeable : closeables) {
			if (closeable != null) {
				try {
					closeable.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	/**
	 * 判断文件是否存在，不存在则判断是否创建成功
	 *
	 * @param file 文件
	 * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
	 */
	public static boolean createOrExistsFile(File file) {
		if (file == null) return false;
		// 如果存在，是文件则返回true，是目录则返回false
		if (file.exists()) return file.isFile();
		if (!createOrExistsDir(file.getParentFile())) return false;
		try {
			return file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 判断目录是否存在，不存在则判断是否创建成功
	 *
	 * @param file 文件
	 * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
	 */
	public static boolean createOrExistsDir(File file) {
		// 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
		return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
	}


	public static  void installApk(Context context, String downloadApk) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		File file = new File(downloadApk);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			Uri apkUri = FileProvider.getUriForFile(context, "com.superfuns.healthcode.provider", file);
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
		} else {
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Uri uri = Uri.fromFile(file);
			intent.setDataAndType(uri, "application/vnd.android.package-archive");
		}
		context.startActivity(intent);

	}
}
