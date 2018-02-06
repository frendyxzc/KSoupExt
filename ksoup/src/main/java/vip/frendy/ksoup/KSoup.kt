package vip.frendy.ksoup

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import vip.frendy.extension.ext.postDelayedToUI
import vip.frendy.ksoup.interfaces.IKSoupListener
import vip.frendy.ksoup.interfaces.JSInterface

/**
 * Created by frendy on 2018/2/6.
 */
open class KSoup(context: Context) {

    protected var mWebView: WebView? = null
    protected var mListener: IKSoupListener? = null
    protected var mJsInterface: JSInterface? = null

    init {
        mWebView = WebView(context)
        //初始化
        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected fun initWebView() {
        mWebView?.settings?.javaScriptEnabled = true
        mWebView?.settings?.blockNetworkImage = true

        mWebView?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                view?.settings?.blockNetworkImage = false

                //获取网页内容
                postDelayedToUI({ getInnerHTML() }, 1000)
            }
        }
    }

    @SuppressLint("JavascriptInterface")
    fun addJavascriptInterface() {
        mJsInterface = JSInterface()
        mJsInterface?.setListener(mListener)

        mWebView?.addJavascriptInterface(mJsInterface, "android")
    }

    fun setKSoupListener(listener: IKSoupListener) {
        mListener = listener
    }

    fun loadUrl(url: String) {
        mWebView?.loadUrl(url)
    }

    fun getInnerHTML() {
        mWebView?.loadUrl("javascript:window.android.onInnerHTMLLoaded(document.body.innerHTML);")
    }

    fun parseAList(html: String): Elements? {
        val doc = Jsoup.parse(html)
        val list = doc.getElementsByTag("a")
        return list
    }
}