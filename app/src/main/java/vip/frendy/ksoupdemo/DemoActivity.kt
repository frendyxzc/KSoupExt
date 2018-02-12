package vip.frendy.ksoupdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by frendy on 2018/2/6.
 */
class DemoActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //未修改UserAgent, 分页请求无效, 页面较规整
        val parser1 = YTSearchParser<ParserResult>(this)
        parser1.setParserListener(object : YTSearchParser.IParserListener<ParserResult> {
            override fun onParserResultWrap(id: String, title: String, img: String): ParserResult {
                return ParserResult(id, title, img)
            }
            override fun onParserResult(result: ArrayList<ParserResult>) {
                Log.i("ksoup", "** ===== data size ==== ${result.size}")
            }
        })

        //修改UserAgent, 分页请求正常，页面不规整，解析还须完善
        val parser2 = YTSearchParser2<ParserResult>(this)
        parser2.setParserListener(object : YTSearchParser2.IParserListener<ParserResult> {
            override fun getId(data: ParserResult): String {
                return data.id
            }
            override fun onParserResultWrap(id: String, title: String, img: String): ParserResult {
                return ParserResult(id, title, img)
            }
            override fun onParserResultUpdate(data: ParserResult, id: String, title: String, img: String) {
                data.title = if(title.equals("")) data.title else title
                data.img = if(img.equals("")) data.img else img
            }
            override fun onParserResult(result: ArrayList<ParserResult>) {
                for(ret in result) {
                    Log.i("ksoup", "** id = ${ret.id}, title = ${ret.title}， img = ${ret.img}")
                }
                Log.i("ksoup", "** ===== data size ==== ${result.size}")
            }
        })

        var page = 1
        go.setOnClickListener {
            val key = editText.text.toString()
            parser2.loadUrl(key, page++)
        }
    }

}