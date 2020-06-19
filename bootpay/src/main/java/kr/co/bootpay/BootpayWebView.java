package kr.co.bootpay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

import kr.co.bootpay.listener.EventListener;
import kr.co.bootpay.model.Item;
import kr.co.bootpay.model.Request;
import kr.co.bootpay.pref.UserInfo;

public class BootpayWebView extends WebView {
private static final String BOOTPAY = "https://inapp.bootpay.co.kr/3.2.5/production.html";

    private Dialog dialog;
//    private ConnectivityManager connManager;

//    private AlertDialog networkErrorDialog;

    private static final int CLOSE = -3;
    private static final int ERROR = -2;
    private static final int CANCEL = -1;
    private static final int READY = 0;
    private static final int CONFIRM = 1;
    private static final int DONE = 2;

    private Request request = null;

//    private Locale locale = Locale.getDefault();

    private boolean isLoaded = false;

    private Handler eventHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String data = msg.obj.toString();
            switch (msg.what) {
                case CLOSE:
                    onCloseHandled(data);
                    break;
                case ERROR:
                    onErrorHandled(data);
                    break;
                case CANCEL:
                    onCancelHandled(data);
                    break;
                case CONFIRM:
                    onConfirmeHandled(data);
                    break;
                case READY:
                    onReadyHandled(data);
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

//    public BootpayWebView(Context context, Request request) {
//        this(context, null);
//    }

    public BootpayWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BootpayWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        setFocusable(true);
        setFocusableInTouchMode(true);

        requestFocus();
        setting(context);

        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (request == null) return;
//                if ("about:blank".equals(url)) {
//                    loadUrl(BOOTPAY);
//                    return;
//                }
                if (!isLoaded) {
                    isLoaded = true;
                    setDevice();
                    setAnalyticsData();

//                    setDevelopMode();
//                    useOneStoreApi();

//                    Log.d("bootpay", "onPageFinished");

                    loadParams(
                            request(
                                    price(),
                                    easyPayUserToken(),
                                    applicationId(),
                                    name(),
                                    pg(),
//                                    userPhone(),
                                    agree(),
                                    method(),
                                    methods(),
                                    items(),
                                    params(),
                                    accountExpireAt(),
                                    orderId(),
                                    useOrderId(),
                                    userJson(),
                                    extraJson()
                            ),
                            error(),
                            cancel(),
                            ready(),
                            confirm(),
                            close(),
                            done()
                    );
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent intent = parse(url);

//                Log.d("bootpay url", url);

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
                return url.contains("vguardend");
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
                        else if (url.startsWith("kftc-bankpay")) intent.setPackage("com.kftc.bankpay.android");
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
//                return url.matches("^intent:?\\w*://\\S+$");
                return url.startsWith("intent:");
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
                final String appPackageName = intent.getPackage();
                if(appPackageName == null) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, intent.getData()));
                    return true;
                }
                try {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                return true;
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) back();
                return super.shouldOverrideKeyEvent(view, event);
            }

        });

        loadUrl(BOOTPAY);
    }

    public void transactionConfirm(String data) {
        load("var data = JSON.parse('" + data + "'); BootPay.transactionConfirm(data);");
    }

    public BootpayWebView setRequest(Request request) {
        this.request = request;
        return this;
    }

    public String removePaymentWindow() {
        return "window.BootPay.removePaymentWindow();)";
    }

    public BootpayWebView setDialog(Dialog dialog) {
        this.dialog = dialog;
        return this;
    }

    boolean back() {
        if (canGoBack()) goBack();
        else if(dialog != null) dialog.dismiss();
        return true;
    }


    private boolean start(Intent intent) {
        getContext().startActivity(intent);
        return true;
    }

    private void setAnalyticsData() {
        String data = String.format(Locale.KOREA,
                "window.BootPay.setAnalyticsData({uuid:'%s',sk:'%s',sk_time:'%d',time:'%d'});"
                , UserInfo.getInstance(this.getContext()).getBootpayUuid()
                , UserInfo.getInstance(this.getContext()).getBootpaySk()
                , UserInfo.getInstance(this.getContext()).getBootpayLastTime()
                , System.currentTimeMillis() - UserInfo.getInstance(this.getContext()).getBootpayLastTime());
        load(data);
    }

    private void setDevice() {
        load("window.BootPay.setDevice('ANDROID');");
    }

    private void setDevelopMode() {
        load("window.BootPay.setMode('development');");
    }


//    private void useOneStoreApi() {
//        if(request != null && "onestore".equals(request.getPG())) {

//        }
//        if(UserInfo.getInstance(this.getContext()).getEnableOneStore()) {
//            load(String.format(Locale.KOREA,
//                    "window.BootPay.useOnestoreApi({ad_id: '%s', sim_operation: '%s', installerPackageName: '%s'});"
//                    , UserInfo.getInstance(this.getContext()).getAdId()
//                    , UserInfo.getInstance(this.getContext()).getSimOperator()
//                    , UserInfo.getInstance(this.getContext()).getInstallPackageMarket()));
//        }
//    }

    private boolean startMarket(Intent intent) {
        Intent market = new Intent(Intent.ACTION_VIEW);
        market.setData(Uri.parse("market://details?id=" + intent.getPackage()));
        start(market);
        return true;
    }

    private String request(String... query) {
        StringBuilder builder = new StringBuilder();
        builder.append("BootPay.request({");
        for (String s : query) {
            if(s.length() > 0) builder.append(s).append(",");
        }
        builder.setLength(builder.length() - 1);
        builder.append("})");
        return builder.toString();
    }

    private String error() { return ".error(function(data){Android.error(JSON.stringify(data));})"; }

    private String ready() { return ".ready(function(data){Android.ready(JSON.stringify(data));})"; }

    private String close() { return ".close(function(data){ Android.close('close') ;})"; }

    private String confirm() { return ".confirm(function(data){Android.confirm(JSON.stringify(data));})"; }

    private String cancel() { return ".cancel(function(data){Android.cancel(JSON.stringify(data));})"; }

    private String done() {
        return ".done(function(data){Android.done(JSON.stringify(data));})";
    }

    private String price() {
        return "price:" + request.getPrice();
    }

    private String easyPayUserToken() {
        if(request.getEasyPayUserToken() == null || "null".equals(request.getEasyPayUserToken())) return "";
        return String.format(Locale.KOREA, "user_token:'%s'", request.getEasyPayUserToken());

    }

    private String applicationId() { return String.format(Locale.KOREA, "application_id:'%s'", request.getApplicationId()); }

    private String name() {
        return String.format(Locale.KOREA, "name:'%s'", request.getName().replace("\"", "'").replace("'", "\\'"));
    }

    private String pg() { return String.format(Locale.KOREA, "pg: '%s'", request.getPG()); }

    private String agree() { return String.format(Locale.KOREA, "show_agree_window: %d", request.getIsShowAgree() == true ? 1 : 0); }

    private String method() {
        return String.format(Locale.KOREA, "method:'%s'", request.getMethod());
    }

    private String methods() { return String.format(Locale.KOREA, "methods:[%s]", listToString(request.getMethods())); }

    private String items() {
        StringBuilder builder = new StringBuilder().append("items:[");
        List<Item> items = request.getItems();
        int size = items.size();
        if (size > 0) for (int i = 0; i < size; i++) {
            Item item = items.get(i);
            if (item != null) {
                builder.append("{");
                builder.append(String.format(Locale.KOREA, "item_name:'%s',", item.getItemName().replace("\"", "'").replace("'", "\\'")));
                builder.append(String.format(Locale.KOREA, "qty:%d,", item.getQty()));
                builder.append(String.format(Locale.KOREA, "unique:'%s',", item.getUnique()));
                builder.append(String.format(Locale.KOREA, "price:'%f',", item.getPrice()));
                builder.append(String.format(Locale.KOREA, "cat1:'%s',", item.getCat1().replace("\"", "'").replace("'", "\\'")));
                builder.append(String.format(Locale.KOREA, "cat2:'%s',", item.getCat2().replace("\"", "'").replace("'", "\\'")));
                builder.append(String.format(Locale.KOREA, "cat3:'%s',", item.getCat3().replace("\"", "'").replace("'", "\\'")));
                if (i != size - 1) builder.append("},");
                else builder.append("}");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    private String orderId() { return String.format(Locale.KOREA, "order_id:'%s'", request.getOrderId()); }

    private String useOrderId() { return String.format(Locale.KOREA, "use_order_id:%d", request.getUseOrderId() == true ? 1 : 0); }

    private String userJson() {
        if(request.getBoot_user() == null) return "";
        return String.format(Locale.KOREA, "user_info: %s", request.getBoot_user().toJson());
    }

    private String extraJson() {
        if(request.getBootExtra(getContext()) == null) return "";
        if(request.getBootExtra(getContext()).toJson().length() == 0) return "";
        return String.format(Locale.KOREA, "extra: %s", request.getBoot_extra().toJson());
    }

    private String listToString(List<String> array) {
        StringBuilder builder = new StringBuilder();
        for(String str : array) {
            if(builder.toString().length() > 0) builder.append(",");
            builder.append(String.format(Locale.KOREA, "'%s'", str));
        }
        return String.format(Locale.KOREA,"%s", builder.toString());
    }

    private String accountExpireAt() {
        return String.format(Locale.KOREA, "account_expire_at:'%s'", request.getAccountExpireAt());
    }

    private String params() {
        if (isNullOrEmpty(request.getParams())) return "params:''";
        else return String.format(Locale.KOREA, "params:JSON.parse('%s')", request.getParams());
    }

    private void registerAppId() {
        load(String.format(Locale.KOREA,  "window.BootPay.setApplicationId('%s');", request.getApplicationId()));
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().equals("");
    }



//    private AlertDialog networkErrorDialog(final Context context) {
//        if (networkErrorDialog == null) networkErrorDialog = new AlertDialog.Builder(context)
//                .setMessage("인터넷 연결이 끊어져 있습니다. 인터넷 연결 상태를 확인해주세요.")
//                .setPositiveButton("설정", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//                    }
//                }).setNegativeButton("확인", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                }).create();
//        return networkErrorDialog;
//    }
//
//    private boolean isNetworkConnected() {
//        return connManager.getActiveNetworkInfo() != null;
//    }

    private void load(String script) {
        loadUrl(String.format(Locale.KOREA, "javascript:(function(){%s})()", script));
    }

    private void loadParams(String... script) {
        StringBuilder builder = new StringBuilder();
        for (String s : script) builder.append(s);
        builder.append(";");
        String request = builder.toString();

//        Log.d("bootpay request -----", request);

        load(request);
    }

    @SuppressLint("setJavaScriptEnabled")
    private void setting(Context context) {
        setWebChromeClient(new Client());
//        if(javascriptInterfaceObject == null) addJavascriptInterface(new AndroidBridge(), "Android");
        addJavascriptInterface(new AndroidBridge(), "Android");
        CookieManager.getInstance().setAcceptCookie(true);
        WebSettings s = getSettings();
        if (Build.VERSION.SDK_INT >= 21) {
//            s.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
            s.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager.getInstance().setAcceptCookie(true);
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
        }
        s.setJavaScriptEnabled(true);
        s.setJavaScriptCanOpenWindowsAutomatically(true);
        s.setDomStorageEnabled(true);
        s.setSupportMultipleWindows(true);
        s.setLoadsImagesAutomatically(true);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            context.getApplicationInfo().flags &=  context.getApplicationInfo().FLAG_DEBUGGABLE;
            if (0 != context.getApplicationInfo().flags)
                WebView.setWebContentsDebuggingEnabled(true);
        }
    }

//    @Override
//    public void postWebMessage(WebMessage message, Uri targetOrigin) {
//        Log.d("PostWebMessage", message.toString());
//        super.postWebMessage(message, targetOrigin);
//    }

    @Override
    public void postUrl(String url, byte[] postData) {
//        Log.d("Post", url);
        super.postUrl(url, postData);
    }

    public void setOnResponseListener(EventListener listener) {
        this.listener = listener;
    }

    private class AndroidBridge implements IScriptFunction {

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

        @JavascriptInterface
        @Override
        public void close(String data) {
            eventHandler.sendMessage(Message.obtain(eventHandler, CLOSE, data));
        }

        @JavascriptInterface
        @Override
        public void ready(String data) {
            eventHandler.sendMessage(Message.obtain(eventHandler, READY, data));
        }
    }

    private void onCloseHandled(String data) {
        if (listener != null) listener.onClose(data);
        if (dialog != null) dialog.dismiss();
    }

    private void onErrorHandled(String data) {
        if (listener != null) listener.onError(data);
    }

    private void onCancelHandled(String data) {
        if (listener != null) listener.onCancel(data);
    }

    private void onConfirmeHandled(String data) {
        if (listener != null) listener.onConfirm(data);
    }

    private void onReadyHandled(String data) {
        if (listener != null) listener.onReady(data);
    }

    private void onDoneHandled(String data) {
        if (listener != null) listener.onDone(data);
    }

    private class Client extends WebChromeClient {

        @SuppressLint("JavascriptInterface")
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            WebView.HitTestResult result = view.getHitTestResult();
            String url = result.getExtra();

            if (isNewWindow(url) && isPopup(url)) {
                start(view, createFrom(url));
            } else if (resultMsg != null) {
                BootpayWebView newWindow = new BootpayWebView(view.getContext());
                if(request != null) {
                    newWindow.setRequest(request)
                            .setDialog(dialog)
                            .setOnResponseListener(listener);
                } else if(listener != null){
                    newWindow.setOnResponseListener(listener);
                }

                addView(newWindow,
                        new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            Gravity.NO_GRAVITY)
                );

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

        private boolean isPopup(String url) {
            return url != null && !url.contains("https://app.bootpay.co.kr/assets/icon");
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
