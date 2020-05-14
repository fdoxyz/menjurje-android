package com.visualcosita.menjurje

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebView
import android.widget.ImageView

class MainActivity : AppCompatActivity(), CustomWebChromeClient.CustomListener {

    private lateinit var webView: WebView
    private lateinit var splash: ImageView

    private var filePathCallback: ValueCallback<Array<Uri>>? = null

    companion object {
        private const val baseUrl = "https://dev.to"
        private const val PIC_CHOOSER_REQUEST = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById<WebView>(R.id.webView)
        splash = findViewById<ImageView>(R.id.splash)

        configureWebView()
        savedInstanceState?.let { restoreState(it) } ?: navigateToHome()
        handleIntent(intent)
    }

    private fun configureWebView() {
        if (BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.userAgentString = "Menjurje"

        val webViewClient = CustomWebViewClient(this@MainActivity) {
            splash.visibility = View.GONE
        }
        webView.webViewClient = webViewClient
        webView.webChromeClient = CustomWebChromeClient(baseUrl, this)
        webView.loadUrl(baseUrl)
    }

    private fun handleIntent(intent: Intent) {
        val appLinkData: Uri? = intent.data
        if (appLinkData != null) {
            webView.loadUrl(appLinkData.toString())
        }
    }

    private fun restoreState(savedInstanceState: Bundle) {
        webView.restoreState(savedInstanceState)
    }

    private fun navigateToHome() {
        webView.loadUrl(baseUrl)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun launchGallery(filePathCallback: ValueCallback<Array<Uri>>?) {
        this.filePathCallback = filePathCallback

        val galleryIntent = Intent().apply {
            // Show only images, no videos or anything else
            type = "image/*"
            action = Intent.ACTION_PICK
        }

        // Always show the chooser (if there are multiple options available)
        startActivityForResult(
            Intent.createChooser(galleryIntent, "Select Picture"),
            PIC_CHOOSER_REQUEST,
            null    // No additional data
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != PIC_CHOOSER_REQUEST) {
            return super.onActivityResult(requestCode, resultCode, data)
        }

        when (resultCode) {
            Activity.RESULT_OK -> data?.data?.let {
                filePathCallback?.onReceiveValue(arrayOf(it))
                filePathCallback = null
            }
            Activity.RESULT_CANCELED -> {
                filePathCallback?.onReceiveValue(null)
                filePathCallback = null
            }
        }
    }
}
