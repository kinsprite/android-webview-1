package cn.qinzhiqiang.webview1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements JsBridgeMsgHandler {
    public static final int TACK_PIC_REQ_CODE = 1101;

    private WebView webView;
    private long exitTime = 0;
    private Timer tickTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWebView();
        // webView.loadUrl("https://micro.qinzhiqiang.cn");
        webView.loadUrl("file:///android_asset/index.html");

        startTick();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeTick();
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

        JavaScriptInjectedNative injectedNative = new JavaScriptInjectedNative();
        injectedNative.SetJsBridgeMsgHandler(this);
        webView.addJavascriptInterface(injectedNative, "injectedNative");
    }


    private void sendMessageToWeb(String msgId, String payload) {
        String jsContent = "if (this.nativeMessageHandler) { this.nativeMessageHandler("
                + Util.toJsString(msgId) + "," + Util.toJsString(payload) + ")}";
        Log.i("MsgToWeb", "javascript:" + jsContent);

        webView.post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= 19 /*Might need 21*/) {
                    webView.evaluateJavascript("javascript:" + jsContent, null);
                }else {
                    webView.loadUrl("javascript:" + jsContent);
                }
            }
        });

        // webView.loadUrl("javascript:" + jsContent);
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

    @Override
    public String onJsBridgeMsg(String msgId, String payload) {
        if (msgId == null) {
            return "Empty msg id";
        }

        switch (msgId) {
            case "toast_show":
                toastShow(payload);
                break;
            case "camera_open":
                cameraOpen();
                break;
            case "vibrator_notify":
                vibratorNotify();
                break;
            case "event_round":
                toastShow("Event round 1");
                sendMessageToWeb("event_round_back", "");
                break;
            default:
                break;
        }

        return "Message done by Main.";
    }

    private void toastShow(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        // toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL , 0, 0);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        // v.setTextColor(Color.WHITE);
        toast.show();
    }
    
    private boolean checkPermissions(String[] permissions) {
        for (String p : permissions) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    private void requestPermissions(String[] permissions) {
        if (!checkPermissions(permissions)) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }

    private void cameraOpen() {
        String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
        };

        requestPermissions(permissions);

//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, TACK_PIC_REQ_CODE);
//        return;

        File dir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (dir.exists()){
            dir.mkdirs();
        }

        File currentImageFile = new File(dir,System.currentTimeMillis() + ".jpg");

        if (!currentImageFile.exists()){
            try {
                currentImageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(it, TACK_PIC_REQ_CODE);
    }

    private void vibratorNotify() {
        String[] permissions = new String[]{
                Manifest.permission.VIBRATE,
        };

        requestPermissions(permissions);
        Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);

        if (!vibrator.hasVibrator()) {
            toastShow("No vibrator");
            return;
        }

        vibrator.cancel();
        vibrator.vibrate(new long[]{100, 200, 100, 200}, -1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TACK_PIC_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                Log.i("CAMERA", "OK");
            } else {
                Log.i("CAMERA", "Error");
            }
        }
    }

    private void startTick() {
        tickTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                sendMessageToWeb("native_tick", "");
            }
        };

        tickTimer.schedule(task,1000,1000);
    }

    private void closeTick() {
        tickTimer.cancel();
    }
}