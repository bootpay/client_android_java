package bootpay.co.kr.samplepayment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URISyntaxException;


public class LocalHtmlActivity extends Activity implements WebAppBridgeInterface {
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
        public static final String INTENT_PROTOCOL_START = "intent:";
        public static final String INTENT_PROTOCOL_INTENT = "#Intent;";
        public static final String INTENT_PROTOCOL_END = ";end;";
        public static final String GOOGLE_PLAY_STORE_PREFIX = "market://details?id=";

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(isLoaded) return;
            isLoaded = true;
            setDevice();
            startTrace();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // market 일 경우 있으면 실행하고, 없으면 다운받게
            if (url.startsWith(GOOGLE_PLAY_STORE_PREFIX) || url.startsWith(INTENT_PROTOCOL_START)) {
                final int endIndex = url.indexOf(INTENT_PROTOCOL_END);
                try {
                    Intent intent = new Intent().parseUri(url, Intent.URI_INTENT_SCHEME);
                    final int s = url.indexOf(INTENT_PROTOCOL_INTENT) + INTENT_PROTOCOL_INTENT.length();

                    PackageManager packageManager = view.getContext().getPackageManager();
                    ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    if (info != null) {
                        view.getContext().startActivity(intent);
                    } else {
                        final String packageName = url.substring(s, endIndex < 0 ? url.length() : endIndex);
                        view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_STORE_PREFIX + packageName)));
                    }
                    return true;
                } catch (ActivityNotFoundException e) {
                    final int s = url.indexOf(INTENT_PROTOCOL_INTENT) + INTENT_PROTOCOL_INTENT.length();
                    final String packageName = url.substring(s, endIndex < 0 ? url.length() : endIndex);
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_STORE_PREFIX + packageName)));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
            return true;
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
