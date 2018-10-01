package bootpay.co.kr.samplepayment;

import android.webkit.JavascriptInterface;


public interface WebAppBridgeInterface {
    @JavascriptInterface
    void error(String data);

    @JavascriptInterface
    void close(String data);

    @JavascriptInterface
    void cancel(String data);

    @JavascriptInterface
    void ready(String data);

    @JavascriptInterface
    void confirm(String data);

    @JavascriptInterface
    void done(String data);
}