/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014. Ray
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.example.moutain.retrofitdemo.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

public class VersionUtils {

    public static String getVersion(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean newThanThis(String thisVersion, String newVersion) {
        if (TextUtils.isEmpty(newVersion)) {
            return false;
        }

        if (TextUtils.isEmpty(thisVersion)) {
            return true;
        }

        String[] splitThisVersion = thisVersion.split("[\\.]");
        String[] splitNewVersion = newVersion.split("[\\.]");

        // a.b.c vs x.y.z
        // a.b vs x.y.z
        // a.b.c vs x.y
        int lessLength = Math.min(splitThisVersion.length, splitNewVersion.length);
        try {
            for (int i = 0; i < lessLength; i++) {
                Integer a = Integer.parseInt(splitThisVersion[i]);
                Integer x = Integer.parseInt(splitNewVersion[i]);

                if (a < x) {
                    return true;
                }
            }
        } catch (NumberFormatException e) {
            return false;
        }

        if (splitNewVersion.length > lessLength) {
            return true;
        }
        return false;
    }

    public static long startDownloadNewVersion(Context context, String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setShowRunningNotification(true);
        request.setTitle("请稍等");
        request.setDescription("正在下载新版私人定制");
        request.setVisibleInDownloadsUi(true);

        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long id = dm.enqueue(request);
        return id;
    }
}
