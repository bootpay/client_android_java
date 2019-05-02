package bootpay.co.kr.samplepayment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.net.URISyntaxException;

import kr.co.bootpay.Bootpay;


public class WebAppActivity extends Activity implements WebAppBridgeInterface {
    WebView webview;
    final String url = "https://test-shop.bootpay.co.kr";



//    final String android_application_id = "59a4d4a1929b3f3b8b6422c8"; //dev
    final String android_application_id = "5b14c0ffb6d49c40cda92c4e";


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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager.getInstance().setAcceptThirdPartyCookies(webview, true);
        }

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        webview.loadUrl(url);
    }

    void setDevice() {
        doJavascript("BootPay.setDevice('ANDROID');");
    }

    void startTrace() {
        doJavascript("BootPay.startTrace();");
    }

    void registerAppId() {
        doJavascript("BootPay.setApplicationId('" + android_application_id + "');");
    }

    void registerAppIdDemo() {
        doJavascript("window.setApplicationId('" + android_application_id + "');");
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
        if(iWantPay == true) { // 재고가 있을 경우
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
            registerAppId();
            setDevice();
            startTrace();
            registerAppIdDemo();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Intent intent = parse(url);
            if (isIntent(url)) {
                if (isExistInfo(intent, view.getContext()) || isExistPackage(intent, view.getContext()))
                    return start(intent, view.getContext());
                else
                    return gotoMarket(intent, view.getContext());
            } else if (isMarket(url)) {
                if (!(isExistInfo(intent, view.getContext()) || isExistPackage(intent, view.getContext())))
                    return gotoMarket(intent, view.getContext());
                else
                    return true;
            } else if (isSpecialCase(url)) {
                if (isExistInfo(intent, view.getContext()) || isExistPackage(intent, view.getContext()))
                    return start(intent, view.getContext());
                else
                    return gotoMarket(intent, view.getContext());
            }
            return url.contains("https://bootpaymark");
        }

        private Boolean isSpecialCase(String url) {
            return url.matches("^shinhan\\S+$")
                    || url.startsWith("kftc-bankpay://")
                    || url.startsWith("v3mobileplusweb://")
                    || url.startsWith("hdcardappcardansimclick://")
                    || url.startsWith("mpocket.online.ansimclick://");
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
            return intent != null &&  intent.getPackage() != null && context.getPackageManager().getLaunchIntentForPackage(intent.getPackage()) != null;
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
