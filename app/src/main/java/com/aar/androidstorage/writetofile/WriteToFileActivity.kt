package com.aar.androidstorage.writetofile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.aar.androidstorage.R
import com.aar.androidstorage.asFormattedDate
import com.aar.androidstorage.askStoragePermission
import com.aar.androidstorage.isStoragePermissionGranted
import kotlinx.android.synthetic.main.activity_write_to_file.*
import java.io.*
import java.sql.Date

class WriteToFileActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MODE = "mode"

        const val MODE_PICTURE_TOP_LEVEL_DIR = 1
        const val MODE_CUSTOM_TOP_LEVEL_DIR = 2
    }

    private val mode: Int by lazy {
        intent?.getIntExtra(EXTRA_MODE, MODE_PICTURE_TOP_LEVEL_DIR) ?: MODE_CUSTOM_TOP_LEVEL_DIR
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_to_file)

        if (!isStoragePermissionGranted()) {
            askStoragePermission()
        }

        text_info.text = "Save this image into: ${topLevelDir.absolutePath}"

        btn_save.setOnClickListener {
            try {
                var path = writeFileContentBitmap("write_bitmap_sample.jpg")
                text_result.text = "Success, $path"
            } catch (e: Exception) {
                text_result.text = "Failed: $e"
            }
        }

        btn_read.setOnClickListener {
            try {
                val fileBitmap = readFileContent("write_bitmap_sample.jpg")
                img_result.setImageBitmap(fileBitmap.second)
                text_file.text = "modified: ${fileBitmap.first.lastModified().asFormattedDate()}"
            } catch (e: Exception) {
                text_file.text = e.toString()
            }
        }

        btn_query.setOnClickListener {
            val result = doQueryMediaStore()
            text_query_result.text = "result: ${result.size}\n0: ${result.firstOrNull()}"
        }
    }

    private fun writeFileContentBitmap(fileName: String): String? {
        if (!topLevelDir.exists()) {
            if (topLevelDir.mkdirs()) {
                Toast.makeText(this, "Success folder created: ${topLevelDir.absolutePath}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to create: ${topLevelDir.absolutePath}", Toast.LENGTH_SHORT).show()
                throw IllegalStateException("Could not create specified folder: ${topLevelDir.absolutePath}")
            }
        }

        val file = File(topLevelDir, fileName)
        FileOutputStream(file).use {
            BitmapFactory.decodeResource(resources, R.drawable.img).compress(
                Bitmap.CompressFormat.JPEG, 90, it
            )
        }

        return file.absolutePath
    }

    private fun readFileContent(fileName: String): Pair<File, Bitmap> {
        val file = File(topLevelDir, fileName)
        return Pair(file, BitmapFactory.decodeStream(FileInputStream(file)))
    }

    private fun doQueryMediaStore(): ArrayList<String> {
        val proj = arrayOf(
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DATA
        )

        val uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val selection = "${MediaStore.Files.FileColumns.DISPLAY_NAME}=?"
        val selArgs = arrayOf("write_bitmap_sample.jpg")

        val result = arrayListOf<String>()
        contentResolver.query(uri, proj, selection, selArgs, null)?.use {
            while (it.moveToNext()) {
                val name = it.getString(0)
                val path = it.getString(1)

                result.add(path)
                Log.d("TestMe", "$name -> $path")
            }
        }

        return result
    }

    private val topLevelDir: File
        get() {
            return if (mode == MODE_PICTURE_TOP_LEVEL_DIR) {
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            } else {
                Environment.getExternalStoragePublicDirectory("APP_SAMPLE_DIR")
            }
        }
}