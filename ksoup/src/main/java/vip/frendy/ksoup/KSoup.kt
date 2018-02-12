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

    val USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36"

    protected var mWebView: WebView? = null
    protected var mListener: IKSoupListener? = null
    protected var mJsInterface: JSInterface? = null

    protected var mDelayInterval = 1000L

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

                //监听回调
                mListener?.onPageFinished(view, url)
                //获取网页内容
                postDelayedToUI({ getInnerHTML() }, mDelayInterval)
            }
        }
    }

    fun setDelayInterval(interval: Long) {
        mDelayInterval = interval
    }

    fun setUserAgent(userAgent: String? = null) {
        mWebView?.settings?.userAgentString = if(userAgent != null) userAgent else USER_AGENT
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