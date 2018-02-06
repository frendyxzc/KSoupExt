package vip.frendy.ksoup.interfaces

/**
 * Created by frendy on 2018/2/6.
 */
open interface IKSoupListener {

    fun onInnerHtmlLoaded(action: Int?, data: String?)
}