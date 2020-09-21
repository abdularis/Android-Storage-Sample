package com.aar.androidstorage

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

const val RC_STORAGE_PERMISSION = 100

fun Activity.askStoragePermission() {
    val perm = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    ActivityCompat.requestPermissions(this, perm, RC_STORAGE_PERMISSION)
}

fun Context.isStoragePermissionGranted(): Boolean {
    return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
}

fun Long.asFormattedDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
    return formatter.format(Date(this))
}

fun Activity.showOpenDocumentPicker(
    mimeType: String = "*/*",
    requestCode: Int
) {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        type = mimeType
        addCategory(Intent.CATEGORY_OPENABLE)
    }

    startActivityForResult(intent, requestCode)
}