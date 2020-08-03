package cn.qinzhiqiang.webview1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWebView();
        // webView.loadUrl("https://micro.qinzhiqiang.cn");
        webView.loadUrl("file:///android_asset/index.html");
    }

    private void initWebView() {
        webView = (WebView)findViewById(R.id.blog_webview);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDefaultFontSize(16);

        //设置缓存模式
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        settings.setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            //在webview里打开新链接，否则会使用系统中浏览器打开新链接
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.addJavascriptInterface(new JavaScriptInjectedNative(), "injectedNative");
    }


    private void sendMessageToWeb(String msgId, String payload) {
        String jsContent = "if (this.nativeMessageHandler) { this.nativeMessageHandler("
                + Util.toJsString(msgId) + "," + Util.toJsString(payload) + ")}";
        webView.loadUrl("javascript:" + jsContent);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
    }
}