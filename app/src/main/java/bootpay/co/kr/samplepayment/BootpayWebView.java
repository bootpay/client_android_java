package bootpay.co.kr.samplepayment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

import bootpay.co.kr.samplepayment.model.Item;
import bootpay.co.kr.samplepayment.model.Request;

public class BootpayWebView extends WebView {

    private static final String BOOTPAY = "https://dev-app.bootpay.co.kr";

    private ConnectivityManager connManager;

    private AlertDialog networkErrorDialog;

    private static final int ERROR = -2;

    private static final int CANCEL = -1;

    private static final int CONFIRM = 1;

    private static final int DONE = 2;

    private Request request = null;

    private Handler eventHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String data = msg.obj.toString();
            switch (msg.what) {
                case ERROR:
                    onErrorHandled(data);
                    break;
                case CANCEL:
                    onCancelHandled(data);
                    break;
                case CONFIRM:
                    onConfirmeHandled(data);
                    break;
                case DONE:
                    onDoneHandled(data);
                    break;
            }
        }
    };

    private EventListener listener;

    public BootpayWebView(Context context) {
        this(context, null);
    }

    public BootpayWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BootpayWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();

        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                request(price() +
                        application_id() +
                        name() +
                        pg() +
                        method() +
                        items() +
                        test_mode() +
                        "order_id: (new Date()).getTime(), " +
                        "params: {test: '테스트', var1: '가짜 데이터1'}, " +
                        "feedback_url: \"https://dev-api.bootpay.co.kr/callback\" " +
                        "}).error(function (data) { " +
                        "alert('에러다'); " +
                        "console.log(data); " +
                        "}).cancel(function (data) { " +
                        "alert('사용자 요청에 의해 취소 되었습니다.'); " +
                        "console.log(data); " +
                        "return true; " +
                        "}).done(function (data) { " +
                        "console.log(data);"
                );
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent intent = parse(url);
                if (isIntentOf(url)) {
                    if (isExistInfo(intent) || isExistPackage(intent)) start(intent);
                    else startMarket(intent);
                } else if (isMarketOf(url)) start(intent);
                return true;
            }
        });

        if (isNetworkConnected()) {
            setting(context);
            loadUrl(BOOTPAY);
        } else networkErrorDialog(context).show();
    }

    boolean back(Dialog dialog) {
        if (canGoBack()) {
            Log.d("Back", "Can go back");
            goBack();
        } else {
            dialog.onBackPressed();
            Log.d("Back", "Can't go back");
        }
        return true;
    }

    private boolean isIntentOf(String url) {
        return url != null && url.startsWith("intent://");
    }

    private boolean isMarketOf(String url) {
        return url != null && url.startsWith("market://");
    }

    private boolean isExistInfo(Intent intent) {
        try {
            return intent != null && getContext().getPackageManager().getPackageInfo(intent.getPackage(), PackageManager.GET_ACTIVITIES) != null;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private boolean isExistPackage(Intent intent) {
        return intent != null && getContext().getPackageManager().getLaunchIntentForPackage(intent.getPackage()) != null;
    }

    private Intent parse(String url) {
        try {
            return Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    private void start(Intent intent) {
        getContext().startActivity(intent);
    }

    private void startMarket(Intent intent) {
        Intent market = new Intent(Intent.ACTION_VIEW);
        market.setData(Uri.parse("market://details?id=" + intent.getPackage()));
        start(market);
    }

    private void request(String query) {
        load(String.format(Locale.getDefault(), "BootPay.request({ %s });", query));
    }

    private String price() {
        return String.format(Locale.getDefault(), "price: '%f', ", request.getPrice());
    }

    private String application_id() {
        return String.format(Locale.getDefault(), "application_id: '%s', ", request.getApplication_id());
    }

    private String name() {
        return String.format(Locale.getDefault(), "name: '%s', ", request.getName());
    }

    private String pg() {
        return String.format(Locale.getDefault(), "pg: '%s', ", request.getPg());
    }

    private String method() {
        return String.format(Locale.getDefault(), "method: '%s', ", request.getMethod());
    }

    private String test_mode() {
        return String.format(Locale.getDefault(), "test_mode: '%s', ", request.isTest_mode());
    }

    private String items() {
        StringBuilder builder = new StringBuilder().append("items: [ ");
        Locale locale = Locale.getDefault();
        List<Item> items = request.getItems();
        int size = items.size();
        if (size > 0) for (int i = 0; i < size; i++) {
            Item item = items.get(i);
            if (item != null) {
                builder.append("{ ");
                builder.append(String.format(locale, "item_name: '%s', ", item.getName()));
                builder.append(String.format(locale, "qty: %d, ", item.getQuantity()));
                builder.append(String.format(locale, "unique: '%s', ", item.getPrimaryKey()));
                builder.append(String.format(locale, "price: %f, ", item.getPrice()));
                if (i != size - 1) builder.append("}, ");
                else builder.append("} ");
            }
        }
        builder.append("], ");
        return builder.toString();
    }


    void setData(Request request) {
        this.request = request;
    }

    private AlertDialog networkErrorDialog(Context context) {
        if (networkErrorDialog == null) networkErrorDialog = new AlertDialog.Builder(context)
                .setMessage("인터넷 연결이 끊어져 있습니다. 인터넷 연결 상태를 확인해주세요.")
                .setPositiveButton("설정", (d, i) -> context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                .setNegativeButton("확인", (d, i) -> { /* Ignore */ })
                .create();
        return networkErrorDialog;
    }

    private boolean isNetworkConnected() {
        return connManager.getActiveNetworkInfo() != null;
    }

    private void load(String script) {
        loadUrl(String.format(Locale.getDefault(), "javascript:(function(){ %s })()", script));
    }

    @SuppressLint("setJavaScriptEnabled")
    private void setting(Context context) {
        setWebChromeClient(new Client());
        addJavascriptInterface(new AndroidBridge(), "Bootpay");
        CookieManager.getInstance().setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= 21)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
        WebSettings s = getSettings();
        s.setJavaScriptEnabled(true);
        s.setJavaScriptCanOpenWindowsAutomatically(true);
        s.setDomStorageEnabled(true);
        s.setSupportMultipleWindows(true);
        s.setLoadsImagesAutomatically(true);
        s.setBuiltInZoomControls(true);
        s.supportZoom();
        s.setUseWideViewPort(true);
        s.setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            if (0 != (context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE))
                WebView.setWebContentsDebuggingEnabled(true);

    }

    void setOnResponseListener(EventListener listener) {
        this.listener = listener;
    }

    private class AndroidBridge implements IScriptFuction {

        @JavascriptInterface
        @Override
        public void error(String data) {
            eventHandler.sendMessage(Message.obtain(eventHandler, ERROR, data));
        }

        @JavascriptInterface
        @Override
        public void cancel(String data) {
            eventHandler.sendMessage(Message.obtain(eventHandler, CANCEL, data));
        }

        @JavascriptInterface
        @Override
        public void confirm(String data) {
            eventHandler.sendMessage(Message.obtain(eventHandler, CONFIRM, data));
        }

        @JavascriptInterface
        @Override
        public void done(String data) {
            eventHandler.sendMessage(Message.obtain(eventHandler, DONE, data));
        }
    }

    private void onErrorHandled(String data) {
        if (listener != null) listener.onError(new Exception(data));
    }

    private void onCancelHandled(String data) {
        if (listener != null) listener.onCancel(data);
    }

    private void onConfirmeHandled(String data) {
        if (listener != null) listener.onConfirmed(data);
    }

    private void onDoneHandled(String data) {
        if (listener != null) listener.onDone(data);
    }

    private class Client extends WebChromeClient {

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            WebView.HitTestResult result = view.getHitTestResult();
            String url = result.getExtra();

            if (isNewWindow(url)) {
                start(view, createFrom(url));
            } else if (resultMsg != null) {
                WebView newWindow = new WebView(view.getContext());
                addView(newWindow);
                WebView.WebViewTransport tr = (WebView.WebViewTransport) resultMsg.obj;
                tr.setWebView(newWindow);
                resultMsg.sendToTarget();
            }
            return true;
        }

        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
            removeView(window);
            window.setVisibility(View.GONE);

        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            new AlertDialog.Builder(view.getContext())
                    .setMessage(message)
                    .setCancelable(true)
                    .create()
                    .show();
            result.confirm();
            return true;
        }

        private void start(View view, Intent intent) {
            view.getContext().startActivity(intent);
        }

        private boolean isNewWindow(String url) {
            return url != null && !url.contains("___target=_blank");
        }

        private Intent createFrom(String url) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            return intent;
        }
    }
}
