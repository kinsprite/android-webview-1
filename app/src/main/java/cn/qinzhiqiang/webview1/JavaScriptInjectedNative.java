package cn.qinzhiqiang.webview1;

import android.webkit.JavascriptInterface;

public class JavaScriptInjectedNative {
    private JsBridgeMsgHandler msgHandler;

    public void SetJsBridgeMsgHandler(JsBridgeMsgHandler handler) {
        msgHandler= handler;
    }

    @JavascriptInterface
    public String sendMessage(String msgId, String payload) {
        if (msgHandler != null) {
            return msgHandler.onJsBridgeMsg(msgId, payload);
        }
        return "Native processed: " + msgId + (payload != null ? ", " + payload : "");
    }
}
