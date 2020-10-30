package bootpay.co.kr.samplepayment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

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
            Intent intent = parse(url);
            if (isIntent(url)) {
                if (isExistInfo(intent, view.getContext()) || isExistPackage(intent, view.getContext()))
                    return start(intent, view.getContext());
                else
                    gotoMarket(intent, view.getContext());
            } else if (isMarket(url)) {
                return start(intent, view.getContext());
            }
            return url.contains("https://bootpaymark");
        }

        private Intent parse(String url) {
            try {
                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                if(intent.getPackage() == null) {
                    if (url == null) return intent;
                    if (url.startsWith("shinhan-sr-ansimclick")) intent.setPackage("com.shcard.smartpay");
                    else if (url.startsWith("kftc-bankpay")) intent.setPackage("com.kftc.bankpay");
                    else if (url.startsWith("ispmobile")) intent.setPackage("kvp.jjy.MispAndroid320");
                    else if (url.startsWith("hdcardappcardansimclick")) intent.setPackage("com.hyundaicard.appcard");
                    else if (url.startsWith("kb-acp")) intent.setPackage("com.kbcard.kbkookmincard");
                    else if (url.startsWith("mpocket.online.ansimclick")) intent.setPackage("kr.co.samsungcard.mpocket");
                    else if (url.startsWith("lotteappcard")) intent.setPackage("com.lcacApp");
                    else if (url.startsWith("cloudpay")) intent.setPackage("com.hanaskcard.paycla");
                    else if (url.startsWith("nhappvardansimclick")) intent.setPackage("nh.smart.nhallonepay");
                    else if (url.startsWith("citispay")) intent.setPackage("kr.co.citibank.citimobile");
                    else if (url.startsWith("kakaotalk")) intent.setPackage("com.kakao.talk");
                }
                return intent;
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return null;
            }
        }

        private Boolean isIntent(String url) {
            return url.matches("^intent:?\\w*://\\S+$");
        }

        private Boolean isMarket(String url) {
            return url.matches("^market://\\S+$");
        }

        private Boolean isExistInfo(Intent intent, Context context) {
            try {
                return intent != null && context.getPackageManager().getPackageInfo(intent.getPackage(), PackageManager.GET_ACTIVITIES) != null;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }

        private Boolean isExistPackage(Intent intent, Context context) {
            return intent != null && context.getPackageManager().getLaunchIntentForPackage(intent.getPackage()) != null;
        }

        private boolean start(Intent intent, Context context) {
            context.startActivity(intent);
            return true;
        }

        private boolean gotoMarket(Intent intent, Context context) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + intent.getPackage())));
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
