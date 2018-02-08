package vip.frendy.ksoupdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by frendy on 2018/2/6.
 */
class DemoActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val parser = YTSearchParser<ParserResult>(this)
        parser.setParserListener(object : YTSearchParser.IParserListener<ParserResult> {
            override fun onParserResultWrap(id: String, title: String, img: String): ParserResult {
                return ParserResult(id, title, img)
            }
            override fun onParserResult(result: ArrayList<ParserResult>) {

            }
        })
        parser.loadUrl("陈奕迅", 1)
    }

}