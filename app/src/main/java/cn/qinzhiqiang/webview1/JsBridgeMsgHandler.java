package cn.qinzhiqiang.webview1;

public interface JsBridgeMsgHandler {
    String onJsBridgeMsg(String msgId, String payload);
}
