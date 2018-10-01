package bootpay.co.kr.samplepayment;


import android.webkit.JavascriptInterface;

public class WebAppBridge {
    WebAppBridgeInterface parent;
    public WebAppBridge(WebAppBridgeInterface parent) {
        this.parent = parent;
    }

    @JavascriptInterface
    public void error(String data) {
        parent.error(data);
    }

    @JavascriptInterface
    public void close(String data) {
        parent.close(data);
    }

    @JavascriptInterface
    public void cancel(String data) {
        parent.cancel(data);
    }

    @JavascriptInterface
    public void ready(String data) {
        parent.ready(data);
    }

    @JavascriptInterface
    public void confirm(String data) {
        parent.confirm(data);
    }

    @JavascriptInterface
    public void done(String data) {
        parent.done(data);
    }
}