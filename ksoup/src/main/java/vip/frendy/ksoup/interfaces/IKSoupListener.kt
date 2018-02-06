package vip.frendy.ksoup.interfaces

import android.webkit.WebView

/**
 * Created by frendy on 2018/2/6.
 */
open interface IKSoupListener {

    fun onPageFinished(view: WebView?, url: String?)

    fun onInnerHtmlLoaded(action: Int?, data: String?)
}