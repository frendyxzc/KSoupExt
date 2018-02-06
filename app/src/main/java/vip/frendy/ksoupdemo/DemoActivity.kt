package vip.frendy.ksoupdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.WebView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import vip.frendy.ksoup.KSoup
import vip.frendy.ksoup.interfaces.IKSoupListener
import vip.frendy.ksoup.interfaces.JSInterface

/**
 * Created by frendy on 2018/2/6.
 */
class DemoActivity: AppCompatActivity() {

    var URL_VIDEO_LIST = "https://www.youtube.com/results?sp=EgIQAVAU&search_query=谢霆锋&p=1"

    var mSoup: KSoup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSoup = KSoup(this)
        mSoup?.addJavascriptInterface(mKSoupListener)

        mSoup?.loadUrl(URL_VIDEO_LIST)
        Log.i("jsoup", "** ===== start to load ====")
    }


    val mKSoupListener = object : IKSoupListener {
        override fun onPageFinished(view: WebView?, url: String?) {
            Log.i("jsoup", "** ===== finish to load page ====")
        }
        override fun onInnerHtmlLoaded(action: Int?, data: String?) {
            when(action) {
                JSInterface.LOAD_INNER_HTML -> {
                    if(data == null) return

                    doAsync {
                        val elements = mSoup?.parseListByTag(data, "a") ?: return@doAsync

                        for (element in elements) {
                            val href = element.attr("href")
                            val label = element.attr("aria-label").split(" - ")[0]

                            if (href.contains("/watch?v")) {
                                val id = href.replace("/watch?v=", "")

                                Log.i("jsoup", "** id = $id, label = $label")
                            }
                        }

                        uiThread {
                            Log.i("jsoup", "** ===== finish to load data ====")
                        }
                    }
                }
            }
        }
    }
}