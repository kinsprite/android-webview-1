package cn.qinzhiqiang.webview1;

public class Util {
    static public String toJsString(String input) {
        if (input == null) {
            return  "null";
        }

        return "\"" + input.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }
}
