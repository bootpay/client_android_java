package kr.co.bootpay.unity;

import android.app.Activity;
import android.graphics.Point;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.unity3d.player.UnityPlayer;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import kr.co.bootpay.BootpayWebView;
import kr.co.bootpay.listner.EventListener;
import kr.co.bootpay.model.Request;

public class UnityView {
    private static FrameLayout layout = null;
    private BootpayWebView webView;
    private Request request;
    private EventListener listener;
    private String gameObject;

    public UnityView(Request request) { this.request = request; }

    public static boolean IsWebViewAvailable() {
        final Activity a = UnityPlayer.currentActivity;

        FutureTask<Boolean> t = new FutureTask<Boolean>(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                boolean isAvailable = false;
                try {
                    BootpayWebView v = new BootpayWebView(a);
                    // 아마 currentActivity 로 뷰가 올바르게 생성되었다는건, 사용 가능한 상태를 말하는듯
                    if (v != null) {
                        v = null;
                        isAvailable = true;
                    }
                } catch (Exception e) {
                }
                return isAvailable;
            }
        });
        a.runOnUiThread(t);
        try {
            return t.get();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean IsInitialized() {
        return webView != null;
    }

    public void init(final String gameObject, final EventListener listener) {
        this.gameObject = gameObject;
        this.listener = listener;
        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(new Runnable() {public void run() {
            if (webView != null) {
                return;
            }

            final BootpayWebView v = new BootpayWebView(a);
            v.setRequest(request);
            v.setOnResponseListener(listener);


//            if (layout == null) {
//                layout = new FrameLayout(a);
//                a.addContentView(
//                        layout,
//                        new ViewGroup.LayoutParams(
//                                ViewGroup.LayoutParams.MATCH_PARENT,
//                                ViewGroup.LayoutParams.MATCH_PARENT));
//                layout.setFocusable(true);
//                layout.setFocusableInTouchMode(true);
//            }
//            layout.addView(
//                    v,
//                    new FrameLayout.LayoutParams(
//                            ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.MATCH_PARENT,
//                            Gravity.NO_GRAVITY));
            webView = v;
        }});

//        final View activityRootView = a.getWindow().getDecorView().getRootView();
//        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                android.graphics.Rect r = new android.graphics.Rect();
//                //r will be populated with the coordinates of your view that area still visible.
//                activityRootView.getWindowVisibleDisplayFrame(r);
//                android.view.Display display = a.getWindowManager().getDefaultDisplay();
//                // cf. http://stackoverflow.com/questions/9654016/getsize-giving-me-errors/10564149#10564149
//                int h = 0;
//                try {
//                    Point size = new Point();
//                    display.getSize(size);
//                    h = size.y;
//                } catch (java.lang.NoSuchMethodError err) {
//                    h = display.getHeight();
//                }
//                int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
//                //System.out.print(String.format("[NativeWebview] %d, %d\n", h, heightDiff));
//                if (heightDiff > h / 3) { // assume that this means that the keyboard is on
//                    UnityPlayer.UnitySendMessage(gameObject, "SetKeyboardVisible", "true");
//                } else {
//                    UnityPlayer.UnitySendMessage(gameObject, "SetKeyboardVisible", "false");
//                }
//            }
//        });
    }

    public void show() {
        final Activity currentActivity = UnityPlayer.currentActivity;
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (layout == null) {
                    layout = new FrameLayout(currentActivity);
                    currentActivity.addContentView(
                            layout,
                            new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT));
                    layout.setFocusable(true);
                    layout.setFocusableInTouchMode(true);
                }
                layout.addView(
                        webView,
                        new FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                Gravity.NO_GRAVITY));

                Log.d("bootpay", "show run");

//                webView.loadUrl("https://www.naver.com");

//                final View activityRootView = currentActivity.getWindow().getDecorView().getRootView();
//                activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        android.graphics.Rect r = new android.graphics.Rect();
//                        //r will be populated with the coordinates of your view that area still visible.
//                        activityRootView.getWindowVisibleDisplayFrame(r);
//                        android.view.Display display = currentActivity.getWindowManager().getDefaultDisplay();
//                        // cf. http://stackoverflow.com/questions/9654016/getsize-giving-me-errors/10564149#10564149
//                        int h = 0;
//                        try {
//                            Point size = new Point();
//                            display.getSize(size);
//                            h = size.y;
//                        } catch (java.lang.NoSuchMethodError err) {
//                            h = display.getHeight();
//                        }
//                        int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
//                        //System.out.print(String.format("[NativeWebview] %d, %d\n", h, heightDiff));
//                        if (heightDiff > h / 3) { // assume that this means that the keyboard is on
//                            UnityPlayer.UnitySendMessage(gameObject, "SetKeyboardVisible", "true");
//                        } else {
//                            UnityPlayer.UnitySendMessage(gameObject, "SetKeyboardVisible", "false");
//                        }
//                    }
//                });
            }
        });
    }

//    public UnityView setOnResponseListener(EventListener listener) {
//        this.listener = listener;
//        return this;
//    }

    public UnityView setRequest(Request request) {
        this.request = request;
        return this;
    }

    protected void transactionConfirm(String data) {
        if (webView != null)
            webView.transactionConfirm(data);
    }

    protected void removePaymentWindow() {
        final Activity currentActivity = UnityPlayer.currentActivity;
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (webView != null) {

//                    webView.removePaymentWindow();
//                    webView.removeAllViews();
//                    webView = null;
//                    layout = null;
//                    layout.removeView(webView);
                    layout.removeAllViews();
                    (( ViewGroup)layout.getParent()).removeView(layout);
                    webView.destroy();
                    webView = null;
                    layout = null;
                }

            }
        });
    }

//    private void afterViewInit() {
//        if (webView != null)
//            webView.setRequest(request)
//                    .setOnResponseListener(listener);
//    }
}
