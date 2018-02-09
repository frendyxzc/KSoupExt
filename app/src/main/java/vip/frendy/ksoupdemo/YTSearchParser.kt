package vip.frendy.ksoupdemo

import android.content.Context
import android.util.Log
import android.webkit.WebView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import vip.frendy.extension.ext.postDelayedToUI
import vip.frendy.ksoup.KSoup
import vip.frendy.ksoup.interfaces.IKSoupListener
import vip.frendy.ksoup.interfaces.JSInterface

/**
 * Created by frendy on 2018/2/6.
 */
class YTSearchParser<T>(context: Context) {
    var URL_VIDEO_SEARCH = "https://www.youtube.com/results?sp=EgIQAVAU"

    protected var mSoup: KSoup? = null
    protected var mParserListener: IParserListener<T>? = null

    protected var mRetryCount = 0
    protected val RETRY_LIMIT = 5

    protected var mLastTimeStamp = 0L

    init {
        mSoup = KSoup(context)
//        mSoup?.setDelayInterval(500)

        mSoup?.addJavascriptInterface(object : IKSoupListener {
            override fun onPageFinished(view: WebView?, url: String?) {
                val now = System.currentTimeMillis()
                Log.i("ksoup", "** ===== finish to load page ==== cost : ${now - mLastTimeStamp}")
                mLastTimeStamp = now
            }
            override fun onInnerHtmlLoaded(action: Int?, data: String?) {
                when(action) {
                    JSInterface.LOAD_INNER_HTML -> {
                        if(data == null) return

                        doAsync {
                            val elements = mSoup?.parseListByTag(data, "a") ?: return@doAsync

                            val result = ArrayList<T>()

                            for (element in elements) {
                                val href = element.attr("href")

                                val labels = element.attr("aria-label").split(" - ")
                                var title = ""
                                for((i, label) in labels.withIndex()) {
                                    if(i == 0) {
                                        title = label
                                    } else if(i < (labels.size - 2)) {
                                        title += " - $label"
                                    }
                                }

                                val imgElements = element.select("img[src~=(?i)\\.jpg]")
                                val img = if(imgElements.size > 0) imgElements[0].attr("src") else ""

                                if (href.contains("/watch?v")) {
                                    val id = href.replace("/watch?v=", "")

                                    if(mParserListener != null) {
                                        result.add(mParserListener!!.onParserResultWrap(id, title, img))
                                    }

                                    Log.i("ksoup", "** id = $id, title = $title， img = $img")
                                }
                            }

                            uiThread {
                                if(result.size <= 0 && mRetryCount < RETRY_LIMIT) {
                                    postDelayedToUI({ mSoup?.getInnerHTML() }, 500)

                                    mRetryCount ++

                                    Log.i("ksoup", "** ===== retry to load data ==== $mRetryCount")
                                } else {
                                    mParserListener?.onParserResult(result)

                                    val now = System.currentTimeMillis()
                                    Log.i("ksoup", "** ===== finish to load data ==== cost : ${now - mLastTimeStamp}")
                                    mLastTimeStamp = now
                                }
                            }
                        }
                    }
                }
            }
        })
    }

    fun loadUrl(key: String, page: Int) {
        loadUrl("${URL_VIDEO_SEARCH}&search_query=${key}&p=${page}")
    }

    fun loadUrl(url: String) {
        mSoup?.loadUrl(url)
        Log.i("ksoup", "** ===== start to load ==== $url")
        mLastTimeStamp = System.currentTimeMillis()
    }

    fun setParserListener(listener: IParserListener<T>) {
        mParserListener = listener
    }

    interface IParserListener<T> {
        fun onParserResultWrap(id: String, title: String, img: String): T
        fun onParserResult(result: ArrayList<T>)
    }
}