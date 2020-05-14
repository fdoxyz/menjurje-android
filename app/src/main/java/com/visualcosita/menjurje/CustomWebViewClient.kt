package com.visualcosita.menjurje

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.browser.customtabs.CustomTabsIntent

class CustomWebViewClient (
    private val context: Context,
    private val onPageFinish: () -> Unit
) : WebViewClient() {
    private val overrideUrlList = listOf(
        "://dev.to",
        "fdoxyz.ngrok.io",
        "api.twitter.com/oauth",
        "api.twitter.com/account/login_verification",
        "github.com/login",
        "github.com/sessions/"
    )

    override fun onPageFinished(view: WebView, url: String?) {
        onPageFinish()
        view.visibility = View.VISIBLE
        super.onPageFinished(view, url)
    }

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        if (overrideUrlList.any { url.contains(it) }) {
            return false
        }

        CustomTabsIntent.Builder()
            .setToolbarColor(Color.parseColor("#00000000"))
            .build()
            .also { it.launchUrl(context, Uri.parse(url)) }

        return true
    }
}