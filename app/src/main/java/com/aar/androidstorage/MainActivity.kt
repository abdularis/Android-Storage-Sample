package com.aar.androidstorage

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aar.androidstorage.writetofile.WriteToFileActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text_info.text = "${packageName}\n\n" +
                "legacyStoageFlag: ${BuildConfig.FLAVOR_legacyStorageFlag}\n" +
                "targetSdkVersion: ${getTargetSdkVersion()}"

        btn_write_file_1.setOnClickListener {
            Intent(this, WriteToFileActivity::class.java).apply {
                putExtra(WriteToFileActivity.EXTRA_MODE, WriteToFileActivity.MODE_PICTURE_TOP_LEVEL_DIR)
                startActivity(this)
            }
        }
        btn_write_file_2.setOnClickListener {
            Intent(this, WriteToFileActivity::class.java).apply {
                putExtra(WriteToFileActivity.EXTRA_MODE, WriteToFileActivity.MODE_CUSTOM_TOP_LEVEL_DIR)
                startActivity(this)
            }
        }
    }

    private fun getTargetSdkVersion(): Int {
        val ai: ApplicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        return ai.targetSdkVersion
    }
}