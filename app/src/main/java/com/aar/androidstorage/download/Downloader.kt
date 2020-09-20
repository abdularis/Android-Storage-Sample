package com.aar.androidstorage.download

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment

fun downloadFile(context: Context, url: Uri, fileName: String) {
    val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    DownloadManager.Request(url).apply {
        setTitle("Sample Downloader")
        setDescription("lagi download $fileName...")
        setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "APP_SAMPLE_DIR/$fileName")
        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        dm.enqueue(this)
    }
}