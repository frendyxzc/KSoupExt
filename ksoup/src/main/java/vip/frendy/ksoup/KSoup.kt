package vip.frendy.ksoup

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.WebView
import android.webkit.WebViewClient
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
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
    fun addJavascriptInterface(listener: IKSoupListener) {
        mListener = listener

        mJsInterface = JSInterface()
        mJsInterface?.setListener(mListener)

        mWebView?.addJavascriptInterface(mJsInterface, "android")
    }

    fun loadUrl(url: String) {
        mWebView?.loadUrl(url)
    }

    fun getInnerHTML() {
        mWebView?.loadUrl("javascript:window.android.onInnerHtmlLoaded(document.body.innerHTML);")
    }

    fun parseDocument(html: String): Document? {
        val doc = Jsoup.parse(html)
        return doc
    }

    fun parseListByTag(html: String, tag: String): Elements? {
        val doc = Jsoup.parse(html)
        val list = doc.getElementsByTag(tag)
        return list
    }
}