package com.aar.androidstorage.download

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aar.androidstorage.R
import kotlinx.android.synthetic.main.activity_download.*

class DownloadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        val url = "https://upload.wikimedia.org/wikipedia/commons/3/3e/Android_logo_2019.png"
        val fileName = "hasil_download_android_logo.png"

        text_info.text = "url: $url\n\nname: $fileName"
        btn_download.setOnClickListener {
            downloadFile(this, Uri.parse(url), fileName)
        }
    }
}