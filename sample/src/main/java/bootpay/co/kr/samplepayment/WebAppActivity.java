package bootpay.co.kr.samplepayment;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WebAppActivity extends Activity implements WebAppBridgeInterface {
    WebView webview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webapp);
        webview = findViewById(R.id.webview);
        webview.setWebViewClient(new BWebviewClient());
        webview.setWebChromeClient(new BChromeClient());
        webview.addJavascriptInterface(new WebAppBridge(this), "Android");
        CookieManager.getInstance().setAcceptCookie(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.loadUrl("file:///android_asset/html/index.html");
    }

    void setDevice() {
        doJavascript("BootPay.setDevice('ANDROID');");
    }

    void startTrace() {
        doJavascript("BootPay.startTrace();");
    }

    void doJavascript(String script) {
        final String str = script;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webview.loadUrl("javascript:(function(){" + str + "})()");
            }
        });
    }

    @Override
    public void error(String data) {
        System.out.println(data);
    }

    @Override
    public void close(String data) {
        System.out.println(data);
    }

    @Override
    public void cancel(String data) {
        System.out.println(data);
    }

    @Override
    public void ready(String data) {
        System.out.println(data);
    }

    @Override
    public void confirm(String data) {
        boolean iWantPay = true;
        if(iWantPay == true) {
            doJavascript("BootPay.transactionConfirm( " + data + ");");
        } else {
            doJavascript("BootPay.removePaymentWindow();");
        }
    }

    @Override
    public void done(String data) {
        System.out.println(data);
    }

    private class BWebviewClient extends WebViewClient {
        private boolean isLoaded = false;

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(isLoaded) return;
            isLoaded = true;
            setDevice();
            startTrace();
        }
    }


    private class BChromeClient extends WebChromeClient {
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }

        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            new AlertDialog.Builder(view.getContext())
                    .setMessage(message)
                    .setCancelable(true)
                    .create()
                    .show();
            return true;
        }
    }
}
