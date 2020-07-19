package cn.qinzhiqiang.webview1;

        import android.webkit.JavascriptInterface;

public class JavaScriptInjectedNative {

    @JavascriptInterface
    public String sendMessage(String msgId, String payload) {
        return "Native processed: " + msgId + (payload != null ? ", " + payload : "");
    }
}
