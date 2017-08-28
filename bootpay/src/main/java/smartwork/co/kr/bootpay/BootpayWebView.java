package smartwork.co.kr.bootpay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebMessage;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

import smartwork.co.kr.bootpay.model.Item;
import smartwork.co.kr.bootpay.model.Request;

final class BootpayWebView extends WebView {


    private static final String BOOTPAY = "https://dev-inapp.bootpay.co.kr/development.html";

    private ConnectivityManager connManager;

    private AlertDialog networkErrorDialog;

    private static final int ERROR = -2;

    private static final int CANCEL = -1;

    private static final int CONFIRM = 1;

    private static final int DONE = 2;

    private Request request = null;

    private Locale locale = Locale.getDefault();

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

    private Dialog dialog;

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
        if (isNetworkConnected()) {
            setting(context);

            setWebViewClient(new WebViewClient() {

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    setDevice();
                    load(
                            request(
                                    price(),
                                    application_id(),
                                    name(),
                                    pg(),
                                    method(),
                                    items(),
                                    test_mode(),
                                    params(),
                                    order_id()
                            ),
                            error(),
                            cancel(),
                            confirm(),
                            done()
                    );
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Intent intent = parse(url);
                    if (isIntentOf(url)) {
                        if (isExistInfo(intent) || isExistPackage(intent)) return start(intent);
                        else return startMarket(intent);
                    } else if (isMarketOf(url)) return start(intent);
                    else return url.contains("vguardend");
                }

                @Override
                public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) back();
                    return super.shouldOverrideKeyEvent(view, event);
                }

            });

            loadUrl(BOOTPAY);
        } else networkErrorDialog(context).show();
    }

    public BootpayWebView setDialog(Dialog dialog) {
        this.dialog = dialog;
        return this;
    }

    boolean back() {
        if (canGoBack()) goBack();
        else dialog.dismiss();
        return true;
    }

    private boolean isIntentOf(@Nullable String url) {
        return url != null && url.startsWith("intent://");
    }

    private boolean isMarketOf(@Nullable String url) {
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

    private boolean start(Intent intent) {
        getContext().startActivity(intent);
        return true;
    }

    private void setDevice() {
        load("window.BootPay.setDevice('ANDROID');");
    }

    private boolean startMarket(Intent intent) {
        Intent market = new Intent(Intent.ACTION_VIEW);
        market.setData(Uri.parse("market://details?id=" + intent.getPackage()));
        start(market);
        return true;
    }

    private String request(String... query) {
        StringBuilder builder = new StringBuilder();
        builder.append("BootPay.request({");
        for (String s : query) builder.append(s).append(",");
        builder.setLength(builder.length() - 1);
        builder.append("})");
        return builder.toString();
    }

    private String error() {
        return ".error(function(data){Android.error(JSON.stringify(data));})";
    }

    private String confirm() {
        return ".confirm(function(data){Android.confirm(JSON.stringify(data));})";
    }

    private String cancel() {
        return ".cancel(function(data){Android.cancel(JSON.stringify(data));})";
    }

    private String done() {
        return ".done(function(data){Android.done(JSON.stringify(data));})";
    }

    private String price() {
        return "price:" + request.getPrice();
    }

    private String application_id() {
        return String.format(locale, "application_id:'%s'", request.getApplication_id());
    }

    private String name() {
        return String.format(locale, "name:'%s'", request.getName());
    }

    private String pg() {
        return String.format(locale, "pg:'%s'", request.getPg());
    }

    private String method() {
        return String.format(locale, "method:'%s'", request.getMethod());
    }

    private String test_mode() {
        return String.format(locale, "test_mode:%s", request.isTest_mode());
    }

    private String params() {
        return String.format(locale, "params:JSON.parse('%s')", request.getParams());
    }

    public void transactionConfirm(String data) {
        load("var data = JSON.parse('" + data + "'); BootPay.transactionConfirm(data);");
    }

    private String items() {
        StringBuilder builder = new StringBuilder().append("items:[");
        List<Item> items = request.getItems();
        int size = items.size();
        if (size > 0) for (int i = 0; i < size; i++) {
            Item item = items.get(i);
            if (item != null) {
                builder.append("{");
                builder.append(String.format(locale, "item_name:'%s',", item.getName()));
                builder.append(String.format(locale, "qty:%d,", item.getQuantity()));
                builder.append(String.format(locale, "unique:'%s',", item.getPrimaryKey()));
                builder.append("price: ").append(item.getPrice());
                if (i != size - 1) builder.append("},");
                else builder.append("}");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    private String order_id() {
        return String.format(locale, "order_id:'%s'", request.getOrderId());
    }

    public BootpayWebView setData(Request request) {
        this.request = request;
        return this;
    }

    private AlertDialog networkErrorDialog(final Context context) {
        if (networkErrorDialog == null) networkErrorDialog = new AlertDialog.Builder(context)
                .setMessage("인터넷 연결이 끊어져 있습니다. 인터넷 연결 상태를 확인해주세요.")
                .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                }).setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        return networkErrorDialog;
    }

    private boolean isNetworkConnected() {
        return connManager.getActiveNetworkInfo() != null;
    }

    private void load(String script) {
        loadUrl(String.format(locale, "javascript:(function(){%s})()", script));
    }

    private void load(String... script) {
        StringBuilder builder = new StringBuilder();
        for (String s : script) builder.append(s);
        builder.append(";");
        String request = builder.toString();
        Log.d("request", request);
        load(request);
    }

    @SuppressLint("setJavaScriptEnabled")
    private void setting(Context context) {
        setWebChromeClient(new Client());
        addJavascriptInterface(new AndroidBridge(), "Android");
        CookieManager.getInstance().setAcceptCookie(true);
        WebSettings s = getSettings();
        if (Build.VERSION.SDK_INT >= 21) {
            s.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
        }
        s.setJavaScriptEnabled(true);
        s.setJavaScriptCanOpenWindowsAutomatically(true);
        s.setDomStorageEnabled(true);
        s.setSupportMultipleWindows(true);
        s.setLoadsImagesAutomatically(true);
        s.setBuiltInZoomControls(true);
        s.supportZoom();
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            if (0 != (context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE))
                WebView.setWebContentsDebuggingEnabled(true);
    }

    @Override
    public void postWebMessage(WebMessage message, Uri targetOrigin) {
        Log.d("PostWebMessage", message.toString());
        super.postWebMessage(message, targetOrigin);
    }

    @Override
    public void postUrl(String url, byte[] postData) {
        Log.d("Post", url);
        super.postUrl(url, postData);
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
        if (listener != null) listener.onError(data);
        if (dialog != null) dialog.dismiss();
    }

    private void onCancelHandled(String data) {
        if (listener != null) listener.onCancel(data);
//        if (dialog != null) dialog.dismiss();
    }

    private void onConfirmeHandled(String data) {
        if (listener != null && listener.onConfirmed(data)) transactionConfirm(data);
    }

    private void onDoneHandled(String data) {
        if (listener != null) listener.onDone(data);
        if (dialog != null) dialog.dismiss();
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
            AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                    .setMessage(message)
                    .setCancelable(true)
                    .create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
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
