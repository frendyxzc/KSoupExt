package vip.frendy.ksoupdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import vip.frendy.ksoup.KSoup;
import vip.frendy.ksoup.interfaces.IKSoupListener;
import vip.frendy.ksoup.interfaces.JSInterface;

/**
 * Created by frendy on 2018/2/6.
 */

public class MainActivity extends AppCompatActivity {

    public static String URL_VIDEO_LIST = "https://www.youtube.com/results?sp=EgIQAVAU&search_query=谢霆锋&p=1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final KSoup soup = new KSoup(this);
        soup.addJavascriptInterface(new IKSoupListener() {
            @Override
            public void onInnerHtmlLoaded(Integer action, String data) {
                if(action == JSInterface.Companion.getLOAD_INNER_HTML()) {
                    Elements elements = soup.parseAList(data);
                    if(elements == null) return;

                    for(Element element : elements) {
                        String href = element.attr("href");
                        String label = element.attr("aria-label").split(" - ")[0];

                        if(href.contains("/watch?v")) {
                            String id = href.replace("/watch?v=", "");

                            Log.i("jsoup", "** id = " + id + ", label = " + label);
                        }
                    }
                }
            }
        });
        soup.loadUrl(URL_VIDEO_LIST);
    }
}
