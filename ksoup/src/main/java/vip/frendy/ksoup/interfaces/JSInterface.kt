package vip.frendy.ksoup.interfaces

import android.webkit.JavascriptInterface
import org.jsoup.Jsoup

/**
 * Created by frendy on 2018/2/6.
 */
open class JSInterface() {
    companion object {
        val LOAD_INNER_HTML = 0
    }

    protected var mListener: IKSoupListener? = null

    fun setListener(listener: IKSoupListener?) {
        mListener = listener
    }

    @JavascriptInterface
    fun onInnerHTMLLoaded(html: String) {
        mListener?.onInnerHTMLLoaded(LOAD_INNER_HTML, html)
    }
}