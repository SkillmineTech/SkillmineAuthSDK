package com.skillmine.webauthsdk

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class AuthenticationActivity : AppCompatActivity() {
    private lateinit var accessToken: String
    private lateinit var baseUrl: String
    private lateinit var clientId: String
    private lateinit var redirectUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val webView: WebView = findViewById(R.id.webview_layout)

        baseUrl = intent.getStringExtra(EXTRA_BASE_URL) ?: ""
        clientId = intent.getStringExtra(EXTRA_CLIENT_ID) ?: ""
        redirectUrl = intent.getStringExtra(EXTRA_REDIRECT_URL) ?: ""

        setupWebView(webView, clientId, redirectUrl, baseUrl)
    }

    private fun setupWebView(webView: WebView, clientId: String, redirectUrl: String, baseUrl: String) {
        webView.clearCache(true)
        webView.clearHistory()
        webView.setInitialScale(0)
        webView.isVerticalScrollBarEnabled = false
        webView.setLayerType(WebView.LAYER_TYPE_HARDWARE, null)
        webView.isScrollbarFadingEnabled = true
        WebView.setWebContentsDebuggingEnabled(true)

        // Build the query string
        val queryParams = buildString {
            append("client_id=").append(clientId)
            append("&response_type=token")
            append("&scope=openid%20profile%20user_info_all")
            append("&redirect_uri=").append(redirectUrl)
            append("&groups_info=0")
            append("&response_mode=query")
        }
        // Combine base URL, paths, and query string
        val completeUrl = "$baseUrl?$queryParams"
        // Load the initial URL
        webView.loadUrl(completeUrl)

        // Use a custom WebViewClient to handle URL loading
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()

                if (url.startsWith(redirectUrl)) {
                    val uri = Uri.parse(url)
                    accessToken = uri.getQueryParameter("access_token").toString()

                    accessToken.let {
                        Toast.makeText(
                            this@AuthenticationActivity,
                            "Access Token Received",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    val resultIntent = Intent()
                    resultIntent.putExtra("access_token", accessToken)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                    return true
                }
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            @SuppressLint("WebViewClientOnReceivedSslError")
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                handler?.proceed() // Ignore SSL certificate errors
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                Log.e("WebAuth", "Error: ${error?.description}, Code: ${error?.errorCode}")
                super.onReceivedError(view, request, error)
            }
        }

        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = false
        webSettings.domStorageEnabled = true
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT // Use default cache settings
    }

    companion object {
        private const val EXTRA_BASE_URL = "baseURL"
        private const val EXTRA_CLIENT_ID = "clientID"
        private const val EXTRA_REDIRECT_URL = "redirectURL"

        fun createIntent(context: Context, baseUrl: String, clientId: String, redirectUrl: String): Intent {
            return Intent(context, AuthenticationActivity::class.java).apply {
                putExtra(EXTRA_BASE_URL, baseUrl)
                putExtra(EXTRA_CLIENT_ID, clientId)
                putExtra(EXTRA_REDIRECT_URL, redirectUrl)
            }
        }
    }
}


